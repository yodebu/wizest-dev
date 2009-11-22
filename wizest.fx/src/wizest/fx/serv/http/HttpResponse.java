/*
 * Created on 2004. 9. 20
 */
package wizest.fx.serv.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import wizest.fx.util.StreamUtils;

public class HttpResponse {
	static class Code {
		private int number;
		private String reason;

		private Code(int i, String r) {
			number = i;
			reason = r;
		}

		public String toString() {
			return number + " " + reason;
		}

		static Code OK = new Code(200, "OK");
		static Code BAD_REQUEST = new Code(400, "Bad Request");
		static Code NOT_FOUND = new Code(404, "Not Found");
		static Code METHOD_NOT_ALLOWED = new Code(405, "Method Not Allowed");
		static Code ERR = new Code(500, "Internal server error");
	}

	private static String CRLF = "\r\n";
	private static Charset charset8859_1 = Charset.forName("8859_1");
	private HttpServiceContext context;
	private boolean isSended = false;
	// private String responseCharset = "UTF-8";
	private List<HeaderEntry> headerList;

	class HeaderEntry {
		final String key;
		final String value;

		HeaderEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	HttpResponse(HttpServiceContext context) {
		this.context = context;
		this.headerList = new ArrayList<HeaderEntry>();
	}

	private ByteBuffer makeHeaders(Code code, Content content) {
		CharBuffer cb = CharBuffer.allocate(1024);
		for (;;) {
			try {
				cb.put("HTTP/1.0 ").put(code.toString()).put(CRLF);
				cb.put("Server: http-serv-fx/0.1").put(CRLF);
				for (HeaderEntry entry : headerList)
					cb.put(entry.key).put(": ").put(entry.value).put(CRLF);
				// Iterator<HeaderEntry> it = headerList.iterator();
				// while (it.hasNext()) {
				// HeaderEntry entry = it.next();
				// cb.put(entry.key).put(": ").put(entry.value).put(CRLF);
				// }
				if (content != null) {
					// System.out.println(content.getContentType());
					// System.out.println(content.length());
					cb.put("Content-Type: ").put(content.getContentType()).put(
							CRLF);
					if (content.length() >= 0)
						cb.put("Content-Length: ").put(
								Long.toString(content.length())).put(CRLF);
				}
				cb.put(CRLF);
				break;
			} catch (BufferOverflowException x) {
				// assert (cb.capacity() < (1 << 16));
				if (!(cb.capacity() < (1 << 16)))
					throw new AssertionError();
				cb = CharBuffer.allocate(cb.capacity() * 2);
				continue;
			}
		}
		cb.flip();
		return charset8859_1.encode(cb);
	}

	public void send(Code code, Content content) throws IOException {
		OutputStream os = context.getOutputStream();
		ByteBuffer buff = makeHeaders(code, content);
		os.write(buff.array());
		if (content != null)
			StreamUtils.copy(content.getInputStream(), os, true, false);
	}

	public void send(Content content) throws IOException {
		send(Code.OK, content);
	}

	public void sendHtmlUTF8(String html) throws IOException {
		send(html, "UTF-8", true);
	}

	public void send(String html, String charset) throws IOException {
		send(html, charset, true);
	}

	public void send(String s, String charset, boolean html) throws IOException {
		send(new StringContent(s, charset, html));
	}

	public void send(DataSource ds) throws IOException {
		send(new DataSourceContent(ds));
	}

	public void send(File f) throws IOException {
		send(f, true);
	}

	/**
	 * @param f
	 * @param inline
	 *            true if 웹브라우저 내에 보이게 할 때, false if 다운로드 시킬 때
	 * @throws IOException
	 */
	public void send(File f, boolean inline) throws IOException {
		// web browser에서 다른 이름으로 저장할 때 원이름이 보이게...
		if (inline)
			addHeader("Content-Disposition", "inline; filename=\""
					+ new String(f.getName().getBytes(), "ISO-8859-1") + "\"");
		else
			addHeader("Content-Disposition", "attachment; filename=\""
					+ new String(f.getName().getBytes(), "ISO-8859-1") + "\"");
		send(new FileDataSource(f));
	}

	public void release() {
		// responseCharset = "UTF-8";
		if (!isSended) {
			try {
				context.getOutputStream().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		headerList = null;
	}

	// public String getResponseCharset() {
	// return responseCharset;
	// }
	//
	// public void setResponseCharset(String charset) {
	// this.responseCharset = charset;
	// }
	public void addHeader(String key, String value) {
		headerList.add(new HeaderEntry(key, value));
	}

	public List<HeaderEntry> getHeaderList() {
		return headerList;
	}

	public void addCookie(Cookie cookie) {
		addHeader("Set-Cookie", cookie.makeSetCookieString());
	}

	public void setNoCache() {
		addHeader("Cache-Control", "no-cache, must-revalidate"); // HTTP/1.1
		addHeader("Pragma", "no-cache"); // HTTP/1.0
	}
}

class StringContent implements Content {
	private byte[] bytes;
	private String charset;
	private boolean htmlString;

	StringContent(String s, String charset, boolean htmlString) {
		this.charset = charset;
		this.htmlString = htmlString;
		try {
			bytes = s.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			bytes = s.getBytes();
		}
	}

	public String getContentType() {
		if (htmlString)
			return charset == null ? "text/html" : "text/html; charset="
					+ charset;
		else
			return charset == null ? "text/plain" : "text/plain; charset="
					+ charset;
	}

	public long length() {
		return bytes.length;
	}

	public InputStream getInputStream() throws IOException {
		return new ByteArrayInputStream(bytes);
	}

	public void release() {
		bytes = null;
		charset = null;
	}
}

class DataSourceContent implements Content {
	DataSource ds;

	DataSourceContent(DataSource ds) {
		this.ds = ds;
	}

	public String getContentType() {
		return ds.getContentType();
	}

	public long length() {
		if (ds instanceof FileDataSource)
			return ((FileDataSource) ds).getFile().length();
		else
			return -1;
	}

	public InputStream getInputStream() throws IOException {
		return ds.getInputStream();
	}

	public void release() {
		ds = null;
	}
}
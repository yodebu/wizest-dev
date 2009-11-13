package wizest.fx.serv.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wizest.fx.serv.ServiceException;

public class HttpRequest {
	private byte[] header;
	private String headerString_ISO8859_1;
	private Action action;
	private String requestURI;
	private String version;
	private String hostname;
	private HttpServiceContext context;

	HttpRequest(HttpServiceContext context) throws ServiceException {
		this.context = context;
		header = readHeader();
		try {
			headerString_ISO8859_1 = new String(header, "8859_1");
		} catch (UnsupportedEncodingException e1) {
			headerString_ISO8859_1 = new String(header);
			e1.printStackTrace();
		}
		parseRequest();
	}

	public static class Action {
		public static Action GET = new Action("GET");
		public static Action PUT = new Action("PUT");
		public static Action POST = new Action("POST");
		public static Action HEAD = new Action("HEAD");
		public final String action;

		private Action(String action) {
			this.action = action;
		}

		public boolean equals(Object obj) {
			return action.equals(obj);
		}

		public static Action parse(String s) throws ServiceException {
			if (s.equals("GET"))
				return GET;
			if (s.equals("PUT"))
				return PUT;
			if (s.equals("POST"))
				return POST;
			if (s.equals("HEAD"))
				return HEAD;
			throw new ServiceException("Illegal action argument: " + s);
		}
	}

	public void release() {
		action = null;
		requestURI = null;
		version = null;
		hostname = null;
		header = null;
		try {
			context.getInputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * The expected message format is first compiled into a pattern, and is then
	 * compared against the inbound character buffer to determine if there is a
	 * match. This convienently tokenizes our request into usable pieces.
	 * 
	 * This uses Matcher "expression capture groups" to tokenize requests like:
	 * 
	 * GET /dir/file HTTP/1.1 Host: hostname
	 * 
	 * into:
	 * 
	 * group[1] = "GET" group[2] = "/dir/file" group[3] = "1.1" group[4] =
	 * "hostname"
	 * 
	 * The text in between the parens are used to captured the regexp text.
	 */
	private static Pattern requestPattern = Pattern.compile(
			"\\A([A-Z]+) +([^ ]+) +HTTP/([0-9\\.]+)$"
					+ ".*^Host: ([^ ]+)$.*\r\n\r\n\\z", Pattern.MULTILINE
					| Pattern.DOTALL);

	private void parseRequest() throws ServiceException {
		Matcher m = requestPattern.matcher(headerString_ISO8859_1);
		if (!m.matches())
			throw new ServiceException("Malformed request exception");
		this.action = Action.parse(m.group(1));
		this.requestURI = m.group(2);
		this.version = m.group(3);
		this.hostname = m.group(4);
	}

	private byte[] readHeader() throws ServiceException {
		InputStream is = context.getInputStream();
		ArrayList<Byte> bytes = new ArrayList<Byte>(1024);
		while (true) {
			int r;
			try {
				r = is.read();
			} catch (IOException e) {
				throw new ServiceException(e.getMessage(), e);
			}
			if (r == -1)
				// break;
				throw new ServiceException("incorrect header");
			bytes.add(Byte.valueOf((byte) r));
			if (bytes.size() > 3) {
				int size = bytes.size();
				if (bytes.get(size - 4).byteValue() == (byte) '\r'
						&& bytes.get(size - 3).byteValue() == (byte) '\n'
						&& bytes.get(size - 2).byteValue() == (byte) '\r'
						&& bytes.get(size - 1).byteValue() == (byte) '\n')
					break;
			}
		}
		byte[] result = new byte[bytes.size()];
		for (int i = 0, length = bytes.size(); i < length; ++i)
			result[i] = bytes.get(i).byteValue();

		// System.out.println(new String(result));
		return result;
	}

	public Action getAction() {
		return action;
	}

	public String getRequestURI_ISO8859_1() {
		return requestURI;
	}

	public String getRequestURI() {
		try {
			return new String(requestURI.getBytes("8859_1"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return getRequestURI_ISO8859_1();
		}
	}

	public String getVersion() {
		return version;
	}

	public String getHostname() {
		return hostname;
	}

	public byte[] getHeader() {
		return header;
	}

	private static Pattern acceptLangPattern = Pattern.compile(
			"^Accept-Language: (.*)$", Pattern.MULTILINE);
	private static Pattern contentLengthPattern = Pattern.compile(
			"^Content-Length: (.*)$", Pattern.MULTILINE);
	private static Pattern cookiePattern = Pattern.compile("^Cookie: (.*)$",
			Pattern.MULTILINE);

	/**
	 * @return ex) ko, jp, en
	 */
	public String parseAcceptLanguage() {
		Matcher m = acceptLangPattern.matcher(headerString_ISO8859_1);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	public String parseContentLength() {
		Matcher m = contentLengthPattern.matcher(headerString_ISO8859_1);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	private String parseCookie() {
		Matcher m = cookiePattern.matcher(headerString_ISO8859_1);
		if (m.find())
			return m.group(1);
		else
			return null;
	}

	private static Pattern headerPattern = Pattern.compile("^(.*): (.*)$",
			Pattern.MULTILINE);

	private Map<String, String> _parseHeaders(String h) {
		Matcher m = headerPattern.matcher(h);
		HashMap<String, String> headerMap = new HashMap<String, String>();
		while (m.find()) {
			headerMap.put(m.group(1), m.group(2));
		}
		return headerMap;
	}

	public Map<String, String> parserHeaders_ISO8859_1() {
		return _parseHeaders(headerString_ISO8859_1);
	}

	public Map<String, String> parserHeaders(Charset decoder) {
		String h = decoder.decode(ByteBuffer.wrap(header)).toString();
		return _parseHeaders(h);
	}

	// public Map parserParameters() {
	// if (action.equals(Action.GET)) {
	// String acceptLang =parseAcceptLanguage();
	// Locale.getDefault();
	// CharsetMapper.getDefaultCharset()
	// }
	// else if (action.equals(Action.POST)) {
	//            
	// }
	// }
	protected static Map<String, String> _parserParameters(String paramString) {
		int idx;
		String params = (idx = paramString.indexOf('?')) == -1 ? ""
				: paramString.substring(idx + 1);
		HashMap<String, String> map = new HashMap<String, String>();
		if (params.length() > 0) {
			String[] ps = params.split("&");
			for (int i = 0, length = ps.length; i < length; ++i) {
				int dim = ps[i].indexOf('=');
				if (dim == -1)
					map.put(ps[i], "");
				else
					map.put(ps[i].substring(0, dim), ps[i].substring(dim + 1));
			}
		}
		return map;
	}

	public Cookie[] getCookies() {
		String cs = parseCookie();
		if (cs == null)
			return new Cookie[0];
		ArrayList<Cookie> list = new ArrayList<Cookie>();
		String[] aCookie = cs.split("; ");
		for (int i = 0, length = aCookie.length; i < length; ++i) {
			int idx = aCookie[i].indexOf('=');
			if (idx < 0)
				list.add(new Cookie(aCookie[i], ""));
			else
				list.add(new Cookie(aCookie[i].substring(0, idx), aCookie[i]
						.substring(idx + 1)));
		}
		return list.toArray(new Cookie[0]);
	}

	public InputStream getInputStream() {
		return context.getInputStream();
	}
}

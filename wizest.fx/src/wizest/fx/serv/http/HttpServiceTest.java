/*
 * Created on 2004. 9. 8
 */
package wizest.fx.serv.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;

import wizest.fx.serv.Server;
import wizest.fx.serv.ServiceException;
import wizest.fx.util.StreamUtils;

/**
 * @author wizest
 */
public class HttpServiceTest {
	public static void main(String[] args) {
		HttpService sv = new HttpService() {
			public void doService(HttpServiceContext c) throws ServiceException {
				try {
					System.out.println(new String(c.getHttpRequest()
							.getHeader()));
					// System.out.println(c.getHttpRequest().getRequestURI());

					// input body
					System.out.println("Input Body:");
					InputStream is = c.getInputStream();
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					StreamUtils.copyUntilEmptyInput(is, os, false);
					System.out.println(new String(os.toByteArray(), "UTF-8"));

					// Cookie[] cs = c.getHttpRequest().getCookie();
					// for (int i = 0, length = cs.length; i < length; ++i) {
					// System.out.println(cs[i].makeSetCookieString());
					// System.out.println("----"+cs[i].getName()+"----");
					// System.out.println("~~~~"+cs[i].getValue()+"~~~~");
					// }
					// System.out.println(c.getHttpRequest().parserHeaders_ISO8859_1());
					// System.out.println(HttpRequest._parserParameters(new
					// String(c.getHttpRequest().getRequestURI_ISO8859_1()
					// .getBytes("8859_1"))));
					// c.getHttpResponse().addCookie(new Cookie("hihi",
					// "byebye"));
					// c.getHttpResponse().addCookie(new Cookie("hihi2",
					// "byebye2"));
					// System.out.println(c.getSession());
					// System.out.println(c.getSession().getId());

					// c.getHttpResponse().send(new File("c:/temp/t.jpg"));
					// c.getHttpResponse().send(new File("/ex.exe"));
					// c.getHttpResponse().send(new File("/su.txt"));

					// c.getHttpResponse().send("<html>¼ö½Å at " +
					// System.currentTimeMillis()+ "</html>", "EUC-KR");

					String r = System.currentTimeMillis() + "\n"
							+ new String(os.toByteArray(), "UTF-8");
					c.getHttpResponse().send(r, "UTF-8");
				} catch (IOException e) {
					throw new ServiceException(e.getMessage(), e);
				}
			}
		};
		Server s = new HttpServer(new InetSocketAddress(8888), sv);
		s.startup();
	}
}
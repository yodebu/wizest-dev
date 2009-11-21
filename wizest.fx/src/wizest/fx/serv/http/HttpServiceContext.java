/*
 * Created on 2004. 9. 7
 */
package wizest.fx.serv.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.SocketChannel;

import wizest.fx.serv.ServerContext;
import wizest.fx.serv.ServiceContext;
import wizest.fx.serv.ServiceException;
import wizest.fx.serv.SocketChannelInputStream;
import wizest.fx.serv.SocketChannelOutputStream;
import wizest.fx.session.Session;
import wizest.fx.session.SessionManager;

/**
 * @author wizest
 */
public class HttpServiceContext extends ServiceContext {
	private InputStream is;
	private OutputStream os;
	private HttpRequest request;
	private HttpResponse response;

	public void init(ServerContext sc, SocketChannel ch)
			throws ServiceException {
		super.init(sc, ch);
		try {
			ch.configureBlocking(false);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
		this.request = new HttpRequest(this);
		this.response = new HttpResponse(this);
	}

	public synchronized void release() {
		is = null;
		os = null;
		if (request != null)
			request.release();
		if (response != null)
			response.release();
		super.release();
	}

	protected InputStream getInputStream() {
		if (is == null) {
			// is = Channels.newInputStream(getSocketChannel());
			is = new SocketChannelInputStream(getSocketChannel());
		}
		return is;
	}

	protected OutputStream getOutputStream() {
		if (os == null)
			// os = Channels.newOutputStream(getSocketChannel());
			os = new SocketChannelOutputStream(getSocketChannel());
		return os;
	}

	public HttpRequest getHttpRequest() {
		return this.request;
	}

	public HttpResponse getHttpResponse() {
		return this.response;
	}

	public Session getSession() {
		return getSession(true);
	}

	public synchronized Session getSession(boolean create) {
		String sid = (String) getAttribute(SESSION_ID_KEY);
		if (sid == null) {
			Cookie[] cs = getHttpRequest().getCookies();
			for (int i = 0, length = cs.length; i < length; ++i) {
				if (cs[i].getName().equals(SESSION_ID_KEY)) {
					sid = cs[i].getValue();
					break;
				}
			}
			if (sid == null) {
				if (create) {
					sid = SessionManager.generateSessionId();
					getHttpResponse()
							.addCookie(new Cookie(SESSION_ID_KEY, sid));
				} else
					return null;
			}
			setAttribute(SESSION_ID_KEY, sid);
			return getSessionManager().getSession(sid, create);
		} else
			return getSessionManager().getSession(sid, create);
	}

	private static final String SESSION_ID_KEY = "__FSESSIONID__";

	private SessionManager getSessionManager() {
		HttpServer s = (HttpServer) getServerContext().getServer();
		return s.getSessionManager();
	}
}
/*
 * Created on 2004. 9. 21
 */
package wizest.fx.serv.http;

import java.net.InetSocketAddress;

import wizest.fx.serv.Server;
import wizest.fx.session.SessionManager;
import wizest.fx.session.SimpleSessionManagerFactory;

/**
 * @author wizest
 */
public class HttpServer extends Server {
	private SessionManager sessionManager;

	public HttpServer(InetSocketAddress socketAddress, HttpService svc) {
		super(socketAddress, svc);
		this.sessionManager = SimpleSessionManagerFactory.createFactory()
				.createManager("session-manager@http-server-fx");
	}

	public void startup() {
		sessionManager.begin();
		super.startup();
	}

	public void shutdown() {
		super.shutdown();
		sessionManager.end();
	}

	protected SessionManager getSessionManager() {
		return this.sessionManager;
	}
}
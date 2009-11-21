/*
 * Created on 2004. 9. 8
 */
package wizest.fx.serv;

import java.nio.channels.SocketChannel;

import wizest.fx.util.Context;
import wizest.fx.util.GenericContext;

/**
 * @author wizest
 */
public class ServiceContext extends GenericContext implements Context {
	private SocketChannel ch;

	public void init(ServerContext sc, SocketChannel ch)
			throws ServiceException {
		setParent(sc);
		this.ch = ch;
	}

	public synchronized void release() {
		ch = null;

		super.release();
	}

	public SocketChannel getSocketChannel() {
		return ch;
	}

	public ServerContext getServerContext() {
		return (ServerContext) getParent();
	}

}
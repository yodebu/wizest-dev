/*
 * Created on 2004. 8. 28
 */
package wizest.fx.serv;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import wizest.fx.logging.LogBroker;
import wizest.fx.util.StackTrace;

/**
 * simple server model
 * 
 * @author Sanghoon, Kim
 */
public class Server implements Runnable {
	private InetSocketAddress socketAddress;
	private final Service service;
	private Thread serverThread = null;
	private Logger log = LogBroker.getLogger(this);

	// private Context externalContext;

	public Server(InetSocketAddress socketAddress, Service svc/*
																 * , Context
																 * externalContext
																 */) {
		this.socketAddress = socketAddress;
		// this.externalContext = externalContext;
		this.service = svc;
	}

	// public Server(InetSocketAddress socketAddress, Service svc) {
	// this(socketAddress, svc, null);
	// }

	public InetSocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void startup() { // blocking method
		log.info("server started: " + socketAddress);
		if (serverThread != null)
			return;
		serverThread = Thread.currentThread();
		ServerContext sc = new ServerContext(/* externalContext, */this);
		ExecutorService es = Executors.newCachedThreadPool(new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setDaemon(true);
				return t;
			}
		});
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().setReuseAddress(true);
			ssc.socket().bind(socketAddress);
			Selector sel = Selector.open();
			ssc.register(sel, SelectionKey.OP_ACCEPT);
			while (!Thread.interrupted()) {
				sel.select(1000);
				SocketChannel ch = ssc.accept();
				if (ch != null) {
					ServiceSuite suite = new ServiceSuite(service, sc, ch);
					es.execute(suite);
				}
			}
		} catch (ClosedByInterruptException ie) {
			log.info("server interrupted: " + socketAddress + ", exception=" + ie.toString());
		} catch (IOException e) {
			log.warning(e.getMessage() + e);
		}
		es.shutdownNow();
		sc.release();
		serverThread = null;
		log.info("server finished: " + socketAddress);
	}

	public void shutdown() {
		if (serverThread != null)
			serverThread.interrupt();
	}

	public void run() {
		startup();

	}
}

class ServiceSuite implements Runnable {
	private Service sv;
	private ServerContext sc;
	private SocketChannel ch;
	private Logger log;

	public ServiceSuite(Service sv, ServerContext sc, SocketChannel ch) {
		this.sv = sv;
		this.sc = sc;
		this.ch = ch;
		this.log = LogBroker.getLogger(this);
	}

	public void run() {
		ServiceContext rc = null;
		try {
			ServiceContext _rc = sv.getServiceContextClass().newInstance();
			_rc.init(sc, ch);
			rc = _rc;
			sv.doService(rc);
		} catch (ServiceException se) {
			// log.log(Level.WARNING, se.getMessage(), ch);
			log.log(Level.WARNING, StackTrace.trace(se), ch);
		} catch (Exception e) {
			// log.log(Level.SEVERE, e.getMessage(), ch);
			log.log(Level.WARNING, StackTrace.trace(e), ch);
		} finally {
			if (rc != null) {
				try {
					rc.getSocketChannel().close();
				} catch (IOException e1) {
					log.log(Level.WARNING, e1.getMessage(), ch);
				}
				rc.release();
			}
		}
	}
}
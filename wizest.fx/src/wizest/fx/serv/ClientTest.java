package wizest.fx.serv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import wizest.fx.util.StreamUtils;

public class ClientTest {
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {

		Server s = new Server(new InetSocketAddress(8888), new Service() {

			public void doService(ServiceContext request)
					throws ServiceException {
				try {
					SocketChannel sc = request.getSocketChannel();
					sc.configureBlocking(false);
					SocketChannelInputStream is = new SocketChannelInputStream(
							sc);

					System.out.println(new String(StreamUtils.toByteArray(is),
							"iso-8859-1"));
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("closing.");
			}

			public Class<? extends ServiceContext> getServiceContextClass() {
				return ServiceContext.class;
			}
		});

		Client c = new Client() {
			public void service(InputStream is, OutputStream os)
					throws IOException {
				PrintWriter out = new PrintWriter(os, true);
				for (int i = 0; i < 2; ++i)
					out.println("hello, world.");
				out.flush();
			}
		};

		Thread t = new Thread(s);
		t.setDaemon(true);
		t.start();

		Thread.currentThread().sleep(1000);
		c.open(new InetSocketAddress("127.0.0.1", 8888));
		Thread.currentThread().sleep(1000);
		c.open(new InetSocketAddress("127.0.0.1", 8888));
		Thread.currentThread().sleep(1000);
		System.out.println("bye.");

	}
}

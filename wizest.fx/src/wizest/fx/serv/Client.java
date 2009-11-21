package wizest.fx.serv;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public abstract class Client {
	public void open(InetSocketAddress addr) throws IOException {
		Socket s = new Socket(addr.getAddress(), addr.getPort());

		InputStream is = s.getInputStream();
		OutputStream os = s.getOutputStream();

		// SocketChannel sc = SocketChannel.open();
		// sc.configureBlocking(false);
		// sc.connect(addr);
		//
		// SocketChannelInputStream is = new SocketChannelInputStream(sc);
		// SocketChannelOutputStream os = new SocketChannelOutputStream(sc);

		service(is, os);
		os.flush();

		is.close();
		os.close();
	}

	public abstract void service(InputStream is, OutputStream os)
			throws IOException;

}

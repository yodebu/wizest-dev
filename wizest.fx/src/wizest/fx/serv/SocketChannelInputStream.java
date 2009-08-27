/*
 * Created on 2004. 9. 8
 */
package wizest.fx.serv;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SocketChannel;

/**
 * @author wizest
 */
public class SocketChannelInputStream extends InputStream {
	private SocketChannel sc;

	/**
	 * @param sc
	 *            non-blocking mode only
	 * @throws IOException
	 */
	public SocketChannelInputStream(SocketChannel sc) {
		super();
		if (sc.isBlocking())
			throw new IllegalBlockingModeException();
		this.sc = sc;
	}

	private byte[] buff = new byte[1];

	public int read() throws IOException {
		for (;;) {
			int r = read(buff, 0, 1);
			if (r < 0)
				return r;
			else if (r > 0)
				break;
		}
		return buff[0];
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return sc.read(ByteBuffer.wrap(b, off, len));
	}

	public SocketChannel getSocketChannel() {
		return sc;
	}

	public void close() throws IOException {
		sc.close();
		super.close();
	}
}
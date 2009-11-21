/*
 * Created on 2004. 9. 20
 */
package wizest.fx.serv;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SocketChannel;

/**
 * @author wizest
 */
public class SocketChannelOutputStream extends OutputStream {
	private SocketChannel sc;

	public SocketChannelOutputStream(SocketChannel sc) {
		super();
		if (sc.isBlocking())
			throw new IllegalBlockingModeException();
		this.sc = sc;
	}

	public synchronized void write(int b) throws IOException {
		write(new byte[] { (byte) b }, 0, 1);
	}

	public synchronized void write(byte[] bs, int off, int len)
			throws IOException {
		ByteBuffer buff = ByteBuffer.wrap(bs, off, len);
		int real = 0;
		do {
			real += sc.write(buff);
			// System.out.println(real);
		} while (real < len);
	}

	public void flush() throws IOException {
		super.flush();
	}

	public void close() throws IOException {
		sc.close();
	}

	public SocketChannel getSocketChannel() {
		return this.sc;
	}
}

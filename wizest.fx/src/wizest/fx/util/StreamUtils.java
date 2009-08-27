package wizest.fx.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


public class StreamUtils {
	private StreamUtils() {
	}

	/**
	 * 복사 하고 나서 stream을 닫는다. (in/out 모두)
	 * 
	 * @param is
	 *            InputStream
	 * @param os
	 *            OutputStream
	 * @throws IOException
	 */
	public static void copy(InputStream is, OutputStream os) throws IOException {
		copy(is, os, true);
	}

	public static void copy(InputStream is, OutputStream os, final boolean closeStreams) throws IOException {
		if (is instanceof FileInputStream) {
			if (os instanceof FileOutputStream) {
				FileInputStream fis = (FileInputStream) is;
				FileOutputStream fos = (FileOutputStream) os;
				FileChannel fc = fis.getChannel();
				FileChannel fc2 = fos.getChannel();
				ByteBuffer buff = ByteBuffer.allocateDirect(40960);
				while (fc.read(buff) >= 0) {
					buff.flip();
					fc2.write(buff);
					buff.clear();
				}
				fos.flush();
				if (closeStreams) {
					fos.close();
					fis.close();
				}
			} else {
				ByteBuffer buff = ByteBuffer.allocate(40960);
				FileChannel fc = ((FileInputStream) is).getChannel();
				while (true) {
					int ret = fc.read(buff);
					if (ret == -1) {
						break;
					}
					buff.flip();
					os.write(buff.array(), 0, ret);
					buff.clear();
				}
				os.flush();
				if (closeStreams) {
					os.close();
					is.close();
				}
			}
		} else {
			BufferedInputStream in = new BufferedInputStream(is);
			BufferedOutputStream out = new BufferedOutputStream(os);
			byte[] buff = new byte[8192];
			for (;;) {
				int read = in.read(buff);
				if (read < 0)
					break;
				out.write(buff, 0, read);
			}
			out.flush();
			if (closeStreams) {
				out.close();
				in.close();
			}
		}
	}

	public static byte[] toByteArray(InputStream is) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		copy(is, os, false);
		os.close();
		return os.toByteArray();
	}

	public static InputStream toInputStream(byte[] bytes) {
		ByteArrayInputStream is = new ByteArrayInputStream(bytes);
		return is;
	}
}
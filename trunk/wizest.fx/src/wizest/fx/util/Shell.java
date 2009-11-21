package wizest.fx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

public class Shell {
	public static boolean move(File source, File target) throws IOException {
		// target = new File (target, source.getName());
		return rename(source, target);
	}

	public static boolean rename(File source, File target) throws IOException {
		if (source.exists()) {
			source = source.getCanonicalFile();

			File parent = target.getParentFile();
			if (parent != null && !parent.exists())
				parent.mkdirs();

			return source.renameTo(target);
		} else {
			return false;
		}
	}

	public static void copy(File source, File target) throws IOException {
		source = source.getCanonicalFile();

		if (source.isDirectory()) {
			if (target.exists() && !target.isDirectory())
				throw new IOException("the target is not a directory.");

			String sPath = null;
			try {
				sPath = source.getParentFile().getAbsolutePath();
			} catch (Exception ex) {
				sPath = source.getAbsolutePath();
			}
			int sPathLength = sPath.length();

			Stack s = new Stack();
			File src = source;
			s.push(src);
			while (!s.empty()) {
				src = (File) s.pop();
				if (src.isDirectory()) {
					File[] fs = src.listFiles();
					for (int i = 0, length = fs.length; i < length; ++i) {
						s.push(fs[i]);
					}
				} else {
					File dst = new File(target, src.getAbsolutePath()
							.substring(sPathLength));
					copyFile(src, dst);
				}
			}
		} else {
			copyFile(source, target);
		}
	}

	private static void copyFile(File source, File target) throws IOException {
		if (!source.isFile())
			throw new IOException("the source is not a file.");

		if (!target.getParentFile().exists()) {
			target.getParentFile().mkdirs();
		}

		if (!target.exists())
			target.createNewFile();

		FileInputStream in = new FileInputStream(source);
		FileOutputStream out = new FileOutputStream(target);
		FileChannel fc = in.getChannel();
		FileChannel fc2 = out.getChannel();

		ByteBuffer buff = ByteBuffer.allocateDirect(4096);
		while (fc.read(buff) >= 0) {
			buff.flip();
			fc2.write(buff);
			buff.clear();
		}

		in.close();
		out.close();
	}

	/**
	 * 디렉토리를 선택하면 하위 디렉토리까지 모두 삭제
	 * 
	 * @param target
	 *            File
	 */
	public static void remove(File target) {
		if (!target.isDirectory())
			target.delete();
		else {
			Stack s = new Stack();
			s.addAll(Arrays.asList(target.listFiles()));

			while (true) {
				try {
					remove((File) s.pop());
				} catch (EmptyStackException ex) {
					break;
				}
			}
			target.delete();
		}
	}

	public static String filterFilename(String s) {
		// filter \ / : * ? " < > |
		s = s.replace("\\", " ");
		s = s.replace("/", " ");
		s = s.replace(":", " ");
		s = s.replace("*", " ");
		s = s.replace("?", " ");
		s = s.replace("\"", " ");
		s = s.replace("<", " ");
		s = s.replace(">", " ");
		s = s.replace("|", " ");
		return s;
	}

}

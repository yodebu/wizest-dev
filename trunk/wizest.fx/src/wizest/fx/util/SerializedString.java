package wizest.fx.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.UnsupportedEncodingException;

/**
 * Object를 String으로 변환해 준다 거꾸로 변환된 String을 Object로 되돌린다.
 * 
 */
public class SerializedString {
	private SerializedString() {
	}

	public static String serialize(Object o) throws SerializedStringException {
		return serialize(o, false);
	}

	public static String serialize(Object o, boolean warp)
			throws SerializedStringException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.flush();
			String r = new String(baos.toByteArray(), "8859_1");
			String s = encodeString(r, warp);
			return s;
		} catch (Exception e) {
			throw new SerializedStringException(e.getMessage(), e);
		}
	}

	public static Object deserialize(String s) throws SerializedStringException {
		return deserialize(s, false);
	}

	public static Object deserialize(String s, boolean warp)
			throws SerializedStringException {
		try {
			String r = decodeString(s, warp);
			byte[] b = r.getBytes("8859_1");
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object o = ois.readObject();
			return o;
		} catch (Exception e) {
			throw new SerializedStringException(e.getMessage(), e);
		}
	}

	/**
	 * @param cls
	 *            Class 역직렬화 할 클래스
	 * @param s
	 *            String
	 * @param warp
	 *            boolean
	 * @throws SerializedStringException
	 * @return Object
	 */
	public static Object deserialize(final Class cls, String s, boolean warp)
			throws SerializedStringException {
		try {
			String r = decodeString(s, warp);
			byte[] b = r.getBytes("8859_1");
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			ObjectInputStream ois = new ObjectInputStream(bais) {
				/*
				 * protected Class< ? > resolveClass(ObjectStreamClass osc)
				 * throws IOException, ClassNotFoundException { if
				 * (osc.toString(
				 * ).equals(ObjectStreamClass.lookup(cls).toString())) return
				 * cls; else return super.resolveClass(osc); }
				 */

				protected Class resolveClass(ObjectStreamClass osc)
						throws IOException, ClassNotFoundException {
					if (osc.toString().equals(
							ObjectStreamClass.lookup(cls).toString()))
						return cls;
					else
						return super.resolveClass(osc);
				}

			};
			Object o = ois.readObject();
			return o;
		} catch (Exception e) {
			throw new SerializedStringException(e.getMessage(), e);
		}
	}

	private static String decodeString(String s, boolean warp) {
		try {
			if (warp)
				s = warpBase64Line(s, false);
			char[] ca = s.toCharArray();
			for (int i = 0, length = ca.length; i < length; ++i) {
				switch (ca[i]) {
				case '@':
					ca[i] = '=';
					break;
				case '*':
					ca[i] = '+';
					break;
				case '|':
					ca[i] = '/';
					break;
				default:
				}
			}
			return new String(Base64.base64ToByteArray(new String(ca)),
					"8859_1");
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	private static String encodeString(String r, boolean warp) {
		try {
			String ss = Base64.byteArrayToBase64(r.getBytes("8859_1"));
			char[] ca = ss.toCharArray();
			for (int i = 0, length = ca.length; i < length; ++i) {
				switch (ca[i]) {
				case '=':
					ca[i] = '@';
					break;
				case '+':
					ca[i] = '*';
					break;
				case '/':
					ca[i] = '|';
					break;
				default:
				}
			}
			String rt = new String(ca);
			if (warp)
				return warpBase64Line(rt, true);
			else
				return rt;
		} catch (UnsupportedEncodingException ex) {
			return null;
		}
	}

	/**
	 * @param s
	 *            String
	 * @param warp
	 *            boolean true if split , false if join
	 * @return String
	 */
	private static String warpBase64Line(String s, boolean split) {
		if (split) {
			StringBuffer buf = new StringBuffer();
			final int LIMIT = 80;
			int cnt = 0;
			for (int i = 0, length = s.length(); i < length; ++i) {
				buf.append(s.charAt(i));
				if (++cnt == LIMIT) {
					buf.append('\n');
					cnt = 0;
				}
			}
			return buf.toString();
		} else {
			// ' ' '\n' 을 다 없애 버린다.
			StringBuffer buf = new StringBuffer();
			for (int i = 0, length = s.length(); i < length; ++i) {
				char c = s.charAt(i);
				if (c == ' ' || c == '\n') {
					;
				} else {
					buf.append(c);
				}
			}
			return buf.toString();
		}
	}

	/**
	 * @param name
	 *            String
	 * @param cl
	 *            ClassLoader null if uses system default class loader
	 * @return String null if exception
	 */
	public static final String saveResource(String name, ClassLoader cl) {
		try {
			ClassLoader loader = cl == null ? ClassLoader
					.getSystemClassLoader() : cl;
			InputStream is = loader.getResourceAsStream(name);
			if (is == null)
				return null;
			byte[] code = StreamUtils.toByteArray(is);
			String codeString = Base64.byteArrayToBase64(code);
			return warpBase64Line(codeString, true);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static final ClassLoader createClassLoader(final String name,
			final String classString, final ClassLoader parentClassLoader) {
		ClassLoader loader = new ClassLoader(parentClassLoader) {
			/*
			 * protected Class< ? > findClass(String findName) throws
			 * ClassNotFoundException { if (findName.equals(name)) { byte[] b =
			 * null; String codeString = warpBase64Line(classString, false); b =
			 * Base64.base64ToByteArray(codeString); try { Class cls =
			 * defineClass(name, b, 0, b.length); // resolveClass(cls); return
			 * cls; } catch (Throwable ex) { ex.printStackTrace(); return null;
			 * } } else return getParent().loadClass(findName); }
			 */
			protected Class findClass(String findName)
					throws ClassNotFoundException {
				if (findName.equals(name)) {
					byte[] b = null;
					String codeString = warpBase64Line(classString, false);
					b = Base64.base64ToByteArray(codeString);
					try {
						Class cls = defineClass(name, b, 0, b.length);
						// resolveClass(cls);
						return cls;
					} catch (Throwable ex) {
						ex.printStackTrace();
						return null;
					}
				} else
					return getParent().loadClass(findName);
			}

		};
		return loader;
	}

	/**
	 * @param name
	 *            String
	 * @param classString
	 *            String
	 * @return Class null if class not found
	 */
	public static final Class loadClass(final String name,
			final String classString, final ClassLoader parentClassLoader) {
		ClassLoader loader = createClassLoader(name, classString,
				parentClassLoader);
		try {
			return Class.forName(name, true, loader);
			// return loader.loadClass(name);
		} catch (ClassNotFoundException ex) {
			return null;
		}
	}

	/**
	 * use Thread.currentThread().getContextClassLoader() as a parent class
	 * loader
	 * 
	 * @param name
	 *            String
	 * @param classString
	 *            String
	 * @return Class
	 */
	public static final Class loadClass(final String name,
			final String classString) {
		return loadClass(name, classString, Thread.currentThread()
				.getContextClassLoader());
	}
	// public static void main(String[] args)
	// {
	// String s =
	// saveResource("wizest/fx/util/SerializedString.class",null);
	// // encoded class
	// System.out.println(s);
	//
	// Class cls =loadClass("wizest.fx.util.SerializedString",s);
	// System.out.println(cls);
	// }
}
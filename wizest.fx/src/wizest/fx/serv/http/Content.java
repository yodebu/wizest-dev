/*
 * Created on 2004. 9. 20
 */
package wizest.fx.serv.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wizest
 */
public interface Content {
	/*
	 * text/html; charset=iso-8859-1 text/plain; charset=iso-8859-1
	 * application/octet-stream
	 */
	String getContentType();

	/**
	 * @return -1 if unknown
	 */
	long length();

	InputStream getInputStream() throws IOException;

	void release();
}
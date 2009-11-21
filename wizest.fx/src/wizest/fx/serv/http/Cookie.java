/*
 * Created on 2004. 9. 20
 */
package wizest.fx.serv.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * http://wp.netscape.com/newsref/std/cookie_spec.html
 * 
 * @author wizest
 */
public class Cookie {
	private String value;
	private String name;
	private Date expire; // may null
	private String path; // may null
	private String domain; // may null
	private boolean secure;
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"EEE, d MMM yyyy HH:mm:ss Z", Locale.US);

	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Returns the value.
	 */
	public String getValue() {
		return value;
	}

	public String makeSetCookieString() {
		return name + "=" + value
				+ ((expire == null) ? "" : "; expires=" + sdf.format(expire))
				+ ((path == null) ? "" : "; path=" + path)
				+ ((secure) ? "; secure" : "")
				+ ((domain == null) ? "" : "; domain=" + domain);
	}
}
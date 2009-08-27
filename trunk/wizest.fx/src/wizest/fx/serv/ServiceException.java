/*
 * Created on 2004. 9. 7
 */
package wizest.fx.serv;

/**
 * @author wizest
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = -883796584223659105L;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}
}

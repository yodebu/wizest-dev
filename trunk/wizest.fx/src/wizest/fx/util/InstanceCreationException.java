package wizest.fx.util;

public class InstanceCreationException extends Exception {
	public InstanceCreationException() {
		super();
	}

	public InstanceCreationException(String msg) {
		super(msg);
	}

	public InstanceCreationException(Throwable cause) {
		super(cause);
	}

	public InstanceCreationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
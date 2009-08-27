/*
 * Created on 2004. 8. 12
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public class OperationException extends Exception {

    public OperationException(Throwable cause) {
        super(cause);
    }

    public OperationException(String msg) {
        super(msg);
    }

    public OperationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
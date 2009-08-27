package wizest.fx.template;

import wizest.fx.util.Marker;

/**
 * @author wizest
 */
public class CompilerException extends Exception {
    /**
     * ���ܰ� �߻��� ����
     */
    private Marker mark = null;

    /**
     * @param mark
     *            exception�� �߻��� ����
     * @param message
     */
    public CompilerException(Marker mark, String message) {
        super(message);
        this.mark = mark;
    }

    public CompilerException(Marker mark, String message, Throwable cause) {
        super(message, cause);
        this.mark = mark;
    }

    public CompilerException(Marker mark, Throwable cause) {
        super(cause);
        this.mark = mark;
    }

    public Marker getMark() {
        return mark;
    }
    //    public String getMessage()
    //    {
    //        return super.getMessage()+" in tag mark="+mark;
    //    }
}
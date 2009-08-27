package wizest.fx.template;

import wizest.fx.util.Marker;


public class TemplateRuntimeException extends Exception {
    private Marker mark = null;

    public TemplateRuntimeException() {
        super();
    }

    public TemplateRuntimeException(String msg) {
        super(msg);
    }

    public TemplateRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TemplateRuntimeException(Throwable cause) {
        super(cause);
    }

    public TemplateRuntimeException(Marker mark, String msg, Throwable cause) {
        super(msg, cause);
        this.mark = mark;
    }

    public Marker getMark() {
        return this.mark;
    }
}
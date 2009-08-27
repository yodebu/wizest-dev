package wizest.fx.template;

import wizest.fx.util.Marker;

/**
 * @author wizest
 */
public class ExceptionCause {
    private String text;
    private Marker mark;
    private Throwable cause;

    public ExceptionCause(String text, Throwable cause) {
        this.text = text;
        this.cause = cause;
        if (cause instanceof CompilerException)
            this.mark = ((CompilerException) cause).getMark();
        else if (cause instanceof TemplateRuntimeException)
            this.mark = ((TemplateRuntimeException) cause).getMark();
    }

    public ExceptionCause(String text, Throwable cause, Marker mark) {
        this.text = text;
        this.cause = cause;
        this.mark = mark;
    }

    public String getText() {
        return text;
    }

    public Marker getMark() {
        return mark;
    }

    public Throwable getCause() {
        return cause;
    }

    public String toString() {
        return cause.getMessage();
    }
}
package wizest.fx.template.tagext;
/**
 * @author wizest
 */
public abstract class BodyTagSupport extends TagSupport implements BodyTag {
    protected BodyContent bodyContent = null;

    public BodyTagSupport() {}

    public int doStartTag() throws TagException {
        return BodyTag.EVAL_BODY_BUFFERED;
    }

    public int doEndTag() throws TagException {
        return super.doEndTag();
    }

    public void setBodyContent(BodyContent b) {
        bodyContent = b;
    }

    public void doInitBody() throws TagException {}

    public int doAfterBody() throws TagException {
        return BodyTag.SKIP_BODY;
    }

    public void release() {
        bodyContent = null;
        super.release();
    }

    public BodyContent getBodyContent() {
        return bodyContent;
    }

    public PageWriter getPreviousOut() {
        return bodyContent.getEnclosingWriter();
    }
}
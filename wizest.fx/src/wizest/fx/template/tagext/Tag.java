package wizest.fx.template.tagext;
/**
 * @see javax.servlet.jsp.tagext.Tag
 */
public interface Tag {
    public static final int SKIP_PAGE = 5;
    public static final int EVAL_PAGE = 6;

    public abstract void setParent(Tag tag);

    public abstract Tag getParent();

    public abstract void release();
}
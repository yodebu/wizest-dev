package wizest.fx.template.tagext;

/**
 * @see javax.servlet.jsp.tagext.BodyTag
 */
public interface BodyTag extends IterationTag {
	public static final int EVAL_BODY_BUFFERED = 2;

	public abstract void setBodyContent(BodyContent bodycontent);

	/**
	 * @throws TagException
	 */
	public abstract void doInitBody() throws TagException;
}
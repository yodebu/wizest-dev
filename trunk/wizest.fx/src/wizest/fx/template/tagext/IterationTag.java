package wizest.fx.template.tagext;

/**
 * @see javax.servlet.jsp.tagext.IterationTag
 */
public interface IterationTag extends UniPageTag {
	public static final int EVAL_BODY_AGAIN = 2;

	/**
	 * IterationTag.EVAL_BODY_AGAIN �Ǵ� IterationTag.SKIP_BODY �� �����Ѵ�.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doAfterBody() throws TagException;
}
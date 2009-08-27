package wizest.fx.template.tagext;

/**
 * @see javax.servlet.jsp.tagext.IterationTag
 */
public interface IterationTag extends UniPageTag {
	public static final int EVAL_BODY_AGAIN = 2;

	/**
	 * IterationTag.EVAL_BODY_AGAIN 또는 IterationTag.SKIP_BODY 를 리턴한다.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doAfterBody() throws TagException;
}
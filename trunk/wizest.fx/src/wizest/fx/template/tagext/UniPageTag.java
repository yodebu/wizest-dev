package wizest.fx.template.tagext;

public interface UniPageTag extends Tag {
	public static final int SKIP_BODY = 0;
	public static final int EVAL_BODY_INCLUDE = 1;

	public abstract void setPageContext(PageContext pagecontext);

	/**
	 * Tag.EVAL_BODY_INCLUDE (BodyTag.EVAL_BODY_BUFFERED) 또는 Tag.SKIP_BODY 를
	 * 리턴한다.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doStartTag() throws TagException;

	/**
	 * Tag.EVAL_PAGE 또는 Tag.SKIP_PAGE 를 리턴한다.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract int doEndTag() throws TagException;
}
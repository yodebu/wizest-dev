package wizest.fx.template.tagext;

/**
 * Template당 단 1개의 MultiPageTag가 존재해야 한다.
 */
public interface MultiPageTag extends Tag {
	public static final int EVAL_PAGE_AGAIN = 7;
	public static final int EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION = 77;
	public static final int SKIP_PAGE_WITHOUT_PAGE_GENERATION = 55;

	public abstract void setTemplateContext(TemplateContext templatecontext);

	public abstract int doStartTemplate() throws TagException;

	public abstract void doInitPage() throws TagException;

	/**
	 * iteration이 시작되기 전에 자식 UniPageTag에서 사용하는 page context의 parameter로 사용될
	 * PageInfo를 생성한다.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract PageInfo createPageInfo() throws TagException;

	public abstract int doAfterPage() throws TagException;

	// iteration 종료
	public abstract void doEndTemplate() throws TagException;
}
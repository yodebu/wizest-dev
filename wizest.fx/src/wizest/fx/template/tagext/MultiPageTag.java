package wizest.fx.template.tagext;

/**
 * Template�� �� 1���� MultiPageTag�� �����ؾ� �Ѵ�.
 */
public interface MultiPageTag extends Tag {
	public static final int EVAL_PAGE_AGAIN = 7;
	public static final int EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION = 77;
	public static final int SKIP_PAGE_WITHOUT_PAGE_GENERATION = 55;

	public abstract void setTemplateContext(TemplateContext templatecontext);

	public abstract int doStartTemplate() throws TagException;

	public abstract void doInitPage() throws TagException;

	/**
	 * iteration�� ���۵Ǳ� ���� �ڽ� UniPageTag���� ����ϴ� page context�� parameter�� ����
	 * PageInfo�� �����Ѵ�.
	 * 
	 * @return
	 * @throws TagException
	 */
	public abstract PageInfo createPageInfo() throws TagException;

	public abstract int doAfterPage() throws TagException;

	// iteration ����
	public abstract void doEndTemplate() throws TagException;
}
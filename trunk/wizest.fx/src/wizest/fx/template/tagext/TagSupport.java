package wizest.fx.template.tagext;

import java.io.Serializable;

/**
 * @author wizest
 */
public abstract class TagSupport extends BaseTagSupport implements IterationTag, Serializable {
    protected PageContext pageContext = null;

    public TagSupport() {}

    public int doStartTag() throws TagException {
        return UniPageTag.SKIP_BODY;
    }

    public int doEndTag() throws TagException {
        return UniPageTag.EVAL_PAGE;
    }

    public int doAfterBody() throws TagException {
        return UniPageTag.SKIP_BODY;
    }

    public void release() {
        pageContext = null;
        super.release();
    }

    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    /**
     * Ư���� ������ ���ٸ� pageContext�� �ٷ� �����ϱ� �ٶ�
     * 
     * @return
     */
    public PageContext _getPageContext() {
        return this.pageContext;
    }
}
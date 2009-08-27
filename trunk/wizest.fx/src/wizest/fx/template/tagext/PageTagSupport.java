package wizest.fx.template.tagext;

import java.io.Serializable;


public class PageTagSupport extends BaseTagSupport implements MultiPageTag, Serializable {
    protected TemplateContext templateContext;

    public PageTagSupport() {}

    public void release() {
        this.templateContext = null;
        super.release();
    }

    public void setTemplateContext(TemplateContext templatecontext) {
        this.templateContext = templatecontext;
    }

    /**
     * @return SKIP_PAGE,EVAL_PAGE
     * @throws TagException
     */
    public int doStartTemplate() throws TagException {
        return EVAL_PAGE;
    }

    /**
     * doStartPage �� EVAL_PAGE�� �������� ��� PAGE�� �����ϸ鼭 �ҷ�����.
     * 
     * @throws TagException
     */
    public void doInitPage() throws TagException {}

    /**
     * @return SKIP_PAGE,EVAL_PAGE_AGAIN
     * @throws TagException
     */
    public int doAfterPage() throws TagException {
        return SKIP_PAGE;
    }

    public void doEndTemplate() throws TagException {}

    public PageInfo createPageInfo() throws TagException {
        return null;
        //    {
        //        TranslationRequester tr=templateContext.getTranslationRequester();
        //        Template tpl=tr.getTemplate();
        //
        //        PageInfo pageInfo=new PageInfo(tpl.getCategoryId(),tpl.getTemplateId(),PageInfo.CONTENT_TYPE_NONE,null);
        //
        //        return pageInfo;
        //    }
    }

    /**
     * Ư���� ������ ���ٸ� templateContext�� �ٷ� �����ϱ� �ٶ�
     * 
     * @return
     */
    public TemplateContext _getTemplateContext() {
        return this.templateContext;
    }
}
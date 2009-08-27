package wizest.fx.template.tagext;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;

import wizest.fx.util.Context;


public class PageContext implements Context {
    private HashMap attribute = null;
    private Stack bodyStack = null;
    private TemplateContext templateContext = null;
    private PageWriter out = null;
    private PageInfo pageInfo = null;
    private Context parent = null;

    public PageContext() {
        this.attribute = new HashMap();
        this.bodyStack = new Stack();
    }

    public synchronized void initialize(TemplateContext templateContext, PageWriter out, PageInfo pageInfo) {
        this.templateContext = templateContext;
        this.out = out;
        this.pageInfo = pageInfo;
        // 혹시나 release() 하지 않고 initialize()를 하는 경우라도 정상 작동하게 한번 더 썼음 ㅡ.ㅡ..
        this.attribute.clear();
        this.bodyStack.clear();
        this.parent = templateContext;
    }

    public synchronized void release() {
        this.templateContext = null;
        this.out = null;
        this.pageInfo = null;
        this.attribute.clear();
        this.bodyStack.clear();
        this.parent = null;
    }

    public TemplateContext getTemplateContext() {
        return this.templateContext;
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public Object findAttribute(String name) {
        Object o = getAttribute(name);
        if (o == null) {
            o = this.templateContext.findAttribute(name);
        }
        return o;
    }

    public void setAttribute(String name, Object obj) {
        this.attribute.put(name, obj);
    }

    public Object getAttribute(String name) {
        return this.attribute.get(name);
    }

    public void removeAttribute(String name) {
        this.attribute.remove(name);
    }

    public Set getAttributeNames() {
        return ((HashMap) this.attribute.clone()).keySet();
    }

    public Context getParent() {
        return this.parent;
    }

    /**
     * The current value of the out object (a PageWriter).
     * 
     * @return
     */
    public PageWriter getOut() {
        return this.out;
    }

    /**
     * Return a new BodyContent object, save the current "out" PageWriter,
     * 
     * @return
     */
    public BodyContent pushBody() {
        bodyStack.push(out);
        out = new BodyContent(out);
        return (BodyContent) out;
    }

    /**
     * Return the previous PageWriter "out" saved by the matching pushBody(),
     */
    public PageWriter popBody() {
        if (bodyStack.empty()) {
            return null;
        }
        out = (PageWriter) bodyStack.pop();
        return out;
    }
}
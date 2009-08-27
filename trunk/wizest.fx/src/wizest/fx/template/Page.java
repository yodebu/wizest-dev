package wizest.fx.template;

import wizest.fx.template.tagext.PageInfo;

/**
 * @author wizest
 */
public class Page {
    private PageInfo pageInfo = null;
    private String pageText = null;

    public Page(PageInfo pageInfo, String pageText) {
        this.pageInfo = pageInfo;
        this.pageText = pageText;
    }

    public String toString() {
        String text = (this.pageText.length() > 30) ? this.pageText.substring(0, 30) + "..." : this.pageText;
        return "{pageInfo=" + this.pageInfo + ",pageText=" + text.getClass().getName() + "@"
                + Integer.toHexString(text.hashCode()) + "}";
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }

    public String getPageText() {
        return this.pageText;
    }
}
package wizest.fx.template;

import java.util.HashMap;

import wizest.fx.template.tagext.Tag;
import wizest.fx.template.tagext.TagInfo;
import wizest.fx.template.tagext.TagLibraryInfo;

/**
 * @author wizest
 */

	public class TagHandlerHost {
    private final TagLibraryInfo tagLibraryInfo;
    private final HashMap handlerClassCache;

    public TagHandlerHost(TagLibraryInfo tagLibraryInfo) {
        this.tagLibraryInfo = tagLibraryInfo;
        this.handlerClassCache = new HashMap();
    }

    // public void destroy() {
    // this.handlerClassCache = null;
    // }
    /**
     * handler name�� �ش��ϴ� Tag handler�� �����Ѵ�. handlerName�� �ش��ϴ� handler�� ã�� ���� ��� library�� default tag handler�� �����Ѵ�.
     *
     * @param handlerName
     * @return null if the tag handler according to tag name does not exist.
     */
    public Tag getTagHandler(String handlerName) {
        Tag handler = null;
        try {
            Class clazz = (Class) this.handlerClassCache.get(handlerName);
            if (clazz == null) {
                TagInfo tagInfo = this.tagLibraryInfo.getTag(handlerName);
                if (tagInfo == null) {
                    // �ױ� �̸��� �´� �ڵ鷯�� ã�� ���Ͽ��� ���
                    // library�� default tag handler�� ����Ѵ� -> �Ϲ������� custom tag �� ó���ϰ� �ȴ�.
                    tagInfo = this.tagLibraryInfo.getTag(this.tagLibraryInfo.getDefaultTagName());
                }
                clazz = Class.forName(tagInfo.getTagClassName());
                this.handlerClassCache.put(handlerName, clazz);
            }
            handler = (Tag) clazz.newInstance();
        } catch (Exception ex) {
            handler = null;
        }
        // }
        //
        return handler;
    }

    /**
     * host���� ������ tag handler�� ��ȯ�Ѵ�.
     *
     * @param handlerName
     * @param handler
     */
    public void releaseTagHandler(String handlerName, Tag handler) {}
}

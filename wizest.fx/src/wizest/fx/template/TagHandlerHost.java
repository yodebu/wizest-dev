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
     * handler name에 해당하는 Tag handler를 리턴한다. handlerName에 해당하는 handler를 찾지 못할 경우 library의 default tag handler를 리턴한다.
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
                    // 테그 이름에 맞는 핸들러를 찾지 못하였을 경우
                    // library의 default tag handler를 사용한다 -> 일반적으로 custom tag 를 처리하게 된다.
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
     * host에서 가져간 tag handler를 반환한다.
     *
     * @param handlerName
     * @param handler
     */
    public void releaseTagHandler(String handlerName, Tag handler) {}
}

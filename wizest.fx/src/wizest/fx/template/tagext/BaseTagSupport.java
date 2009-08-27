package wizest.fx.template.tagext;

import java.util.Enumeration;
import java.util.Hashtable;

import wizest.fx.util.Marker;


public class BaseTagSupport implements Tag {
    private Tag parent = null;
    private Hashtable values = null;
    private String id = null;
    private Marker tagMark = null;
    private AttributeMap attributeMap = null;
    private Tag parentRuntimeTag = null;

    public void release() {
        values = null;
        parent = null;
        id = null;
        tagMark = null;
        attributeMap = null;
    }

    /**
     * tag로 세팅된 attribute map을 가져온다. (주의! : runtime evaluate 되지 않는다)
     * 
     * @return
     */
    public AttributeMap getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(AttributeMap attributeMap) {
        this.attributeMap = attributeMap;
    }

    public void setParent(Tag t) {
        parent = t;
    }

    public Tag getParent() {
        return parent;
    }

    /**
     * TemplateRuntime에 의해 불러진다.
     * 
     * @param t
     */
    public void setParentRuntimeTag(Tag t) {
        parentRuntimeTag = t;
    }

    /**
     * Template Runtime이 계층적으로 운영될때 하위 Runtime을 품고 있는 부모 tag를 알고자 할 때 부른다.
     * 
     * @return
     */
    public Tag getParentRuntimeTag() {
        return parentRuntimeTag;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTagMarker(Marker mark) {
        this.tagMark = mark;
    }

    public Marker getTagMarker() {
        return this.tagMark;
    }

    public void setValue(String k, Object o) {
        if (values == null) {
            values = new Hashtable();
        }
        values.put(k, o);
    }

    public Object getValue(String k) {
        if (values == null) {
            return null;
        } else {
            return values.get(k);
        }
    }

    public void removeValue(String k) {
        if (values != null) {
            values.remove(k);
        }
    }

    public Enumeration getValues() {
        if (values == null) {
            return null;
        } else {
            return values.keys();
        }
    }

    public static final Tag findAncestorWithClass(Tag from, Class klass) {
        boolean isInterface = false;
        if (from == null || klass == null || !(wizest.fx.template.tagext.Tag.class).isAssignableFrom(klass) && !(isInterface = klass.isInterface())) {
            return null;
        }
        do {
            Tag tag = from.getParent();
            if (tag == null) {
                return null;
            }
            if (isInterface && klass.isInstance(tag) || klass.isAssignableFrom(tag.getClass())) {
                return tag;
            }
            from = tag;
        } while (true);
    }
}
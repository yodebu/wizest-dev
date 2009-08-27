package wizest.fx.template.tagext;

import java.util.HashMap;

/**
 * Translation-time information associated with a taglib directive, and its underlying TLD file. Most of the information is directly from the TLD, except for the prefix and the uri values used in the taglib directive
 * 
 * <p>
 * 테그 묶음에 관한 정보 -> tld 파일 한개의 정보
 * </p>
 */
public class TagLibraryInfo {
    protected TagInfo tags[] = null;
    private String name;
    private String defaultPageTagName;
    private String defaultTagName;
    private String innerPageTagName;
    private HashMap tagInfoMap;
    private HashMap tagInfoMapByCalssName;

    public TagLibraryInfo(String libName, String defaultPageTagName, String defaultTagName, String innerPageTagName,
            TagInfo[] tags) {
        this.name = libName;
        this.defaultPageTagName = defaultPageTagName;
        this.defaultTagName = defaultTagName;
        this.innerPageTagName = innerPageTagName;
        this.tags = tags;
        this.tagInfoMap = new HashMap();
        this.tagInfoMapByCalssName = new HashMap();
        for (int i = 0; i < tags.length; i++) {
            TagInfo tagInfo = tags[i];
            tagInfoMap.put(tagInfo.getTagName(), tagInfo);
            tagInfoMapByCalssName.put(tagInfo.getTagClassName(), tagInfo);
        }
    }

    public TagInfo[] getTags() {
        return tags;
    }

    public TagInfo getTag(String shortname) {
        return (TagInfo) tagInfoMap.get(shortname);
    }

    public TagInfo getTag(Tag tagHandler) {
        return (TagInfo) tagInfoMapByCalssName.get(tagHandler.getClass().getName());
    }

    public String getName() {
        return this.name;
    }

    public String getDefaultPageTagName() {
        return this.defaultPageTagName;
    }

    public String getDefaultTagName() {
        return this.defaultTagName;
    }

    public String getInnerPageTagName() {
        return this.innerPageTagName;
    }
}
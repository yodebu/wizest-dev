package wizest.fx.template.tagext;

import java.util.HashMap;

/**
 * Tag information for a tag in a Tag Library;
 * 
 * tld ���Ͽ��� ���� �� �ִ� tag �Ѱ��� ����
 */
public class TagInfo {
	public static final String BODY_CONTENT_ACTION_TAG = "ACTIONTAG";
	public static final String BODY_CONTENT_TAG_DEPENDENT = "TAGDEPENDENT";
	public static final String BODY_CONTENT_EMPTY = "EMPTY";
	public static final String BODY_CONTENT_PAGE = "PAGE";
	private final String tagName;
	private final String tagClassName;
	private final String bodyContent;
	private final String infoString;
	private final String defaultAttrName;
	private final boolean unknownAttrNameAllowed;
	private final TagAttributeInfo[] attributeInfo;
	private final HashMap attributeInfoMap;
	// attribute�� ���� tag handler�� reflection���� ���� ���� �� required �� attribute�� ���
	// ������ Ȯ���� �� ����Ѵ�.
	private final int numberOfRequiredAttributes;

	public TagInfo(String tagName, String tagClassName, String bodycontent, String defaultAttrName, boolean unknownAttrNameAllowed, String infoString, TagAttributeInfo[] attributeInfo) {
		this.tagName = tagName;
		this.tagClassName = tagClassName;
		this.infoString = infoString;
		this.attributeInfo = attributeInfo;
		this.bodyContent = bodycontent;
		this.defaultAttrName = defaultAttrName;
		this.unknownAttrNameAllowed = unknownAttrNameAllowed;
		this.attributeInfoMap = new HashMap();
		int cnt = 0;
		for (int i = 0; i < attributeInfo.length; ++i) {
			TagAttributeInfo attrInfo = attributeInfo[i];
			this.attributeInfoMap.put(attrInfo.getName(), attrInfo);
			if (attrInfo.isRequired()) {
				++cnt;
			}
		}
		this.numberOfRequiredAttributes = cnt;
	}

	public String getTagName() {
		return tagName;
	}

	public TagAttributeInfo[] getAttributes() {
		return attributeInfo;
	}

	public TagAttributeInfo getAttribute(String attributeName) {
		return (TagAttributeInfo) this.attributeInfoMap.get(attributeName);
	}

	public String getTagClassName() {
		return tagClassName;
	}

	public String getBodyContent() {
		return bodyContent;
	}

	public String getInfoString() {
		return infoString;
	}

	public String getDefaultAttributeName() {
		return defaultAttrName;
	}

	public int getNumberOfRequiredAttributes() {
		return numberOfRequiredAttributes;
	}

	public String toString() {
		StringBuffer b = new StringBuffer();
		b.append("{name=" + tagName);
		b.append(",class=" + tagClassName);
		b.append(",body=" + bodyContent);
		b.append(",default attribute name=" + defaultAttrName);
		b.append(",unknownAttrNameAllowed=" + unknownAttrNameAllowed);
		b.append(",info=" + infoString);
		b.append(",attributes = {");
		for (int i = 0; i < attributeInfo.length; i++) {
			b.append(attributeInfo[i].toString());
		}
		b.append("}");
		return b.toString();
	}

	public boolean isUnknownAttrNameAllowed() {
		return unknownAttrNameAllowed;
	}
}
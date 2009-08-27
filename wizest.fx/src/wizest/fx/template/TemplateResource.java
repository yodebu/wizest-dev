package wizest.fx.template;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import wizest.fx.configuration.Configuration;
import wizest.fx.configuration.ConfigurationException;
import wizest.fx.configuration.NotFoundXmlTagException;
import wizest.fx.logging.LogBroker;
import wizest.fx.template.parse.ParserFactory;
import wizest.fx.template.tagext.BodyTagSupport;
import wizest.fx.template.tagext.PageTagSupport;
import wizest.fx.template.tagext.TagAttributeInfo;
import wizest.fx.template.tagext.TagInfo;
import wizest.fx.template.tagext.TagLibraryInfo;
import wizest.fx.template.tagext.TagSupport;
import wizest.fx.util.InvalidSetupException;


public class TemplateResource {
    private final TagLibraryInfo libraryInfo;
    private final TagHandlerHost handlerHost;
    private final ParserHost tagParserHost;
    private final ParserHost tagNameParserHost;
    private final ParserHost attrParserHost;
    private final ParserHost attrNameParserHost;
    private final ParserHost attrValueParserHost;
    private final ParserHost attrValueEvalParserHost;
//    private final Logger log;

    public TemplateResource(TagLibraryInfo libraryInfo, ParserFactory parserFactory) throws InvalidSetupException {
//        this.log = LogBroker.getLogger(this);
        this.handlerHost = new TagHandlerHost(libraryInfo);
        this.tagParserHost = new ParserHost(parserFactory.getTagParserClass());
        this.tagNameParserHost = new ParserHost(parserFactory.getTagNameParserClass());
        this.attrParserHost = new ParserHost(parserFactory.getAttributeParserClass());
        this.attrNameParserHost = new ParserHost(parserFactory.getAttributeNameParserClass());
        this.attrValueParserHost = new ParserHost(parserFactory.getAttributeValueParserClass());
        this.attrValueEvalParserHost = new ParserHost(parserFactory.getAttributeValueEvalParserClass());
        this.libraryInfo = libraryInfo;
    }

    public static TemplateResource newTemplateResource(Configuration taglib, ParserFactory parserFactory) throws InvalidSetupException {
        TagLibraryInfo libraryInfo = readTagLibraryInfo(taglib);
        return new TemplateResource(libraryInfo, parserFactory);
    }

    public final TagLibraryInfo getTagLibraryInfo() {
        return this.libraryInfo;
    }

    public final TagHandlerHost getTagHandlerHost() {
        return this.handlerHost;
    }

    public final ParserHost getTagNameParserHost() {
        return tagNameParserHost;
    }

    public final ParserHost getTagParserHost() {
        return tagParserHost;
    }

    public final ParserHost getAttrNameParserHost() {
        return attrNameParserHost;
    }

    public final ParserHost getAttrParserHost() {
        return attrParserHost;
    }

    public final ParserHost getAttrValueParserHost() {
        return attrValueParserHost;
    }

    public final ParserHost getAttrValueEvalParserHost() {
        return attrValueEvalParserHost;
    }

    //    public final ParserFactory getParserFactory() {
    //        return this.parserFactory;
    //    }
    //  *
    //  * *.����
    //  *
    //  * +: �ݵ�� �ʿ�
    //  * -: ��� �������
    //  *
    //  * [+translator]
    //  * [+actiontag]
    //  * +[+short-name]
    //  * +[+default-tag-name] : default�� ����� tag handler �̸�
    //  * +[+default-page-tag-name] : default�� ����� page tag �̸� (page tag�� ���� template�� ���� �⺻ ó�� �ڵ鷯 ����)
    //  * +[-decription]
    //  * +[-tag] : Tag ���� (������ ���� ����)
    //  * +[+name] : Tag �̸�
    //  * +[-alias] : Tag ���� (�̸��� ���� ȿ��) ',' �� �����Ͽ� ������ ��������
    //  * +[+tag-class] : Tag handler (the fully qualified name)
    //  * +[+body-content] : Tag �� �ѷ����� �κ��� ���� -> [ ACTIONTAG | TAGDEPENDENT | EMPTY ] -> ACTIONTAG: body�ȿ� �� ActionTag�� �ִ�, TAGDEPENDENT:TAG handler�� å�� ���� ó���� ���̹Ƿ� Action Tag ������ ���� �� ��, EMPTY:body�� �������� ���� ���
    //  * +[-default-attribute-name] : attribute �̸��� ���� ���� �ִ� attribute�� ��� ������ ������ ���� (�������� ���� ��� default attribute�� ����� �� ����.)
    //  * +[allow-unknown-attribute-name] : ���ǵ��� �ʴ� attribute name�� ������� ���� (true/false)
    //  * +[-description]
    //  * +[-attribute] : Tag �� attribute ���� (������ ���� ����)
    //  * +[+name]
    //  * +[-required] : attribute�� �ʿ俩�� (true/false)
    //  * +[-type] : attribute�� Ÿ�� (the fully qualified class name, type name) ex) java.lang.String, int
    //  * +[-rtexprvalue] : ������ �Ӽ����� �������� ���� (true/false)
    //  * +[-description]
    //  *
    //      <taglib>
    //          <short-name>Action Tag for ICS</short-name>
    //          <default-tag-name>Example</default-tag-name>
    //          <default-page-tag-name>Page</default-page-tag-name>
    //          <inner-page-tag-name>_InnerPage</inner-page-tag-name>
    //          <description>Action tag descriptor</description>
    //          <tag>
    //              <name>Page</name>
    //              <tag-class>wizest.cms.publish.actiontag.PageTag</tag-class>
    //              <body-content>PAGE</body-content>
    //				<allow-unknown-attribute-name>false</allow-unknown-attribute-name>
    //              <description>...</description>
    //          </tag>
    //          <tag>
    //              <name>ExampleLayout</name>
    //              <tag-class>wizest.cms.publish.actiontag.ExampleLayoutTag</tag-class>
    //              <body-content>ACTIONTAG</body-content>
    //              <alias>Example2,Example3</alias>
    //              <description>���̾ƿ� ���ִ� �ױ�</description>
    //              <default-attribute-name>title</default-attribute-name>
    //				<allow-unknown-attribute-name>false</allow-unknown-attribute-name>
    //              <attribute>
    //                  <name>cols</name>
    //                  <required>true</required>
    //                  <type>int</type><rtexprvalue>false</rtexprvalue>
    //                  <description>�� ��</description>
    //              </attribute>
    //              <attribute>
    //                  <name>rows</name>
    //                  <required>true</required>
    //                  <type>int</type><rtexprvalue>false</rtexprvalue>
    //                  <description>�� ��</description>
    //              </attribute>
    //              <attribute>
    //                  <name>title</name>
    //                  <required>false</required>
    //                  <type>java.lang.String</type><rtexprvalue>false</rtexprvalue>
    //                  <description>����</description>
    //              </attribute>
    //          </tag>
    //          <tag>
    //              <name>Example2</name>
    //              <tag-class>wizest.cms.publish.actiontag.Example2Tag</tag-class>
    //              <body-content>EMPTY</body-content>
    //              <description>�׳� -_- ������...</description>
    //				<allow-unknown-attribute-name>false</allow-unknown-attribute-name>
    //          </tag>
    //          <tag>
    //              <name>Example3</name>
    //              <tag-class>wizest.cms.publish.actiontag.Example3Tag</tag-class>
    //              <body-content>TAGDEPENDENT</body-content>
    //              <description>���� �ױ�3</description>
    //				<allow-unknown-attribute-name>false</allow-unknown-attribute-name>
    //              <attribute>
    //                  <name>width</name>
    //                  <required>true</required>
    //                  <type>int</type><rtexprvalue>false</rtexprvalue>
    //                  <description>��</description>
    //              </attribute>
    //              <attribute>
    //                  <name>height</name>
    //                  <required>true</required>
    //                  <type>int</type><rtexprvalue>false</rtexprvalue>
    //                  <description>����</description>
    //              </attribute>
    //              <attribute>
    //                  <name>alt</name>
    //                  <required>false</required><rtexprvalue>false</rtexprvalue>
    //                  <description>ǳ�� ����</description>
    //              </attribute>
    //          </tag>
    //      </taglib>
    public static TagLibraryInfo readTagLibraryInfo(Configuration taglib) throws InvalidSetupException {
        Logger log = LogBroker.getLogger(TemplateResource.class);
        // setup libraryInfo from 'taglib' configuration
        {
            if (!taglib.getTagName().equals("taglib")) {
                throw new InvalidSetupException("invalid configuration.");
            }
            try {
                Configuration[] tags = taglib.getChildren("tag");
                ArrayList tagInfoList = new ArrayList();
                for (int i = 0; i < tags.length; ++i) {
                    Configuration tag = tags[i];
                    String tagName = tag.getChild("name").getValue().toUpperCase();
                    String tagClass = tag.getChild("tag-class").getValue();
                    String bodyContent = tag.getChild("body-content").getValue();
                    // validate tag-class, body-content
                    try {
                        Class cls = Class.forName(tagClass);
                        if (PageTagSupport.class.isAssignableFrom(cls)) {
                            if (!(bodyContent.equals(TagInfo.BODY_CONTENT_PAGE))) {
                                throw new InvalidSetupException("incorrect body-content at " + tagName + " tag");
                            }
                        } else if (BodyTagSupport.class.isAssignableFrom(cls)) {
                            if (!(bodyContent.equals(TagInfo.BODY_CONTENT_ACTION_TAG) || bodyContent.equals(TagInfo.BODY_CONTENT_TAG_DEPENDENT))) {
                                throw new InvalidSetupException("incorrect body-content at " + tagName + " tag");
                            }
                        } else if (TagSupport.class.isAssignableFrom(cls)) {
                            if (!bodyContent.equals(TagInfo.BODY_CONTENT_EMPTY)) {
                                throw new InvalidSetupException("incorrect body-content at " + tagName + " tag");
                            }
                        } else {
                            throw new InvalidSetupException("invalid tag-class at " + tagName);
                        }
                    } catch (ClassNotFoundException cfe) {
                        throw new InvalidSetupException("invalid tag-class at " + tagName, cfe);
                    }
                    String defaultAttrName;
                    try {
                        defaultAttrName = tag.getChild("default-attribute-name").getValue().toUpperCase();
                    } catch (NotFoundXmlTagException ex) {
                        defaultAttrName = null;
                    }
                    boolean unknownAttrNameAllowed;
                    try {
                        unknownAttrNameAllowed = Boolean.valueOf(tag.getChild("allow-unknown-attribute-name").getValue()).booleanValue();
                    } catch (NotFoundXmlTagException ex) {
                        unknownAttrNameAllowed = false;
                    }
                    String infoString;
                    try {
                        infoString = tag.getChild("description").getValue();
                    } catch (NotFoundXmlTagException ex3) {
                        infoString = "";
                    }
                    TagAttributeInfo[] tagAttributeInfo;
                    Configuration[] attributes = null;
                    try {
                        attributes = tag.getChildren("attribute");
                        tagAttributeInfo = new TagAttributeInfo[attributes.length];
                        for (int j = 0; j < attributes.length; ++j) {
                            Configuration attribute = attributes[j];
                            String attrName = attribute.getChild("name").getValue().toUpperCase();
                            boolean required;
                            String type;
                            boolean rtexprvalue;
                            try {
                                required = attribute.getChild("required").getValueAsBoolean();
                            } catch (NotFoundXmlTagException ex1) {
                                required = false;
                            }
                            try {
                                type = attribute.getChild("type").getValue();
                            } catch (NotFoundXmlTagException ex2) {
                                type = "java.lang.String";
                            }
                            try {
                                rtexprvalue = attribute.getChild("rtexprvalue").getValueAsBoolean(false);
                            } catch (NotFoundXmlTagException ex2) {
                                rtexprvalue = false;
                            }
                            tagAttributeInfo[j] = new TagAttributeInfo(attrName, required, type, rtexprvalue);
                        }
                    } catch (NotFoundXmlTagException ex) {
                        tagAttributeInfo = new TagAttributeInfo[0];
                    }
                    tagInfoList.add(new TagInfo(tagName, tagClass, bodyContent, defaultAttrName, unknownAttrNameAllowed, infoString, tagAttributeInfo));
                    // alias ����
                    try {
                        String alias = tag.getChild("alias").getValue().toUpperCase();
                        StringTokenizer tokener = new StringTokenizer(alias, ",");
                        while (tokener.hasMoreTokens()) {
                            tagInfoList.add(new TagInfo(tokener.nextToken().trim(), tagClass, bodyContent, defaultAttrName, unknownAttrNameAllowed, infoString, tagAttributeInfo));
                        }
                    } catch (NotFoundXmlTagException nf) {
                    }
                }
                String libShortName = taglib.getChild("short-name").getValue();
                String defaultTagName = null;
                String defaultPageTagName = null;
                String innerPageTagName = null;
                try {
                    defaultTagName = taglib.getChild("default-tag-name").getValue().toUpperCase();                    
                } catch (NotFoundXmlTagException ex5) {
                    log.severe("not found <default-tag-name/>");
                }
                try {
                    defaultPageTagName = taglib.getChild("default-page-tag-name").getValue().toUpperCase();
                } catch (NotFoundXmlTagException ex6) {
                    log.severe("not found <default-page-tag-name/>");
                }
                try {
                    innerPageTagName = taglib.getChild("inner-page-tag-name").getValue().toUpperCase();
                } catch (NotFoundXmlTagException ex7) {
                    log.severe("not found <inner-page-tag-name/>");
                }
                TagInfo[] tagInfo = (TagInfo[]) tagInfoList.toArray(new TagInfo[0]);
                TagLibraryInfo tagLibraryInfo = new TagLibraryInfo(libShortName, defaultPageTagName, defaultTagName, innerPageTagName, tagInfo);
                return tagLibraryInfo;
            } catch (ConfigurationException ex) {
                throw new InvalidSetupException(ex);
            }
        }
    }
}
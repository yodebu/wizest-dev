package wizest.fx.template;

import java.util.ArrayList;
import java.util.Stack;

import wizest.fx.template.parse.TagNameParser;
import wizest.fx.template.parse.TagParser;
import wizest.fx.template.tagext.AttributeMap;
import wizest.fx.template.tagext.BodyTagSupport;
import wizest.fx.template.tagext.PageTagSupport;
import wizest.fx.template.tagext.TagAttributeInfo;
import wizest.fx.template.tagext.TagInfo;
import wizest.fx.template.tagext.TagLibraryInfo;
import wizest.fx.template.tagext.TagSupport;
import wizest.fx.util.InstanceCreationException;
import wizest.fx.util.InstanceCreator;
import wizest.fx.util.Marker;
import wizest.fx.util.Parser;
import wizest.fx.util.ParserException;

/**
 * @author wizest
 */
public class TemplateCompiler {
    public static final int COMPILE_MODE_OUTER_TEMPLATE = Template.TYPE_OUTER_TEMPLATE;
    public static final int COMPILE_MODE_INNER_TEMPLATE = Template.TYPE_INNER_TEMPLATE;
    private TagLibraryInfo libraryInfo = null;
    private ParserHost tagParserHost = null;
    private ParserHost tagNameParserHost = null;
    private ParserHost attrParserHost = null;
    private ParserHost attrNameParserHost = null;
    private ParserHost attrValueParserHost = null;

    public TemplateCompiler(TemplateResource tr) {
        this.libraryInfo = tr.getTagLibraryInfo();
        this.tagParserHost = tr.getTagParserHost();
        this.tagNameParserHost = tr.getTagNameParserHost();
        this.attrParserHost = tr.getAttrParserHost();
        this.attrNameParserHost = tr.getAttrNameParserHost();
        this.attrValueParserHost = tr.getAttrValueParserHost();
    }

    /**
     * handler�� reflection���� ���� ���� ���� �� ����� parameter���� �����Ѵ�.
     * 
     * @param tagName
     *            TagSupport �� id �ʵ忡 ������ �� (template���� ����� tag�̸�), ����: tag handler �̸��� �ƴϴ�.
     * @param tagInfo
     * @param tagMark
     * @return
     * @throws CompilerException
     */
    private ReflectionMap retrieveParametersToReflectAttributes(final String tagName, final TagInfo tagInfo, final Marker tagMark) throws CompilerException {
        Parser ap = this.attrParserHost.getParser();
        Parser np = this.attrNameParserHost.getParser();
        Parser vp = this.attrValueParserHost.getParser();
        try {
            ArrayList names = new ArrayList();
            ArrayList values = new ArrayList();
            String name;
            Object value;
            Marker attrMark = null;
            Marker nameMark = null;
            Marker valueMark = null;
            @SuppressWarnings("unused")
            boolean existsDefaultAttribute = false;
            int numberOfRequiredAttributes = 0;
            ap.initialize(tagMark);
            try {
                while (true) {
                    // reset
                    name = null;
                    value = null;
                    // set attrMark
                    attrMark = ap.parseNext();
                    if (attrMark == null) {
                        break; // ending condition
                    }
                    // end of attrMark
                    np.initialize(attrMark);
                    vp.initialize(attrMark);
                    // set name
                    nameMark = np.parseNext();
                    if (nameMark == null) {
                        // default attribute ó��
                        // �߼� �Ӽ����� ���� ����ó���� ���� �ʴ´�.
                        // ���� ���� ���� ��� �ش� ������ ��ŭ set �޼ҵ尡 ���� �� ȣ��ȴ�.
                        // if (existsDefaultAttribute) {
                        //  throw new CompilerException(tagMark, "duplicated default attribute at '" + attrMark.getValue() + "'");
                        // } else
                        {
                            existsDefaultAttribute = true;
                            name = tagInfo.getDefaultAttributeName(); // default attribute
                            // default attribute�� �����Ǿ� ���� ���� ���
                            if (name == null) {
                                throw new CompilerException(tagMark, "does not allow the default attribute at '" + attrMark.getValue() + "'");
                            }
                        }
                    } else
                        name = nameMark.getValue();
                    // end of name
                    // set value
                    valueMark = vp.parseNext();
                    if (valueMark == null) {
                        throw new CompilerException(tagMark, "null value at '" + attrMark.getValue() + "'");
                    }
                    String valueStr = valueMark.getValue();
                    TagAttributeInfo attrInfo = tagInfo.getAttribute(name);
                    if (attrInfo == null) {
                        if (tagInfo.isUnknownAttrNameAllowed())
                            value = valueStr;
                        else
                            throw new CompilerException(tagMark, "unknown attribute name at '" + attrMark.getValue() + "'");
                    } else {
                        if (attrInfo.isRequired()) {
                            numberOfRequiredAttributes++;
                        }
                        // if run-time value
                        if (attrInfo.isRtexprvalue()) {
                            value = valueStr; // template run-time environment���� �� ���� ��ó��������Ѵ�!!!
                        }
                        // if compile-time value
                        else {
                            String className = attrInfo.getTypeName();
                            try {
                                value = InstanceCreator.getObjectInstance(className, valueStr);
                            } catch (InstanceCreationException ex) {
                                throw new CompilerException(tagMark, "incorrect attribute type at '" + attrMark.getValue() + "'", ex);
                            } catch (ClassNotFoundException ex) {
                                throw new CompilerException(tagMark, "incorrect attribute type. class not found:" + className + " at '" + attrMark.getValue() + "'", ex);
                            }
                        }
                    }
                    names.add(name);
                    values.add(value);
                }
                if (numberOfRequiredAttributes < tagInfo.getNumberOfRequiredAttributes()) {
                    StringBuffer buf = new StringBuffer("too few required attributes, {");
                    TagAttributeInfo[] as = tagInfo.getAttributes();
                    for (int i = 0, len = as.length; i < len; ++i)
                        if (as[i].isRequired())
                            buf.append(" " + as[i].getName());
                    buf.append(" }");
                    throw new CompilerException(tagMark, buf.toString());
                }
                final String[] arrNames = (String[]) names.toArray(new String[0]);
                final Object[] arrValues = values.toArray();
                ReflectionMap rm = new ReflectionMap(arrNames, arrValues);
                // �߰��� static property
                {
                    // tag name�� TagSupport.id �ʵ忡 �����Ѵ�.
                    // Tag handler���� template���� � �̸����� handler�� invoke�ߴ��� �˱� ����
                    // (default tag ���� ��� ������ handler�� �پ��� �̸����� �θ���.)
                    rm.append(ReflectionMap.STATIC_PROPERTY_id, tagName);
                    // tagMark�� �����Ѵ�.
                    rm.append(ReflectionMap.STATIC_PROPERTY_tagMarker, tagMark);
                    // attributeMap�� �����Ѵ�. (runtime eval�� ���� �ʴ´�.)
                    rm.append(ReflectionMap.STATIC_PROPERTY_attributeMap, new AttributeMap(arrNames, arrValues));
                }
                return rm;
            } catch (ParserException pex) {
                throw new CompilerException(tagMark, "parsing exception at '" + attrMark.getValue() + "'", pex);
            }
        } finally {
            ap.release();
            np.release();
            vp.release();
            this.attrParserHost.releaseParser(ap);
            this.attrNameParserHost.releaseParser(np);
            this.attrValueParserHost.releaseParser(vp);
        }
    }

    /**
     * ���� tag parser�� ���� �ִ� body�� end tag �ٷ� ������ �ǳʶڴ�. (�� tag�� body content�� tag dependent��� �����Ѵ�)
     * 
     * @param tagParser
     * @throws ParserException
     */
    private void parseToEndTagOfCurrentTagDependentBody(Marker nameStartMark, Parser tagParser) throws ParserException {
        /**
         * ����) start tag�� ��Ÿ���� depth�� 1�� �����ϰ� end tag�� ��Ÿ���� depth�� 1�� �����Ѵ�.
         * 
         * ���� ���� ������ ����� �Ǿ� �ִٸ� depth�� 0�� �Ǵ� ���� tag�� ������ �����̴�.
         */
        int depth = 1; // �� �޼ҵ带 �θ� ���¿� �̹� ������ �ִ� ����(start tag�� ��������) �̹Ƿ� 1�� ����
        Marker mark = null;
        TagNameParser nameParser = (TagNameParser) this.tagNameParserHost.getParser();
        String tagName = nameParser.getRealTagName(nameStartMark);
        try {
            do {
                mark = tagParser.parseNext();
                if (mark == null) {
                    throw new ParserException("could not find the corresponding end tag.");
                }
                nameParser.initialize(mark);
                Marker nameMark = nameParser.parseNext();
                if (tagName.equals(nameParser.getRealTagName(nameMark)))
                    if (nameParser.isBodyTag(nameMark)) {
                        if (nameParser.isStartTag(nameMark))
                            ++depth;
                        else
                            --depth;
                    }
            } while (depth > 0);
            // end tag�� ���� �κ��� ���� parsing point�� �����Ѵ�.
            tagParser.setParsingPoint(mark.getBeginIndex());
        } finally {
            nameParser.release();
            this.tagNameParserHost.releaseParser(nameParser);
        }
    }

    public TemplateBinary compile(Template template) throws CompilerException {
        // ����!!!
        // template�� template type�� compile mode �� ������ ���� �����ٰ� ����
        return compile(template.getTemplateText(), template.getTemplateType(), true);
    }

    public TemplateBinary compile(String templateText) throws CompilerException {
        return compile(templateText, COMPILE_MODE_OUTER_TEMPLATE, true);
    }

    /**
     * @param templateText
     * @param COMPILE_MODE
     * @param strictHierarchyMode
     *            �ױ� ���� ���踦 ��ٷӰ� �м����� ����, ���� �ױ׸� �ݱ� ���� ���� �ױ׸� ���� ���, start/end �ױ��� ���� ���� ���� ���, body�� ���� �� ���µ� body�� ������ �ױ� ��
     * @return
     * @throws CompilerException
     */
    public TemplateBinary compile(final String templateText, final int COMPILE_MODE, final boolean strictHierarchyMode) throws CompilerException {
        OperationCode code = new OperationCode();
        OperationData data = new OperationData();
        Marker tagMark = null;
        Marker nameMark = null;
        TagParser tagP = (TagParser) this.tagParserHost.getParser();
        TagNameParser nameP = (TagNameParser) this.tagNameParserHost.getParser();
        int lineNumber = 1;
        try {
            tagP.initialize(templateText);
            int prevParsedPoint = 0;
            TagInfo tagInfo = null;
            String tagName = null;
            String tagHandlerName = null;
            // Page Tag ó��
            boolean existsCustomPageTagHandler = false;
            int addrPageTagHandlerName = 0; // data addr
            int addrPageTagHandlerReflectionMap = 0; // data addr
            // reset vector : entry point
            int addrResetVector = code.add(OperationCode.OPCODE_TERMINATE);
            // address
            int addrTagHandlerName; // tag handler name�� ����Ǿ� �ִ� �ּ� - data addr
            // special address table (�Ʒ��� ������ �ſ� �߿���!!!!!!!!!! - �����ϰ� �����ϱ� �ٶ� (code�� �ִ� ����)
            int addrGenericException = code.add(OperationCode.OPCODE_EXCEPTION, data.add("incompleted template binary")); // code addr
            int addrEnd = code.add(OperationCode.OPCODE_NO_OPERATION); // page�� ������ ���� - code addr
            int addrTermination = code.add(OperationCode.OPCODE_TERMINATE); // translation�� ����Ǵ� ���� - code addr
            int addrStart = code.add(OperationCode.OPCODE_NO_OPERATION); // page�� ���� �Ǵ� ���� - code addr
            // end of address
            // body ó���� ���� stack
            Stack tagNameStack = new Stack();
            class IntStack {
                final static int MAX_SIZE = 1024;//256;
                int[] buf;
                int ptr;

                public IntStack() {
                    this.buf = new int[MAX_SIZE];
                }

                public int popInt() {
                    return buf[--ptr];
                }

                public int pushInt(int item) {
                    return buf[ptr++] = item;
                }
            }
            IntStack addrAfterBodyStack = new IntStack();
            IntStack addrDoEndTagStack = new IntStack();
            // core start
            while (true) {
                tagMark = tagP.parseNext();
                // debug
                //                System.out.println(tagNameStack);
                //                System.out.println(tagMark);
                // end of debug
                // print <- ending condition ����
                if (tagMark == null) { // �� �̻� tag�� ���� �� text�� ������
                    code.add(OperationCode.OPCODE_PRINT, data.add(tagP.filterComments(templateText.substring(prevParsedPoint))));
                    // ������ ������ ���� ���� �ױװ� �ִ� ���̴�.
                    if (!tagNameStack.isEmpty()) {
                        if (strictHierarchyMode) {
                            String tagN = (String) tagNameStack.peek();
                            throw new CompilerException(null, "incorrect tag hierarchy, too few end tag: the end tag of '" + tagN + "' not found in Line " + lineNumber);
                        } else {
                            // ���ÿ� ���� �ױ׸� ��� �ݴ´�.
                            do {
                            	@SuppressWarnings("unused")
                                String tagN = (String) tagNameStack.pop();
                                int addrAfterBody = addrAfterBodyStack.popInt();
                                int addrDoEndTag = addrDoEndTagStack.popInt();
                                code.add(OperationCode.OPCODE_GOTO, addrAfterBody);
                                int addrReleaseHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                code.updateCodeAt(addrDoEndTag, 2, addrReleaseHandler);
                            } while (!tagNameStack.isEmpty());
                        }
                    }
                    code.add(OperationCode.OPCODE_GOTO, addrEnd); // goto end point
                    break; // loop�� ���� - ending condition!!!
                } else { // ���� tag�� ã���� �� parsing �� �� ����
                    // text
                    String text = tagP.filterComments(templateText.substring(prevParsedPoint, tagMark.getBeginIndex()));
                    if (text.length() > 0)
                        code.add(OperationCode.OPCODE_PRINT, data.add(text));
                    // line number
                    for (int cIdx = prevParsedPoint, endIdx = tagMark.getEndIndex(); cIdx < endIdx; ++cIdx)
                        if (templateText.charAt(cIdx) == '\n')
                            ++lineNumber;
                    code.add(OperationCode.OPCODE_LINE_NUMBER, lineNumber);
                    // store previous parsing point
                    prevParsedPoint = tagMark.getEndIndex(); // update point
                }
                // eval mark
                code.add(OperationCode.OPCODE_EVAL_MARK, data.add(tagMark));
                nameP.initialize(tagMark);
                nameMark = nameP.parseNext();
                if (nameMark == null)
                    continue;
                tagName = nameP.getRealTagName(nameMark);
                tagInfo = this.libraryInfo.getTag(tagName);
                if (tagInfo == null) {
                    tagInfo = this.libraryInfo.getTag(this.libraryInfo.getDefaultTagName()); // default tag handler
                    if(tagInfo == null)
                        throw new CompilerException(tagMark,"undefined default tag");
                }
                tagHandlerName = tagInfo.getTagName();
                addrTagHandlerName = data.add(tagHandlerName);
                Class handlerCls = Class.forName(tagInfo.getTagClassName());
                if (BodyTagSupport.class.isAssignableFrom(handlerCls)) { // body tag
                    if (nameP.isBodyTag(nameMark)) {
                        if (nameP.isStartTag(nameMark)) { // startTag
                            code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));
                            // SKIP_BODY�� ���� �޾��� �� ���� end tag�� parsing���� �ʾ����Ƿ� ���� jump���� �𸥴�. �׷��� gerneric exception�� ������ �ӽ÷� �ΰ� ���߿� �ּҸ� update ��Ų��.
                            // EVAL_BODY_INCLUDE�� ���� �޾��� �� body content�� �缳������ �����Ƿ� body content�� push ���� �ʱ� ���� +1�� skip�Ѵ�
                            int addrDoStartTag = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY �� ��
                                    addrGenericException, // EVAL_BODY_INCLUDE �� ��
                                    addrGenericException); // EVAL_BODY_BUFFERED �� ��
                            int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY �� ��
                                    addrGenericException); // EVAL_BODY_AGAIN �� ��
                            int addrDoEndTag = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
                                    addrGenericException); // EVAL_PAGE �� ��
                            int addrBody = code.add(OperationCode.OPCODE_NO_OPERATION); // (body�� ���� ������ ǥ���� �� ������) compiling�� ���� �Ϸ���
                            // �� ���� generic exception�� ó�� �ߴ� �κ��� ���� �ּҷ� update��Ų��.
                            // doStartTag
                            code.updateCodeAt(addrDoStartTag, 1, addrDoEndTag);
                            code.updateCodeAt(addrDoStartTag, 2, addrBody);
                            code.updateCodeAt(addrDoStartTag, 3, addrBody);
                            // doAfterTag
                            code.updateCodeAt(addrDoAfterBody, 1, addrDoEndTag);
                            code.updateCodeAt(addrDoAfterBody, 2, addrBody);
                            // doEndTag
                            // doEndTag�� generic exception �� ���߿� end tag�� parsing�Ͽ� update�Ѵ�.
                            // endTag ���� ������ �κ��� ó���ϱ� ���� stack�� ���� �ִ´�
                            tagNameStack.push(tagName);
                            addrAfterBodyStack.pushInt(addrDoAfterBody);
                            addrDoEndTagStack.pushInt(addrDoEndTag);
                            // BODY CONTENT �� TAGDEPENDERNT�� ��� body���� action tag�� �������� �ʰ� skip �ع�����.
                            if (tagInfo.getBodyContent().equals(TagInfo.BODY_CONTENT_TAG_DEPENDENT)) {
                                try {
                                    parseToEndTagOfCurrentTagDependentBody(nameMark, tagP);
                                } catch (ParserException pe) {
                                    if (strictHierarchyMode)
                                        throw pe;
                                }
                            }
                        } else if (nameP.isEndTag(nameMark)) { // endTag
                            if (tagNameStack.isEmpty()) {
                                if (strictHierarchyMode)
                                    throw new CompilerException(tagMark, "incorrect tag hierarchy, too much end tag in Line " + lineNumber);
                                else {
                                } // ����
                            } else {
                                String tagNameToCompare = (String) tagNameStack.pop();
                                int addrAfterBody = addrAfterBodyStack.popInt();
                                int addrDoEndTag = addrDoEndTagStack.popInt();
                                if (tagName.equals(tagNameToCompare)) {
                                    code.add(OperationCode.OPCODE_GOTO, addrAfterBody);
                                    int addrReleaseHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                    code.updateCodeAt(addrDoEndTag, 2, addrReleaseHandler);
                                } else {
                                    // ������ ���� ���� �ױװ� ���� ��
                                    if (strictHierarchyMode)
                                        throw new CompilerException(tagMark, "incorrect tag hierarchy, the unclosed preceding tag exists: '" + tagNameToCompare + "' in Line " + lineNumber);
                                    else {
                                        // 1. ���ÿ� ���� �ױװ� ���� ��
                                        // 	���� �ױ׸� ���� �� ���� ���� �ױ׸� ��� ���� �� ���� �ױ׸� �ݴ´�
                                        if (tagNameStack.contains(tagName)) {
                                            // ���� �ױ� �ݱ�
                                            for (;;) {
                                                code.add(OperationCode.OPCODE_GOTO, addrAfterBody);
                                                int addrReleaseHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                                code.updateCodeAt(addrDoEndTag, 2, addrReleaseHandler);
                                                // pop
                                                tagNameToCompare = (String) tagNameStack.pop();
                                                addrAfterBody = addrAfterBodyStack.popInt();
                                                addrDoEndTag = addrDoEndTagStack.popInt();
                                                if (tagName.equals(tagNameToCompare))
                                                    break;
                                            }
                                            code.add(OperationCode.OPCODE_GOTO, addrAfterBody);
                                            int addrReleaseHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                            code.updateCodeAt(addrDoEndTag, 2, addrReleaseHandler);
                                        }
                                        // 2. ���ÿ� ���� �ױװ� ���� ��
                                        //	����
                                        else {
                                            // �ٽ� push �صд�.
                                            tagNameStack.push(tagNameToCompare);
                                            addrAfterBodyStack.pushInt(addrAfterBody);
                                            addrDoEndTagStack.pushInt(addrDoEndTag);
                                            // throw new CompilerException(tagMark,"no "+tagName+" but "+tagNameStack);
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));
                        // do start & end
                        int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : ���� jump���� ���������·� �д�(generic exception)
//                        int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue() + "'"));
//                        int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue() + "'"));
                        int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY �� ��
                                addrGenericException, // EVAL_BODY_INCLUDE �� ��
                                addrGenericException); // EVAL_BODY_BUFFERED �� ��
                        int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY �� ��
                                addrGenericException); // EVAL_BODY_AGAIN �� ��
                        int addrDoEnd = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
                                code.getAfterAddressToAdd()); // EVAL_PAGE �� ��
                        code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                        code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)�� �������� goto target�� exception �κ��� �ǳʶٰ� do start�� �����Ѵ�.
                        // doStartTag
                        code.updateCodeAt(addrDoStart, 1, addrDoEnd); // addrDoStart�� �������� �ּ� ����
                        code.updateCodeAt(addrDoStart, 2, addrDoAfterBody);
                        code.updateCodeAt(addrDoStart, 3, addrDoAfterBody);
                        // doAfterTag
                        code.updateCodeAt(addrDoAfterBody, 1, addrDoEnd);
                        code.updateCodeAt(addrDoAfterBody, 2, addrDoAfterBody);
                    }
                } else if (TagSupport.class.isAssignableFrom(handlerCls)) { // non-body tag
                    if (nameP.isBodyTag(nameMark)) {
                        if (strictHierarchyMode)
                            throw new CompilerException(tagMark, "'" + tagName + "' should be a non-body tag in Line " + lineNumber);
                        else {
                            // body�� �����鼭 start tag�� ���ܷ� �������. eg. <br> ���� ��
                            if (nameP.isStartTag(nameMark)) {
                                code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));
                                // do start & end
                                int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : ���� jump���� ���������·� �д�(generic exception)
                                int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue() + "'"));
                                int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue() + "'"));
                                int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, code.getAfterAddressToAdd(), // SKIP_BODY �� ��
                                        addrEx1, // EVAL_BODY_INCLUDE �� ��
                                        addrEx2); // EVAL_BODY_BUFFERED �� ��
                                code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
                                        code.getAfterAddressToAdd()); // EVAL_PAGE �� ��
                                code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)�� �������� goto target�� exception �κ��� �ǳʶٰ� do start�� �����Ѵ�.
                            } else {
                                // body�� �����鼭 end tag�� ����
                                //  throw new CompilerException(tagMark, "'" + nameP.getRealTagName(nameMark) + "' should be a non-body tag");
                            }
                        }
                    } else {
                        code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));
                        // do start & end
                        int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : ���� jump���� ���������·� �д�(generic exception)
                        int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue() + "'"));
                        int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue() + "'"));
                        int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, code.getAfterAddressToAdd(), // SKIP_BODY �� ��
                                addrEx1, // EVAL_BODY_INCLUDE �� ��
                                addrEx2); // EVAL_BODY_BUFFERED �� ��
                        code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE �� ��
                                code.getAfterAddressToAdd()); // EVAL_PAGE �� ��
                        code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                        code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)�� �������� goto target�� exception �κ��� �ǳʶٰ� do start�� �����Ѵ�.
                    }
                } else if (PageTagSupport.class.isAssignableFrom(handlerCls)) { // page tag
                    // PageTag�� Body ����̰� �ƴϰ� ������� �ʴ´�.
                    if (nameP.isBodyTag(nameMark)) {
                        if (nameP.isStartTag(nameMark)) { // start form
                            if (existsCustomPageTagHandler)
                                throw new CompilerException(tagMark, "too much page tag. only one page tag allowed in Line " + lineNumber);
                            if (COMPILE_MODE == COMPILE_MODE_INNER_TEMPLATE)
                                throw new CompilerException(tagMark, "not support a page tag in a INNER template (custom action tag or article content) in Line " + lineNumber);
                            addrPageTagHandlerName = data.add(tagHandlerName);
                            addrPageTagHandlerReflectionMap = data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark));
                            existsCustomPageTagHandler = true;
                        } else { // end form
                            if (!existsCustomPageTagHandler)
                                throw new CompilerException(tagMark, "the corresponding start tag for this does not exist in Line " + lineNumber);
                        }
                    } else {
                        if (existsCustomPageTagHandler)
                            throw new CompilerException(tagMark, "too much page tag. only one page tag allowed. in Line " + lineNumber);
                        if (COMPILE_MODE == COMPILE_MODE_INNER_TEMPLATE)
                            throw new CompilerException(tagMark, "not support a page tag in a INNER template (custom action tag or article content) in Line " + lineNumber);
                        addrPageTagHandlerName = data.add(tagHandlerName);
                        addrPageTagHandlerReflectionMap = data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark));
                        existsCustomPageTagHandler = true;
                    }
                } else {
                    throw new CompilerException(tagMark, "incorrect handler, handler name=" + tagInfo.getTagName() + " in Line " + lineNumber);
                }
            }
            // end of core
            // custom page tag handler�� �����Ǿ� ���� ������ default page tag handler�� �����Ѵ�.
            if (!existsCustomPageTagHandler) {
                try {
                    String pageTagName = null;
                    if (COMPILE_MODE == COMPILE_MODE_OUTER_TEMPLATE)
                        pageTagName = this.libraryInfo.getDefaultPageTagName();
                    else if (COMPILE_MODE == COMPILE_MODE_INNER_TEMPLATE)
                        pageTagName = this.libraryInfo.getInnerPageTagName();
                    else
                        throw new IllegalArgumentException("unknown compile mode.");
                    TagInfo pageTagInfo = this.libraryInfo.getTag(pageTagName);
                    Class pageTagCls = Class.forName(pageTagInfo.getTagClassName());
                    if (!PageTagSupport.class.isAssignableFrom(pageTagCls))
                        throw new CompilerException(new Marker(0, 0, ""), "incorrect handler for page tag");
                    String pageTagParsingString = tagP.getTagOpenString() + pageTagName + tagP.getTagCloseString();
                    Marker pageTagMark = new Marker(0, pageTagParsingString.length(), pageTagParsingString);
                    addrPageTagHandlerName = data.add(pageTagName);
                    addrPageTagHandlerReflectionMap = data.add(retrieveParametersToReflectAttributes(pageTagName, pageTagInfo, pageTagMark));
                } catch (CompilerException ce) {
                    throw new CompilerException(null, "it was 'in a default page tag handler' that the compiler exception occured: " + ce.getMessage(), ce);
                }
            }
            // bind 'addrResetVector' , 'addrStart' , 'addrEnd'
            {
                int addrLoadPageTagHandler = code.add(OperationCode.OPCODE_LOAD_PAGE_HANDLER, addrPageTagHandlerName, addrPageTagHandlerReflectionMap);
                int addrStartTemplate = code.add(OperationCode.OPCODE_DO_START_TEMPLATE, addrGenericException, // SKIP_PAGE �� ���
                        addrStart); // EVAL_PAGE �� ���
                int addrAfterTemplate = code.add(OperationCode.OPCODE_DO_AFTER_PAGE, addrGenericException, // SKIP_PAGE �� ���
                        addrStart); // EVAL_PAGE_AGAIN �� ���
                int addrEndPage = code.add(OperationCode.OPCODE_DO_END_TEMPLATE);
                @SuppressWarnings("unused")
                int addrReleasePageTagHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                code.add(OperationCode.OPCODE_GOTO, addrTermination); // jump to terminate.
                // addrGenericException���� binding ���� �� ���� Ȯ�� ���´�.
                code.updateCodeAt(addrStartTemplate, 1, addrEndPage); // doStartTemplate()�� skip_page�� ��� jump �� �� ����
                code.updateCodeAt(addrAfterTemplate, 1, addrEndPage); // doAfterTemplate()�� skip_page�� ��� jump �� �� ����
                // bind reset vector
                code.updateCodeAt(addrResetVector, new int[] { OperationCode.OPCODE_GOTO, addrLoadPageTagHandler }); // doStartTemplate()�� ����
                // bind start point
                code.updateCodeAt(addrStart, new int[] { OperationCode.OPCODE_DO_CREATE_PAGEINFO }); // updatePageInfo()�� invoke�Ѵ�.
                // bind end point
                code.updateCodeAt(addrEnd, new int[] { OperationCode.OPCODE_GOTO, addrAfterTemplate }); // doAfterTemplate()�� ����
            }
        } catch (CompilerException ce) {
            throw ce;
        } catch (ClassNotFoundException cfe) {
            throw new CompilerException(tagMark, "the propery handler does not exists:" + cfe.getMessage() + " in Line " + lineNumber, cfe);
        } catch (ParserException pe) {
            throw new CompilerException(tagMark, "parsing error:" + pe.getMessage() + " in Line " + lineNumber, pe);
        } catch (Throwable t) {
            throw new CompilerException(tagMark, "unexpected error:" + t.getMessage() + " in Line " + lineNumber, t);
        } finally {
            tagP.release();
            this.tagParserHost.releaseParser(tagP);
            nameP.release();
            this.tagNameParserHost.releaseParser(nameP);
        }
        return new TemplateBinary(code, data);
    }
}
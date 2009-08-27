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
     * handler에 reflection으로 값을 집어 넣을 때 사용할 parameter들을 추출한다.
     * 
     * @param tagName
     *            TagSupport 의 id 필드에 저장할 값 (template에서 사용한 tag이름), 주의: tag handler 이름이 아니다.
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
                        // default attribute 처리
                        // 중속 속성값에 대한 예외처리를 하지 않는다.
                        // 값이 여러 개일 경우 해당 가지수 만큼 set 메소드가 여러 번 호출된다.
                        // if (existsDefaultAttribute) {
                        //  throw new CompilerException(tagMark, "duplicated default attribute at '" + attrMark.getValue() + "'");
                        // } else
                        {
                            existsDefaultAttribute = true;
                            name = tagInfo.getDefaultAttributeName(); // default attribute
                            // default attribute가 지정되어 있지 않을 경우
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
                            value = valueStr; // template run-time environment에서 이 값을 재처리해줘야한다!!!
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
                // 추가할 static property
                {
                    // tag name을 TagSupport.id 필드에 저장한다.
                    // Tag handler에서 template에서 어떤 이름으로 handler를 invoke했는지 알기 위해
                    // (default tag 같은 경우 동일한 handler를 다양한 이름으로 부른다.)
                    rm.append(ReflectionMap.STATIC_PROPERTY_id, tagName);
                    // tagMark를 저장한다.
                    rm.append(ReflectionMap.STATIC_PROPERTY_tagMarker, tagMark);
                    // attributeMap을 저장한다. (runtime eval은 하지 않는다.)
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
     * 현재 tag parser가 속해 있는 body의 end tag 바로 앞으로 건너뛴다. (단 tag의 body content가 tag dependent라고 가정한다)
     * 
     * @param tagParser
     * @throws ParserException
     */
    private void parseToEndTagOfCurrentTagDependentBody(Marker nameStartMark, Parser tagParser) throws ParserException {
        /**
         * 원리) start tag가 나타나면 depth를 1씩 증가하고 end tag가 나타나면 depth를 1씩 감소한다.
         * 
         * 만일 계층 구조가 제대로 되어 있다면 depth가 0이 되는 순간 tag가 닫히는 순간이다.
         */
        int depth = 1; // 이 메소드를 부른 상태에 이미 열려져 있는 상태(start tag를 지나쳤음) 이므로 1로 설정
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
            // end tag의 시작 부분을 다음 parsing point로 설정한다.
            tagParser.setParsingPoint(mark.getBeginIndex());
        } finally {
            nameParser.release();
            this.tagNameParserHost.releaseParser(nameParser);
        }
    }

    public TemplateBinary compile(Template template) throws CompilerException {
        // 가정!!!
        // template의 template type랑 compile mode 가 동일한 값을 가진다고 가정
        return compile(template.getTemplateText(), template.getTemplateType(), true);
    }

    public TemplateBinary compile(String templateText) throws CompilerException {
        return compile(templateText, COMPILE_MODE_OUTER_TEMPLATE, true);
    }

    /**
     * @param templateText
     * @param COMPILE_MODE
     * @param strictHierarchyMode
     *            테그 계층 관계를 까다롭게 분석할지 여부, 선행 테그를 닫기 전에 후행 테그를 닫은 경우, start/end 테그의 수가 같지 않은 경우, body를 가질 수 없는데 body를 가지는 테그 등
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
            // Page Tag 처리
            boolean existsCustomPageTagHandler = false;
            int addrPageTagHandlerName = 0; // data addr
            int addrPageTagHandlerReflectionMap = 0; // data addr
            // reset vector : entry point
            int addrResetVector = code.add(OperationCode.OPCODE_TERMINATE);
            // address
            int addrTagHandlerName; // tag handler name이 저장되어 있는 주소 - data addr
            // special address table (아래는 순서가 매우 중요함!!!!!!!!!! - 신중하게 변동하길 바람 (code에 넣는 순서)
            int addrGenericException = code.add(OperationCode.OPCODE_EXCEPTION, data.add("incompleted template binary")); // code addr
            int addrEnd = code.add(OperationCode.OPCODE_NO_OPERATION); // page가 끝나는 지점 - code addr
            int addrTermination = code.add(OperationCode.OPCODE_TERMINATE); // translation이 종료되는 지점 - code addr
            int addrStart = code.add(OperationCode.OPCODE_NO_OPERATION); // page가 시작 되는 지점 - code addr
            // end of address
            // body 처리를 위한 stack
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
                // print <- ending condition 포함
                if (tagMark == null) { // 더 이상 tag가 없을 때 text의 끝까지
                    code.add(OperationCode.OPCODE_PRINT, data.add(tagP.filterComments(templateText.substring(prevParsedPoint))));
                    // 스택이 남으면 닫지 않은 테그가 있는 것이다.
                    if (!tagNameStack.isEmpty()) {
                        if (strictHierarchyMode) {
                            String tagN = (String) tagNameStack.peek();
                            throw new CompilerException(null, "incorrect tag hierarchy, too few end tag: the end tag of '" + tagN + "' not found in Line " + lineNumber);
                        } else {
                            // 스택에 남은 테그를 모두 닫는다.
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
                    break; // loop를 종료 - ending condition!!!
                } else { // 다음 tag를 찾았을 때 parsing 된 곳 까지
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
                            // SKIP_BODY를 리턴 받았을 때 아직 end tag를 parsing하지 않았으므로 어디로 jump할지 모른다. 그래서 gerneric exception을 내도록 임시로 두고 나중에 주소를 update 시킨다.
                            // EVAL_BODY_INCLUDE를 리턴 받았을 때 body content를 재설정하지 않으므로 body content를 push 하지 않기 위해 +1로 skip한다
                            int addrDoStartTag = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY 일 때
                                    addrGenericException, // EVAL_BODY_INCLUDE 일 때
                                    addrGenericException); // EVAL_BODY_BUFFERED 일 때
                            int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY 일 때
                                    addrGenericException); // EVAL_BODY_AGAIN 일 때
                            int addrDoEndTag = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
                                    addrGenericException); // EVAL_PAGE 일 때
                            int addrBody = code.add(OperationCode.OPCODE_NO_OPERATION); // (body의 시작 지점을 표시할 수 있으니) compiling을 쉽게 하려고
                            // 위 에서 generic exception로 처리 했던 부분을 실제 주소로 update시킨다.
                            // doStartTag
                            code.updateCodeAt(addrDoStartTag, 1, addrDoEndTag);
                            code.updateCodeAt(addrDoStartTag, 2, addrBody);
                            code.updateCodeAt(addrDoStartTag, 3, addrBody);
                            // doAfterTag
                            code.updateCodeAt(addrDoAfterBody, 1, addrDoEndTag);
                            code.updateCodeAt(addrDoAfterBody, 2, addrBody);
                            // doEndTag
                            // doEndTag의 generic exception 은 나중에 end tag를 parsing하여 update한다.
                            // endTag 에서 나머지 부분을 처리하기 위해 stack에 집에 넣는다
                            tagNameStack.push(tagName);
                            addrAfterBodyStack.pushInt(addrDoAfterBody);
                            addrDoEndTagStack.pushInt(addrDoEndTag);
                            // BODY CONTENT 가 TAGDEPENDERNT일 경우 body내의 action tag는 번역하지 않고 skip 해버린다.
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
                                } // 무시
                            } else {
                                String tagNameToCompare = (String) tagNameStack.pop();
                                int addrAfterBody = addrAfterBodyStack.popInt();
                                int addrDoEndTag = addrDoEndTagStack.popInt();
                                if (tagName.equals(tagNameToCompare)) {
                                    code.add(OperationCode.OPCODE_GOTO, addrAfterBody);
                                    int addrReleaseHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                    code.updateCodeAt(addrDoEndTag, 2, addrReleaseHandler);
                                } else {
                                    // 닫히지 않은 선행 테그가 있을 때
                                    if (strictHierarchyMode)
                                        throw new CompilerException(tagMark, "incorrect tag hierarchy, the unclosed preceding tag exists: '" + tagNameToCompare + "' in Line " + lineNumber);
                                    else {
                                        // 1. 스택에 현재 테그가 있을 때
                                        // 	현재 테그를 만날 때 까지 선행 테그를 모두 닫은 후 현재 테그를 닫는다
                                        if (tagNameStack.contains(tagName)) {
                                            // 선행 테그 닫기
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
                                        // 2. 스택에 현재 테그가 없을 때
                                        //	무시
                                        else {
                                            // 다시 push 해둔다.
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
                        int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : 어디로 jump할지 미지정상태로 둔다(generic exception)
//                        int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue() + "'"));
//                        int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue() + "'"));
                        int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, addrGenericException, // SKIP_BODY 일 때
                                addrGenericException, // EVAL_BODY_INCLUDE 일 때
                                addrGenericException); // EVAL_BODY_BUFFERED 일 때
                        int addrDoAfterBody = code.add(OperationCode.OPCODE_DO_AFTER_BODY, addrGenericException, // SKIP_BODY 일 때
                                addrGenericException); // EVAL_BODY_AGAIN 일 때
                        int addrDoEnd = code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
                                code.getAfterAddressToAdd()); // EVAL_PAGE 일 때
                        code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                        code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)의 미지정된 goto target을 exception 부분을 건너뛰고 do start로 지정한다.
                        // doStartTag
                        code.updateCodeAt(addrDoStart, 1, addrDoEnd); // addrDoStart의 미지정된 주소 결정
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
                            // body를 가지면서 start tag는 예외로 허용하자. eg. <br> 같은 것
                            if (nameP.isStartTag(nameMark)) {
                                code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));
                                // do start & end
                                int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : 어디로 jump할지 미지정상태로 둔다(generic exception)
                                int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue() + "'"));
                                int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue() + "'"));
                                int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, code.getAfterAddressToAdd(), // SKIP_BODY 일 때
                                        addrEx1, // EVAL_BODY_INCLUDE 일 때
                                        addrEx2); // EVAL_BODY_BUFFERED 일 때
                                code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
                                        code.getAfterAddressToAdd()); // EVAL_PAGE 일 때
                                code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                                code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)의 미지정된 goto target을 exception 부분을 건너뛰고 do start로 지정한다.
                            } else {
                                // body를 가지면서 end tag는 무시
                                //  throw new CompilerException(tagMark, "'" + nameP.getRealTagName(nameMark) + "' should be a non-body tag");
                            }
                        }
                    } else {
                        code.add(OperationCode.OPCODE_LOAD_HANDLER, addrTagHandlerName, data.add(retrieveParametersToReflectAttributes(tagName, tagInfo, tagMark)));
                        // do start & end
                        int addrGotoDoStart = code.add(OperationCode.OPCODE_GOTO, addrGenericException); // -(1) : 어디로 jump할지 미지정상태로 둔다(generic exception)
                        int addrEx1 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_INCLUDE, but not allowed at '" + tagMark.getValue() + "'"));
                        int addrEx2 = code.add(OperationCode.OPCODE_EXCEPTION, data.add("a handler returned EVAL_BODY_BUFFERED, but not allowed at '" + tagMark.getValue() + "'"));
                        int addrDoStart = code.add(OperationCode.OPCODE_DO_START_TAG, code.getAfterAddressToAdd(), // SKIP_BODY 일 때
                                addrEx1, // EVAL_BODY_INCLUDE 일 때
                                addrEx2); // EVAL_BODY_BUFFERED 일 때
                        code.add(OperationCode.OPCODE_DO_END_TAG, addrEnd, // SKIP_PAGE 일 때
                                code.getAfterAddressToAdd()); // EVAL_PAGE 일 때
                        code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                        code.updateCodeAt(addrGotoDoStart, 1, addrDoStart); // (1)의 미지정된 goto target을 exception 부분을 건너뛰고 do start로 지정한다.
                    }
                } else if (PageTagSupport.class.isAssignableFrom(handlerCls)) { // page tag
                    // PageTag는 Body 모양이건 아니건 상관하지 않는다.
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
            // custom page tag handler가 설정되어 있지 않으면 default page tag handler로 설정한다.
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
                int addrStartTemplate = code.add(OperationCode.OPCODE_DO_START_TEMPLATE, addrGenericException, // SKIP_PAGE 일 경우
                        addrStart); // EVAL_PAGE 일 경우
                int addrAfterTemplate = code.add(OperationCode.OPCODE_DO_AFTER_PAGE, addrGenericException, // SKIP_PAGE 일 경우
                        addrStart); // EVAL_PAGE_AGAIN 일 경우
                int addrEndPage = code.add(OperationCode.OPCODE_DO_END_TEMPLATE);
                @SuppressWarnings("unused")
                int addrReleasePageTagHandler = code.add(OperationCode.OPCODE_RELEASE_HANDLER);
                code.add(OperationCode.OPCODE_GOTO, addrTermination); // jump to terminate.
                // addrGenericException으로 binding 시켜 둔 것을 확정 짓는다.
                code.updateCodeAt(addrStartTemplate, 1, addrEndPage); // doStartTemplate()가 skip_page일 경우 jump 할 곳 지정
                code.updateCodeAt(addrAfterTemplate, 1, addrEndPage); // doAfterTemplate()가 skip_page일 경우 jump 할 곳 지정
                // bind reset vector
                code.updateCodeAt(addrResetVector, new int[] { OperationCode.OPCODE_GOTO, addrLoadPageTagHandler }); // doStartTemplate()로 연결
                // bind start point
                code.updateCodeAt(addrStart, new int[] { OperationCode.OPCODE_DO_CREATE_PAGEINFO }); // updatePageInfo()를 invoke한다.
                // bind end point
                code.updateCodeAt(addrEnd, new int[] { OperationCode.OPCODE_GOTO, addrAfterTemplate }); // doAfterTemplate()로 연결
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
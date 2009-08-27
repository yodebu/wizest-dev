package wizest.fx.template;

import java.util.Stack;

import wizest.fx.template.tagext.BodyTag;
import wizest.fx.template.tagext.MultiPageTag;
import wizest.fx.template.tagext.PageContext;
import wizest.fx.template.tagext.PageInfo;
import wizest.fx.template.tagext.PageWriter;
import wizest.fx.template.tagext.Tag;
import wizest.fx.template.tagext.TagAttributeInfo;
import wizest.fx.template.tagext.TagException;
import wizest.fx.template.tagext.TagInfo;
import wizest.fx.template.tagext.TagLibraryInfo;
import wizest.fx.template.tagext.TemplateContext;
import wizest.fx.template.tagext.UniPageTag;
import wizest.fx.util.Context;
import wizest.fx.util.InstanceCreationException;
import wizest.fx.util.InstanceCreator;
import wizest.fx.util.Marker;
import wizest.fx.util.Parser;
import wizest.fx.util.ParserException;

/**
 * @author wizest
 */
public class TemplateRuntime {
    private final long WATCHDOG_THRESHOLD;
    private PageReceiver pageReceiver;
    private TagLibraryInfo libraryInfo;
    private TagHandlerHost handlerHost;
    private ParserHost attrValueEvalParserHost;
    private boolean breakRequest;

    public TemplateRuntime(TemplateResource tr, PageReceiver pageReceiver, long pageTimeoutMsec) {
        this.pageReceiver = pageReceiver;
        this.libraryInfo = tr.getTagLibraryInfo();
        this.handlerHost = tr.getTagHandlerHost();
        this.attrValueEvalParserHost = tr.getAttrValueEvalParserHost();
        this.WATCHDOG_THRESHOLD = pageTimeoutMsec;
    }

    /**
     * attribute value�� ����� run-time ������ �����Ѵ�.
     * 
     * @param tagInfo
     * @param context
     *            null�� ��� context�� ���� ���� ã�� �ʴ´�. �׷��Ƿ� [%����%] -> null�� ġȯ�Ѵ�.
     * @param evalValueParser
     * @param staticMap
     *            TemplateCompiler �� ó���� reflectionMap
     * @param parentRuntimeTag
     *            ���� template runtime �� �����Ű�� �ִ� tag (baseTagSupport�� getParentRuntimeTag()�� ���� ������ tag�� ����), ���� ���������� ���� ��� null
     * @return
     * @throws RuntimeException
     */
    public static ReflectionMap evalReflectionMap(final TagInfo tagInfo, Parser evalValueParser, final ReflectionMap staticMap, Context context, Tag parentRuntimeTag) throws TemplateRuntimeException {
        ReflectionMap runtimeMap = staticMap.copy();
        // set RUNTIME_PROPERTY
        {
            runtimeMap.append(ReflectionMap.RUNTIME_PROPERTY_parentRuntimeTag, parentRuntimeTag);
        }
        _EVAL_CONDITION: {
            // 1. ���� reflectionMap�� static entry�� �� (id,tagMarker) 2���� ��� run-time ó���� �ʿ����.
            int propertyLen = runtimeMap.getNames().length;
            if (propertyLen == ReflectionMap.PROPERTY_LEN)
                return runtimeMap;
            // 2. rtexprevalue�� �ִ��� üũ -> ������ eval �� �ʿ䰡 ����.
            {
                TagAttributeInfo[] attrs = tagInfo.getAttributes();
                for (int i = 0, len = attrs.length; i < len; ++i)
                    if (attrs[i].isRtexprvalue())
                        break _EVAL_CONDITION;
                return runtimeMap;
            }
        }
        // evaluate values
        {
            String[] names = runtimeMap.getNames();
            int length = names.length;
            Object[] values = new Object[length];
            System.arraycopy(runtimeMap.getValues(), 0, values, 0, length);
            TagAttributeInfo attrInfo = null;
            int i = 0;
            try {
                for (; i < length; ++i) {
                    attrInfo = tagInfo.getAttribute(names[i]);
                    if (attrInfo != null && attrInfo.isRtexprvalue()) {
                        StringBuffer evalString = new StringBuffer();
                        // evaluate - context�� ���� name�� ã�Ƽ� ġȯ�Ѵ�.
                        {
                            String src = values[i].toString();
                            evalValueParser.initialize(src);
                            int pivot = 0;
                            for (;;) {
                                Marker mark = evalValueParser.parseNext();
                                if (mark == null) {
                                    evalString.append(src.substring(pivot));
                                    break;
                                }
                                evalString.append(src.substring(pivot, mark.getBeginIndex()));
                                if (context == null) {
                                    evalString.append("null");
                                } else {
                                    Object obj = context.findAttribute(mark.getValue());
                                    if (obj == null)
                                        throw new TemplateRuntimeException("not found the value in contexts, name=" + mark.getValue());
                                    evalString.append(obj);
                                }
                                pivot = mark.getEndIndex();
                            }
                        }
                        values[i] = InstanceCreator.getObjectInstance(attrInfo.getTypeName(), evalString.toString());
                    }
                }
                evalValueParser.release();
            } catch (ParserException ex) {
                throw new java.lang.RuntimeException(ex);
            } catch (InstanceCreationException ex) {
                throw new TemplateRuntimeException("incorrect attribute type at '" + names[i] + "'", ex);
            } catch (ClassNotFoundException ex) {
                throw new TemplateRuntimeException("incorrect attribute type. class not found:" + attrInfo.getTypeName() + " at '" + names[i] + "'", ex);
            }
            return new ReflectionMap(names, values);
        }
    }

    public void exec(final TemplateContext templateContext, final TemplateBinary compiledTemplate) throws TemplateRuntimeException {
        exec(templateContext, compiledTemplate, null);
    }

    public void exec(final TemplateContext templateContext, final TemplateBinary compiledTemplate, final Tag parentRuntimeTag) throws TemplateRuntimeException {
        resetBreakRequest();
        TemplateBinary binary = compiledTemplate;
        OperationCode code = binary.getOperationCode();
        OperationData data = binary.getOperationData();
        Tag handler = parentRuntimeTag;
        Tag parentHandler = (handler == null) ? null : handler.getParent();
        PageContext pageContext = new PageContext();
        PageWriter writer = new PageWriter();
        PageInfo pageInfo = null;
        Marker tagMark = null;
        int lineNumber = -1;
        Parser evalValueP = attrValueEvalParserHost.getParser();
        long watchDogCounter = System.currentTimeMillis();
        try {
            DECODING: {
                int cp = 0; // code pointer
                int[] opcode = null;
                Object opdata = null;
                Stack handlerStack = new Stack();
                Stack isBodyBufferedStack = new Stack();
                String handlerName = null;
                ReflectionMap reflectionMap = null;
                while (true) {
                    if (breakRequest)
                        throw new TemplateRuntimeException("User requested template-execution break.");
                    opcode = code.getCodeAt(cp);
                    // decoder
                    switch (opcode[0]) {
                    case OperationCode.OPCODE_TERMINATE:
                        break DECODING;
                    case OperationCode.OPCODE_EXCEPTION:
                        opdata = data.getDataAt(opcode[1]);
                        throw new TemplateRuntimeException(opdata.toString());
                    case OperationCode.OPCODE_GOTO:
                        cp = opcode[1];
                        break;
                    case OperationCode.OPCODE_NO_OPERATION:
                        cp++;
                        break;
                    case OperationCode.OPCODE_PRINT:
                        opdata = data.getDataAt(opcode[1]);
                        pageContext.getOut().print(opdata);
                        cp++;
                        {
                            // runtime�ڽ��� ��ȣ�ϱ� ���� page ������ ��µ� �ð��� �ʹ� ���� �ɸ��� exception �������� �����Ѵ�.
                            // ���� loop�� �ֻ�ܿ� �־����� �񱳸� �ʹ� �ϸ� ������ ������ ������ �ɸ��� �κ����� �ű�
                            if ((System.currentTimeMillis() - watchDogCounter) > WATCHDOG_THRESHOLD)
                                throw new TemplateRuntimeException("time-out to generate a page, threshold:" + WATCHDOG_THRESHOLD + "ms per page");
                        }
                        break;
                    case OperationCode.OPCODE_LOAD_HANDLER: // uni page tag handler
                        handlerStack.push(handler); // push a handler
                        handlerName = (String) data.getDataAt(opcode[1]);
                        parentHandler = handler;
                        handler = handlerHost.getTagHandler(handlerName);
                        handler.setParent(parentHandler);
                        ((UniPageTag) handler).setPageContext(pageContext); // !!!!!
                        reflectionMap = evalReflectionMap(libraryInfo.getTag(handlerName), evalValueP, (ReflectionMap) data.getDataAt(opcode[2]), pageContext, parentRuntimeTag);
                        InstanceCreator.setPropertyValues(handler, reflectionMap.getNames(), reflectionMap.getValues());
                        cp++;
                        break;
                    case OperationCode.OPCODE_LOAD_PAGE_HANDLER: // multi page tag handler
                        handlerStack.push(handler); // push a handler
                        handlerName = (String) data.getDataAt(opcode[1]);
                        parentHandler = handler;
                        handler = handlerHost.getTagHandler(handlerName);
                        handler.setParent(parentHandler);
                        ((MultiPageTag) handler).setTemplateContext(templateContext); // !!!!!
                        reflectionMap = evalReflectionMap(libraryInfo.getTag(handlerName), evalValueP, (ReflectionMap) data.getDataAt(opcode[2]), templateContext, parentRuntimeTag);
                        InstanceCreator.setPropertyValues(handler, reflectionMap.getNames(), reflectionMap.getValues());
                        cp++;
                        break;
                    case OperationCode.OPCODE_RELEASE_HANDLER:
                        handler.release();
                        handlerHost.releaseTagHandler(handlerName, handler);
                        handler = (Tag) handlerStack.pop(); // pop a handler
                        if (handler == null) {
                            handlerName = null;
                            parentHandler = null;
                        } else {
                            handlerName = libraryInfo.getTag(handler).getTagName();
                            parentHandler = handler.getParent();
                        }
                        cp++;
                        break;
                    case OperationCode.OPCODE_DO_START_TAG:
                        switch (((UniPageTag) handler).doStartTag()) {
                        case UniPageTag.SKIP_BODY:
                            isBodyBufferedStack.push(Boolean.FALSE);
                            cp = opcode[1];
                            break;
                        case UniPageTag.EVAL_BODY_INCLUDE:
                            isBodyBufferedStack.push(Boolean.FALSE);
                            cp = opcode[2];
                            break;
                        case BodyTag.EVAL_BODY_BUFFERED:
                            ((BodyTag) handler).setBodyContent(pageContext.pushBody());
                            ((BodyTag) handler).doInitBody();
                            isBodyBufferedStack.push(Boolean.TRUE);
                            cp = opcode[3];
                            break;
                        default:
                            throw new TemplateRuntimeException("incorrect return value of doStartTag() at handlerName=" + handlerName);
                        }
                        break;
                    case OperationCode.OPCODE_DO_AFTER_BODY:
                        switch (((BodyTag) handler).doAfterBody()) {
                        case UniPageTag.SKIP_BODY:
                            cp = opcode[1];
                            break;
                        case BodyTag.EVAL_BODY_AGAIN:
                            cp = opcode[2];
                            break;
                        default:
                            throw new TemplateRuntimeException("incorrect return value of doAfterBody() at handlerName=" + handlerName);
                        }
                        break;
                    case OperationCode.OPCODE_DO_END_TAG:
                        if (((Boolean) isBodyBufferedStack.pop()).booleanValue()) {
                            pageContext.popBody();
                        }
                        switch (((UniPageTag) handler).doEndTag()) {
                        case Tag.SKIP_PAGE:
                            cp = opcode[1];
                            break;
                        case Tag.EVAL_PAGE:
                            cp = opcode[2];
                            break;
                        default:
                            throw new TemplateRuntimeException("incorrect return value of doEndTag() at handlerName=" + handlerName);
                        }
                        break;
                    case OperationCode.OPCODE_DO_START_TEMPLATE:
                        switch (((MultiPageTag) handler).doStartTemplate()) {
                        case MultiPageTag.SKIP_PAGE:
                            cp = opcode[1];
                            break;
                        case MultiPageTag.EVAL_PAGE:
                            cp = opcode[2];
                            ((MultiPageTag) handler).doInitPage();
                            break;
                        default:
                            throw new TemplateRuntimeException("incorrect return value of doStartPage() at handlerName=" + handlerName);
                        }
                        break;
                    case OperationCode.OPCODE_DO_CREATE_PAGEINFO:
                        // initialize page context for uni page tags
                        pageInfo = ((MultiPageTag) handler).createPageInfo();
                        pageContext.initialize(templateContext, writer, pageInfo);
                        cp++;
                        break;
                    case OperationCode.OPCODE_DO_AFTER_PAGE:
                        boolean doGenerate = true;
                        pageContext.release();
                        switch (((MultiPageTag) handler).doAfterPage()) {
                        case MultiPageTag.SKIP_PAGE:
                            cp = opcode[1];
                            break;
                        case MultiPageTag.SKIP_PAGE_WITHOUT_PAGE_GENERATION:
                            doGenerate = false;
                            cp = opcode[1];
                            break;
                        case MultiPageTag.EVAL_PAGE_AGAIN:
                            cp = opcode[2];
                            break;
                        case MultiPageTag.EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION:
                            doGenerate = false;
                            cp = opcode[2];
                            break;
                        default:
                            throw new TemplateRuntimeException("incorrect return value of doAfterPage() at handlerName=" + handlerName);
                        }
                        // G E N E R A T E D P A G E --------------------------------------------------------------
                        if (doGenerate) {
                            String generatedPage = writer.toString();
                            Page page = new Page(pageInfo, generatedPage);
                            this.pageReceiver.receivePage(page); // page�� �����Ǵ� �ż��� ��!!
                        }
                        writer.clear();
                        watchDogCounter = System.currentTimeMillis(); // reset a watchDogCounter
                        break;
                    case OperationCode.OPCODE_DO_END_TEMPLATE:
                        ((MultiPageTag) handler).doEndTemplate();
                        cp++;
                        break;
                    case OperationCode.OPCODE_EVAL_MARK:
                        tagMark = (Marker) data.getDataAt(opcode[1]);
                        cp++;
                        break;
                    case OperationCode.OPCODE_LINE_NUMBER:
                        lineNumber = opcode[1];
                        cp++;
                        break;
                    default:
                        throw new TemplateRuntimeException("incorrect operation code at code address=" + cp);
                    }
                }
            }
        } catch (InstanceCreationException ie) {
            //            String line = (tagMark == null || templateText == null) ? "" : " at Line " + getLineNumber(templateText, tagMark.getBeginIndex());
            String line = (lineNumber < 0) ? "" : " at Line " + lineNumber;
            throw new TemplateRuntimeException(tagMark, "could not reflect tag attributes for the handler" + line, ie);
        } catch (TagException te) {
            //            String line = (tagMark == null || templateText == null) ? "" : " at Line " + getLineNumber(templateText, tagMark.getBeginIndex());
            String line = (lineNumber < 0) ? "" : " at Line " + lineNumber;
            throw new TemplateRuntimeException(tagMark, te.getMessage() + line, te);
        } catch (TemplateRuntimeException re) {
            //            if (tagMark == null || templateText == null)
            //                throw re;
            if (tagMark == null && lineNumber < 0)
                throw re;
            //            String line = " at Line " + getLineNumber(templateText, tagMark.getBeginIndex());
            String line = (lineNumber < 0) ? "" : " at Line " + lineNumber;
            throw new TemplateRuntimeException(tagMark, re.getMessage() + line, re);
        } catch (RuntimeException ex) { // �� ������ �ݵ�� ���� �ȵȴ�!! ������ ���� �� ��ƾ� �Ѵ�. -> report�� unexpected exception���� ��ϵȴ�.
            //            if (tagMark == null || templateText == null)
            //                throw ex;
            if (tagMark == null && lineNumber < 0)
                throw ex;
            //            String line = " at Line " + getLineNumber(templateText, tagMark.getBeginIndex());
            String line = (lineNumber < 0) ? "" : " at Line " + lineNumber;
            String msg = ex.getMessage() == null ? ("unexpected exception occured: " + ex.getClass().getName()) : ex.getMessage();
            throw new RuntimeException(msg + line, ex);
        } finally {
            if (handler != null && parentRuntimeTag != handler) {
                handler.release();
                handlerHost.releaseTagHandler(libraryInfo.getTag(handler).getTagName(), handler);
            }
            evalValueP.release();
            attrValueEvalParserHost.releaseParser(evalValueP);
            pageContext.release();
            writer.clear();
            // pageContext = null;
            // writer = null;
            // pageInfo = null;
        }
    }

    public void breakExec() {
        breakRequest = true;
    }

    private void resetBreakRequest() {
        breakRequest = false;
    }
}
package wizest.fx.template.ref;

import java.util.ArrayList;

import wizest.fx.configuration.Configuration;
import wizest.fx.configuration.ConfigurationException;
import wizest.fx.configuration.ConfigurationFactory;
import wizest.fx.template.CompilerException;
import wizest.fx.template.Page;
import wizest.fx.template.PageReceiver;
import wizest.fx.template.Template;
import wizest.fx.template.TemplateBinary;
import wizest.fx.template.TemplateCompiler;
import wizest.fx.template.TemplateResource;
import wizest.fx.template.TemplateRuntime;
import wizest.fx.template.TemplateRuntimeException;
import wizest.fx.template.parse.ParserFactory;
import wizest.fx.template.tagext.TemplateContext;
import wizest.fx.util.Context;
import wizest.fx.util.GenericContext;
import wizest.fx.util.InvalidSetupException;
import wizest.fx.util.StreamUtils;

/**
 * template framework 응용 참조 구현
 * 
 * @author wizest
 */
public class Renderer {
    private TemplateResource resource;
    private TemplateCompiler compiler;
    
    private String name;
    private boolean strictTagHierarchy;

    public Renderer(String rendererName, final String taglibString, ParserFactory parserFactory, boolean strictTagHierarchy) throws InvalidSetupException {
        try {
            ConfigurationFactory factory = ConfigurationFactory.getInstance(rendererName + "-renderer");
            byte[] b = taglibString.getBytes();
            factory.build(StreamUtils.toInputStream(b));
            Configuration taglib = factory.getConfiguration("taglib");
            this.resource = TemplateResource.newTemplateResource(taglib, parserFactory);
        } catch (ConfigurationException ex) {
            throw new InvalidSetupException(ex);
        }
        this.compiler = new TemplateCompiler(resource);
        this.name = rendererName;
        this.strictTagHierarchy = strictTagHierarchy;
    }

    public String getName() {
        return name;
    }

    protected TemplateResource getTemplateResource() {
        return this.resource;
    }

    public TemplateBinary compile(String templateText, int templateType) throws CompilerException {
        return compiler.compile(templateText, templateType, strictTagHierarchy);
    }

    public Page[] exec(final TemplateBinary template, long onePageTimeOutMSec) throws TemplateRuntimeException {
        class GenericTemplateContext extends GenericContext implements TemplateContext {
            public GenericTemplateContext() {
                this(null);
            }

            public GenericTemplateContext(Context parent) {
                super(parent);
            }
        }
        return exec(template, new GenericTemplateContext(), onePageTimeOutMSec);
    }
    class InnerPageReceiver implements PageReceiver {
        public final ArrayList list;

        public InnerPageReceiver() {
            list = new ArrayList();
        }

        public void receivePage(Page page) {
            list.add(page);
        }
    }

    public Page[] exec(final TemplateBinary template, TemplateContext context, long onePageTimeOutMSec) throws TemplateRuntimeException {
        InnerPageReceiver receiver = new InnerPageReceiver();
        TemplateRuntime runtime = new TemplateRuntime(this.resource, receiver, onePageTimeOutMSec);
        runtime.exec(context, template);
        return (Page[]) receiver.list.toArray(new Page[0]);
    }
    private static final long TIME_OUT_PER_ONE_PAGE = 1000 * 3; // 3sec

    public Page[] render(Template tpl) throws CompilerException, TemplateRuntimeException {
        TemplateBinary c = compile(tpl.getTemplateText(), tpl.getTemplateType());
        return exec(c, TIME_OUT_PER_ONE_PAGE);
    }

    public Page[] render(String tpl) throws CompilerException, TemplateRuntimeException {
        TemplateBinary c = compile(tpl, Template.TYPE_OUTER_TEMPLATE);
        return exec(c, TIME_OUT_PER_ONE_PAGE);
    }
}

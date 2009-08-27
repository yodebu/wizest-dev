/*
 * Created on 2004. 10. 6
 */
package wizest.fx.template.ref;

import java.io.InputStream;

import wizest.fx.template.Page;
import wizest.fx.template.Template;
import wizest.fx.template.TemplateBinary;
import wizest.fx.template.parse.actiontag.ActionTagParserFactory;
import wizest.fx.template.parse.html.HtmlParserFactory;
import wizest.fx.util.StreamUtils;

@SuppressWarnings("unused")

/*
 * @author wizest
 */
public class TestRenderer {
	public void testActionTags() throws Exception {
		InputStream is = ClassLoader.getSystemResourceAsStream("wizest/fx/template/ref/test-action-taglib.xml");
		String taglib = new String(StreamUtils.toByteArray(is));
		Renderer rd = new Renderer("rd", taglib, new ActionTagParserFactory(), true);
		String tpl = "[[--default,attr1:value--]][[--ex3,v1,v2,attr1:value,attr1:value2,v3--]]";

		Page[] ps = rd.render(tpl);
		// System.out.println(ps[0].getPageText());
	}

	public void testHtml() throws Exception {
		InputStream is = ClassLoader.getSystemResourceAsStream("wizest/fx/template/ref/test-html-taglib.xml");
		String taglib = new String(StreamUtils.toByteArray(is));
		Renderer rd = new Renderer("rd2", taglib, new HtmlParserFactory(), false);
		// String tpl =
		// "<a asasa bba=1 href=222><default attr1=vasslue></default> <aaa/><ex3 v1 v2 attr1=value attr1=value2 v3><default attr1=value/><ex5 attr1=aaa></ex5></ex3> <default attr1=value/> <><.../>";
		String tpl = "<aaa></aaa><ccc></aaa><bbb></ddd>";
		// Page[] ps = rd.render(tpl);
		// System.out.println(ps[0].getPageText());
		TemplateBinary c = rd.compile(tpl, Template.TYPE_OUTER_TEMPLATE);
		for (int i = 0; i < 1; ++i) {
			rd.exec(c, 1000);
		}
	}

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();
		TestRenderer tr = new TestRenderer();
		// tr.testActionTags();
		tr.testHtml();
		long end = System.currentTimeMillis();
		System.out.println("elapsed time: " + (end - start));
	}
}

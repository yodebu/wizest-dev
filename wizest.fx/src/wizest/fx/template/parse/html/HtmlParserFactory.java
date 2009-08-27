/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.html;

import wizest.fx.template.parse.ParserFactory;

/**
 * @author wizest
 */
public class HtmlParserFactory extends ParserFactory {

	protected Class _getTagParserClass() {
		return HtmlTagParser.class;
	}

	protected Class _getTagNameParserClass() {
		return HtmlTagNameParser.class;
	}

	protected Class _getAttributeParserClass() {
		return HtmlAttributeParser.class;
	}

	protected Class _getAttributeNameParserClass() {
		return HtmlAttributeNameParser.class;
	}

	protected Class _getAttributeValueParserClass() {
		return HtmlAttributeValueParser.class;
	}

	protected Class _getAttributeValueEvalParserClass() {
		return HtmlAttributeValueEvalParser.class;
	}
}
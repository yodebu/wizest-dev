/*
 * Created on 2004. 10. 1
 */
package wizest.fx.template.parse.actiontag;

import wizest.fx.template.parse.ParserFactory;

/**
 * @author wizest
 */
public class ActionTagParserFactory extends ParserFactory {

    protected Class _getTagParserClass() {
        return ATTagParser.class;
    }

    protected Class _getTagNameParserClass() {
        return ATTagNameParser.class;
    }


    protected Class _getAttributeParserClass() {
        return ATAttributeParser.class;
    }

  
    protected Class _getAttributeNameParserClass() {
        return ATAttributeNameParser.class;
    }

   
    protected Class _getAttributeValueParserClass() {
        return ATAttributeValueParser.class;
    }

   
    protected Class _getAttributeValueEvalParserClass() {
        return ATAttributeValueEvalParser.class;
    }
}
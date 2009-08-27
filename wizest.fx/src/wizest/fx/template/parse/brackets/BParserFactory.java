/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.brackets;

import wizest.fx.template.parse.ParserFactory;

/**
 * @author wizest
 */
public class BParserFactory extends ParserFactory {
   
    protected Class _getTagParserClass() {
        return BTagParser.class;
    }

    
    protected Class _getTagNameParserClass() {
        return BTagNameParser.class;
    }

    
    protected Class _getAttributeParserClass() {
        return BAttributeParser.class;
    }

   
    protected Class _getAttributeNameParserClass() {
        return BAttributeNameParser.class;
    }

    
    protected Class _getAttributeValueParserClass() {
        return BAttributeValueParser.class;
    }

   
    protected Class _getAttributeValueEvalParserClass() {
        return BAttributeValueEvalParser.class;
    }
}
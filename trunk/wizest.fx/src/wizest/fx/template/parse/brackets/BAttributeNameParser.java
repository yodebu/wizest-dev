/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.brackets;

import wizest.fx.template.parse.AttributeNameParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * 속성 이름을 얻는다.
 * 
 * @author wizest
 */
public class BAttributeNameParser extends AbstractParser implements AttributeNameParser {
    private static final char ATTR_SEPARATOR = '=';

    public Marker parseNext() throws ParserException {
        String text = getText();
        Marker mark;
        int start = getParsingPoint();
        int end = text.indexOf(ATTR_SEPARATOR);
        if (start == 0 && end > 0) { // attribute name이 있을 경우
            mark = new Marker(start, end, text.substring(0, end).trim().toUpperCase());
        } else
            mark = null;
        setParsingPoint(mark);
        return mark;
    }
    //public static void main(String[] args) throws ParserException {
    //    String s = "<name sdsd= =dds \\\\ attr1=fdgdsh=ds s = attr2=a attr3= attr4 />";
    //    Parser p = new HtmlAttributeParser();
    //    Parser pp = new HtmlAttributeNameParser();
    //    p.initialize(s);
    //    Marker m;
    //    Marker mm;
    //    while ((m = p.parseNext()) != null) {
    //        System.out.println(m);
    //        pp.initialize(m);
    //        while ((mm = pp.parseNext()) != null)
    //            System.out.println(" "+mm);
    //    }
    //}
}
/*
 * Created on 2004. 10. 1
 */
package wizest.fx.template.parse.actiontag;

import wizest.fx.template.parse.AttributeValueParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * �Ӽ� ���� �����´�.
 * 
 * <pre>
 * 
 *  
 *    ����:
 *    [[--tagname,,Attrname:Value,:Value,:--]]
 *  
 *                          -&gt; null : �̷� ��� �����Ѵ�. �Ӽ��� ���� �Ľ��� �Ǳ� ���� null�� ����Ƿ� �Ľ��� �ߴ��ϰ� �� �� �ִ�.
 *        Attrname:Value    -&gt; Value
 *        :Value            -&gt; Value
 *        :                 -&gt; ���ڿ�
 *   
 *  
 * </pre>
 * 
 * @author wizest
 */
public class ATAttributeValueParser extends AbstractParser implements AttributeValueParser {
    private static final char ATTR_SEPARATOR = ATAttributeParser.ATTR_SEPARATOR;

    public Marker parseNext() throws ParserException {
        String text = getText();
        int start, end;
        Marker mark = null;
        if (getParsingPoint() == 0) {
            start = text.indexOf(ATTR_SEPARATOR);
            end = text.length();
            // �Ӽ� ���� ���� ��
            if (start < 0) {
                if (end > 0) {
                    mark = new Marker(0, end, text.trim());
                }
            }
            // �Ӽ� ���� ���� ��
            else {
                mark = new Marker(start + 1, end, text.substring(start + 1).trim());
            }
        }
        setParsingPoint(mark);
        return mark;
    }
    //    public static void main( String[] args ) throws ParserException
    //    {
    //
    //        String s =
    //                "[[--name,sdsd:,:dds, attr1:ds ,:, attr2:a , attr3: ,attr4 ,,dd--]]";
    //
    //        Parser p = new AttributeParser();
    //        Parser pp = new AttributeValueParser();
    //
    //        p.initialize( s );
    //
    //        Mark m;
    //        Mark mm;
    //
    //        while ( ( m = p.parseNext() ) != null )
    //        {
    //            pp.initialize( m );
    //            while ( ( mm = pp.parseNext() ) != null )
    //                System.out.println( mm.getParsedString() );
    //
    //        }
    //    }
}
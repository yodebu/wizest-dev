/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.brackets;

import wizest.fx.template.parse.AttributeValueParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * @author wizest
 */
public class BAttributeValueParser extends AbstractParser implements AttributeValueParser {
    private static final char ATTR_SEPARATOR = '=';

    public Marker parseNext() throws ParserException {
        String text = getText();
        int start, end;
        Marker mark;
        if (getParsingPoint() == 0) {
            start = text.indexOf(ATTR_SEPARATOR);
            end = text.length();
            // �Ӽ� ���� ���� ��
            if (start < 0) {
                if (end > 0)
                    mark = new Marker(0, end, filterQuotationMark(text));
                else
                    mark = null;
            }
            // �Ӽ� ���� ���� ��
            else {
                mark = new Marker(start + 1, end, filterQuotationMark(text.substring(start + 1)));
            }
        } else
            mark = null;
        setParsingPoint(mark);
        return mark;
    }

    /**
     * �Ӽ����� �ο� ��ȣ(",') �� �׿� ���� ��� ��ȣ�� �����Ѵ�. �ο� ��ȣ�� ������ ������ ���� ���� ��� ���� ��ȣ�� �����Ѵ�.
     * 
     * @param text
     * @return
     */
    private String filterQuotationMark(String text) {
        char[] val = text.toCharArray();
        int st = 0;
        int len = val.length;
        // left trim
        while ((st < len) && (val[st] <= ' ')) {
            st++;
        }
        // right trim
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        if (st < len)
            if (val[st] == '"') {
                st++;
                if (val[len - 1] == '"')
                    len--;
            } else if (val[st] == '\'') {
                st++;
                if (val[len - 1] == '\'')
                    len--;
            }
        return String.valueOf(val, st, Math.max(0, len - st));
    }
    //public static void main(String[] args) throws ParserException {
    //    String s = "<name sdsd= =dds attr1='s = a'ttr2=a attr3= \"att'r4 dd\" attr5=-_- />";
    //    Parser p = new HtmlAttributeParser();
    //    Parser pp = new HtmlAttributeValueParser();
    //    p.initialize(s);
    //    Marker m;
    //    Marker mm;
    //    while ((m = p.parseNext()) != null) {
    //        System.out.println(m);
    //        pp.initialize(m);
    //        while ((mm = pp.parseNext()) != null)
    //            System.out.println(" " + mm);
    //    }
    //}
}
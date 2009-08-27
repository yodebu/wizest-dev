/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.brackets;

import wizest.fx.template.parse.TagNameParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * Tag���� Tag name�� ã���ش�.
 * 
 * @author wizest
 */
public class BTagNameParser extends AbstractParser implements TagNameParser {
    class HtmlMarker extends Marker {
        final boolean isStartTag;
        final boolean isEndTag;

        public HtmlMarker(int startIndex, int endIndex, String value, boolean isStartTag, boolean isEndTag) {
            super(startIndex, endIndex, value);
            this.isStartTag = isStartTag;
            this.isEndTag = isEndTag;
        }
    }

    public Marker parseNext() throws ParserException {
        String text = getText();
        //        �翬�ϴٰ� ���� ����
        //        if (!(text.startsWith(HtmlTagParser.TAG_OPEN) && text.endsWith(HtmlTagParser.TAG_CLOSE))) {
        //            throw new ParserException("invalid tag.");
        //        }
        int start, end;
        HtmlMarker mark = null;
        boolean isStartTag, isEndTag;
        if (getParsingPoint() < BTagParser.TAG_OPEN.length()) // name �� tag�� �ϳ� �ۿ� �����ϱ� �ѹ��� pasing�Ѵ�. ��.��;;
        {
            start = BTagParser.TAG_OPEN.length();
            end = text.indexOf(' ');
            if (text.charAt(start) == '/') {
                ++start;
                isStartTag = false;
                isEndTag = true;
                // �Ӽ��� ���ٸ�
                if (end < 0 || end == start)
                    end = text.length() - BTagParser.TAG_CLOSE.length();
            } else {
                isStartTag = true;
                if (text.charAt(text.length() - BTagParser.TAG_CLOSE.length() - 1) == '/')
                    isEndTag = true;
                else
                    isEndTag = false;
                // �Ӽ��� ���ٸ�
                if (end < 0 || end == start)
                    end = text.length() - BTagParser.TAG_CLOSE.length() + (isEndTag ? -1 : 0);
            }
            if (end > start) {
                String body = text.substring(start, end).trim().toUpperCase();
                mark = new HtmlMarker(0, text.length(), body, isStartTag, isEndTag);
            }
        }
        setParsingPoint(mark);
        return mark;
    }

    protected static String _getRealTagName(Marker mark) {
        return mark.getValue();
    }

    protected static boolean _isStartTag(Marker mark) {
        if (mark instanceof HtmlMarker)
            return ((HtmlMarker) mark).isStartTag;
        else
            return true;
    }

    protected static boolean _isEndTag(Marker mark) {
        if (mark instanceof HtmlMarker)
            return ((HtmlMarker) mark).isEndTag;
        else
            return true;
    }

    protected static boolean _isBodyTag(Marker mark) {
        //        if (_isStartTag(mark) == _isEndTag(mark))
        //            return false;
        //        else
        //            return true;
        if (_isStartTag(mark) && _isEndTag(mark))
            return false;
        else
            return true;
    }

    public boolean isBodyTag(Marker mark) {
        return _isBodyTag(mark);
    }

    public String getRealTagName(Marker mark) {
        return _getRealTagName(mark);
    }

    public boolean isStartTag(Marker mark) {
        return _isStartTag(mark);
    }

    public boolean isEndTag(Marker mark) {
        return _isEndTag(mark);
    }
    //public static void main(String[] args) throws ParserException {
    //    String s = "< >1111111<><aaa2222222222222/>333333333333<bbb4444444 />></ccc555555555-]><6,>7< ><,/><>>>";
    //    Parser p = new HtmlTagParser();
    //    Parser pp = new HtmlTagNameParser();
    //    p.initialize(s);
    //    Marker m;
    //    Marker mm;
    //    while ((m = p.parseNext()) != null) {
    //        pp.initialize(m.getValue());
    //        while ((mm = pp.parseNext()) != null) {
    //            System.out.print(mm);
    //            System.out.print(", isStartTag=" + _isStartTag(mm));
    //            System.out.print(", isEndTag=" + _isEndTag(mm));
    //            System.out.println(", isBodyTag=" + _isBodyTag(mm));
    //        }
    //    }
    //}
}

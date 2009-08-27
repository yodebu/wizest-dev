/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.html;

import wizest.fx.template.parse.AttributeParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * @author wizest
 */
public class HtmlAttributeParser extends AbstractParser implements AttributeParser {
    final static int OPEN_SIZE = HtmlTagParser.TAG_OPEN.length();
    final static int CLOSE_SIZE = HtmlTagParser.TAG_CLOSE.length();

    public Marker parseNext() throws ParserException {
        char[] text = getText().toCharArray();
        // int length = text.length;
        // if (!(text[0] == '<' && text[length - 1] == '>'))
        //  throw new ParserException("incorrect tag.");
        // �ԷµǴ� ���ڿ��� ��� <���� �����Ͽ� > ���� ������ ���ڶ� �����ϸ�
        // length �� > �Ǵ� /> �� ������ ���̸� ����Ų��.
        int limit = text[text.length - 2] == '/' ? text.length - CLOSE_SIZE - 1 : text.length - CLOSE_SIZE;
        int start = getParsingPoint();
        // ���� ������ tag name ���� ����
        if (start == 0) {
            start = text[1] == '/' ? OPEN_SIZE + 1 : OPEN_SIZE;
            // left trim
            while ((start < limit) && (text[start] <= ' '))
                start++;
            // find the next SPC
            while ((start < limit) && (text[start] > ' '))
                start++;
        }
        Marker mark = null;
        // left trim
        while ((start < limit) && (text[start] <= ' '))
            start++;
        if (limit > start + 1) {
            int end = start + 1;
            // scan the next SPC
            do {
                while ((end < limit) && (text[end] > ' '))
                    end++;
                while ((end < limit) && (text[end] <= ' '))
                    end++;
            } while ((end < limit) && (text[end] == '='));
            int equalIdx = indexOf(text, '=', start, end);
            if (equalIdx < 0)
                mark = new Marker(start, end, String.valueOf(text, start, end - start).trim());
            else {
                ++equalIdx;
                // left trim
                while ((equalIdx < limit) && (text[equalIdx] <= ' '))
                    equalIdx++;
                if (text[equalIdx] == '"')
                    end = indexOf(text, '"', equalIdx + 1, limit) + 1;
                else if (text[equalIdx] == '\'')
                    end = indexOf(text, '\'', equalIdx + 1, limit) + 1;
                if (end <= 0)
                    end = limit;
                mark = new Marker(start, end, String.valueOf(text, start, end - start).trim());
            }
        }
        setParsingPoint(mark);
        return mark;
    }

    private int indexOf(char[] text, char target, int start, int limit) {
        for (int i = start; i < limit; ++i)
            if (text[i] == target)
                return i;
        return -1;
    }
    //    public static void main(String[] args) throws ParserException {
    //        String s = "<name =attr1 =aa attr2 =aaa bbb attr3 = ' ccc ' attr5=\"sasfaf\" attr4=ddd/>";
    //        Parser p = new HtmlAttributeParser();
    //        p.initialize(s);
    //        Marker m;
    //        while ((m = p.parseNext()) != null) {
    //            System.out.println(m);
    //        }
    //    }
}
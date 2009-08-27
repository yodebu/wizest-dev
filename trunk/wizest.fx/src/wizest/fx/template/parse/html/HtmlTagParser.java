/*
 * Created on 2004. 10. 4
 */
package wizest.fx.template.parse.html;

import wizest.fx.template.parse.TagParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * @author wizest
 */
public class HtmlTagParser extends AbstractParser implements TagParser {
    public static final String TAG_OPEN = "<";
    public static final String TAG_CLOSE = ">";
    private static final String COMMENT_OPEN = "<!--";
    private static final String COMMENT_CLOSE = "-->";
    private static final int TAG_OPEN_LENGTH = TAG_OPEN.length();
    private static final int TAG_CLOSE_LENGTH = TAG_CLOSE.length();
    private static final int COMMENT_OPEN_LENGTH = COMMENT_OPEN.length();
    private static final int COMMENT_CLOSE_LENGTH = COMMENT_CLOSE.length();

    public Marker parseNext() throws ParserException {
        String text = getText();
        int parsingPoint = getParsingPoint();
        int start, end;
        Marker mark = null;
        while (true) {
            start = text.indexOf(TAG_OPEN, parsingPoint);
            if (start >= 0) {
                // �ּ��� ���
                if (text.startsWith(COMMENT_OPEN, start)) {
                    end = text.indexOf(COMMENT_CLOSE, start + COMMENT_OPEN_LENGTH);
                    if (end >= 0) {
                        end += COMMENT_CLOSE_LENGTH;
                        parsingPoint = end;
                    } else {
                        break;
                    }
                }
                // �ױ��� ���
                else {
                    end = text.indexOf(TAG_CLOSE, start + TAG_OPEN_LENGTH);
                    if (end >= 0) {
                        end += TAG_CLOSE_LENGTH;
                        mark = new Marker(start, end, text.substring(start, end).trim());
                        setParsingPoint(end);
                    }
                    break;
                }
            } else {
                break;
            }
        }
        setParsingPoint(mark);
        return mark;
    }

    public String filterComments(String s) // �ּ��� �ɷ�����
    {
        StringBuffer buff = new StringBuffer();
        int pivot = 0;
        while (true) {
            int start = s.indexOf(COMMENT_OPEN, pivot);
            int end;
            if (start < 0) {
                buff.append(s.substring(pivot));
                break;
            } else { // �ּ��� ã������
                end = s.indexOf(COMMENT_CLOSE, start + COMMENT_OPEN_LENGTH);
                if (end < 0) {
                    buff.append(s.substring(start));
                    break;
                } else {
                    buff.append(s.substring(pivot, start));
                    pivot = end + COMMENT_CLOSE_LENGTH;
                }
            }
        }
        return buff.toString();
    }

   
    public String getTagOpenString() {
        return TAG_OPEN;
    }

   
    public String getTagCloseString() {
        return TAG_CLOSE;
    }

    //    public static void main(String[] args) throws ParserException {
    //        String s = "�����ٶ�<sfafsa > <!--����� �ּ��Դϴ�.--->gsdgsd <sddsgsdgsg>]fdgsgs<1>><11144214-]--><--2-->2<>";
    //        Parser p = new HtmlTagParser();
    //        p.initialize(s);
    //        Marker m;
    //        while ((m = p.parseNext()) != null) {
    //            System.out.println(m);
    //        }
    //    }
    //public static void main(String[] args) throws Exception
    //        {
    //            System.out.println(filterComments("������[[---�����ٶ󸶹ٻ�~~~���ó�̤���ä�������--]]
    //     ��[[---[[--�ּ�2---]]�ٻ������ī"));
    //        }
}

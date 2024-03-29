/*
 * Created on 2004. 10. 1
 */
package wizest.fx.template.parse.actiontag;

import wizest.fx.template.parse.TagNameParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.ParserException;

/**
 * Tag에서 Tag name을 찾아준다.
 * 
 * <pre>
 * 
 *  
 *   
 *     [[--abc--]] -&gt; abc
 *     [[--   abc --]] -&gt; abc : trimming된다. 단,startIndex,endIndex는 trim이 반영되지 않는다. 즉,(4,10)이다.
 *     [[--abc,dd,ee,ff--]] -&gt; abc
 *   
 *     주의:
 *         [[----]] -&gt; null
 *         [[-- --]] -&gt; 빈문자열
 *         [[--,something--]] -&gt; null
 *         로 파싱한다.
 *    
 *   
 *  
 * </pre>
 * 
 */
public class ATTagNameParser extends AbstractParser implements TagNameParser {
    private static final String TAG_OPEN = ATTagParser.TAG_OPEN;
    private static final String TAG_CLOSE = ATTagParser.TAG_CLOSE;
    public static final char NAME_SEPARATOR = ',';
    public static final String START_TAG_POSTFIX = "Start".toUpperCase();
    public static final String END_TAG_POSTFIX = "End".toUpperCase();
    private static final int START_TAG_POSTFIX_LENGTH = START_TAG_POSTFIX.length();
    private static final int END_TAG_POSTFIX_LENGTH = END_TAG_POSTFIX.length();

    public Marker parseNext() throws ParserException {
        String text = getText();
        if (!(text.startsWith(TAG_OPEN) && text.endsWith(TAG_CLOSE))) {
            throw new ParserException("invalid tag.");
        }
        int start, end;
        Marker mark = null;
        if (getParsingPoint() < TAG_OPEN.length()) // name 은 tag당 하나 밖에 없으니깐 한번만 pasing한다. ㅡ.ㅡ;;
        {
            start = TAG_OPEN.length();
            // attribute가 있을 경우
            if (!((end = text.indexOf(NAME_SEPARATOR)) > 0)) {
                // attribute가 없을 경우
                end = text.indexOf(TAG_CLOSE);
            }
            if (end > start) {
                String body = text.substring(start, end).trim().toUpperCase();
                mark = new Marker(start, end, body);
            }
        }
        setParsingPoint(mark);
        return mark;
    }

    protected static boolean _isBodyTag(Marker mark) {
        if (_isStartTag(mark) || _isEndTag(mark)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * BodyTag는 [[--TagNameStart--]],[[--TagNameEnd--]] 와 같이 테그 이름 에 Start,End가 붙는다. Start,End를 제외한 Tag이름만 추출한다.
     * 
     * @param mark
     * @return
     */
    protected static String _getRealTagName(Marker mark) {
        if (_isStartTag(mark)) {
            String s = mark.getValue();
            return s.substring(0, s.length() - START_TAG_POSTFIX_LENGTH); // Start 부분을 제거
        } else if (_isEndTag(mark)) {
            String s = mark.getValue();
            return s.substring(0, s.length() - END_TAG_POSTFIX_LENGTH); // End 부분을 제거
        } else {
            return mark.getValue();
        }
    }

    protected static boolean _isStartTag(Marker mark) {
        if (mark.getValue().endsWith(START_TAG_POSTFIX)) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean _isEndTag(Marker mark) {
        if (mark.getValue().endsWith(END_TAG_POSTFIX)) {
            return true;
        } else {
            return false;
        }
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
    //    public static void main( String[] args ) throws ParserException
    //    {
    //        String s =
    //                "[[-- --]]sdgsdgsd[[----]][[--sddsgsdgsg--]]fdgsgs[[--1--]]--]][[--11144214-]--]][[--2,--]]2[[-- --]][[--,--]]";
    //
    //        Parser p = new TagParser();
    //        Parser pp = new TagNameParser();
    //
    //        p.initialize( s );
    //
    //        Marker m;
    //        Marker mm;
    //
    //        while ( ( m = p.parseNext() ) != null )
    //        {
    //            pp.initialize( m.getParsedString() );
    //            while ( ( mm = pp.parseNext() ) != null )
    //                System.out.println( mm.getParsedString() );
    //        }
    //    }
}
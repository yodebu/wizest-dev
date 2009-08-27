package wizest.fx.template.parse;

import wizest.fx.util.Marker;
import wizest.fx.util.Parser;

/**
 * Tag에서 Tag name을 찾아준다.
 * 
 */
public interface TagNameParser extends Parser {
    boolean isBodyTag(Marker mark);

    String getRealTagName(Marker mark);

    boolean isStartTag(Marker mark);

    boolean isEndTag(Marker mark);
}
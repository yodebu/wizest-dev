package wizest.fx.template.parse;

import wizest.fx.util.Parser;


public interface TagParser extends Parser {
    String getTagOpenString();

    String getTagCloseString();

    /**
     * @param s
     * @return s에서 주석을 제외한 문자열
     */
    String filterComments(String s);
}
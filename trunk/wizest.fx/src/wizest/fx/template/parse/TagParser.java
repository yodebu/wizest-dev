package wizest.fx.template.parse;

import wizest.fx.util.Parser;


public interface TagParser extends Parser {
    String getTagOpenString();

    String getTagCloseString();

    /**
     * @param s
     * @return s���� �ּ��� ������ ���ڿ�
     */
    String filterComments(String s);
}
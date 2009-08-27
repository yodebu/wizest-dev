/*
 * Created on 2004. 10. 1
 */
package wizest.fx.template.parse.actiontag;

import wizest.fx.template.parse.TagParser;
import wizest.fx.util.AbstractParser;
import wizest.fx.util.Marker;
import wizest.fx.util.Parser;
import wizest.fx.util.ParserException;

/**
 * ���ڿ����� Action Tag�� ã�´�.
 * 
 * <pre>
 * 
 *      action tag�� ������ ���Ѵ�.
 *       [[--�ױ׳���,�ױ׼Ӽ�,...--]]
 * 
 * </pre>
 * 
 */
public class ATTagParser extends AbstractParser implements TagParser {
	public static final String TAG_OPEN = "[[--";
	public static final String TAG_CLOSE = "--]]";
	private static final String COMMENT_OPEN = "[[---";
	private static final String COMMENT_CLOSE = "---]]";
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

	/**
	 * ���� Tag(Start�ױ׿����Ѵ�)�� �ش��ϴ� EndTag�� Marker�� �����Ѵ�.
	 * 
	 * @param text
	 *            String
	 * @param startMark
	 *            Marker
	 * @throws ParserException
	 *             ���������� �ùٸ��� �ʰų� End Tag�� ã�� �� ���� ���(Start Tag�� �ִµ�..)
	 * @return Marker
	 */
	public Marker getEndTagMarkerOf(String text, Marker startMark) throws ParserException {
		/**
		 * ����) start tag�� ��Ÿ���� depth�� 1�� �����ϰ� end tag�� ��Ÿ���� depth�� 1�� �����Ѵ�.
		 * 
		 * ���� ���� ������ ����� �Ǿ� �ִٸ� depth�� 0�� �Ǵ� ���� tag�� ������ �����̴�.
		 */
		int depth = 1; // �� �޼ҵ带 �θ� ���¿� �̹� ������ �ִ� ����(start tag�� ��������) �̹Ƿ� 1�� ����
		Marker mark = null;
		Parser tagParser = new ATTagParser();
		tagParser.initialize(text);
		tagParser.setParsingPoint(startMark);
		Parser nameParser = new ATTagNameParser();
		nameParser.initialize(startMark);
		Marker nameStartMark = nameParser.parseNext();
		// Start Tag�� �ƴϸ� �� �ʿ䰡 ����.
		if (!ATTagNameParser._isStartTag(nameStartMark))
			return startMark;
		String tagName = ATTagNameParser._getRealTagName(nameStartMark);
		try {
			do {
				mark = tagParser.parseNext();
				if (mark == null) {
					throw new ParserException("could not find the corresponding end tag.");
				}
				nameParser.initialize(mark);
				Marker nameMark = nameParser.parseNext();
				if (tagName.equals(ATTagNameParser._getRealTagName(nameMark)))
					if (ATTagNameParser._isBodyTag(nameMark)) {
						if (ATTagNameParser._isStartTag(nameMark))
							++depth;
						else
							--depth;
					}
			} while (depth > 0);
			// // end tag�� ���� �κ��� ���� parsing point�� �����Ѵ�.
			// tagParser.setParsingPoint(mark.getBeginIndex());
			return mark;
		} finally {
			tagParser.release();
			nameParser.release();
		}
	}

	
	public String getTagOpenString() {
		return TAG_OPEN;
	}

	
	public String getTagCloseString() {
		return TAG_CLOSE;
	}
	// public static void main( String[] args ) throws ParserException
	// {
	// String s =
	// "�����ٶ�[[--sfafsa--]] [[---�����
	// �ּ��Դϴ�.---]]gsdgsd[[--sddsgsdgsg--]]fdgsgs[[--1--]]--]][[--11144214-]--]][[--2--]]2[[--]]";
	//
	// Parser p = new TagParser();
	// p.initialize( s );
	// Marker m;
	//
	// while ( ( m = p.parseNext() ) != null )
	// {
	// System.out.println( m.getValue() );
	// }
	// }
	// public static void main(String[] args) throws Exception
	// {
	// System.out.println(filterComments("������[[---�����ٶ󸶹ٻ�~~~���ó�̤���ä�������--]]
	// ��[[---[[--�ּ�2---]]�ٻ������ī"));
	// }
}
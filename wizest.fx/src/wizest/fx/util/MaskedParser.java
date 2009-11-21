package wizest.fx.util;

public class MaskedParser extends AbstractParser {
	private final Parser mask;
	private final Parser apply;

	public MaskedParser(Parser mask, Parser apply) {
		this.mask = mask;
		this.apply = apply;
	}

	public void initialize(Marker mark) {
		mask.initialize(mark);
		super.initialize(mark);
	}

	public void initialize(String text) {
		mask.initialize(text);
		super.initialize(text);
	}

	public void release() {
		mask.release();
		super.release();
	}

	public Marker parseNext() throws ParserException {
		Marker m = null;

		int begin = getParsingPoint();
		mask.setParsingPoint(begin);
		m = mask.parseNext();

		String text = getText();

		// -.deny 될 것을 찾지 못하면
		// 현재 지점부터 끝까지 해서 allow parsing!!
		// -.찾으면
		// 현재 지점부터 찾은 곳까지 해서 allow parsing!!

		if (m == null) {
			apply.initialize(text.substring(begin));
			m = apply.parseNext();
			apply.release();

			if (m == null)
				setParsingPoint(text.length());
			else {
				setParsingPoint(begin + m.getEndIndex());
				// convert inner to outer
				m = new Marker(begin + m.getBeginIndex(), begin
						+ m.getEndIndex(), m.getValue());
			}

			return m;
		} else {
			int end = m.getBeginIndex();
			int nextBegin = m.getEndIndex();
			apply.initialize(text.substring(begin, end));
			m = apply.parseNext();
			apply.release();

			if (m == null) {
				setParsingPoint(nextBegin);
				return parseNext();
			} else {
				setParsingPoint(begin + m.getEndIndex());
				// convert inner to outer
				m = new Marker(begin + m.getBeginIndex(), begin
						+ m.getEndIndex(), m.getValue());
			}

			return m;
		}
	}
}

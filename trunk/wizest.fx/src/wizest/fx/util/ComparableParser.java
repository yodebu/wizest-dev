package wizest.fx.util;

import java.util.Arrays;
import java.util.Comparator;

public class ComparableParser extends AbstractParser {
	private final int minStringLength;

	private final Parser[] ps;
	private final int psLen;

	private final Comparator comparator;

	/**
	 * @param ps
	 *            Parser[]
	 * @param comparator
	 *            Comparator 서로 다른 parser에서 parsing된 marker를 비교 한다. (최종 정렬되었을 때
	 *            첫번째 mark가 선택됨)
	 * @param minStringLength
	 *            int
	 */
	public ComparableParser(Parser[] ps, Comparator comparator,
			int minStringLength) {
		this.ps = ps;
		psLen = ps.length;
		this.comparator = comparator;
		this.minStringLength = minStringLength;
	}

	public void initialize(Marker mark) {
		for (int i = 0; i < psLen; ++i) {
			ps[i].initialize(mark);
		}
		super.initialize(mark);
	}

	public void initialize(String text) {
		for (int i = 0; i < psLen; ++i) {
			ps[i].initialize(text);
		}
		super.initialize(text);
	}

	public void release() {
		for (int i = 0; i < psLen; ++i) {
			ps[i].release();
		}
		super.release();
	}

	private void setAllParsingPoint(int p) {
		for (int i = 0; i < psLen; ++i) {
			ps[i].setParsingPoint(p);
		}
	}

	public Marker parseNext() throws ParserException {
		setAllParsingPoint(getParsingPoint());

		Marker[] ms = new Marker[psLen];
		for (int i = 0; i < psLen; ++i) {
			ms[i] = ps[i].parseNext();
		}

		Arrays.sort(ms, comparator);

		Marker mark = ms[0];
		setParsingPoint(mark);

		if (mark != null && mark.getValue().length() < minStringLength)
			return parseNext();

		return mark;
	}
}

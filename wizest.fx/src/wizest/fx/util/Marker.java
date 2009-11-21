package wizest.fx.util;

/**
 * parser에 의해 얻은 결과 / abcdef 에서 bcd 를 얻었으면 beginIndex는 1, endIndex는 4이며
 * parsedString은 bcd이다.
 * 
 */
public class Marker {
	private int beginIndex = 0;
	private int endIndex = 0;
	private String value = null;

	public Marker(int startIndex, int endIndex, String value) {
		this.beginIndex = startIndex;
		this.endIndex = endIndex;
		this.value = value;
	}

	public int getBeginIndex() {
		return beginIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public String getValue() {
		return value;
	}

	public String toString() {
		return "{idx=" + beginIndex + "~" + endIndex + ",val=" + value + "}";
	}
}

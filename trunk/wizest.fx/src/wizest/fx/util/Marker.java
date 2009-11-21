package wizest.fx.util;

/**
 * parser�� ���� ���� ��� / abcdef ���� bcd �� ������� beginIndex�� 1, endIndex�� 4�̸�
 * parsedString�� bcd�̴�.
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

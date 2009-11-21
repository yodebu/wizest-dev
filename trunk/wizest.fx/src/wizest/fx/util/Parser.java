package wizest.fx.util;

public interface Parser {
	/**
	 * parsing하고자 하는 string을 설정한다. parsingPoint는 reset된다.
	 * 
	 * @param text
	 */
	void initialize(String text);

	/**
	 * 재 사용을 위해 release한다.
	 */
	void release();

	/**
	 * parsing하고자 하는 string을 설정한다. parsingPoint는 reset된다.
	 * 
	 * @param mark
	 *            mark.getParsedString()이 parsing 하려는 string으로 설정된다.
	 */
	void initialize(Marker mark);

	/**
	 * parsing 대상 문자열(text) 를 가져온다.
	 * 
	 * @return
	 */
	String getText();

	/**
	 * parsing을 시작할 위치를 지정한다. mark가 null일 경우 text의 마지막 위치+1을 가르킨다.
	 * 
	 * @param mark
	 *            mark의 endIndex+1 를 다음 parsing의 시작 위치로 한다.
	 */
	void setParsingPoint(Marker mark);

	/**
	 * parsing을 시작할 위치를 지정한다.
	 * 
	 * @param index
	 *            시작 위치
	 */
	void setParsingPoint(int index);

	/**
	 * parsing을 시작할 위치를 리턴한다.
	 * 
	 * @return
	 */
	int getParsingPoint();

	/**
	 * 현재 위치에서 parsing을 시작하여 찾은 정보를 리턴한다. (현재 위치는 찾은 위치로 업데이트된다.)
	 * 
	 * @return null if no more parsing data
	 * @throws ParserException
	 */
	Marker parseNext() throws ParserException;
}
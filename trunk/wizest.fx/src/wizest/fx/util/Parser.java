package wizest.fx.util;

public interface Parser {
	/**
	 * parsing�ϰ��� �ϴ� string�� �����Ѵ�. parsingPoint�� reset�ȴ�.
	 * 
	 * @param text
	 */
	void initialize(String text);

	/**
	 * �� ����� ���� release�Ѵ�.
	 */
	void release();

	/**
	 * parsing�ϰ��� �ϴ� string�� �����Ѵ�. parsingPoint�� reset�ȴ�.
	 * 
	 * @param mark
	 *            mark.getParsedString()�� parsing �Ϸ��� string���� �����ȴ�.
	 */
	void initialize(Marker mark);

	/**
	 * parsing ��� ���ڿ�(text) �� �����´�.
	 * 
	 * @return
	 */
	String getText();

	/**
	 * parsing�� ������ ��ġ�� �����Ѵ�. mark�� null�� ��� text�� ������ ��ġ+1�� ����Ų��.
	 * 
	 * @param mark
	 *            mark�� endIndex+1 �� ���� parsing�� ���� ��ġ�� �Ѵ�.
	 */
	void setParsingPoint(Marker mark);

	/**
	 * parsing�� ������ ��ġ�� �����Ѵ�.
	 * 
	 * @param index
	 *            ���� ��ġ
	 */
	void setParsingPoint(int index);

	/**
	 * parsing�� ������ ��ġ�� �����Ѵ�.
	 * 
	 * @return
	 */
	int getParsingPoint();

	/**
	 * ���� ��ġ���� parsing�� �����Ͽ� ã�� ������ �����Ѵ�. (���� ��ġ�� ã�� ��ġ�� ������Ʈ�ȴ�.)
	 * 
	 * @return null if no more parsing data
	 * @throws ParserException
	 */
	Marker parseNext() throws ParserException;
}
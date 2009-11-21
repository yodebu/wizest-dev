package wizest.fx.util;

/**
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public interface DataKeys {
	int size();

	/**
	 * ���� �＼�� �ǹǷ� �� ������ �Ѵ�.
	 * 
	 * @param key
	 * @return -1 if not found
	 */
	int indexOf(Object key);

	/**
	 * ���Ͻ� �ݵ�� key�� �ش��ϴ� class�� array ���·� �����ؾ��Ѵ�!
	 * 
	 * @param from
	 * @param to
	 * @return key array
	 */
	Object[] indexRange(int from, int to);
}
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
	 * 자주 억세스 되므로 잘 만들어야 한다.
	 * 
	 * @param key
	 * @return -1 if not found
	 */
	int indexOf(Object key);

	/**
	 * 리턴시 반드시 key에 해당하는 class의 array 형태로 리턴해야한다!
	 * 
	 * @param from
	 * @param to
	 * @return key array
	 */
	Object[] indexRange(int from, int to);
}
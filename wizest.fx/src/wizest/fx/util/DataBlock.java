package wizest.fx.util;

/**
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public interface DataBlock {
	/**
	 * 블럭 내에 key 해당 하는 값을 찾아서 리턴한다. 자주 쓰이므로 퍼포먼스를 고려하여 작성한다.
	 * 
	 * @param key
	 * @return null if not found
	 */
	Object find(Object key);
}

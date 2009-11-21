package wizest.fx.util;

/**
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public interface DataBlock {
	/**
	 * �� ���� key �ش� �ϴ� ���� ã�Ƽ� �����Ѵ�. ���� ���̹Ƿ� �����ս��� ����Ͽ� �ۼ��Ѵ�.
	 * 
	 * @param key
	 * @return null if not found
	 */
	Object find(Object key);
}

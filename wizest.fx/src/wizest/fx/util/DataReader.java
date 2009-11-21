package wizest.fx.util;

/**
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public interface DataReader {
	/**
	 * @param keys
	 * @param from
	 * @param to
	 * @return must not null
	 */
	DataBlock read(DataKeys keys, int from, int to);
}
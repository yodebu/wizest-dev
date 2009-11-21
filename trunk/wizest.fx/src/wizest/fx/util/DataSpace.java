package wizest.fx.util;

import java.util.Arrays;

/**
 * @see DataBlock
 * @see DataKeys
 * @see DataReader
 * @see DataSpace
 */

public class DataSpace {
	private DataKeys keys;
	private DataReader reader;

	private int blockSize;
	private Slots slots;

	DataSpace() {
	}

	public DataSpace(DataKeys keys, DataReader reader, int blockSize,
			int slotSize) {
		this.keys = keys;
		this.reader = reader;
		this.blockSize = blockSize;
		this.slots = new Slots(slotSize);
	}

	public DataKeys getDataKeys() {
		return this.keys;
	}

	/**
	 * 각 slot에 저장되어 있는 block들을 검색하여 key에 해당하는 값을 가져온다.
	 * 
	 * @param key
	 * @return null if not found
	 */
	public/* synchronized */Object find(Object key) {
		int keyIndex = keys.indexOf(key);
		if (keyIndex < 0)
			return null;

		int blkIndex = keyIndex / blockSize;
		DataBlock blk = slots.getSlot(blkIndex);

		// load data block into data space ; 해당 블럭이 없으면 DB에서 로드한다.
		if (blk == null) {
			if (keyIndex < 0) {
				return null;
			}

			// 범위지정
			int from = keyIndex - (keyIndex % blockSize);
			int to = from + blockSize - 1;
			if (to >= keys.size()) {
				to = keys.size() - 1; // 최대 범위를 넘어가는 것을 제한

			}
			blk = reader.read(keys, from, to);
			slots.feedNewSlot(blkIndex, blk);
		}
		return blk.find(key);
	}
}

class Slots {
	private int size;
	private DataBlock[] dbSlots;
	private int[] blkIdxSlots;
	private int slotPtr;

	private int cachePtr;
	private DataBlock cacheSlot;

	public Slots(int size) {
		this.size = size;
		this.dbSlots = new DataBlock[size];
		this.blkIdxSlots = new int[size];
		Arrays.fill(blkIdxSlots, -Integer.MIN_VALUE); // has no block.

		this.slotPtr = 0;
		this.cachePtr = -Integer.MIN_VALUE;
		cacheSlot = null;
	}

	public synchronized void feedNewSlot(int blkIndex, DataBlock blk) {
		dbSlots[slotPtr] = blk;
		blkIdxSlots[slotPtr] = blkIndex;
		slotPtr = ++slotPtr % size;
	}

	/**
	 * @param blkIndex
	 * @return not if not found
	 */
	public synchronized DataBlock getSlot(int blkIndex) {
		if (cachePtr == blkIndex) {
			return cacheSlot;
		} else {
			// 해당 slot을 찾는다.
			for (int i = 0; i < this.size; ++i) {
				if (blkIdxSlots[i] == blkIndex) {
					cachePtr = blkIndex;
					cacheSlot = dbSlots[i];
					return cacheSlot;
				}
			}
			return null;
		}
	}
}

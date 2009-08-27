package wizest.fx.util;

import java.util.LinkedHashMap;
import java.util.Map;

//import org.apache.commons.collections.map.LRUMap;



public class GenericCache {
	private ICache cache;

	public GenericCache(int cacheSize) {
		this.cache = new HashMapCache(cacheSize);

		// use jakarta collection's LRUMap
		// this.cache=new LRUCache(cacheSize);
	}

	public GenericCache(int cacheSize, int initialCapacity) {
		this.cache = new HashMapCache(cacheSize, initialCapacity);
	}

	public void put(Object key, Object value) {
		synchronized (cache) {
			cache.put(key, value);
		}
	}

	public Object get(Object key) {
		synchronized (cache) {
			return cache.get(key);
		}
	}

	public void clear() {
		synchronized (cache) {
			cache.clear();
		}
	}

	public Object remove(Object key) {
		synchronized (cache) {
			return cache.remove(key);
		}
	}

	public boolean containsKey(Object key) {
		return cache.containsKey(key);
	}
}

interface ICache {
	Object put(Object key, Object value);

	Object get(Object key);

	Object remove(Object o);

	void clear();

	boolean containsKey(Object key);
}

class HashMapCache extends LinkedHashMap implements ICache {
	int cacheSize;

	public HashMapCache(int cacheSize) {
		super(cacheSize);
		this.cacheSize = cacheSize;
	}

	public HashMapCache(int cacheSize, int initialCapacity) {
		super(initialCapacity);
		this.cacheSize = cacheSize;
	}

	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > cacheSize;
	}
}

// class LRUCache
// extends LRUMap
// implements ICache
// {
// public LRUCache(int cacheSize) {
// super(cacheSize);
// }
//
// }

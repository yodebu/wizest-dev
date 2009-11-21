package wizest.fx.util;

import java.util.HashMap;
import java.util.Set;

public class GenericContext implements Context {
	private HashMap map;
	private Context parent;

	public GenericContext() {
		this(null);
	}

	public GenericContext(Context parent) {
		map = new HashMap();
		this.parent = parent;
	}

	public synchronized void release() {
		map.clear();
		map = null;
		parent = null;
	}

	public Object findAttribute(String name) {
		return getAttribute(name);
	}

	public void setAttribute(String name, Object obj) {
		synchronized (map) {
			map.put(name, obj);
		}
	}

	public Object getAttribute(String name) {
		synchronized (map) {
			return map.get(name);
		}
	}

	public void removeAttribute(String name) {
		synchronized (map) {
			map.remove(name);
		}
	}

	public Set getAttributeNames() {
		return ((HashMap) map.clone()).keySet();
	}

	public Context getParent() {
		return parent;
	}

	protected void setParent(Context parent) {
		this.parent = parent;
	}
}

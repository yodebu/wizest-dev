package wizest.fx.session;

import java.util.Enumeration;
import java.util.Hashtable;

public class SimpleSession extends AbstractSession {
	SessionContext context;

	Hashtable repository;
	String id;
	long creationTime;
	long lastAccessedTime;

	int maxInactiveInterval;
	boolean isNew;

	public SimpleSession(SessionContext context, String id, int maxInactiveInterval) {
		this.repository = new Hashtable();

		this.setSessionContext(context);
		this.setId(id);
		this.setCreationTime(System.currentTimeMillis());
		this.setMaxInactiveInterval(maxInactiveInterval);

		this.isNew = true;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public void setCreationTime(long time) {
		this.creationTime = time;
		this.lastAccessedTime = time;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		if (id == null) {
			throw new IllegalStateException("Null Session ID.");
		}
		this.id = id;
	}

	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	public void setLastAccessedTime(long time) {
		this.lastAccessedTime = time;
	}

	public SessionContext getSessionContext() {
		return this.context;
	}

	public void setSessionContext(SessionContext context) {
		this.context = context;
	}

	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int interval) {
		this.maxInactiveInterval = interval;
	}

	public Object getAttribute(String name) {
		refreshAccessedTime();
		return repository.get(name);
	}

	public void setAttribute(String name, Object value) {
		refreshAccessedTime();
		if (name == null) {
			throw new NullPointerException("key must NOT be null.");
		}
		if (value == null) {
			this.removeAttribute(name);
		} else {
			this.repository.put(name, value);
		}
	}

	public void removeAttribute(String name) {
		refreshAccessedTime();
		this.repository.remove(name);
	}

	public void removeAttributeAll() {
		refreshAccessedTime();
		this.repository.clear();
	}

	public Enumeration getAttributeNames() {
		refreshAccessedTime();
		return this.repository.elements();
	}

	public void invalidate() {
		refreshAccessedTime();
		this.repository.clear();
	}

	public boolean isNew() {
		return this.isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getInfo() {
		return this.getClass().getName();
	}

	private void refreshAccessedTime() {
		this.setLastAccessedTime(System.currentTimeMillis());
	}
}

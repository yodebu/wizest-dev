package wizest.fx.pool;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author wizest
 */
public final class SimpleKeyedObjectPool implements KeyedObjectPool {
    Hashtable poolMap = null;
    int maxObject;

    /**
     * @param maxObject
     *            각 key마다 최대 object 크기
     */
    public SimpleKeyedObjectPool(int maxObject) {
        this.poolMap = new Hashtable();
        this.maxObject = maxObject;
    }
    
    public SimpleKeyedObjectPool() {
        this(16);
    }

    public synchronized void addObject(Object key, Object o) {
        if (poolMap.contains(key)) {
            ObjectPool pool = (ObjectPool) poolMap.get(key);
            pool.addObject(o);
        } else {
            ObjectPool pool = new SimpleObjectPool(this.maxObject);
            pool.addObject(o);
            poolMap.put(key, pool);
        }
    }

    public synchronized Object getObject(Object key) {
        if (poolMap.contains(key)) {
            ObjectPool pool = (ObjectPool) poolMap.get(key);
            return pool.getObject();
        } else {
            return null;
        }
    }

    public void releaseObject(Object key, Object o) {
        addObject(key, o);
    }

    public synchronized void clear() {
        Enumeration e = poolMap.elements();
        while (e.hasMoreElements()) {
            ObjectPool pool = (ObjectPool) e.nextElement();
            pool.destroy();
        }
        poolMap.clear();
    }

    public synchronized void destroy() {
        Enumeration e = poolMap.elements();
        while (e.hasMoreElements()) {
            ObjectPool pool = (ObjectPool) e.nextElement();
            pool.destroy();
        }
        poolMap = null;
    }
}
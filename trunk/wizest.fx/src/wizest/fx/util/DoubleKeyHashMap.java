package wizest.fx.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 주의: mapping 데이터를 추가(put),삭제(remove)하는 것은 반드시 이 클래스를 통해야 하며 이 클래스로 부터 얻은 collection에 직접 추가, 삭제 하면 안된다.
 *
 */

public class DoubleKeyHashMap
{
    private HashMap map;

    public DoubleKeyHashMap()
    {
        map=new HashMap();
    }

    public int size()
    {
        int size=0;
        Iterator i=map.values().iterator();

        while(i.hasNext()) {
            size+=((HashMap)i.next()).size();

        }
        return size;
    }

    public boolean isEmpty()
    {
        if(map.isEmpty()) {
            return true;
        }

//        int size=0;
        Iterator i=map.values().iterator();

        while(i.hasNext()) {
            if(!((HashMap)i.next()).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public boolean containsKey(Object key1,Object key2)
    {
        if(map.containsKey(key1)) {
            if(((HashMap)map.get(key1)).containsKey(key2)) {
                return true;
            }
        }

        return false;
    }

    public boolean containsValue(Object value)
    {
        Iterator i=map.values().iterator();

        while(i.hasNext()) {
            if(((HashMap)i.next()).containsValue(value)) {
                return true;
            }
        }

        return false;
    }

    public Object get(Object key1,Object key2)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            return null;
        }
        else {
            return inner.get(key2);
        }
    }

    private HashMap get(Object key1)
    {
        return(HashMap)map.get(key1);
    }

    public Object put(Object key1,Object key2,Object value)
    {
        // return a previous value
        synchronized(map) {
            HashMap inner = (HashMap) map.get(key1);
            if (inner == null) {
                inner = new HashMap();
                inner.put(key2, value);
                map.put(key1, inner);

                return null;
            } else {
                return inner.put(key2, value);
            }
        }
    }

    public Object remove(Object key1,Object key2)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            map.remove(key1);
            return null;
        }
        else {
            Object previous=inner.remove(key2);
            if(inner.isEmpty()) {
                map.remove(key1);

            }
            return previous;
        }
    }

    /**
     * key1으로 시작하는 모든 entry를 지운다.
     * @param key1
     * @return 지워진 innerHashMap
     */
    public HashMap remove(Object key1)
    {
        return(HashMap)map.remove(key1);
    }

    public void putAll(Object key1,Map t)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            inner=new HashMap(t);
            map.put(key1,inner);
        }
        else {
            inner.putAll(t);
        }
    }

    public void clear()
    {
        map.clear();
    }

    public Set key1Set()
    {
        return map.keySet();
    }

    public Set key2Set(Object key1)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            return new HashMap().keySet();
        }
        else {
            return inner.keySet();
        }
    }

    public Collection values(Object key1)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            return new HashMap().values();
        }
        else {
            return inner.values();
        }
    }

    public Set entrySet(Object key1)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            return new HashMap().entrySet();
        }
        else {
            return inner.entrySet();
        }
    }

    public boolean equals(Object o)
    {
        if(o instanceof DoubleKeyHashMap) {
            DoubleKeyHashMap c=(DoubleKeyHashMap)o;

            if(!c.key1Set().equals(map.keySet())) {
                return false;
            }

            Set keys=map.keySet();
            Iterator i=keys.iterator();

            while(i.hasNext()) {
                Object k=i.next();

                HashMap h1=(HashMap)map.get(k);
                HashMap h2=c.get(k);

                if(!h1.equals(h2)) {
                    return false;
                }
            }

            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 주의! : key1에 해당하는 innerHashMap이 없을 경우 새로운 HashMap을 생성해서 리턴한다.
     * 그러므로 얻은 innerHashMap에 직접 entry를 추가하면 안된다!
     * @param key1
     * @return
     */
    public HashMap innerHashMap(Object key1)
    {
        HashMap inner=(HashMap)map.get(key1);
        if(inner == null) {
            return new HashMap();
        }
        else {
            return inner;
        }
    }

    public String toString()
    {
        return map.toString();
    }
}
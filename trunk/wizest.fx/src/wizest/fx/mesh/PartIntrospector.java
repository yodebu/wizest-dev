/*
 * Created on 2004. 8. 17
 */
package wizest.fx.mesh;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author user
 */
public class PartIntrospector {
    public final static String LISTENER_PREFIX = "listen";
    public final static String FIRENER_PREFIX = "fire";

    private PartIntrospector() {}

    private final static Map fireMethodsCache = new HashMap();
    private final static Map listenMethodsCache = new HashMap();
    private final static Map listenMethodMapCache = new HashMap();

    public static Method[] getListenMethods(Class partClass) {
        Method[] mArr = (Method[]) listenMethodsCache.get(partClass);
        if (mArr == null) {
            ArrayList list = new ArrayList();

            Method[] ms = partClass.getMethods();
            for (int i = 0, length = ms.length; i < length; ++i) {
                Method m = ms[i];

                if ((m.getModifiers() & Modifier.PUBLIC) > 0 && m.getReturnType() == Void.class && m.getName().startsWith(LISTENER_PREFIX) && m.getParameterTypes().length > 0 && m.getParameterTypes()[0].equals(Part.class)
                        && m.getExceptionTypes().length == 0)
                    list.add(m);
            }
            listenMethodsCache.put(partClass, mArr = (Method[]) list.toArray(new Method[0]));
        }
        return mArr;
    }

    public static Method[] getFireMethods(Class partClass) {
        Method[] mArr = (Method[]) fireMethodsCache.get(partClass);
        if (mArr == null) {
            ArrayList list = new ArrayList();

            Method[] ms = partClass.getMethods();
            for (int i = 0, length = ms.length; i < length; ++i) {
                Method m = ms[i];

                if ((m.getModifiers() & Modifier.PUBLIC) > 0 && m.getReturnType() == Void.class && m.getName().startsWith(FIRENER_PREFIX) &&
                //m.getParameterTypes().length > 0 &&
                        //m.getParameterTypes()[0].equals(Part.class) &&
                        m.getExceptionTypes().length == 0)
                    list.add(m);
            }
            fireMethodsCache.put(partClass, mArr = (Method[]) list.toArray(new Method[0]));
        }
        return mArr;
    }

    public static Method getListenMethod(Class partClass, String method, Object[] args) {
        Map _listeners = (Map) listenMethodMapCache.get(partClass);
        if (_listeners == null) {
            _listeners = _buildListenerMethods(partClass.getClass());
            listenMethodMapCache.put(partClass, _listeners);
        }

        return (Method) _listeners.get(_getMangledListenMethodKey(method, args));
    }

    public static Method getListenMethod(Class partClass, String method) {
        return getListenMethod(partClass, method, new Object[0]);
    }

    /**
     * private String _getMangledName(String method, Object[] args) 과 같은 이름을
     * 생성해야한다.
     */
    private static String _getMangledListenMethodKey(Method m) {
        StringBuffer buf = new StringBuffer(m.getName());
        Class[] ts = m.getParameterTypes();
        for (int i = 0, length = ts.length; i < length; ++i)
            buf.append(ts[i].getName());

        return buf.toString();
    }

    /**
     * private String _getMangledNameKey(Method m) 과 같은 이름을 생성해야한다.
     */
    private static String _getMangledListenMethodKey(String method, Object[] args) {
        StringBuffer buf = new StringBuffer(LISTENER_PREFIX + method);
        buf.append(Part.class.getName());
        for (int i = 0, length = args.length; i < length; ++i)
            buf.append(args[i].getClass().getName());

        return buf.toString();
    }

    private static Map _buildListenerMethods(Class partClass) {
        HashMap _listeners = new HashMap();
        Method[] ms = getListenMethods(partClass);
        for (int i = 0, length = ms.length; i < length; ++i)
            _listeners.put(_getMangledListenMethodKey(ms[i]), ms[i]);

        return _listeners;
    }
}
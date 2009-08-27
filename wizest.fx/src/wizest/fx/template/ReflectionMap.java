package wizest.fx.template;
/**
 * Attribute의 value를 handler에 reflect할 때 사용한다.
 * 
 * @author wizest
 */
public class ReflectionMap implements Cloneable {
    public static final String STATIC_PROPERTY_id = "id";
    public static final String STATIC_PROPERTY_tagMarker = "tagMarker";
    public static final String STATIC_PROPERTY_attributeMap = "attributeMap";
    public static final String RUNTIME_PROPERTY_parentRuntimeTag = "parentRuntimeTag";
    public static final int PROPERTY_LEN = 4;
    private String[] names;
    private Object[] values;

    public ReflectionMap(String[] names, Object[] values) {
        this.names = names;
        this.values = values;
    }

    public String[] getNames() {
        return this.names;
    }

    public Object[] getValues() {
        return this.values;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{");
        for (int i = 0; i < names.length; ++i) {
            buff.append(names[i]);
            buff.append("=");
            buff.append(values[i]);
            if (i + 1 < names.length)
                buff.append(",");
        }
        buff.append("}");
        return buff.toString();
    }

    protected void append(String name, Object value) {
        int len = names.length;
        String[] nN = new String[len + 1];
        Object[] nV = new Object[len + 1];
        nN[len] = name;
        nV[len] = value;
        System.arraycopy(names, 0, nN, 0, len);
        System.arraycopy(values, 0, nV, 0, len);
        names = nN;
        values = nV;
    }

    public ReflectionMap copy() {
        try {
            return (ReflectionMap) clone();
        } catch (CloneNotSupportedException e) {
            //            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    //    protected void append(String[] aNames, Object[] aValues) {
    //        int len = names.length;
    //        int newLen = aNames.length;
    //        String[] nN = new String[len + newLen];
    //        Object[] nV = new Object[len + newLen];
    //        System.arraycopy(aNames, 0, nN, len, newLen);
    //        System.arraycopy(aValues, 0, nV, len, newLen);
    //        System.arraycopy(names, 0, nN, 0, len);
    //        System.arraycopy(values, 0, nV, 0, len);
    //        names = nN;
    //        values = nV;
    //    }
}
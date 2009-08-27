/*
 * Created on 2004. 10. 7
 */
package wizest.fx.template.tagext;
/**
 * @author wizest
 */
public class AttributeMap {
    private final String[] names;
    private final Object[] values;

    public AttributeMap(final String[] names, final Object[] values) {
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
}

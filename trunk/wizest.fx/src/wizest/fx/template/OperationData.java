package wizest.fx.template;

import java.util.ArrayList;

/**
 * @author wizest
 */
public class OperationData {
    private ArrayList data = null;

    public OperationData() {
        this.data = new ArrayList();
    }

    /**
     * @param dataObject
     * @return dataObject가 저장된 address
     */
    public int add(Object dataObject) {
        data.add(dataObject);
        return getLastAddress();
    }

    public int getLastAddress() {
        return data.size() - 1;
    }

    public int getSize() {
        return data.size();
    }

    public final Object[] getDataArray() {
        return data.toArray();
    }

    public final Object getDataAt(int address) {
        return data.get(address);
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        Object[] os = getDataArray();
        for (int i = 0; i < os.length; ++i) {
            buff.append("Address ");
            buff.append(i);
            buff.append(":  ");
            buff.append(os[i]);
            buff.append("\n");
        }
        return buff.toString();
    }
}
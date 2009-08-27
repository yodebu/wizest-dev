package wizest.fx.template;
/**
 * @author wizest
 */
public class TemplateBinary {
    private OperationCode code;
    private OperationData data;

    public TemplateBinary(OperationCode code, OperationData data) {
        this.code = code;
        this.data = data;
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("{code=\n");
        buff.append(code);
        buff.append(",data=\n");
        buff.append(data);
        buff.append("}");
        return buff.toString();
    }

    public OperationCode getOperationCode() {
        return this.code;
    }

    public OperationData getOperationData() {
        return this.data;
    }
}
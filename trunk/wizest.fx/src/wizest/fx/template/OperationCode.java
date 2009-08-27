package wizest.fx.template;

import java.util.ArrayList;

/**
 * @author wizest
 */
public class OperationCode {
    // operation code -----------------------------------------------------------------------------
    //                      operator operand (�ǿ����� ��,�ǹ�(Ÿ��))
    // --------------------------------------------------------------------------------------------
    public final static int OPCODE_TERMINATE = 0; //  0 X ���� ����
    public final static int OPCODE_EXCEPTION = 1; //  1 �����޼���(data address) ���ܸ� �߻� -> ������ ����
    public final static int OPCODE_GOTO = 2; //  1 ������ code address
    public final static int OPCODE_NO_OPERATION = 3; //  0 �ƹ� �ϵ� ���� �ʴ´�.
    public final static int OPCODE_PRINT = 4; //  1 ����Ʈ�� ������(String)(data address)
    public final static int OPCODE_LOAD_HANDLER = 10; //  2 tag handler name(data address), reflection parameter(data address)
    public final static int OPCODE_LOAD_PAGE_HANDLER = 11; //  2 tag handler name(data address), reflection parameter(data address)
    public final static int OPCODE_RELEASE_HANDLER = 12; //  0 X
    // conditional jump operation
    public final static int OPCODE_DO_START_TAG = 20; //  3 SKIP_BODY�� ��� ������ �ּ�(code address),EVAL_BODY_INCLUDE�� ���,EVAL_BODY_BUFFERED�� ���
    public final static int OPCODE_DO_END_TAG = 21; //  2 SKIP_PAGE�� ��� ������ �ּ�(code address),EVAL_PAGE�� ��� ������ �ּ�(code address),
    public final static int OPCODE_DO_AFTER_BODY = 22; //  2 SKIP_BODY�� ��� ������ �ּ�(code address),EVAL_BODY_AGAIN�� ��� ������ �ּ�(code address)
    public final static int OPCODE_DO_START_TEMPLATE = 23; // 2 SKIP_PAGE�� ��� ������ �ּ�(code address),EVAL_PAGE�� �� ������ �ּ�(code address)
    public final static int OPCODE_DO_END_TEMPLATE = 24; // 0 X
    public final static int OPCODE_DO_CREATE_PAGEINFO = 25; // 0 X
    public final static int OPCODE_DO_AFTER_PAGE = 26; // 2 SKIP_PAGE/SKIP_PAGE_WITHOUT_PAGE_GENERATION�� ��� ������ �ּ�(code address),EVAL_PAGE_AGAIN/EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION�� �� ������ �ּ�(code address)
    // evaluating mark for error tracing
    public final static int OPCODE_EVAL_MARK = 30; // 1 ���� �۾����� tag�� Marker(wizest.fx.Marker)(data address)
    // add 2004.10.28
    public final static int OPCODE_LINE_NUMBER = 31; // 1 line number(int)
    // --------------------------------------------------------------------------------------------
    private ArrayList code = null;

    public OperationCode() {
        this.code = new ArrayList();
    }

    public int add(int opcode) {
        int[] c = new int[] { opcode };
        code.add(c);
        return getLastAddress();
    }

    public int add(int opcode, int operand1) {
        int[] c = new int[] { opcode, operand1 };
        code.add(c);
        return getLastAddress();
    }

    public int add(int opcode, int operand1, int operand2) {
        int[] c = new int[] { opcode, operand1, operand2 };
        code.add(c);
        return getLastAddress();
    }

    public int add(int opcode, int operand1, int operand2, int operand3) {
        int[] c = new int[] { opcode, operand1, operand2, operand3 };
        code.add(c);
        return getLastAddress();
    }

    public final int[] getCodeAt(int address) {
        return (int[]) code.get(address);
    }

    public void updateCodeAt(int address, int[] newCode) {
        this.code.set(address, newCode);
    }

    public void updateCodeAt(int address, int index, int value) {
        int[] oldCode = getCodeAt(address);
        oldCode[index] = value;
        updateCodeAt(address, oldCode);
    }

    /**
     * code�� ����� ������ �ּ�(�ε���)
     * 
     * @return
     */
    public int getLastAddress() {
        return code.size() - 1;
    }

    /**
     * ���� code�� ����� �ּ� = getLastAddress()+1
     * 
     * @return
     */
    public int getAddressToAdd() {
        return code.size();
    }

    /**
     * ���� code�� ����ǰ� �� ���� �� ������ �ּ� = getAddressToAdd()+1 = getLastAddress()+2
     * 
     * @return
     */
    public int getAfterAddressToAdd() {
        return code.size() + 1;
    }

    public int getSize() {
        return code.size();
    }

    /**
     * ù��° index -> code address, �ι�° index -> operation code(0) �Ǵ� operand index(1,2,3)
     * 
     * <pre>
     *        int[][] code = cs.getCodeArray();
     *        code[1][2]  -&gt; address:1 �� operand2
     *        code[1][0]  -&gt; address:1 �� operation code
     *        code[1][1]  -&gt; address:1 �� operand1
     *        code[2][3]  -&gt; address:2 �� operand3
     * </pre>
     * 
     * @return
     */
    public final int[][] getCodeArray() {
        return (int[][]) code.toArray(new int[0][0]);
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        int[][] codeArray = getCodeArray();
        for (int i = 0; i < codeArray.length; ++i) {
            buff.append("Address ");
            buff.append(i);
            buff.append(":  ");
            int[] opcode = codeArray[i];
            for (int j = 0; j < opcode.length; ++j) {
                buff.append("\t");
                buff.append(opcode[j]);
            }
            buff.append("\n");
        }
        return buff.toString();
    }
}
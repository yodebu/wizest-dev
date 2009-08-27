package wizest.fx.template;

import java.util.ArrayList;

/**
 * @author wizest
 */
public class OperationCode {
    // operation code -----------------------------------------------------------------------------
    //                      operator operand (피연산자 수,의미(타입))
    // --------------------------------------------------------------------------------------------
    public final static int OPCODE_TERMINATE = 0; //  0 X 정상 종료
    public final static int OPCODE_EXCEPTION = 1; //  1 에러메세지(data address) 예외를 발생 -> 비정상 종료
    public final static int OPCODE_GOTO = 2; //  1 점프할 code address
    public final static int OPCODE_NO_OPERATION = 3; //  0 아무 일도 하지 않는다.
    public final static int OPCODE_PRINT = 4; //  1 프린트할 데이터(String)(data address)
    public final static int OPCODE_LOAD_HANDLER = 10; //  2 tag handler name(data address), reflection parameter(data address)
    public final static int OPCODE_LOAD_PAGE_HANDLER = 11; //  2 tag handler name(data address), reflection parameter(data address)
    public final static int OPCODE_RELEASE_HANDLER = 12; //  0 X
    // conditional jump operation
    public final static int OPCODE_DO_START_TAG = 20; //  3 SKIP_BODY일 경우 점프할 주소(code address),EVAL_BODY_INCLUDE일 경우,EVAL_BODY_BUFFERED일 경우
    public final static int OPCODE_DO_END_TAG = 21; //  2 SKIP_PAGE일 경우 점프할 주소(code address),EVAL_PAGE일 경우 점프할 주소(code address),
    public final static int OPCODE_DO_AFTER_BODY = 22; //  2 SKIP_BODY일 경우 점프할 주소(code address),EVAL_BODY_AGAIN일 경우 점프할 주소(code address)
    public final static int OPCODE_DO_START_TEMPLATE = 23; // 2 SKIP_PAGE일 경우 점프할 주소(code address),EVAL_PAGE일 때 점프할 주소(code address)
    public final static int OPCODE_DO_END_TEMPLATE = 24; // 0 X
    public final static int OPCODE_DO_CREATE_PAGEINFO = 25; // 0 X
    public final static int OPCODE_DO_AFTER_PAGE = 26; // 2 SKIP_PAGE/SKIP_PAGE_WITHOUT_PAGE_GENERATION일 경우 점프할 주소(code address),EVAL_PAGE_AGAIN/EVAL_PAGE_AGAIN_WITHOUT_PAGE_GENERATION일 때 점프할 주소(code address)
    // evaluating mark for error tracing
    public final static int OPCODE_EVAL_MARK = 30; // 1 현재 작업중인 tag의 Marker(wizest.fx.Marker)(data address)
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
     * code가 저장된 마지막 주소(인덱스)
     * 
     * @return
     */
    public int getLastAddress() {
        return code.size() - 1;
    }

    /**
     * 다음 code가 저장될 주소 = getLastAddress()+1
     * 
     * @return
     */
    public int getAddressToAdd() {
        return code.size();
    }

    /**
     * 다음 code가 저장되고 난 다음 번 저장할 주소 = getAddressToAdd()+1 = getLastAddress()+2
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
     * 첫번째 index -> code address, 두번째 index -> operation code(0) 또는 operand index(1,2,3)
     * 
     * <pre>
     *        int[][] code = cs.getCodeArray();
     *        code[1][2]  -&gt; address:1 의 operand2
     *        code[1][0]  -&gt; address:1 의 operation code
     *        code[1][1]  -&gt; address:1 의 operand1
     *        code[2][3]  -&gt; address:2 의 operand3
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
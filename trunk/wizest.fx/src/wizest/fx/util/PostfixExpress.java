package wizest.fx.util;

public class PostfixExpress {
	private String origin;

	private String[] express;
	private int[] needOperandNumber;

	public PostfixExpress(String origin, String[] express, int[] needOperand) {
		this.origin = origin;

		this.express = express;
		this.needOperandNumber = needOperand;
	}

	public String getOrigin() {
		return this.origin;
	}

	public String[] getExpressions() {
		return express;
	}

	public int[] getNeedOperandNumber() {
		return needOperandNumber;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("{");
		for (int i = 0; i < express.length; ++i) {
			if (needOperandNumber[i] > 0) {
				buf.append("Op:");
			}
			buf.append(express[i]);
			buf.append(",");
		}
		buf.append("}");

		return buf.toString();
	}
}

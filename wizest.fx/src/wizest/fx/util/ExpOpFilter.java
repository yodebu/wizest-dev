package wizest.fx.util;

import java.util.Stack;

/**
 * Operation Filter of Expression
 * 
 * @author �����(wizest@gmail.com)
 * 
 */
public class ExpOpFilter {
	private ToPostfixExpress to;

	public ExpOpFilter(ToPostfixExpress to) {
		this.to = to;
	}

	/**
	 * ǥ���Ŀ��� �����ڸ� ������. �̶� �ǿ������� ���� 2���� ����. �����ڰ� ���� �� �� altExp�� null�� ��� ù��°
	 * �ǿ����ڸ� ��ü ǥ�������� ���Ǹ� �׷��� ���� ��� altExp�� ���. <code>
	 * 	A + B -> A when alt==null 
	 *  A + B -> C when alt=="C"
	 * </code>
	 * 
	 * @param eval
	 *            ���ϴ� ǥ����
	 * @param operator
	 *            �����ϰ� ���� ������
	 * @param altExp
	 *            alternative expression - �����ڰ� ���ŵ� �� ����� ǥ����, null�� ��� ù��°
	 *            �ǿ������ ��ü������ ���
	 * @return �����ڰ� ���ŵ� ǥ����
	 * @throws UnsupportedOperationException
	 */
	public String filter(String eval, String operator, String altExp)
			throws UnsupportedOperationException {
		PostfixExpress exp = to.toPostfixExpress(eval);
		Stack<String> s = new Stack<String>();
		String[] es = exp.getExpressions();
		int[] esN = exp.getNeedOperandNumber();

		for (int i = 0, len = es.length; i < len; ++i) {
			String e = es[i];
			int n = esN[i];

			if (n == 0)
				s.push(e);
			else if (n == 2) {
				String e1 = s.pop();
				String e2 = s.pop();

				String t;
				if (e.equals(operator))
					if (altExp == null)
						t = e2;
					else
						t = altExp;
				else
					t = "(" + e2 + " " + e + " " + e1 + ")";
				s.push(t);
			} else
				// ����: Operand 2�� �ƴ� �����ڴ� �������� ����
				throw new UnsupportedOperationException(
						"Only supports the operation which has two operands.");
		}
		return s.pop();
	}

	public static void test() {
		String eval;
		String value;

		// 1
		eval = "a>1 && b>3 && ( ( c>d && (d=3 || x>1) && d>3 ) ) && f=7 && (a<9 || b<9 ) && (d>5 && f>5)";
		value = "a>1&&b>3&&c>d&&d>3&&f=7&&d>5&&f>5";
		testPrint(eval, value);
		// 2
		eval = "a>1 && b>3 || x>3";
		value = "";
		testPrint(eval, value);
		// 3
		eval = "a>1 && b>3 || (x>3 && v>7) ";
		value = "";
		testPrint(eval, value);
		// 4
		eval = "a>1 && (b>3 || (x>3 && v>7)) ";
		value = "a>1";
		testPrint(eval, value);
		// 5
		eval = " x>3 || (a>1 && b>3)";
		value = "";
		testPrint(eval, value);
		// 6
		eval = " x>3 && (a>1 || b>3)";
		value = "x>3";
		testPrint(eval, value);
		// 7
		eval = " (a>1 || b>3 && (a>1 && b>3)) && x>3 ";
		value = "x>3";
		testPrint(eval, value);
		// 8
		eval = " (a>1 || b>3 && (a>1 && b>3)) && x>3 ";
		value = "x>3";
		testPrint(eval, value);
		// 9
		eval = " ((a>1 || b>3 && (a>1 && b>3)) || x>3) && x>3 ";
		value = "x>3";
		testPrint(eval, value);
	}

	private static void testPrint(String eval, String value) {
		String[][][] sortOps = { { {} }, { { "(", ")" } },
				{ {}, { "&&", "||" } } };
		ToPostfixExpress to = new ToPostfixExpress(sortOps);
		ExpOpFilter f = new ExpOpFilter(to);

		// ����
		System.out.println(eval);
		System.out.println(value);

		// �ƹ� �͵� ���� ���� ���� -> ��� ��� ��ȣ ���
		System.out.println(f.filter(eval, "", null));

		// || ������ ����
		System.out.println(f.filter(eval, "||", "TRUE"));

		// && ������ ����
		// System.out.println(f.filter(eval, "&&", null));

		System.out.println("-----");
	}

	public static void main(String[] args) {
		test();
	}

}

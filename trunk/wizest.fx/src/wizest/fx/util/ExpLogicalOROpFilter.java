package wizest.fx.util;

import java.util.Stack;

/**
 * Logical OR Operation Filter of Expression
 * 
 * @author �����(wizest@gmail.com)
 * 
 */
public class ExpLogicalOROpFilter {
	public final static String TRUE_EXP = "__TRUE__";
	private final static String[][][] sortOps = { { {} }, { { "(", ")" } },
			{ {}, { "&&", "||" } } };
	private final static ToPostfixExpress to = new ToPostfixExpress(sortOps);

	/**
	 * &&, || �� �� �����ڸ� �����ϴ� ������ �ִٰ� �����ϰ� || ������ TRUE�� �Ͽ� 2���� �ǿ������ ������
	 * 
	 * @note �������� TRUE�̾ ������ ���� ���� ��� ���ڿ�("")�� ��ȯ
	 * 
	 * @param eval
	 * @return
	 * @throws UnsupportedOperationException
	 */
	public static String filterOR(String eval)
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
				if (e.equals("||"))
					t = TRUE_EXP;
				// when && operator
				else if (e1.equals(TRUE_EXP))
					if (e2.equals(TRUE_EXP))
						t = TRUE_EXP;
					else
						t = e2;
				else if (e2.equals(TRUE_EXP))
					if (e1.equals(TRUE_EXP))
						t = TRUE_EXP;
					else
						t = e1;
				else
					t = "(" + e2 + " " + e + " " + e1 + ")";
				s.push(t);
			} else
				// ����: Operand 2�� �ƴ� �����ڴ� �������� ����
				throw new UnsupportedOperationException(
						"Only supports the operation which has two operands.");
		}
		return s.peek().equals(TRUE_EXP) ? "" : s.pop();
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
		System.out.println(filterOR(eval));

		System.out.println("-----");
	}

	public static void main(String[] args) {
		test();
	}
}

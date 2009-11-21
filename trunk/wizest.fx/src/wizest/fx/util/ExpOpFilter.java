package wizest.fx.util;

import java.util.Stack;

/**
 * Operation Filter of Expression
 * 
 * @author 김상훈(wizest@gmail.com)
 * 
 */
public class ExpOpFilter {
	private ToPostfixExpress to;

	public ExpOpFilter(ToPostfixExpress to) {
		this.to = to;
	}

	/**
	 * 표현식에서 연산자를 제거함. 이때 피연산자의 수는 2개로 가정. 연산자가 제거 될 떄 altExp가 null일 경우 첫번째
	 * 피연산자를 대체 표현식으로 사용되며 그렇지 않을 경우 altExp를 사용. <code>
	 * 	A + B -> A when alt==null 
	 *  A + B -> C when alt=="C"
	 * </code>
	 * 
	 * @param eval
	 *            평가하는 표현식
	 * @param operator
	 *            제거하고 싶은 연산자
	 * @param altExp
	 *            alternative expression - 연산자가 제거될 때 사용할 표현식, null일 경우 첫번째
	 *            피연산식을 대체식으로 사용
	 * @return 연산자가 제거된 표현식
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
				// 주의: Operand 2가 아닌 연산자는 지원하지 않음
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

		// 원본
		System.out.println(eval);
		System.out.println(value);

		// 아무 것도 삭제 하지 않음 -> 모든 경우 괄호 사용
		System.out.println(f.filter(eval, "", null));

		// || 연산자 없앰
		System.out.println(f.filter(eval, "||", "TRUE"));

		// && 연산자 없앰
		// System.out.println(f.filter(eval, "&&", null));

		System.out.println("-----");
	}

	public static void main(String[] args) {
		test();
	}

}

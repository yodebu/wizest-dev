package wizest.fx.util;

import java.util.ArrayList;

/**
 * "�켱������ ������� ����" infix expression���� ����
 * 
 * '\' �� �����ڸ� �Ϲ� ���ڷ� �ٲپ� �ִ� ����� �Ѵ�. ������� '+' �� �����ڶ�� ���ǵɶ� \+ �� �Ϲ� ���ڿ� '+'����
 * �����Ѵ�. infixExpress �� express �� '\' �� ����Ͽ� ���� ����� ����.
 * 
 */

public class ToInfixExpress {
	private String[] plainOps;

	private GenericCache cache;
	private static final int MAX_CACHE = 100;

	public ToInfixExpress(String[] plainOps) {
		init(plainOps);
	}

/**
     * *. ������ ����Ʈ ����
     *
     *   private static String[][] searchOps= { {"(",")"}
     *                                        , {"<>","==",">=","<=","=",">","<"}
     *                                        , {"&&","||"}};
     *
     * @param orderedOperators - ���������δ� �켱������ ������� �����Ƿ� ����!
     */
	public ToInfixExpress(String[][] orderedOperators) {
		ArrayList al = new ArrayList();
		for (int i = 0; i < orderedOperators.length; ++i) {
			for (int j = 0; j < orderedOperators[i].length; ++j) {
				al.add(orderedOperators[i][j]);
			}
		}

		this.plainOps = (String[]) al.toArray(new String[0]);

		init(plainOps);
	}

	public ToInfixExpress(String[][][] orderedOperators) {
		ArrayList al = new ArrayList();
		for (int i = 0; i < orderedOperators.length; ++i) {
			for (int j = 0; j < orderedOperators[i].length; ++j) {
				for (int k = 0; k < orderedOperators[i][j].length; ++k) {
					al.add(orderedOperators[i][j][k]);
				}
			}
		}

		this.plainOps = (String[]) al.toArray(new String[0]);

		init(plainOps);

	}

	private void init(String[] plainOps) {
		this.plainOps = plainOps;
		this.cache = new GenericCache(ToInfixExpress.MAX_CACHE);
	}

	public InfixExpress toInfixExpress(String evalString) {
		if (evalString == null) {
			return new InfixExpress(null, new String[0], new boolean[0]);
		}

		InfixExpress infixEx = null;

		ArrayList ex = new ArrayList();
		ArrayList bs = new ArrayList();

		Marker mark = null;
		int parseIdx = 0;
		String op;
		String value;

		while (true) {
			// parseIdx���� �����ڸ� ã�´�.
			mark = indexOf(parseIdx, evalString); // ,plainOps);

			// �����ڸ� ã�� ���� ���
			if (mark == null) {
				op = null;
				value = evalString.substring(parseIdx).trim();
				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					bs.add(Boolean.FALSE);
				}
				break;
			}
			// �����ڸ� ã���� ���
			else {
				op = mark.getValue();
				value = evalString.substring(parseIdx, mark.getBeginIndex())
						.trim();

				if (value.length() > 0) {
					ex.add(filterSlashMark(value));
					bs.add(Boolean.FALSE);
				}
				ex.add(op);
				bs.add(Boolean.TRUE);

			}
			parseIdx = mark.getEndIndex();
		}

		String[] exs = (String[]) ex.toArray(new String[0]);
		boolean[] bss = new boolean[bs.size()];

		for (int i = 0; i < bss.length; ++i) {
			bss[i] = ((Boolean) bs.get(i)).booleanValue();
		}
		infixEx = new InfixExpress(evalString, exs, bss);
		cache.put(evalString, infixEx);
		return infixEx;
	}

	/**
	 * @param fromIndex
	 * @param text
	 * @return fromIndex�� ���� ���� operator�� ã�´�.
	 */
	private Marker indexOf(int fromIndex, String text) {
		Marker m = null;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < plainOps.length; ++i) {
			int pivot;
			int begin = fromIndex;
			for (;;) { // SCAN
				pivot = text.indexOf(plainOps[i], begin);
				if (USE_SLASH_MARK && pivot - 1 >= 0) {

					// backward slash count
					// slash�� Ȧ�����̸�
					int slashCnt = 0;
					for (int j = pivot - 1; j > 0
							&& text.charAt(j) == SLASH_MARK; --j) {
						++slashCnt;
					}
					if (slashCnt % 2 > 0) {
						begin = pivot + 1;
						continue;
					}
				}
				break;
			}

			if (pivot >= 0 && pivot < min) {
				min = pivot;
				m = new Marker(pivot, pivot + plainOps[i].length(), plainOps[i]);
			}
		}
		return m;
	}

	private static final char SLASH_MARK = '\\';
	private static final boolean USE_SLASH_MARK = true;

	private static String filterSlashMark(String s) {
		if (USE_SLASH_MARK) {
			if (s.indexOf(SLASH_MARK) < 0)
				return s;

			StringBuffer s2 = new StringBuffer(s.length());
			int slashCnt = 0;
			for (int i = 0, length = s.length(); i < length; ++i) {
				if (s.charAt(i) == SLASH_MARK)
					++slashCnt;
				else
					slashCnt = 0;

				// ������ ���� Ȧ���̸� �ô´�.
				if ((slashCnt % 2) > 0)
					;
				else
					s2.append(s.charAt(i));
			}
			return s2.toString();
		} else
			return s;
	}

	// public static void main(String[] args)
	// {
	// String s="aaa\\=bbbb=\\\\\\(ccc\\)\\cc\\dddddd\\\\\\eeeeee";
	// ToInfixExpress toInfix=new ToInfixExpress(new String[] {"(",")","="});
	// InfixExpress ie=toInfix.toInfixExpress(s);
	// System.out.println(ie);
	//
	// System.out.println(filterSlashMark(s));
	// System.out.println(s);
	// }
}

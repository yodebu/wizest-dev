package wizest.fx.util.win32;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.internal.win32.OS;

import wizest.fx.logging.LogBroker;

public class WindowUtil {
//	private static final Logger log = Logger.getLogger(WindowUtil.class.getName());
	private static final Logger log = LogBroker.getLogger(WindowUtil.class.getName());
	
	public static void main(String[] args) throws InterruptedException {
		// int hWnd = OS.FindWindowW(null, "KB�������� plustar\0".toCharArray());//
		// Ű���庸�Ƚ���
		// int hWnd = OS.FindWindowW(null,
		// "KB���� PLUSTAR HTS LOGIN\0".toCharArray()); // �α�
		// int hWnd = OS.FindWindowW(null,"KB plustar\0".toCharArray()); //����ȭ��

		// System.out.println(hWnd);
		// if (hWnd > 0)
		// OS.SetForegroundWindow(hWnd);
		// OS.SetFocus(hWnd);

		// System.out.println(enumHWnd());
		// System.out.println(enumWindowText());
		System.out.println(findWindowWithText("KB plustar", "PLUSTAR HTS"));
		System.out.println(setForegroundWindowWithText("KB plustar", "PLUSTAR HTS", 1000));
		Thread.sleep(500);

	}

	/**
	 * @param text
	 *            ������ ���� / �����찡 �������� ù��° ��
	 * @param className
	 *            ������ Ŭ���� �̸�
	 * @param timeout
	 *            ms - �����찡 ���������� ��ٸ��� �ð� 0�̸� �ѹ��� �õ�
	 * @return text�� �����ϴ� �������� ��
	 */
	public static int setForegroundWindowWithText(String text, String className, long timeout) {
		long start = System.currentTimeMillis();
		List<Integer> l = findWindowWithText(text, className);

		while (timeout > 0 && System.currentTimeMillis() < start + timeout && l.isEmpty()) {
			try {
				Thread.sleep(100); // wait 100 ms
			} catch (InterruptedException e) {
				log.log(Level.WARNING, e.getMessage(), e);
				return 0;
			}
			l = findWindowWithText(text, className);
		}
		if (!l.isEmpty()) {
			OS.SetFocus(l.get(0));
			OS.SetFocus(l.get(0));
			OS.SetActiveWindow(l.get(0));
			OS.SetActiveWindow(l.get(0));
			OS.SetForegroundWindow(l.get(0));
			OS.SetForegroundWindow(l.get(0));
			OS.ShowWindow(l.get(0), 1);
			OS.ShowWindow(l.get(0), 1);
		}

		return l.size();
	}

	public static int setFocusWithText(String text, String className, long timeout) {
		long start = System.currentTimeMillis();
		List<Integer> l = findWindowWithText(text, className);

		while (timeout > 0 && System.currentTimeMillis() < start + timeout && l.isEmpty()) {
			try {
				Thread.sleep(100); // wait 100 ms
			} catch (InterruptedException e) {
				log.log(Level.WARNING, e.getMessage(), e);
				return 0;
			}
			l = findWindowWithText(text, className);
			// System.out.println(l.size());
		}
		if (!l.isEmpty()) {
			OS.SetActiveWindow(l.get(0));
			OS.SetActiveWindow(l.get(0));
			OS.SetFocus(l.get(0));
			OS.SetFocus(l.get(0));
		}

		return l.size();
	}

	public static List<Integer> findWindowWithText(String text, String className) {
		text = text.toLowerCase();
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int h : enumHWnd()) {
			char[] t = new char[100];
			OS.GetWindowTextW(h, t, 100);
			String s = new String(t).trim().toLowerCase();
			if (s.indexOf(text) >= 0) {
				if (className == null)
					l.add(h);
				else {
					char[] t2 = new char[100];
					OS.GetClassNameW(h, t2, 100);
					String s2 = new String(t2).trim().toLowerCase();
					if (s2.indexOf(className.toLowerCase()) >= 0)
						l.add(h);
				}
			}
		}
		return l;
	}

	public static List<String> enumWindowText() {
		ArrayList<String> sl = new ArrayList<String>();
		for (int i : enumHWnd()) {
			char[] t = new char[100];
			OS.GetWindowTextW(i, t, 100);
			String s = new String(t).trim();
			if (s.length() > 0) {
				// System.out.println(s);
				sl.add(s);
			}
		}
		// System.out.println(sl);
		return sl;
	}

	public static List<Integer> enumHWnd() {
		ArrayList<Integer> l = new ArrayList<Integer>();
		Stack<Integer> s = new Stack<Integer>();

		int h = OS.GetDesktopWindow();
		s.push(h);
		while (!s.isEmpty()) {
			h = s.pop();
			l.add(h);
			int t = OS.GetWindow(h, OS.GW_CHILD);
			if (t > 0)
				s.push(t);
			t = OS.GetWindow(h, OS.GW_HWNDNEXT);
			if (t > 0)
				s.push(t);
		}
		return l;
	}
}

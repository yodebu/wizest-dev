package wizest.fx.util.win32;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;

import wizest.fx.logging.LogBroker;

public class WindowUtil {

	// private static final Logger log =
	// Logger.getLogger(WindowUtil.class.getName());
	private static final Logger log = LogBroker.getLogger(WindowUtil.class.getName());

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
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

		// System.out.println(findWindowWithText("KB plustar", "PLUSTAR HTS"));
		// System.out.println(setForegroundWindowWithText("KB plustar",
		// "PLUSTAR HTS", 1000));
		// sleep(500);

		for (int i = 0; i < 1; i++) {
			setForegroundWindowWithText("Lyrics", null, 0);
			WindowInfo wi = enumWindowInfo("����(").get(0);
			
			Macro.mouseLClick(wi.left+160, wi.top+145);

			wi = enumWindowInfo("Lyrics").get(1);

			
			System.out.println(Macro.mouseRClick(wi.left, wi.top));
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			
			System.out.println(Macro.mouseRClick(wi.left, wi.top));
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
			Macro.keyInputEnter();
		
			System.out.println(SimpleClipboard.fromClipboard());
			sleep(500);
			
			wi = enumWindowInfo("����(").get(0);
			Macro.mouseLClick(wi.left + 1, wi.top + 10);
			Macro.keyChar('b');
			sleep(500);
		}

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
			sleep(100);
			l = findWindowWithText(text, className);
		}
		if (!l.isEmpty())
			setForegroundWindow(l.get(0));

		return l.size();
	}

	public static void setForegroundWindow(int hWnd) {
		OS.ShowWindow(hWnd, 1);
		sleep(10);
		OS.SetActiveWindow(hWnd);
		sleep(10);
		OS.SetForegroundWindow(hWnd);
		sleep(10);
		OS.SetFocus(hWnd);
		sleep(10);
	}

	// public static int setFocusWithText(String text, String className,
	// long timeout) {
	// long start = System.currentTimeMillis();
	// List<Integer> l = findWindowWithText(text, className);
	//	
	// while (timeout > 0 && System.currentTimeMillis() < start + timeout
	// && l.isEmpty()) {
	// sleep(100);
	// l = findWindowWithText(text, className);
	// // System.out.println(l.size());
	// }
	// if (!l.isEmpty()) {
	// // OS.SetActiveWindow(l.get(0));
	// OS.SetActiveWindow(l.get(0));
	// sleep(10);
	// // OS.SetFocus(l.get(0));
	// OS.SetFocus(l.get(0));
	// sleep(10);
	// }
	//	
	// return l.size();
	// }

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

	public static List<WindowInfo> enumWindowInfo() {
		ArrayList<WindowInfo> wl = new ArrayList<WindowInfo>();
		for (int i : enumHWnd()) {
			WindowInfo wi = new WindowInfo();
			wi.hWnd = i;

			char[] t = new char[100];
			OS.GetWindowTextW(i, t, 100);
			wi.text = new String(t).trim();

			char[] t2 = new char[100];
			OS.GetClassNameW(i, t2, 100);
			wi.className = new String(t2).trim();

			RECT rect = new RECT();
			OS.GetWindowRect(i, rect);
			wi.top = rect.top;
			wi.bottom = rect.bottom;
			wi.left = rect.left;
			wi.right = rect.right;
			wl.add(wi);
		}
		// System.out.println(sl);
		return wl;
	}

	public static List<WindowInfo> enumWindowInfo(String textFilter) {
		return enumWindowInfo(textFilter, null);
	}

	public static List<WindowInfo> enumWindowInfo(String textFilter, String classNameFilter) {
		List<WindowInfo> l = new ArrayList<WindowInfo>();
		for (WindowInfo i : enumWindowInfo()) {
			if (i.text != null && i.text.toLowerCase().indexOf(textFilter.toLowerCase()) >= 0)
				if (classNameFilter == null)
					l.add(i);
				else if (i.className != null && i.className.toLowerCase().indexOf(classNameFilter.toLowerCase()) >= 0)
					l.add(i);
		}
		return l;
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

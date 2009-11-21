package wizest.fx.util.win32;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.internal.win32.TCHAR;

import wizest.fx.logging.LogBroker;

public class WindowUtil {

	// private static final Logger log =
	// Logger.getLogger(WindowUtil.class.getName());
	private static final Logger log = LogBroker.getLogger(WindowUtil.class
			.getName());

	public static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		// int hWnd = OS.FindWindowW(null, "KB투자증권 plustar\0".toCharArray());//
		// 키보드보안실패
		// int hWnd = OS.FindWindowW(null,
		// "KB증권 PLUSTAR HTS LOGIN\0".toCharArray()); // 로긴
		// int hWnd = OS.FindWindowW(null,"KB plustar\0".toCharArray()); //메인화면

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

		// for (int i = 0; i < 1; i++) {
		// setForegroundWindowWithText("Lyrics", null, 0);
		// WindowInfo wi = enumWindowInfo("벅스(").get(0);
		//
		// Macro.mouseLClick(wi.left + 160, wi.top + 145);
		//
		// wi = enumWindowInfo("Lyrics").get(1);
		//
		// System.out.println(Macro.mouseRClick(wi.left, wi.top));
		// Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
		// Macro.keyInputEnter();
		//
		// System.out.println(Macro.mouseRClick(wi.left, wi.top));
		// Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
		// Macro.keyInputEnter();
		//
		// System.out.println(SimpleClipboard.fromClipboard());
		// sleep(500);
		//
		// wi = enumWindowInfo("벅스(").get(0);
		// System.out.println(wi);
		// Macro.mouseLClick(wi.left + 1, wi.top + 10);
		// Macro.keyChar('b');
		// sleep(500);
		// }

		// long start = System.currentTimeMillis();
		// while(System.currentTimeMillis()-start <5000) {
		// WindowInfo wi = enumWindowInfo("벅스(").get(0);
		// System.out.println(wi.text);
		// sleep(100);
		// }

		// for (WindowInfo i : enumWindowInfoByTextClassName("", "systree"))
		// System.out.println(i);

		for (WindowInfo i : filterWindowInfoByClassName(
				enumWindowInfoByAncestor(findHWnd("벅스", "afx")), "explorer"))
			System.out.println(i);

		int len = OS.SendMessage(590930, 0x000E, 0, 0); // 0x000E -
		// WM_GETTEXTLEN
		TCHAR text = new TCHAR(OS.CP_ACP, len);
		OS.SendMessage(590930, 0x000D, len, text); // 0x000D - WM_GETTEXT
		System.out.println(len);
		System.out.println(text);

		// int hHeap = OS.GetProcessHeap();
		// int pInputs = OS.HeapAlloc(hHeap, OS.HEAP_ZERO_MEMORY, INPUT.sizeof);
		// OS.MoveMemory(pInputs, new int[] { OS.INPUT_KEYBOARD }, 4);
		// OS.MoveMemory(pInputs + 4, inputs, KEYBDINPUT.sizeof);
		// boolean result = OS.SendInput(1, pInputs, INPUT.sizeof) != 0;
		// OS.HeapFree(hHeap, 0, pInputs);
	}

	/**
	 * @param text
	 *            윈도우 제목 / 윈도우가 여러개면 첫번째 것
	 * @param classNameb
	 *            윈도우 클래스 이름
	 * @param timeout
	 *            ms - 윈도우가 있을때까지 기다리는 시간 0이면 한번만 시도
	 * @return foreground 된 hWnd
	 */
	public static int setForegroundWindowByText(String text, String className,
			long timeout) {
		long start = System.currentTimeMillis();
		int hWnd = findHWnd(text, className);

		while (timeout > 0 && System.currentTimeMillis() < start + timeout
				&& hWnd < 0) {
			sleep(100);
			hWnd = findHWnd(text, className);
		}
		if (hWnd >= 0)
			setForegroundWindow(hWnd);
		return hWnd;
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

	// public static List<Integer> findWindow(String text, String className) {
	// text = text.toLowerCase();
	// ArrayList<Integer> l = new ArrayList<Integer>();
	// for (int h : enumHWnd()) {
	// char[] t = new char[100];
	// OS.GetWindowTextW(h, t, 100);
	// String s = new String(t).trim().toLowerCase();
	// if (s.indexOf(text) >= 0) {
	// if (className == null)
	// l.add(h);
	// else {
	// char[] t2 = new char[100];
	// OS.GetClassNameW(h, t2, 100);
	// String s2 = new String(t2).trim().toLowerCase();
	// if (s2.indexOf(className.toLowerCase()) >= 0)
	// l.add(h);
	// }
	// }
	// }
	// return l;
	// }

	/**
	 * @param text
	 * @param className
	 *            null if unknown
	 * @return 첫번째 찾은 것, 검색 실패시 음수
	 */
	public static int findHWnd(String text, String className) {
		text = text.toLowerCase();
		for (int h : enumHWnd()) {
			char[] t = new char[100];
			OS.GetWindowTextW(h, t, 100);
			String s = new String(t).trim().toLowerCase();
			if (s.indexOf(text) >= 0) {
				if (className == null)
					return h;
				else {
					char[] t2 = new char[100];
					OS.GetClassNameW(h, t2, 100);
					String s2 = new String(t2).trim().toLowerCase();
					if (s2.indexOf(className.toLowerCase()) >= 0)
						return h;
				}
			}
		}
		return -1;
	}

	public static List<WindowInfo> enumWindowInfoByAncestor(int hAncestorWnd) {
		Set<WindowInfo> s = new HashSet<WindowInfo>();
		for (WindowInfo i : enumWindowInfo()) {
			int p = i.hWnd;
			while (p > 0)
				if ((p = OS.GetParent(p)) == hAncestorWnd) {
					s.add(i);
					break;
				}
		}
		return new ArrayList<WindowInfo>(s);
	}

	public static List<WindowInfo> enumWindowInfoByParent(int hParentWnd) {
		Set<WindowInfo> s = new HashSet<WindowInfo>();
		for (WindowInfo i : enumWindowInfo()) {
			if (OS.GetParent(i.hWnd) == hParentWnd)
				s.add(i);
		}
		return new ArrayList<WindowInfo>(s);
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

	public static List<WindowInfo> enumWindowInfoByText(String text) {
		return filterWindowInfoByText(enumWindowInfo(), text);
	}

	public static List<WindowInfo> enumWindowInfoByTextClassName(String text,
			String className) {
		return filterWindowInfoByClassName(filterWindowInfoByText(
				enumWindowInfo(), text), className);
	}

	public static List<WindowInfo> filterWindowInfoByText(
			List<WindowInfo> list, String text) {
		List<WindowInfo> l = new ArrayList<WindowInfo>();
		for (WindowInfo i : list) {
			if (i.text != null
					&& i.text.toLowerCase().indexOf(text.toLowerCase()) >= 0)
				l.add(i);
		}
		return l;
	}

	public static List<WindowInfo> filterWindowInfoByClassName(
			List<WindowInfo> list, String classNameFilter) {
		List<WindowInfo> l = new ArrayList<WindowInfo>();
		for (WindowInfo i : list) {
			if (i.className != null
					&& i.className.toLowerCase().indexOf(
							classNameFilter.toLowerCase()) >= 0)
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

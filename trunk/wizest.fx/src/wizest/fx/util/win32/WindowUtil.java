package wizest.fx.util.win32;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;

import wizest.fx.logging.LogBroker;

public class WindowUtil {

	// private static final Logger log =
	// Logger.getLogger(WindowUtil.class.getName());
	private static final Logger log = LogBroker.getLogger(WindowUtil.class
			.getName());

	public static void main(String[] args) throws InterruptedException {
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
		System.out.println(findWindowWithText("KB plustar", "PLUSTAR HTS"));
		System.out.println(setForegroundWindowWithText("KB plustar",
				"PLUSTAR HTS", 1000));
		Thread.sleep(500);

	}

	/**
	 * @param text
	 *            윈도우 제목 / 윈도우가 여러개면 첫번째 것
	 * @param className
	 *            윈도우 클래스 이름
	 * @param timeout
	 *            ms - 윈도우가 있을때까지 기다리는 시간 0이면 한번만 시도
	 * @return text로 시작하는 윈도우의 수
	 */
	public static int setForegroundWindowWithText(String text,
			String className, long timeout) {
		long start = System.currentTimeMillis();
		List<Integer> l = findWindowWithText(text, className);

		while (timeout > 0 && System.currentTimeMillis() < start + timeout
				&& l.isEmpty()) {
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

	public static int setFocusWithText(String text, String className,
			long timeout) {
		long start = System.currentTimeMillis();
		List<Integer> l = findWindowWithText(text, className);

		while (timeout > 0 && System.currentTimeMillis() < start + timeout
				&& l.isEmpty()) {
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

	public static List<WindowInfo> enumWindowInfo(String textFilter,
			String classNameFilter) {
		List<WindowInfo> l = new ArrayList<WindowInfo>();
		for (WindowInfo i : enumWindowInfo()) {
			if (i.text != null
					&& i.text.toLowerCase().indexOf(textFilter.toLowerCase()) >= 0)
				if (classNameFilter == null)
					l.add(i);
				else if (i.className != null
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

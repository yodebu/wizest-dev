package wizest.fx.util.win32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

public class Macro {
	static final int BUTTON_Left = 1;
	static final int BUTTON_Middle = 2;
	static final int BUTTON_Right = 3;

	public static synchronized boolean mouseLClick(int x, int y) {
		return mouseClick(x, y, BUTTON_Left);
	}

	public static synchronized boolean mouseLDbClick(int x, int y) {
		return mouseDbClick(x, y, BUTTON_Left);
	}

	public static synchronized boolean mouseRClick(int x, int y) {
		return mouseClick(x, y, BUTTON_Right);
	}

	public static synchronized boolean mouseRDbClick(int x, int y) {
		return mouseDbClick(x, y, BUTTON_Right);
	}

	public static synchronized boolean mouseClick(int x, int y, int BUTTON) {
		Event e = new Event();
		e.type = SWT.MouseMove;
		e.x = x;
		e.y = y;
		if (Win32SendInput.post(e)) {
			e.type = SWT.MouseDown;
			e.button = BUTTON;
			if (Win32SendInput.post(e)) {
				e.type = SWT.MouseUp;
				e.button = BUTTON;
				return Win32SendInput.post(e);
			}
		}
		return false;
	}

	public static synchronized boolean mouseDbClick(int x, int y, int BUTTON) {
		Event e = new Event();
		e.type = SWT.MouseMove;
		e.x = x;
		e.y = y;
		if (Win32SendInput.post(e)) {
			e.type = SWT.MouseDown;
			e.button = BUTTON;
			if (Win32SendInput.post(e)) {
				e.type = SWT.MouseUp;
				e.button = BUTTON;
				if (Win32SendInput.post(e)) {
					e.type = SWT.MouseDown;
					e.button = BUTTON;
					if (Win32SendInput.post(e)) {
						e.type = SWT.MouseUp;
						e.button = BUTTON;
						return Win32SendInput.post(e);
					}
				}
			}
		}
		return false;
	}

	public static synchronized boolean keyInputString(String s) {
		for (int i = 0, len = s.length(); i < len; ++i) {
			char c = s.charAt(i);
			if (!keyChar(c))
				return false;
		}
		return true;
	}

	public static synchronized boolean keyInput(int KEYCODE) {
		Event e = new Event();
		e.type = SWT.KeyDown;
		e.keyCode = KEYCODE;
		if (Win32SendInput.post(e)) {
			e.type = SWT.KeyUp;
			e.keyCode = KEYCODE;
			return Win32SendInput.post(e);
		}
		return false;
	}

	public static synchronized boolean keyChar(char CHARACTER) {
		Event e = new Event();
		e.type = SWT.KeyDown;
		e.character = CHARACTER;
		if (Win32SendInput.post(e)) {
			e.type = SWT.KeyUp;
			e.character = CHARACTER;
			return Win32SendInput.post(e);
		}
		return false;
	}

	/**
	 * 하나씩 입력
	 * 
	 * @param KEYCODEs
	 * @return
	 */
	public static synchronized boolean keyInputs(int[] KEYCODEs) {
		for (int i = 0, len = KEYCODEs.length; i < len; ++i) {
			if (!keyInput(KEYCODEs[i]))
				return false;

			// try {
			// Thread.currentThread().sleep(500);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		return true;
	}

	/**
	 * 동시에 입력 ex) ALT+TAB
	 * 
	 * @param KEYCODEs
	 * @return
	 */
	public static synchronized boolean keyMultiInput(int[] KEYCODEs) {
		int len = KEYCODEs.length;
		Event e = new Event();
		e.type = SWT.KeyDown;
		for (int i = 0; i < len; ++i) {
			e.keyCode = KEYCODEs[i];
			if (!Win32SendInput.post(e))
				return false;
		}
		e.type = SWT.KeyUp;
		for (int i = 0; i < len; ++i) {
			e.keyCode = KEYCODEs[len - 1 - i];
			if (!Win32SendInput.post(e))
				return false;
		}
		return true;
	}

	public static synchronized boolean keyInputESC() {
		return keyInput(SWT.ESC);
	}

	public static synchronized boolean keyInputTab() {
		return keyInput(SWT.TAB);
	}

	public static synchronized boolean keyInputEnter() {
		return keyInput(SWT.CR);
	}

	public static synchronized boolean keyInputAltF4() {
		return keyMultiInput(new int[] { SWT.ALT, SWT.F4 });
	}

	public static synchronized boolean keyInputWin() {
		return keyMultiInput(new int[] { 0x1FE0 });
	}

	// public void eval() {
	//
	// }
	//
	// public void read(String macro) {
	// }

	public static void main(String[] args) {
		
//		keyInputString("1234abcdABCD");
//		keyInputTab();
//		keyInputEnter();
//		keyInputAltF4();
		keyInputWin();
	}
}

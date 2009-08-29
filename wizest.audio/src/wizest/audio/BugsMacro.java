package wizest.audio;

import org.eclipse.swt.SWT;

import wizest.fx.util.win32.Macro;
import wizest.fx.util.win32.SimpleClipboard;
import wizest.fx.util.win32.WindowInfo;
import wizest.fx.util.win32.WindowUtil;

public class BugsMacro {

	void play() {
		for (int i = 0; i < 1; i++) {
			WindowUtil.setForegroundWindowWithText("Lyrics", null, 0);
			WindowInfo wi = WindowUtil.enumWindowInfo("¹÷½º(").get(0);

			Macro.mouseLClick(wi.left + 160, wi.top + 145);

			wi = WindowUtil.enumWindowInfo("Lyrics").get(1);

			System.out.println(Macro.mouseRClick(wi.left, wi.top));
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			System.out.println(Macro.mouseRClick(wi.left, wi.top));
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			System.out.println(SimpleClipboard.fromClipboard());
			WindowUtil.sleep(500);

			wi = WindowUtil.enumWindowInfo("¹÷½º(").get(0);
			System.out.println(wi);
			Macro.mouseLClick(wi.left + 1, wi.top + 10);
			Macro.keyChar('b');
			WindowUtil.sleep(500);
		}

		// monitor window's text -> catch the timing of going to the next song.
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < 5000) {
			WindowInfo wi = WindowUtil.enumWindowInfo("¹÷½º(").get(0);
			System.out.println(wi.text);
			WindowUtil.sleep(100);
		}
	}
	
	public static void main(String[] args) {
		BugsMacro m = new BugsMacro();
		m.play();
	}

}

package wizest.audio;

import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;

import wizest.fx.util.Shell;
import wizest.fx.util.win32.Macro;
import wizest.fx.util.win32.SimpleClipboard;
import wizest.fx.util.win32.WindowInfo;
import wizest.fx.util.win32.WindowUtil;

/**
 * ������ �������� �����ؾ� ���� �۵��Ѵ�.
 * 
 * @author wizest
 */
public class BugsMacro {
	private Logger log;

	public BugsMacro() {
		this.log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	void play() {
		for (int i = 0; i < 1; i++) {
			WindowUtil.setForegroundWindowWithText("Lyrics", null, 0);
			WindowInfo wi = WindowUtil.enumWindowInfo("����(").get(0);

			Macro.mouseLClick(wi.left + 160, wi.top + 145);

			wi = WindowUtil.enumWindowInfo("Lyrics").get(1);

			Macro.mouseRClick(wi.left, wi.top);
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			Macro.mouseRClick(wi.left, wi.top);
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			System.out.println(SimpleClipboard.fromClipboard());
			WindowUtil.sleep(500);

			wi = WindowUtil.enumWindowInfo("����(").get(0);
			System.out.println(wi);
			Macro.mouseLClick(wi.left + 1, wi.top + 10);
			Macro.keyChar('b');
			WindowUtil.sleep(500);
		}

	}

	public String getLyrics() {
		WindowUtil.setForegroundWindowWithText("����(", null, 0);
		WindowInfo wi = WindowUtil.enumWindowInfo("����(").get(0);

		Macro.mouseLClick(wi.left + 160, wi.top + 145);

		wi = WindowUtil.enumWindowInfo("Lyrics").get(1);
		Macro.mouseRClick(wi.left, wi.top);
		Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
		Macro.keyInputEnter();

		Macro.mouseRClick(wi.left, wi.top);
		Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
		Macro.keyInputEnter();

		WindowUtil.sleep(500);
		return SimpleClipboard.fromClipboard();
	}

	/**
	 * ���� ������ ����� ������ ��ٸ� (������ ���� ������ ����͸�)
	 * 
	 * @param timeoutMs
	 * @return title for filename
	 */
	public String waitForNewTitle(long timeoutMs) throws TimeoutException {
		WindowInfo wi = WindowUtil.enumWindowInfo("����(").get(0);
		String title = wi.text;
		WAIT: {
			try {
				// monitor window's text -> catch the timing of going to the
				// next
				// song.
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < timeoutMs) {
					wi = WindowUtil.enumWindowInfo("����(").get(0);
					if (!title.equals(wi.text)) {
						title = wi.text;
						break WAIT;
					}
					WindowUtil.sleep(100);
				}
				throw new TimeoutException("Overtime than " + timeoutMs + " ms");

			} catch (IndexOutOfBoundsException e) {
				log.warning(e.getMessage());
			}
		}

		return Shell.filterFilename(title.substring(0, title.lastIndexOf("- ����(")).trim());
	}

	public static void main(String[] args) throws Exception {
		BugsMacro m = new BugsMacro();
		// m.play();
		System.out.println(m.getLyrics());
		System.out.println(m.waitForNewTitle(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNewTitle(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNewTitle(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNewTitle(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNewTitle(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNewTitle(5 * 60000)); // 5 min

	}
}

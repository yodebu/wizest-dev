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
 * 관리자 권한으로 실행해야 정상 작동한다.
 * 
 * @author wizest
 */
public class BugsMacro {
	private Logger log;

	public BugsMacro() {
		this.log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	void _play() {
		for (int i = 0; i < 1; i++) {
			WindowUtil.setForegroundWindow(WindowUtil.findHWnd("Lyrics", null));
			WindowInfo wi = WindowUtil.enumWindowInfoByText("벅스(").get(0);

			Macro.mouseLClick(wi.left + 160, wi.top + 145);

			wi = WindowUtil.enumWindowInfoByText("Lyrics").get(1);

			Macro.mouseRClick(wi.left, wi.top);
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			Macro.mouseRClick(wi.left, wi.top);
			Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
			Macro.keyInputEnter();

			System.out.println(SimpleClipboard.fromClipboard());
			WindowUtil.sleep(500);

			wi = WindowUtil.enumWindowInfoByText("벅스(").get(0);
			System.out.println(wi);
			Macro.mouseLClick(wi.left + 1, wi.top + 10);
			Macro.keyChar('b');
			WindowUtil.sleep(500);
		}

	}

	public boolean existsBugsPlayer() {
		return WindowUtil.findHWnd("벅스(", "Afx:00400000:8") >= 0;
	}

	public String getLyrics() {
		WindowUtil.setForegroundWindowByText("벅스(", null, 0);
		WindowInfo wi = WindowUtil.enumWindowInfoByText("벅스(").get(0);

		Macro.mouseLClick(wi.left + 160, wi.top + 145);

		wi = WindowUtil.enumWindowInfoByText("Lyrics").get(1);
		Macro.mouseRClick(wi.left, wi.top);
		Macro.keyInputs(new int[] { SWT.ARROW_DOWN, SWT.ARROW_DOWN });
		Macro.keyInputEnter();

		Macro.mouseRClick(wi.left, wi.top);
		Macro.keyInputs(new int[] { SWT.ARROW_DOWN });
		Macro.keyInputEnter();

		WindowUtil.sleep(500);
		return SimpleClipboard.fromClipboard();
	}
	
	

	// public String convertLyricsTextToLRC(String lyrics) {
	// return lyrics;
	// }

	/**
	 * 다음 곡으로 변경될 때까지 기다림 (윈도우 제목 변경을 모니터링)
	 * 
	 * @param timeoutMs
	 * @return title for filename
	 */
	public String waitForNextSong(long timeoutMs) throws TimeoutException {
		WindowInfo wi = WindowUtil.enumWindowInfoByText("벅스(").get(0);
		String title = wi.text;
		WAIT: {
			try {
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < timeoutMs) {
					wi = WindowUtil.enumWindowInfoByText("벅스(").get(0);
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

		return Shell.filterFilename(title.substring(0, title.lastIndexOf("- 벅스(")).trim());
	}

	public String getTitle() {
		WindowInfo wi = WindowUtil.enumWindowInfoByText("벅스(").get(0);
		String title = wi.text;
		return Shell.filterFilename(title.substring(0, title.lastIndexOf("- 벅스(")).trim());
	}

	public static void main(String[] args) throws Exception {
		BugsMacro m = new BugsMacro();

		System.out.println(m.existsBugsPlayer());

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

		System.out.println(m.getLyrics());
		System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

	}
}

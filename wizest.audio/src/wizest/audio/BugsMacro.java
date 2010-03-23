package wizest.audio;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;

import wizest.fx.util.ScreenCapture;
import wizest.fx.util.Shell;
import wizest.fx.util.SimpleClipboard;
import wizest.fx.util.win32.Macro;
import wizest.fx.util.win32.WindowInfo;
import wizest.fx.util.win32.WindowUtil;

/**
 * °ü¸®ÀÚ ±ÇÇÑÀ¸·Î ½ÇÇàÇØ¾ß Á¤»ó ÀÛµ¿ÇÑ´Ù.
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
			WindowUtil.setForegroundWindow(WindowUtil.findHWnd("Lyrics", "afx"));
			WindowInfo wi = WindowUtil.enumWindowInfoByTextClassName("¹÷½º(", "afx").get(0);

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

			wi = WindowUtil.enumWindowInfoByTextClassName("¹÷½º(", "afx").get(0);
			System.out.println(wi);
			Macro.mouseLClick(wi.left + 1, wi.top + 10);
			Macro.keyChar('b');
			WindowUtil.sleep(500);
		}

	}

	public boolean existsBugsPlayer() {
		return WindowUtil.findHWnd("¹÷½º(", "Afx:00400000:8") >= 0;
	}

	public String getLyrics() {
		WindowUtil.setForegroundWindowByText("¹÷½º(", "afx", 0);
		WindowInfo wi = WindowUtil.enumWindowInfoByTextClassName("¹÷½º(", "afx").get(0);

		// System.out.println(wi);
		Macro.mouseLClick(wi.left + 160, wi.top + 145);

		wi = WindowUtil.enumWindowInfoByTextClassName("Lyrics", "afx").get(1);
		// System.out.println(wi);
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
	 * ´ÙÀ½ °îÀ¸·Î º¯°æµÉ ¶§±îÁö ±â´Ù¸² (À©µµ¿ì Á¦¸ñ º¯°æÀ» ¸ð´ÏÅÍ¸µ)
	 * 
	 * @param timeoutMs
	 * @return title for filename
	 */
	public String waitForNextSong(long timeoutMs) throws TimeoutException {
		WindowInfo wi = WindowUtil.enumWindowInfoByTextClassName("¹÷½º(", "afx").get(0);
		String title = wi.text;
		WAIT: {
			try {
				long start = System.currentTimeMillis();
				while (System.currentTimeMillis() - start < timeoutMs) {
					wi = WindowUtil.enumWindowInfoByTextClassName("¹÷½º(", "afx").get(0);
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

		return Shell.filterFilename(title.substring(0, title.lastIndexOf("- ¹÷½º(")).trim());
	}

	public void captureAlbumArtImage(File albumArt) {
		WindowUtil.setForegroundWindowByText("¹÷½º(", "afx", 0);
		WindowInfo wi = WindowUtil.enumWindowInfoByTextClassName("BassTreeCtrl", "afx").get(0);

		// System.out.println(wi);
		Macro.mouseLClick(wi.left + 20, wi.top + 10);
		WindowUtil.sleep(1000);

		WindowUtil.setForegroundWindowByText("¹÷½º(", "afx", 0);
		WindowUtil.sleep(100);
		wi = WindowUtil.filterWindowInfoByClassName(WindowUtil.enumWindowInfoByAncestor(WindowUtil.findHWnd("¹÷½º(", "afx")), "Internet Explorer_Server").get(0);
		for (WindowInfo i : WindowUtil.filterWindowInfoByClassName(WindowUtil.enumWindowInfoByAncestor(WindowUtil.findHWnd("¹÷½º(", "afx")), "Internet Explorer_Server"))
			if (wi.top > i.top)
				wi = i;

		// System.out.println(WindowUtil.filterWindowInfoByClassName(WindowUtil.enumWindowInfoByAncestor(WindowUtil.findHWnd("¹÷½º(",
		// "afx")), "Internet Explorer_Server"));
		// System.out.println(wi);

		try {
			ScreenCapture.captureAsPng(wi.left + 22, wi.top + 22, 140, 140, albumArt);
		} catch (IOException e) {
			log.warning(e.getMessage());
		}
	}

	public String getTitle() {
		WindowInfo wi = WindowUtil.enumWindowInfoByTextClassName("¹÷½º(", "afx").get(0);
		String title = wi.text;
		return Shell.filterFilename(title.substring(0, title.lastIndexOf("- ¹÷½º(")).trim());
	}

	public static void main(String[] args) throws Exception {
		BugsMacro m = new BugsMacro();

		System.out.println(m.existsBugsPlayer());

		// System.out.println(m.getLyrics());
		m.captureAlbumArtImage(new File("capture.png"));

		// System.out.println(m.waitForNextSong(5 * 60000)); // 5 min
		//
		// System.out.println(m.getLyrics());
		// System.out.println(m.waitForNextSong(5 * 60000)); // 5 min
		//
		// System.out.println(m.getLyrics());
		// System.out.println(m.waitForNextSong(5 * 60000)); // 5 min
		//
		// System.out.println(m.getLyrics());
		// System.out.println(m.waitForNextSong(5 * 60000)); // 5 min
		//
		// System.out.println(m.getLyrics());
		// System.out.println(m.waitForNextSong(5 * 60000)); // 5 min
		//
		// System.out.println(m.getLyrics());
		// System.out.println(m.waitForNextSong(5 * 60000)); // 5 min

	}

}

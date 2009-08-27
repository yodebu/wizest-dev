package wizest.fx.util.win32;


public class WindowInfo {
	public int hWnd;
	public String text;
	public String className;
	public int top;
	public int left;
	public int right;
	public int bottom;

	public String toString() {
		return "{hWnd=" + hWnd + ", className=" + className + ", text=" + text
				+ ", top=" + top + ", bottom=" + bottom + ", left=" + left
				+ " ,right=" + right + "}";
	}
}

package wizest.fx.util.win32;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class KeyCapture {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT KeyEvent Example");

		shell.setLayout(new FillLayout());

		Button button = new Button(shell, SWT.CENTER);

		button.setText("Type Something");

		button.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {
				System.out.print("Released:");
				System.out.println(e);

			}

			public void keyPressed(KeyEvent e) {
				String string = "";

				// check click together?
				if ((e.stateMask & SWT.ALT) != 0)
					string += "ALT - keyCode = " + e.keyCode;
				if ((e.stateMask & SWT.CTRL) != 0)
					string += "CTRL - keyCode = " + e.keyCode;
				if ((e.stateMask & SWT.SHIFT) != 0)
					string += "SHIFT - keyCode = " + e.keyCode;

				if (e.keyCode == SWT.BS) {
					string += "BACKSPACE - keyCode = " + e.keyCode;
				}

				if (e.keyCode == SWT.ESC) {
					string += "ESCAPE - keyCode = " + e.keyCode;
				}

				// check characters
				if (e.keyCode >= 97 && e.keyCode <= 122) {
					string += " " + e.character + " - keyCode = " + e.keyCode;
				}

				// check digit
				if (e.keyCode >= 48 && e.keyCode <= 57) {
					string += " " + e.character + " - keyCode = " + e.keyCode;
				}

				if (!string.equals(""))
					System.out.println(string);
				// else
				
				System.out.print("Pressed :");
				System.out.println(e.toString());
			}
		});

		shell.setSize(new Point(200, 100));
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
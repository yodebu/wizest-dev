package wizest.fx.util;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ScreenCapture {

	public static void captureAsPng(int x, int y, int width, int height, File fileToSave) throws IOException {
		BufferedImage screencapture;
		try {
			screencapture = new Robot().createScreenCapture(new Rectangle(x, y, width, height));
		} catch (Exception e) {
			throw new IOException(e);
		}

		// Save as JPEG
		ImageIO.write(screencapture, "png", fileToSave);
	}

	public static void captureAsPng(File fileToSave) throws IOException {
		Rectangle r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		captureAsPng(r.x, r.y, r.width, r.height, fileToSave);
	}

	public static void main(String args[]) throws Exception {

		// Save as JPEG
		File file = new File("screencapture.png");
		captureAsPng(file);
		System.out.println(file.getCanonicalFile());

		file = new File("screencapture2.png");
		captureAsPng(100, 100, 100, 100, file);
		System.out.println(file.getCanonicalFile());

	}
}
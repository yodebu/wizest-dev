package wizest.audio;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ScreenCapture {
	public static void main(String args[]) throws Exception {
		// capture the whole screen
		BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

		// Save as JPEG
		File file = new File("screencapture.png");
		ImageIO.write(screencapture, "png", file);

		// Save as PNG
		// File file = new File("screencapture.png");
		// ImageIO.write(screencapture, "png", file);
	}
}
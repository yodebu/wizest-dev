package wizest.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class Capture {

	private boolean running;
	private boolean stopping; // true로 하면 녹음이 정지된다.
	private Logger log;

	public Capture() {
		this.running = false;
		this.stopping = false;
		this.log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	public static AudioFormat getFormat() {
		float sampleRate = 48000;
		int sampleSizeInBits = 16;
		int channels = 2;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public synchronized void start(final OutputStream os) throws LineUnavailableException {
		stop(); // 기존 작업은 종료

		log.info("capture started.");

		final AudioFormat format = getFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		Runnable runner = new Runnable() {
			public void run() {
				running = true;
				try {
					int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
					byte buffer[] = new byte[bufferSize];

					while (!stopping) {
						int count = line.read(buffer, 0, buffer.length);
						if (count > 0) {
							os.write(buffer, 0, count);
						}
					}
					os.flush();
				} catch (IOException e) {
					log.severe(e.getMessage());
				} finally {
					if (line != null && line.isOpen()) {
						line.stop();
						line.close();
					}
				}
				stopping = false;
				running = false;
			}
		};

		Thread captureThread = new Thread(runner);
		captureThread.setDaemon(true);
		captureThread.start();
	}

	public synchronized void stop() {
		try {
			if (running) {
				stopping = true;
				while (stopping)
					Thread.sleep(1);
				log.info("capture stopped.");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void play(final InputStream is, long length) throws LineUnavailableException {
		final AudioFormat format = getFormat();
		final AudioInputStream ais = new AudioInputStream(is, format, length / format.getFrameSize());
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();

		Runnable runner = new Runnable() {
			int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
			byte buffer[] = new byte[bufferSize];

			public void run() {
				log.info("audio playing.");

				try {
					int count;
					while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
						if (count > 0) {
							line.write(buffer, 0, count);
						}
					}
					line.drain();
					line.close();
				} catch (IOException e) {
					log.severe(e.getMessage());
				} finally {
					if (line != null && line.isOpen()) {
						line.stop();
						line.close();
					}
				}

				log.info("audio played.");
			}
		};

		Thread playThread = new Thread(runner);
		playThread.setDaemon(true);
		playThread.start();
	}

	public static void main(String[] args) throws Exception {

		Capture ac = new Capture();

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		ac.start(os);
		Thread.sleep(5000); // 5sec
		ac.stop();
		Thread.sleep(100);

		byte[] audio = os.toByteArray();
		ac.play(new ByteArrayInputStream(audio), audio.length);
		Thread.sleep(5000); // 5sec
		Thread.sleep(100);
	}
}

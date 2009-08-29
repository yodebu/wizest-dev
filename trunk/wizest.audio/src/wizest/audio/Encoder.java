package wizest.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.tritonus.share.sampled.AudioFileTypes;
import org.tritonus.share.sampled.Encodings;

import javazoom.spi.mpeg.sampled.file.MpegAudioFileFormat;
import javazoom.spi.mpeg.sampled.file.MpegFileFormatType;

public class Encoder {
	private Logger log;

	public Encoder() {
		this.log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	public void encodeWAV(InputStream is, OutputStream os, long length) throws UnsupportedAudioFileException, IOException {
		final AudioFormat format = Capture.getFormat();
		final AudioInputStream ais = new AudioInputStream(is, format, length / format.getFrameSize());

		ais.reset(); // rewind

		if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, ais)) {
			log.info("encoding");
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, os);
			os.flush();
			ais.close();
			log.info("encoded");
		} else
			throw new UnsupportedAudioFileException("file type not supported: WAV");
	}

	public void encodeMP3(InputStream is, OutputStream os, long length) throws UnsupportedAudioFileException, IOException {
		final AudioFormat format = Capture.getFormat();
		final AudioInputStream ais = new AudioInputStream(is, format, length / format.getFrameSize());

		ais.reset(); // rewind

		
		AudioFormat.Encoding f = Encodings.getEncoding("MPEG1L3");
		AudioFileFormat.Type type = AudioFileTypes.getType("MP3");
		if (AudioSystem.isFileTypeSupported(type, ais)) {
			log.info("encoding");
			AudioSystem.write(ais, type, os);
			os.flush();
			ais.close();
			log.info("encoded");
		} else
			throw new UnsupportedAudioFileException("file type not supported: MP3");
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

		Encoder en = new Encoder();
		OutputStream fos = new FileOutputStream("C:/temp/audio.wav");
		en.encodeWAV(new ByteArrayInputStream(audio), fos, audio.length);

		fos = new FileOutputStream("C:/temp/audio.mp3");
		en.encodeMP3(new ByteArrayInputStream(audio), fos, audio.length);

	}
}

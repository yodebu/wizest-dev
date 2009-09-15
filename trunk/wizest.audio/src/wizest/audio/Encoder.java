package wizest.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

import org.farng.mp3.TagException;

import wizest.fx.util.StackTrace;

public class Encoder {
	private Logger log;

	public Encoder() {
		this.log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

	public void encodeWAV(InputStream is, OutputStream os, long length) throws UnsupportedAudioFileException, IOException {
		final AudioFormat format = Capture.getFormat();
		final AudioInputStream ais = new AudioInputStream(is, format, length / format.getFrameSize());

		// ais.reset(); // rewind

		if (AudioSystem.isFileTypeSupported(AudioFileFormat.Type.WAVE, ais)) {
			log.info("wav encoding");
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, os);
			os.flush();
			ais.close();
			log.info("wav encoded");
		} else
			throw new UnsupportedAudioFileException("file type not supported: WAV");
	}

	public void encodeMP3(File wavFile, File mp3File, String title, String artist, String album, File albumArtImage) throws IOException {
		try {
			log.info("mp3 encoding");

			// search lame binary
			String lame = "./lame/lame";
			for (String s : new File(".").list()) {
				if (s.startsWith("lame") && new File(s).isDirectory())
					lame = "./" + s + "/lame";
			}

			lame = new File(lame).getCanonicalFile().toString();
			String cmd = lame + " -h -b 192 \"" + wavFile.getCanonicalFile() + "\" \"" + mp3File.getCanonicalFile() + "\"";
			if (title != null && title.length() > 0)
				cmd += " --tt \"" + title + "\"";
			if (artist != null && artist.length() > 0)
				cmd += " --ta \"" + artist + "\"";
			if (album != null && album.length() > 0)
				cmd += " --tl \"" + album + "\"";
			if (albumArtImage != null && albumArtImage.exists())
				cmd += " --ti \"" + albumArtImage.getCanonicalPath() + "\"";

			Runtime rt = Runtime.getRuntime();
			log.info(cmd);
			rt.exec(cmd);
//			Process p = rt.exec(cmd);
			// int exitValue = p.waitFor();
			// log.info("Lame exits with exitValue=" + exitValue);
			log.info("mp3 encoded");
		} catch (IOException e) {
			throw e;
		} catch (Throwable t) {
			log.severe(StackTrace.trace(t));
			throw new IOException(t);
		}
	}

	public void addLyrics(File mp3File, String lyrics) throws IOException, TagException {
		// MP3File f = new MP3File(mp3File);
		// // f.getID3v1Tag().setSongLyric(lyrics);
		// // f.getID3v2Tag().setSongLyric(lyrics);
		//
		// AbstractID3v2 tag = f.getID3v2Tag();
		// AbstractID3v2Frame field = tag.getFrame("USLT");
		//
		// if (field == null) {
		// field = new ID3v2_3Frame(new FrameBodyUSLT((byte) 1, "KOR", "",
		// lyrics.trim()));
		// tag.setFrame(field);
		// } else {
		// ((FrameBodyUSLT) field.getBody()).setLyric(lyrics.trim());
		// }
		//
		// f.save();
		//
		// f = new MP3File(mp3File);
		// System.out.println(f.getID3v2Tag().getSongLyric());
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
		en.encodeMP3(new File("C:/temp/audio.wav"), new File("C:/temp/audio.mp3"), null, null, null, null);
	}
}

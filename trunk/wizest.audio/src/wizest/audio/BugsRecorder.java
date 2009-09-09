package wizest.audio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import javax.sound.sampled.LineUnavailableException;

import org.farng.mp3.TagException;

import wizest.fx.pool.ThreadPool;
import wizest.fx.template.ExceptionTracer;
import wizest.fx.util.StackTrace;

public class BugsRecorder {
	private long timeout;
	private File root;
	private BugsMacro macro;
	private Logger log;
	private String prefix;
	private ThreadPool threadPool;

	public BugsRecorder(long timeout, File root, String prefix) {
		this.timeout = timeout;
		this.root = root;
		this.prefix = prefix;
		this.macro = new BugsMacro();
		this.log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		this.threadPool = new ThreadPool("Encoder", 5, Thread.NORM_PRIORITY, true);
	}

	public static void main(String[] args) throws Exception {
		long timeout = 1000 * 60 * 10; // 10 min
		String workDir = "./out";
		String prefix = "";

		System.out.println("Bugs Recorder v1.0");
		System.out.println("by Sanghoon Kim (http://blog.naver.com/wizest)");
		System.out.println("");

		// help
		if (args.length == 0)
			printHelp();
		else
			for (String s : args)
				if (s.startsWith("-h"))
					printHelp();
				else if (s.startsWith("-t"))
					timeout = Long.parseLong(s.substring(2));
				else if (s.startsWith("-o"))
					workDir = s.substring(2);
				else if (s.startsWith("-p"))
					prefix = s.substring(2);

		File root = new File(workDir).getCanonicalFile();
		System.out.println("Work directory: " + root);
		if (!root.exists())
			throw new IOException("Work directory does not exist.");
		if (!root.isDirectory())
			throw new IOException("Work directory is not a directory.");

		System.out.println("Timeout per a song: " + timeout / 1000. / 60. + " min");

		BugsRecorder bugs = new BugsRecorder(timeout, root, prefix);
		bugs.recordSongs();

		// BugsMacro bugM = new BugsMacro();
		// bugs
	}

	private static void printHelp() {

		System.out.println("Usage: java -jar BugsRecorder.jar [-option] [-option] ...");
		System.out.println("    -t<timeout>            set timeout for one song in ms");
		System.out.println("    -o<work directory>     set work directory to create mp3 files");
		System.out.println("    -p<filename prefix>    set filename prefix");
		System.out.println("    -h                     print this help message");
		System.out.println("");
		System.out.println("Example: java -jar BugsRecorder.jar -t600000 -oc:/temp/bugs");
	}

	private void recordSongs() throws LineUnavailableException, IOException {
		if (!macro.existsBugsPlayer())
			throw new IOException("does not exist a running bugsplayer.");
		try {
			for (;;)
				recordASong();
		} catch (TimeoutException e) {
		}
	}

	private void recordASong() throws LineUnavailableException, TimeoutException, IOException {
		Capture ac = new Capture();

		final File fTmp = new File(root, System.currentTimeMillis() + ".tmp");
		log.info(fTmp + " created");

		FileOutputStream os = new FileOutputStream(fTmp);
		// final ByteArrayOutputStream os = new ByteArrayOutputStream();
		ac.start(os);
		final String title = macro.getTitle();
		final String lyrics = macro.getLyrics();

		final File fAlbumArt = new File(root, System.currentTimeMillis() + ".png");
		macro.captureAlbumArtImage(fAlbumArt);

		macro.waitForNextSong(timeout);
		ac.stop();
		os.flush();
		os.close();

		Runnable r = new Runnable() {
			public void run() {
				try {
					final Encoder en = new Encoder();

					// WAV
					final File fWav = new File(root, prefix + title + ".wav").getCanonicalFile();
					if (fWav.exists()) {
						fWav.delete();
						log.info(fWav + " deleted");
					}
					fWav.createNewFile();
					log.info(fWav + " created");
					OutputStream fos = new FileOutputStream(fWav);

					FileInputStream fin = new FileInputStream(fTmp);
					en.encodeWAV(fin, fos, fTmp.length());
					fos.flush();
					fos.close();

					fTmp.delete();

					// MP3
					final File fMp3 = new File(root, prefix + title + ".mp3").getCanonicalFile();
					if (fMp3.exists()) {
						fMp3.delete();
						log.info(fMp3 + " deleted");
					}
					String titleTag = title.substring(0, title.lastIndexOf(" - ")).trim();
					String artistTag = title.substring(title.lastIndexOf(" - ") + 3).trim();
					en.encodeMP3(fWav, fMp3, titleTag, artistTag, null, fAlbumArt);

					Runnable delR = new Runnable() {
						public void run() {
							// delete WAV after 5 min
							try {
								Thread.sleep(1000 * 60 * 2);// 2 min
//								Thread.sleep(1000 * 10);// 10 sec
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							try {
								en.addLyrics(fMp3, lyrics);
							} catch (Exception e) {
								log.warning(StackTrace.trace(e));
							}
							fWav.delete();
							log.info(fWav + " deleted");
							if (fAlbumArt != null && fAlbumArt.exists())
								fAlbumArt.delete();
							log.info(fAlbumArt + " deleted");
						}
					};

					threadPool.execute(delR);

				} catch (Exception e) {
					log.severe(StackTrace.trace(e));
				}
			}
		};

		threadPool.execute(r);
	}
}

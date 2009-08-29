package wizest.audio;

import java.io.File;
import java.io.IOException;

import wizest.fx.util.Shell;

public class BugsRecorder {
	private long timeout;
	private String workDir;

	public BugsRecorder(long timeout, String workDir) {
		this.timeout = timeout;
		this.workDir = workDir;
	}

	public static void main(String[] args) throws Exception {
		long timeout = 1000 * 60 * 10; // 10 min
		String workDir = "./out";

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
					Long.parseLong(s.substring(2));
				else if (s.startsWith("-o"))
					workDir = s.substring(2);

		BugsRecorder bugs = new BugsRecorder(timeout, workDir);
		bugs.record();

		// BugsMacro bugM = new BugsMacro();
		// bugs
	}

	private static void printHelp() {

		System.out.println("Usage: java -jar BugsRecorder.jar [-option] [-option] ...");
		System.out.println("    -t<timeout>            set timeout for one song in ms");
		System.out.println("    -o<work directory>     set work directory to create mp3 files");
		System.out.println("    -h                     print this help message");
		System.out.println("");
		System.out.println("Example: java -jar BugsRecorder.jar -t600000 -oc:/temp/bugs");
	}

	private void record() throws IOException {
		File root = new File(workDir).getCanonicalFile();
		System.out.println("Work directory: " + root);
		if (!root.exists())
			throw new IOException("Work directory does not exist.");
		if (!root.isDirectory())
			throw new IOException("Work directory is not a directory.");

		System.out.println("Timeout per a song: " + timeout / 1000. / 60. + " min");

	}

}

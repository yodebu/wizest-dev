package wizest.fx.schedule;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import wizest.fx.logging.LogBroker;
import wizest.fx.util.SerializedString;
import wizest.fx.util.SerializedStringException;
import wizest.fx.util.StackTrace;


public class Scheduler extends Thread {
	private String name = null;

	private Hashtable jobs = null;
	private Logger logger = null;

	public Scheduler() {
		this("Anonymous scheduler(" + System.currentTimeMillis() + ")"); // 임의로
		// 이름
		// 만들어
		// 준다.
	}

	public Scheduler(String name) {
		this(name, true);
	}

	public Scheduler(String name, boolean deamon) {
		this.name = name;
		jobs = new Hashtable();
		logger = LogBroker.getLogger(this);
		this.setDaemon(deamon);
	}

	/**
	 * 주의!!!: job의 name이 같을 경우 기존의 job이 교체된다.
	 * 
	 * @param job
	 */
	public synchronized void addJob(Job job) {
		this.jobs.put(job.getName(), job);
		logger.log(Level.INFO, "job added.:" + job);
	}

	public void removeJob(Job job) {
		removeJob(job.getName());
	}

	public synchronized void removeJob(String jobName) {
		Job job = getJob(jobName);
		logger.log(Level.INFO, "job removed.:" + job);
		this.jobs.remove(jobName);
	}

	public Job getJob(String jobName) {
		return (Job) this.jobs.get(jobName);
	}

	public Job[] getJobs() {
		return (Job[]) this.jobs.values().toArray(new Job[0]);
	}

	public String[] getJobNames() {
		return (String[]) this.jobs.keySet().toArray(new String[0]);
	}

	public String toString() {
		String[] jobNames = getJobNames();

		StringBuffer buff = new StringBuffer();

		buff.append("{name=" + this.name + ",jobs={");

		for (int i = 0; i < jobNames.length; ++i) {
			buff.append(jobNames[i]);
			if (i + 1 != jobNames.length) {
				buff.append(",");
			}
		}
		buff.append("}}");

		return buff.toString();
	}

	public void run() {
		logger.log(Level.INFO, "Scheduler started. :" + this.toString());

		int minute = 0;
		Calendar cal = null;

		try {
			while (!isInterrupted()) {
				cal = Calendar.getInstance();

				// 1분 마다 한번씩 jobs을 검색해서 실행한다.
				if (minute != cal.get(Calendar.MINUTE)) {
					minute = cal.get(Calendar.MINUTE);

					synchronized (this) {
						Enumeration je = jobs.elements();
						while (je.hasMoreElements()) {
							Job j = (Job) je.nextElement();
							AtTime at = j.getAtTime();
							Runnable r = j.getScheduledRunnable();

							if (at.match()) {
								try {
									RunnableThreadBox tb = new RunnableThreadBox(r);
									tb.setDaemon(true);
									tb.setPriority(j.getPriority());
									tb.start();
								} catch (Throwable t) {
									logger.log(Level.SEVERE, j.toString() + ":" + StackTrace.trace(t));
								}
							}

							if (at.wasExpired()) {
								removeJob(j);
							}
						}
					}
				}

				sleep(1000 * 10); // 10 sec
			}
		} catch (Exception e) {
		}

		logger.log(Level.INFO, "Scheduler stopped. :" + this.toString());
	}

	public void start() {
		super.start();
	}

	public void interrupt() {
		super.interrupt();
	}

	/**
	 * serialized 된 job 문자열을 job object로 가져온다.
	 * 
	 * @param serializedJobs
	 * @return
	 * @throws SerializedStringException
	 */
	public Job[] deserializeJobs(String serializedJobs) throws SerializedStringException {
		Hashtable j = (Hashtable) SerializedString.deserialize(serializedJobs);
		Job[] js = (Job[]) j.values().toArray(new Job[0]);

		logger.log(Level.INFO, "jobs deserialized.");
		return js;
	}

	/**
	 * scheduler가 가지고 있는 jobs 을 String으로 serialization 한다.
	 * 
	 * @return
	 * @throws SerializedStringException
	 */
	public String serializeJobs() throws SerializedStringException {
		String s = SerializedString.serialize(jobs);
		logger.log(Level.INFO, "jobs serialized.");
		return s;
	}

	/**
	 * serialized된 job 문자열을 object로 복원하여 scheduler에 추가한다.
	 * 
	 * @param serializedJobs
	 * @throws SerializedStringException
	 */
	public void loadSerializedJobs(String serializedJobs) throws SerializedStringException {
		Job[] js = deserializeJobs(serializedJobs);
		for (int i = 0; i < js.length; ++i) {
			addJob(js[i]);
		}
	}

	/*
	 * public static void main(String[] args) throws Exception { Scheduler s =
	 * new Scheduler(); s.setDaemon(false); s.start(); // s.addJob(new
	 * Job("test", // new SerializableRunnable() // { // public void run() // { //
	 * System.out.println( "hi~" ); // } // }, // new AtTime("* * * * * 2") //
	 * )); // s.addJob(new Job("test3", new SerializableRunnable() { public void
	 * run() { System.out.println( "hi~3" ); } }, new AtTime("* * * * * *") ));
	 * System.out.println(s); }
	 */
}
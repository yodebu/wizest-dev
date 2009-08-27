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
		this("Anonymous scheduler(" + System.currentTimeMillis() + ")"); // ���Ƿ�
		// �̸�
		// �����
		// �ش�.
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
	 * ����!!!: job�� name�� ���� ��� ������ job�� ��ü�ȴ�.
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

				// 1�� ���� �ѹ��� jobs�� �˻��ؼ� �����Ѵ�.
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
	 * serialized �� job ���ڿ��� job object�� �����´�.
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
	 * scheduler�� ������ �ִ� jobs �� String���� serialization �Ѵ�.
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
	 * serialized�� job ���ڿ��� object�� �����Ͽ� scheduler�� �߰��Ѵ�.
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
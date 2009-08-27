package wizest.fx.pool;

import java.util.LinkedList;
import java.util.logging.Logger;

import wizest.fx.logging.LogBroker;
import wizest.fx.util.StackTrace;

public class ThreadPool {
	private String poolName;
	private LinkedList waitingLine;
	private RunningSuite[] suites;
	int threadPriority;
	boolean daemonThread;
	private int threadUid;

	public ThreadPool(String poolName, int numberOfThreads, int threadPriority, boolean daemonThread) {
		this.poolName = poolName;
		numberOfThreads = Math.max(1, numberOfThreads);
		this.waitingLine = new LinkedList();
		this.threadPriority = threadPriority;
		this.daemonThread = daemonThread;
		this.threadUid = 0;
		this.suites = new RunningSuite[numberOfThreads];
		for (int i = 0; i < numberOfThreads; ++i) {
			this.suites[i] = new RunningSuite();
		}
	}

	public synchronized void destroy() {
		for (int i = 0; i < suites.length; ++i) {
			this.suites[i].stopSuite();
		}
		// give runners a quick chance to die.
		// try {
		// Thread.sleep(250);
		// }
		// catch(InterruptedException ex) {
		// }
	}

	public void execute(Runnable target) {
		if (target != null)
			synchronized (this.waitingLine) {
				this.waitingLine.add(target);
				this.waitingLine.notify();
			}
	}

	public String getName() {
		return this.poolName;
	}

	public int getWaitingLineLength() {
		synchronized (this.waitingLine) {
			return this.waitingLine.size();
		}
	}

	public void _clearWaitingLine() {
		synchronized (this.waitingLine) {
			this.waitingLine.clear();
		}
	}

	/**
	 * @return thread pool 에서 실행 중인 runnable 객체를 리턴한다. (null이 포함될 수 있다.)
	 */
	public Runnable[] _getCurrentTargets() {
		Runnable[] rs = new Runnable[suites.length];
		for (int i = 0; i < suites.length; ++i) {
			rs[i] = suites[i].getCurrentTarget();
		}
		return rs;
	}

	// /**
	// * 작업 종료를 평가하기 위해서 만든 메소드 -__- 작업 중인 active target의 수와 대기열의 수가 입력 값보다 작으면
	// true 아니면 false
	// *
	// * @param cntMaxActiveTarget
	// * @param waitLineMaxLength
	// * @return
	// */
	// public synchronized boolean _evalThreshold(int cntMaxActiveTarget, int
	// waitLineMaxLength) {
	// synchronized (waitingLine) {
	// Runnable[] rs = _getCurrentTargets();
	// int cntTarget = 0;
	// for (int i = 0, len = rs.length; i < len; ++i)
	// if (rs[i] != null)
	// ++cntTarget;
	// System.out.println(cntTarget+", "+getWaitingLineLength());
	// if (cntTarget <= cntMaxActiveTarget && getWaitingLineLength() <=
	// waitLineMaxLength)
	// return true;
	// else
	// return false;
	// }
	// }
	class RunningSuite {
		private Thread internalThread;
		private Runnable target;

		private RunningSuite() {
			Runnable r = new Runnable() {
				public void run() {
					try {
						invokeRunnable();
					} catch (Throwable t) {
						Logger logger = LogBroker.getLogger(this);
						logger.severe(StackTrace.trace(t));
					}
				}
			};
			this.internalThread = new Thread(r, getName() + "-" + ++threadUid);
			this.internalThread.setDaemon(daemonThread);
			this.internalThread.setPriority(threadPriority);
			this.internalThread.start();
		}

		private void invokeRunnable() {
			try {
				while (!Thread.interrupted()) {
					try {
						target = null;
						synchronized (waitingLine) {
							target = (Runnable) waitingLine.removeFirst();
						}
						try {
							target.run();
						} catch (Throwable t) {
							t.printStackTrace();
						}
						target = null;
					} catch (Exception ne) {
						synchronized (waitingLine) {
							waitingLine.wait(1000 * 60); // 1분에 한번씩 깨어난다. 혹시나
															// dead lock 걸릴까 하는
															// 염려 ㅡ.ㅡ"
						}
					}
				}
			} catch (InterruptedException ie) {
			}
		}

		private void stopSuite() {
			this.internalThread.interrupt();
		}

		private Runnable getCurrentTarget() {
			return this.target;
		}
	}
}

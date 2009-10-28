package wizest.fx.stat;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.math.stat.regression.SimpleRegression;

public class RegressionSlope implements Slope {

	private SimpleRegression regression;
	private int i;
	private int windowSize;
	private ArrayBlockingQueue<Double> q;

	/**
	 * @param windowSize
	 *            windowSize should be greater than 1.
	 */
	public RegressionSlope(int windowSize) {
		regression = new SimpleRegression();
		q = new ArrayBlockingQueue<Double>(windowSize);
		this.windowSize = windowSize;
		i = 0;
	}

	public void addValue(double v) {
		if (i >= windowSize)
			regression.removeData(i - windowSize, q.poll());

		regression.addData(i++, v);
		q.offer(v);
	}

	public double getSlope() {
		return Math.atan(regression.getSlope()) * 2 / Math.PI;
	}

	public static void main(String[] args) {
		Slope s = new RegressionSlope(5);
		s.addValue(10);
		s.addValue(11);
		s.addValue(12);
		s.addValue(11);
		s.addValue(14);
		s.addValue(12);
		s.addValue(10);
		s.addValue(17);
		s.addValue(14);
		s.addValue(10);
		s.addValue(0);
		s.addValue(0);
		s.addValue(0);
		s.addValue(0);
		s.addValue(2);

		System.out.println(s.getSlope());
		System.out.println(Math.asin(s.getSlope()) * 180. / Math.PI);
	}
}

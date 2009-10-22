package wizest.fx.stat;

import org.apache.commons.math.stat.regression.SimpleRegression;

public class RegressionSlope implements Slope {

	private SimpleRegression regression;
	private int x;

	public RegressionSlope() {
		regression = new SimpleRegression();
		x = 0;
	}

	public void addValue(double v) {
		regression.addData(x++, v);
	}

	public double getSlope() {
		return Math.sin(Math.atan(regression.getSlope()));
	}

	public static void main(String[] args) {
		Slope s = new RegressionSlope();
		s.addValue(10);
		s.addValue(11);
		s.addValue(12);
		s.addValue(11);
		s.addValue(14);
		s.addValue(12);
		s.addValue(10);
		s.addValue(17);
		s.addValue(14);
		s.addValue(15);

		System.out.println(s.getSlope());
		System.out.println(Math.asin(s.getSlope()) * 180. / Math.PI);
	}
}

package wizest.fx.stat;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class StochasticSlope implements Slope {

	private DescriptiveStatistics stats;
	private double k; // coefficient of standard deviation
	private double x; // recent value

	public StochasticSlope(int windowSize, double sigmaK) {
		stats = new DescriptiveStatistics(windowSize);
		this.k = sigmaK;
		x = Double.NaN;
	}

	@Override
	public void addValue(double v) {
		stats.addValue(v);
		x = v;
	}

	@Override
	public double getSlope() {
		double m = stats.getMean();
		double sig = stats.getStandardDeviation();
		double z = (x - m) / sig;

		// saturate and normalize
		z = Math.max(Math.min(z, k), -k) / k;
		return z;
	}

	public static void main(String[] args) {
		Slope s = new StochasticSlope(10, 2);
		s.addValue(10);
		s.addValue(11);
		s.addValue(12);
		s.addValue(11);
		s.addValue(4);
		s.addValue(2);
		s.addValue(1);
		s.addValue(7);
		s.addValue(0);
		s.addValue(2);

		System.out.println(s.getSlope());
		System.out.println(Math.asin(s.getSlope()) * 180. / Math.PI);
	}
}

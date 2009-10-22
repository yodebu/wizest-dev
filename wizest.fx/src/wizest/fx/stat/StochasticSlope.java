package wizest.fx.stat;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class StochasticSlope implements Slope {

	private DescriptiveStatistics stats;

	public StochasticSlope(int windowSize) {
		stats = new DescriptiveStatistics(windowSize);
	}

	@Override
	public void addValue(double v) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getSlopeRad() {
		double mean = stats.getMean();
		double std = stats.getStandardDeviation();

		// TODO Auto-generated method stub
		return 0;
	}

	public static void main(String[] args) {
		StochasticSlope s = new StochasticSlope(10);
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

		System.out.println(s.getSlopeRad());
		System.out.println(s.getSlopeRad() * 180. / Math.PI);
	}

}

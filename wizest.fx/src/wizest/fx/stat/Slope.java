package wizest.fx.stat;

public interface Slope {

	void addValue(double v);

	/**
	 * @return -Math.PI/2 < value <= Math.PI/2 slope in radian angle
	 */
	double getSlopeRad();

}

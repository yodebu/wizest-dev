package wizest.fx.stat;

public interface Slope {

	void addValue(double v);

	/**
	 * This method returns the manipulated angle which is the sine of the radian
	 * angle of slope.
	 * 
	 * @return -1 < returnValue < 1 for which the returnValue equals to
	 *         sin(radian angle of slope)
	 */
	double getSlope();
}

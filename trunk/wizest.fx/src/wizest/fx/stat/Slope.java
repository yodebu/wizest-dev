package wizest.fx.stat;

public interface Slope {

	void addValue(double v);

	/**
	 * This method returns the manipulated angle which is between -1 and 1.
	 * 
	 * @return -1 <= returnValue <= 1
	 */
	double getSlope();
	
}

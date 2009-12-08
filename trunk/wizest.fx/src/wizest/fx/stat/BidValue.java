package wizest.fx.stat;

public class BidValue {

	private int p; // bid price
	private long r; // remain
	private int v; // value

	public BidValue() {
		p = 0;
		r = 0;
		v = 0;
	}

	public int updateValue(int bidPrice, long remain) {
		long d;
		switch (2) {
		case 1: // TYPE1
			if (p == bidPrice)
				d = remain - r;
			else if (p < bidPrice)
				d = -r;
			else
				d = r;
			break;
		case 2: // TYPE2
			if (p == bidPrice)
				d = remain - r;
			else if (p < bidPrice)
				d = -r - remain;
			else
				d = r + remain;
			break;
		default:
			d = 0;
		}
		p = bidPrice;
		r = remain;
		v += d;

		return v;
	}

	public static void main(String[] args) {
		BidValue psv = new BidValue();

		System.out.println(psv.updateValue(10, 10));
		System.out.println(psv.updateValue(10, 9));
		System.out.println(psv.updateValue(10, 8));
		System.out.println(psv.updateValue(10, 7));
		System.out.println(psv.updateValue(10, 4));
		System.out.println(psv.updateValue(11, 7));
		System.out.println(psv.updateValue(11, 5));
		System.out.println(psv.updateValue(11, 2));
		System.out.println(psv.updateValue(12, 12));
		System.out.println();
		System.out.println(psv.updateValue(12, 12));
		System.out.println(psv.updateValue(11, 2));
		System.out.println(psv.updateValue(11, 5));
		System.out.println(psv.updateValue(11, 7));
		System.out.println(psv.updateValue(10, 4));
		System.out.println(psv.updateValue(10, 7));
		System.out.println(psv.updateValue(10, 8));
		System.out.println(psv.updateValue(10, 9));
		System.out.println(psv.updateValue(10, 10));
		System.out.println();
		System.out.println(psv.updateValue(10, 10));
		System.out.println(psv.updateValue(10, 9));
		System.out.println(psv.updateValue(10, 8));
		System.out.println(psv.updateValue(10, 7));
		System.out.println(psv.updateValue(10, 4));
		System.out.println(psv.updateValue(11, 7));
		System.out.println(psv.updateValue(11, 5));
		System.out.println(psv.updateValue(11, 2));
		System.out.println(psv.updateValue(12, 12));
		System.out.println();
	}
}


public class R01 {
	// full size map: (50p x 50p) or (5r x 5r) 
	// 1 region = (1r x 1r) = (10p x 10p)
	// Region = 1,2,3,4,5... 0 = unknown
	public static final int REGION_SIZE = 10;
	public static int toR(int x) {
		return 1 + x/REGION_SIZE;
	}
	public static int toP(int x) {
		if (x == 0) {
			return -1;
		} else {
			return (x-1)*REGION_SIZE + REGION_SIZE/2;
		}
	}
}

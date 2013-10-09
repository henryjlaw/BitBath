
public class R02 {	
	// full size map: (50p x 50p) or (4r x 4r) 
	// 1 region = (1r x 1r) = (12p x 12p)*
	// Region = 0,1,2,3
	public static final int REGION_SIZE = 12;

	public static int toR(int x) {
		return Math.min( x/REGION_SIZE, 3 );
	}
	
	public static int toP(int x) {
		return x*REGION_SIZE + REGION_SIZE/2;
	}
}

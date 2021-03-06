
public class R02 {	
	// full size map: (50p x 50p) or (4r x 4r) 
	// 1 region = (1r x 1r) = (12p x 12p)*
	// Region = 0,1,2,3
	public static final int REGION_SIZE = 12;

	public static int toR(double x) {
		return (int)Math.min( x/REGION_SIZE, 3 );
	}
	
	public static int toP(int x) {
		return x*REGION_SIZE + REGION_SIZE/2;
	}
	public static int toR(double x, double y) {
		return 4*toR(x) + toR(y);
	}
	public static void toP(double[] p, int r) {
		p[0] = toP(r/4);	// X
		p[1] = toP(r%4);	// Y
	}
}

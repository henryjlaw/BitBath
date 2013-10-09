/**
 * 16 region, 12p x 12p
 * region[0..15]
 * {
 * 		0:cityF:1:2
 * 		1:cityE:1:2
 * 		2:cityN:1:2
 * 		3:unitF:3:8
 * 		4:unitE:3:8
 * }
 * total of 9 bits
 * 3 region * 9 bit/region = 27 bit < 32 bit 
 * 3 integer = 3 integer * 32 bit / integer > 96 bit
 * 
 */
public class M02 {
	final static int CITY_F = 0, CITY_E = 1, CITY_N = 2, UNIT_F = 3, UNIT_E = 4;
	final static int[] f = { 2, 2, 2, 8, 8 };
	final static int base27 = (int)Math.pow(2, 27);
	
	public static void toRegion( int[] region, int radio, int index ) {
		for(int i = 0; i < index; i++) {
			radio /= base27;
		}
		radio %= base27;
		for(int i = 0; i < 5; i++) {
			region[i] = radio % f[i];
			radio /= f[i];
		}
	}
	
	public static int toRadio( int[] region, int radio, int index) {
		int sum = 0;
		for	(int i = 0; i < 5; i++) {
			sum += sum*f[i] + region[i]; 
		}
		for (int i = 0; i < index; i++) {
			sum *= base27;
		}
		radio += sum;
		return radio;
	}
	
	public static void main(String[] args) {
		int[] region = { 1,1,1,7,7 };
		int r1 = toRadio( region, 0, 0 );
		int r2 = toRadio( region, r1, 1 );
		int r3 = toRadio( region, r2, 2 );
		System.out.println(r3);
	}

	/**
	 * Convert (Merge) radio to region map
	 * @param regionMap
	 * @param incomingRadio
	 */
	public static void toMap(int[][] regionMap, int[] radio) {
		for (int i = 0; i < radio.length; i++) {
			
		}
	}
	
	/**
	 * Convert object list into radio
	 * @param radio
	 * @param objX
	 * @param objY
	 * @param objID
	 * @param objFaction
	 * @param objType
	 */
	public static void toRadio(int[] radio, double[] objX, double[] objY, int[] objID, int[] objFaction, int[] objType) {
		for( int i = 0; i < objID.length; i++) {
			if( objType[i] == 0) {
				
			}
		}
	}
}

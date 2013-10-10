import java.util.Arrays;

// Global view (16 region, ~12p x ~12p)
// [ 00, 01, 02, 03 ]
// [ 04, 05, 06, 07 ]
// [ 08, 09, 10, 11 ]
// [ 12, 13, 14, 15 ]
//
// Unit view
// [ -5, -4, -3 ] ~ [ radio[0]/0, radio[0]/1, radio[0]/2 ]
// [ -1, 00, +1 ] ~ [ radio[1]/0, radio[1]/1, radio[1]/2 ]
// [ +3, +4, +5 ] ~ [ radio[2]/0, radio[2]/1, radio[2]/2 ]

// region[0..15] 
//	{ 
// 		position 	field 	size(bit) 	base 	factor 
// 		0 			cityF 	1 			2 		256 
// 		1 			cityE 	1 			2 		128 
// 		2 			cityN 	1 			2 		64 
// 		3 			unitF 	3 			8 		8 
// 		4 			unitE 	3 			8 		1 
// 		total 				9 
//	} 
// 3 region * 9 bit/region = 27 bit < 32 bit 
// 3 integer = 3 integer * 32 bit / integer > 96 bit
// 
// status 
//	{ 
//	position 	field 	size(bit) 	base 	factor 
//	0 			R 		4 			16 		1
// 	total 				4 
//	}

public class M02 {
	// Radio: Region
	final static int CITY_F = 0, CITY_E = 1, CITY_N = 2, UNIT_F = 3,
			UNIT_E = 4;
	final static int[] regionFactor = { 256, 128, 64, 8, 1 };
	final static int binary10 = (int) Math.pow(2, 10);
	// Radio: Status
	final static int R = 0;
	final static int[] statusFactor = { 1 };

	public static void toRegion(int[] region, int radio, int index) {
		int code = T01.shiftDown(radio, binary10, index);
		T01.decode(region, code, regionFactor);
	}

	public static int toRadio(int[] region, int radio, int index) {
		int code = T01.encode(region, regionFactor);
		return radio + T01.shiftUp(code, binary10, index);
	}

	public static void toStatus(int[] status, int radio) {
		T01.decode(status, radio, statusFactor);
	}

	public static void mergeRegion(int[] region, int[] update) {
		for (int i = 0; i < region.length; i++) {
			region[i] = Math.max(region[i], update[i]);
		}
	}

	/**
	 * Convert (Merge) radio to region map Global View
	 * 
	 * @param regionMap
	 * @param incomingRadio
	 */
	public static void toMap(int[][] regionMap, int[] radio) {
		int[] status = { -1 };
		int[] update = new int[regionFactor.length];
		for (int i = 0; i < radio.length; i++) {
			toStatus(status, radio[3]);
			int r = status[R];
			if (r >= 5) {
				toRegion(update, radio[0], 0);
				mergeRegion(regionMap[r - 5], update);
			}
			if (r >= 4) {
				toRegion(update, radio[0], 1);
				mergeRegion(regionMap[r - 4], update);
			}
			if (r >= 3) {
				toRegion(update, radio[0], 2);
				mergeRegion(regionMap[r - 3], update);
			}
			if (r >= 1) {
				toRegion(update, radio[1], 0);
				mergeRegion(regionMap[r - 1], update);
			}
			if (r >= 0) {
				toRegion(update, radio[1], 1);
				mergeRegion(regionMap[r + 0], update);
			}
			if (r <= 14) {
				toRegion(update, radio[1], 2);
				mergeRegion(regionMap[r + 1], update);
			}
			if (r <= 12) {
				toRegion(update, radio[2], 0);
				mergeRegion(regionMap[r + 3], update);
			}
			if (r <= 11) {
				toRegion(update, radio[2], 1);
				mergeRegion(regionMap[r + 4], update);
			}
			if (r <= 10) {
				toRegion(update, radio[2], 2);
				mergeRegion(regionMap[r + 5], update);
			}
		}
	}

	/**
	 * Convert object list into radio
	 * 
	 * @param radio
	 * @param objX
	 * @param objY
	 * @param objID
	 * @param objFaction
	 *            [-1,0,?]
	 * @param objType
	 *            [0,1,2,3]
	 */
	public static void toRadio(int[] radio, double x, double y, double[] objX,
			double[] objY, int[] objFaction, int[] objType) {
		int r = -1;
		int[][] regionMap = new int[16][regionFactor.length];
		// build regionMap
		for (int i = 0; i < objFaction.length; i++) {
			r = R02.toR(objX[i], objY[i]); // region
			if (objType[i] == 0 && objFaction[i] == 0) {
				// friendly city
				regionMap[r][CITY_F] = 1;
			} else if (objType[i] == 0 && objFaction[i] > 0) {
				// enemy city
				regionMap[r][CITY_E] = 1;
			} else if (objType[i] == 0 && objFaction[i] == -1) {
				// neutral city
				regionMap[r][CITY_N] = 1;
			} else if (objType[i] > 0 && objFaction[i] == 0
					&& regionMap[r][UNIT_F] < 8) {
				// friendly unit
				regionMap[r][UNIT_F] += 1;
			} else if (objType[i] > 0 && objFaction[i] > 0
					&& regionMap[r][UNIT_E] < 8) {
				// enemy unit
				regionMap[r][UNIT_E] += 1;
			} else {
				// unknown
			}
		}

		// convert regionMap to radio
		r = R02.toR(x, y); // my region
		if (r >= 5)
			radio[0] = toRadio(regionMap[r - 5], radio[0], 0);
		if (r >= 4)
			radio[0] = toRadio(regionMap[r - 4], radio[0], 1);
		if (r >= 3)
			radio[0] = toRadio(regionMap[r - 3], radio[0], 2);
		if (r >= 1)
			radio[1] = toRadio(regionMap[r - 1], radio[1], 0);
		if (r >= 0)
			radio[1] = toRadio(regionMap[r + 0], radio[1], 1);
		if (r <= 14)
			radio[1] = toRadio(regionMap[r + 1], radio[1], 2);
		if (r <= 12)
			radio[2] = toRadio(regionMap[r + 3], radio[2], 0);
		if (r <= 11)
			radio[2] = toRadio(regionMap[r + 4], radio[2], 1);
		if (r <= 10)
			radio[2] = toRadio(regionMap[r + 5], radio[2], 2);

		/**
		 * <developer comment>: given this method might result in mapping data
		 * to the wrong region, but such scenario should never happen as units
		 * are limited to their vision
		 */
	}

	public static void main01(String[] args) {
		// basic test
		// int[] region1 = { 1, 1, 1, 7, 7 };
		// int r1 = toRadio(region1, 0, 0);
		// System.out.println(r1);
		//
		// int[] region2 = { 1, 0, 1, 0, 7 };
		// int r2 = toRadio(region2, r1, 1);
		// System.out.println(r2);
		//
		// int[] region3 = { 0, 1, 0, 7, 0 };
		// int r3 = toRadio(region3, r2, 2);
		// System.out.println(r3);
		//
		// toRegion(region1, r3, 0);
		// System.out.println(Arrays.toString(region1));
		//
		// toRegion(region2, r3, 1);
		// System.out.println(Arrays.toString(region2));
		//
		// toRegion(region3, r3, 2);
		// System.out.println(Arrays.toString(region3));
		//
		// System.out.println(Integer.MAX_VALUE);
		// System.out.println(binary10);
	}

	public static void main(String[] args) {
		// sample objects
		final int N = 2;
		double[] objX = new double[N], objY = new double[N];
		int[] objFaction = new int[N], objType = new int[N];
		for (int i = 0; i < N; i++) {
			objX[i] = Math.round(5000 * Math.random()) / 100.0; // (0.00,50.00)
			objY[i] = Math.round(5000 * Math.random()) / 100.0; // (0.00,50.00)
			objFaction[i] = (int) (3 * Math.random()) - 1; // -1,0,1
			if (objFaction[i] == -1) {
				objType[i] = 0; // neutral can only be city
			} else {
				objType[i] = (int) (4 * Math.random()); // 0,1,2,3
			}
		}
		// display data
		System.out.println("objX: " + Arrays.toString(objX));
		System.out.println("objY: " + Arrays.toString(objY));
		System.out.println("objFaction: " + Arrays.toString(objFaction));
		System.out.println("objType: " + Arrays.toString(objType));
		// generate radio
		double x, y;
		double[] objX2 = new double[N - 1], objY2 = new double[N - 1];
		int[] objFaction2 = new int[N - 1], objType2 = new int[N - 1];
		int[][] radio = new int[N][4];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (j < i) {
					objX2[j] = objX[j];
					objY2[j] = objY[j];
					objFaction2[j] = objFaction[j];
					objType2[j] = objType[j];
				} else if (j > i) {
					objX2[j - 1] = objX[j];
					objY2[j - 1] = objY[j];
					objFaction2[j - 1] = objFaction[j];
					objType2[j - 1] = objType[j];
				}
			}
			toRadio(radio[i], objX[i], objY[i], objX2, objY2, objFaction2,
					objType2);
		}
		// display radio
		for (int i = 0; i < radio.length; i++) {
			System.out
					.println("radio[" + i + "]: " + Arrays.toString(radio[i]));
		}
		// generate map
		int[][] regionMap = new int[16][regionFactor.length];
		for (int i = 0; i < N; i++) {
			toMap(regionMap, radio[i]);
		}
		// display map
		for (int i = 0; i < regionMap.length; i++) {
			System.out.println("region[" + i + "]: "
					+ Arrays.toString(regionMap[i]));
		}

	}
}

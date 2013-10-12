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

public class Map01 implements Map {
	// Connectivity
	final static int[][] CONNECT = {
			//
			{ 0, 1, 4, 5 }, // 0
			{ 1, 0, 2, 4, 5, 6 }, // 1
			{ 2, 1, 3, 5, 6, 7 }, // 2
			{ 3, 2, 6, 7 }, // 3
			{ 4, 0, 1, 5, 8, 9 },// 4
			{ 5, 0, 1, 2, 4, 6, 8, 9, 10 },// 5
			{ 6, 1, 2, 3, 5, 7, 9, 10, 11 }, // 6
			{ 7, 2, 3, 6, 10, 11 }, // 7
			{ 8, 4, 5, 9, 12, 13 }, // 8
			{ 9, 4, 5, 6, 8, 10, 12, 13, 14 }, // 9
			{ 10, 5, 6, 7, 9, 11, 13, 14, 15 }, // 10
			{ 11, 6, 7, 10, 14, 15 },// 11
			{ 12, 8, 9, 13 },// 12
			{ 13, 8, 9, 10, 12, 14 },// 13
			{ 14, 9, 10, 11, 13, 15 },// 14
			{ 15, 10, 11, 14 } // 15
	};
	// Radio: Region
	final static int CITY_F = 0, CITY_E = 1, CITY_N = 2, UNIT_F = 3,
			UNIT_E = 4;
	final static int[] regionFactor = { 256, 128, 64, 8, 1 };
	final static int binary10 = (int) Math.pow(2, 10);
	// Radio: Status
	final static int R = 0;
	final static int[] statusFactor = { 1 };

	final static int UNIT_DATA = 9;
	final static int CENTER = 0;

	public static void updateMapByRadio(int[][] map, int[] radio) {
		int[][] data = new int[10][5];
		Radio01.decodeRadio(data, radio);
		int center = data[UNIT_DATA][CENTER];
		for (int i = 0; i < CONNECT[center].length; i++) {
			updateRegion(map[CONNECT[center][i]], data[i]);
		}
	}

	public static void updateRegion(int[] region, int[] update) {
		for (int i = 0; i < region.length; i++) {
			region[i] = Math.max(region[i], update[i]);
		}
	}

	public static void buildMapByRadar(int[][] map, double[] objX,
			double[] objY, int[] objFaction, int[] objType) {
		for (int i = 0; i < objFaction.length; i++) {
			updateMap(map, objX[i], objY[i], objFaction[i], objType[i]);
		}
	}

	public static void updateMap(int[][] map, double objX, double objY,
			int objFaction, int objType) {
		int region = R02.toR(objX, objY);
		if (objType == 0 && objFaction == 0) {
			// friendly city
			map[region][CITY_F] = 1;
		} else if (objType == 0 && objFaction > 0) {
			// enemy city
			map[region][CITY_E] = 1;
		} else if (objType == 0 && objFaction == -1) {
			// neutral city
			map[region][CITY_N] = 1;
		} else if (objType > 0 && objFaction == 0 && map[region][UNIT_F] < 8) {
			// friendly unit
			map[region][UNIT_F] += 1;
		} else if (objType > 0 && objFaction > 0 && map[region][UNIT_E] < 8) {
			// enemy unit
			map[region][UNIT_E] += 1;
		} else {
			// unknown
		}
	}

	public static void createUnitView(int[][] data, int[][] map, double x,
			double y) {
		int center = R02.toR(x, y);
		for (int i = 0; i < CONNECT[center].length; i++) {
			data[i] = map[CONNECT[center][i]];
		}
		/**
		 * <developer comment>: given this method might result in mapping data
		 * to the wrong region, but such scenario should never happen as units
		 * are limited to their vision
		 */
	}

	public static void toRadio(int[] radio, double x, double y, int type,
			double[] objX, double[] objY, int[] objFaction, int[] objType) {
		int[][] map = new int[16][5];
		buildMapByRadar(map, objX, objY, objFaction, objType);
		updateMap(map, x, y, 0, type);
		int[][] data = new int[10][5];
		createUnitView(data, map, x, y);
		data[UNIT_DATA][CENTER] = R02.toR(x, y);
		Radio01.encodeRadio(radio, data);
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
		main02(args);
	}

	public static void main03(String[] args) {
		final int N = 3;
		double x = 34.38, y = 13.88;
		int type = 0;
		double[] objX2 = { 30.22, 4.17 };
		double[] objY2 = { 0.4, 25.78};
		int[] objFaction2 = { 1, -1 };
		int[] objType2 = { 3, 0 };
		int[][] radio = new int[N][4];

		toRadio(radio[0], x, y, type, objX2, objY2, objFaction2, objType2);
		System.out.println(Arrays.toString(radio[0]));
		showRadio(radio[0]);
	}

	public static void main02(String[] args) {
		// sample objects
		final int N = 10;
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
		// display data (master)
		System.out.println("objX: " + Arrays.toString(objX));
		System.out.println("objY: " + Arrays.toString(objY));
		System.out.println("objFaction: " + Arrays.toString(objFaction));
		System.out.println("objType: " + Arrays.toString(objType));
		System.out.println();
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
			// display data
			toRadio(radio[i], objX[i], objY[i], objType[i], objX2, objY2,
					objFaction2, objType2);
		}
		// display radio
		for (int j = 0; j < radio.length; j++) {
			System.out
					.println("radio[" + j + "]: " + Arrays.toString(radio[j]));
			showRadio(radio[j]);
		}
		System.out.println();
		// generate map
		for (int i = 0; i < N; i++) {
			int[][] regionMap = new int[16][regionFactor.length];
			for (int j = 0; j < N; j++) {
				if (j != i)
					updateMapByRadio(regionMap, radio[j]);
			}
			// display map
			System.out.println("map[" + i + "]: ");
			for (int j = 0; j < regionMap.length; j++) {
				System.out.print(Arrays.toString(regionMap[j]));
				if ((j + 1) % 4 == 0) {
					System.out.println();
				} else {
					System.out.print("~");
				}
			}
			System.out.println();
		}
	}

	public void decode(Map map, Radio radio) {
		// TODO Auto-generated method stub

	}

	public void encode(Map map, Radio radio) {
		// TODO Auto-generated method stub

	}

	public static void showRadio(int[] radio) {
		int[][] data = new int[10][5];
		Radio01.decodeRadio(data, radio);
		int center = data[UNIT_DATA][CENTER];
		System.out.println("---Start of Radio---");
		for (int i = 0; i < CONNECT[center].length; i++) {
			for (int j = 0; j < 5; j++) {
				if (data[i][j] > 0) {
					System.out.print("Region[" + (CONNECT[center][i]) + "]:");
					switch (j) {
					case 0:
						System.out.println("CityF:" + data[i][j]);
						break;
					case 1:
						System.out.println("CityE:" + data[i][j]);
						break;
					case 2:
						System.out.println("CityN:" + data[i][j]);
						break;
					case 3:
						System.out.println("UnitF:" + data[i][j]);
						break;
					case 4:
						System.out.println("UnitE:" + data[i][j]);
						break;
					}
				}
			}
		}
		System.out.println("---End of Radio---");
		System.out.println();
	}
}

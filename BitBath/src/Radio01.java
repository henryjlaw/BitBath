public class Radio01 implements Radio {
	// Regional Statistic ~ data[0..8][0..4]
	public final static int CITY_F = 0, CITY_E = 1, CITY_N = 2, UNIT_F = 3,
			UNIT_E = 4;
	public final static int[] regionFactor = { 256, 128, 64, 8, 1 };
	public final static int binary10 = (int) Math.pow(2, 10);
	// Unit Status ~ ~ data[9][0]
	public final static int R = 0;
	public final static int[] statusFactor = { 1 };

	public static void decodeRadio(int[][] data, int[] radio) {
		// int[][] data = new int[10][5];
		for (int i = 0; i < 9; i++) {
			int code = T01.shiftDown(radio[i / 3], binary10, i % 3);
			T01.decode(data[i], code, regionFactor);
		}
		T01.decode(data[9], radio[3], statusFactor);
		// return data;
	}

	public static void encodeRadio(int[] radio, int[][] data) {
		// int[] radio = new int[4];
		for (int i = 0; i < 9; i++) {
			int code = T01.encode(data[i], regionFactor);
			radio[i / 3] += T01.shiftUp(code, binary10, i % 3);
		}
		radio[3] = data[9][0];
		// return radio;
	}

	public int[][] decode(int[] radio) {
		int[][] data = new int[10][5];
		decodeRadio(data, radio);
		return data;
	}

	public int[] encode(int[][] data) {
		int[] radio = new int[4];
		encodeRadio(radio, data);
		return radio;
	}
}

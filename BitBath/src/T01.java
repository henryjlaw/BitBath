
public class T01 {

	// shift down: code -> code / factor
	public static int shiftDown(int code, int base, int count) {
		for(int i = 0; i < count; i++) {
			code /= base;
		}
		code %= base;
		return code;
	}

	// shift up: code -> code * factor
	public static int shiftUp(int code, int base, int count) {
		return code*(int)Math.pow(base, count);
	}

	// decode: code -> data
	public static void decode(int[] data, int code, int[] codepage) {
		for(int i = 0; i < codepage.length; i++) {
			data[i] = code / codepage[i];
			code %= codepage[i];
		}
	}

	// encode: data -> code
	public static int encode(int[] data, int[] codepage) {
		int sum = 0;
		for	(int i = 0; i < codepage.length; i++) {
			sum += codepage[i]*data[i]; 
		}
		return sum;
	}

}

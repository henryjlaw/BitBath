/**
 * 16 region, 12p x 12p
 * region: { city faction - boolean, unit count - 0-3 }
 * region: 0-15
 */
public class M02 {
	final int CITY_F = 0, CITY_E = 1, CITY_N = 2, UNIT_F = 3, UNIT_E = 4;

	// radio: counter of city in each region in format of 5XR + YR, 16 regions
	// with 3-bit number

	// Count Of Faction: 16 region, 3 faction/region, 3 bit/faction = 225 bits
	// City Location: 7 city, 3 faction/city, 2 regionXY/city, 3 bit/regionXY =
	// 84 bits
	// X, Y: 0 (unknown) or 1,2,3,4,5
	// Faction: [friend][enemy][neutral]

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
			
		}
	}
}

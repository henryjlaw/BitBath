
public class M01 {
	public static void toMap(int[][] cityMap, int[][] incomingRadio) {
		// radio: counter of city in each region in format of 5XR + YR, 25 regions with 3-bit number
		
		// Count Of Faction: 25 region, 3 faction/region, 3 bit/faction = 225 bits
		// City Location: 7 city, 3 faction/city, 2 regionXY/city, 3 bit/regionXY = 84 bits
		// X, Y: 0 (unknown) or 1,2,3,4,5
		// Faction: [friend][enemy][neutral]
		
		
		
		for (int i = 0; i < incomingRadio.length; i++) {
			
		}
	}
}

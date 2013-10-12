import java.util.Random;

public class F0003_ShareMap {
	public int orderType = 1; // always a move order
	public double destX, destY; // the move order will use these fields to
								// communicate a destination
	Random r = new Random(); // help us randomize our actions

	public int[] radio = new int[4]; // we'll use this to communicate with other
										// bots
	protected int[][] map = new int[16][5];

	public static final int CITY = 0;
	public static final int GRUNT = 1;
	public static final int HOVERCRAFT = 2;
	public static final int ARTIL = 3;

	public Object think(double dx, double dy, double x, double y,
			boolean moving, int terrain, int ourID, int ourType, double hp,
			double maxHP, double range, double time, double[] objX,
			double[] objY, int[] objID, int[] objFaction, int[] objType,
			int[][] incomingRadio) {
		if (moving)
			return null;
		orderType = 1;

		// create map
		Map01.buildMapByRadar(map, objX, objY, objFaction, objType);
		Map01.updateMap(map, x, y, 0, ourType);
		for (int i = 0; i < incomingRadio.length; i++) {
			Map01.updateMapByRadio(map, incomingRadio[i]);
		}

		// prepare radio
		Map01.toRadio(radio, x, y, ourType, objX, objY, objFaction, objType);

		int center = R02.toR(x, y);
		double[] target = { -1, -1 };
		// objectives
		// #1 - save friendly city - look for friendly city in danger (enemy in
		// region)
		for (int i = 1; i < Map01.CONNECT[center].length; i++) {
			int region = Map01.CONNECT[center][i];
			int cityF = map[region][Map01.CITY_F];
			int unitE = map[region][Map01.UNIT_E];
			if (unitE > 0 && cityF > 0) {
				R02.toP(target, region);
				destX = target[0];
				destY = target[1];
				return this;
			}
		}
		// #2 - get neutral city
		for (int i = 1; i < Map01.CONNECT[center].length; i++) {
			int region = Map01.CONNECT[center][i];
			int cityN = map[region][Map01.CITY_N];
			if (cityN > 0) {
				R02.toP(target, region);
				destX = target[0];
				destY = target[1];
				return this;
			}
		}
		// #3 - attack enemy city
		for (int i = 1; i < Map01.CONNECT[center].length; i++) {
			int region = Map01.CONNECT[center][i];
			int cityE = map[region][Map01.CITY_E];
			int unitE = map[region][Map01.UNIT_E];
			int unitF = map[center][Map01.UNIT_F];
			if (cityE > 0 && unitF > unitE) {
				R02.toP(target, region);
				destX = target[0];
				destY = target[1];
				return this;
			}
		}
		// #4 - patrol
		int cityF = map[center][Map01.CITY_F];
		int cityE = map[center][Map01.CITY_E];
		int cityN = map[center][Map01.CITY_N];
		int unitF = map[center][Map01.UNIT_F];
		int unitE = map[center][Map01.UNIT_E];
		if (cityN == 0 && cityE == 0 && unitE == 0 && unitF > 5) {
			int[] path = {
					//
					5, 5, 6, 6, //
					5, 6, 10, 6, //
					9, 5, 9, 10, //
					9, 9, 10, 10 //
			};
			R02.toP(target, path[center]);
			destX = target[0];
			destY = target[1];
			return this;
		}
		// // look for closest enemy or neutral city
		// double best = 100000000;
		// boolean found = false;
		// for (int i = 0; i < objX.length; i++) {
		// if (objFaction[i] == 0)
		// continue; // our team -- not interesting
		// double d = (x - objX[i]) * (x - objX[i]) + (y - objY[i]) * (y -
		// objY[i]);
		// if (d > best)
		// continue; // we've already seen something closer
		// best = d;
		// destX = objX[i];
		// destY = objY[i];
		// return this;
		// }
		// #5 - just wait
		this.orderType = 0;
		return this;
	}

	public int build(double dx, double dy, double x, double y, int terrain,
			int id, int buildItem, double hp, double maxHP, double time,
			double[] objX, double[] objY, int[] objID, int[] objFaction,
			int[] objType, int[][] incomingRadio) {
		if (buildItem != 0)
			return 0;
		// avoid worst unit by terrain
		switch (terrain) {
		case 0: // grass
			return 1 + r.nextInt(3); // random
		case 1: // forest
			return 1; // Grunt
		case 2: // swamp
			return 2; // Hovercraft
		default:
			return 3; // Artillery
		}

	}

}

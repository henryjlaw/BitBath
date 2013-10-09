import java.util.Random;

public class F0002_MoveToCenter {
    public int orderType = 1; // always a move order
    public double destX, destY; // the move order will use these fields to communicate a destination
    Random r = new Random(); // help us randomize our actions

    public int[] radio = new int[4]; // we'll use this to communicate with other bots

	public static final int CITY = 0;
	public static final int GRUNT = 1;
	public static final int HOVERCRAFT = 2;
	public static final int ARTIL = 3;

	public Object think(double dx, double dy, double x, double y, boolean moving, int terrain, int ourID, int ourType, double hp, double maxHP,
			double range, double time, double[] objX, double[] objY, int[] objID, int[] objFaction, int[] objType, int[][] incomingRadio) {
		if (moving) return null;
		orderType = 1;
		destX = 25;
		destY = 25;
		return this;
	}

	public int build(double dx, double dy, double x, double y, int terrain, int id, int buildItem,
    		double hp, double maxHP, double time,
    		double[] objX, double[] objY, int[] objID, int[] objFaction, int[] objType, int[][] incomingRadio) {
		if (buildItem != 0) return 0;
		return 1 + r.nextInt(3);
	}
}

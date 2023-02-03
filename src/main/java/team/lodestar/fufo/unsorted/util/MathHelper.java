package team.lodestar.fufo.unsorted.util;

public class MathHelper {
	private MathHelper() {}
	
	public static boolean isInRange(int x, int min, int max) {
		return x >= min && x < max;
	}
}

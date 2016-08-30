package gridWalk;

import java.util.HashSet;

/**
 * We start at (0,0) and we have to count how many places we can explore given a
 * certain condition. This condition means the following: (14,5) is valid since
 * |1+4|+|5|=10 < 19, but (19,99) is not valid since |1+9|+|9+9|=28>19. The
 * numbers in coordinates have to sum up to number lower than 19.
 * 
 * We can travel down, up, left or right
 * 
 * @author Robb
 *
 */
public class Main {
	// contains explored places in a coordinate system in the form of "x, y"
	private HashSet<String> explored;
	// counts the places explored
	private int count;

	public Main() {
		explored = new HashSet<String>();
		count = 0;
		dfs(0, 0);

	}

	public int count() {
		return count;
	}
	// simple recursive dfs exploring all 4 directions
	private void dfs(int x, int y) {
		// we backtrack if position was already explored or the position is not
		// valid
		if (!explored(x, y) && isValid(x, y)) {
			count++;
			markExplored(x, y);
			dfs(x + 1, y);
			dfs(x - 1, y);
			dfs(x, y + 1);
			dfs(x, y - 1);

		}
	}
	/**
	 * Checks the constraint given in the beginning
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 * @return true if the sum of coordinate numbers are lower or equal to 19
	 */
	public boolean isValid(int x, int y) {

		x = Math.abs(x);
		y = Math.abs(y);

		int sumX = 0;
		while (x > 0) {
			sumX += x % 10;
			x = x / 10;
		}

		int sumY = 0;
		while (y > 0) {
			sumY += y % 10;
			y = y / 10;
		}

		return sumX + sumY <= 19;

	}
	/**
	 * We save the coordinate in the format "x, y"
	 * 
	 * @param x
	 *            x-coordinate
	 * @param y
	 *            y-coordinate
	 */
	private void markExplored(int x, int y) {
		String xCoord = String.valueOf(x);
		String yCoord = String.valueOf(y);

		String point = xCoord + "," + yCoord;
		explored.add(point);
	}
	/**
	 * Whether a given position was already explored
	 * 
	 * 
	 * @param x
	 * @param y
	 * @return true if the position was already explored
	 */
	private boolean explored(int x, int y) {
		String xCoord = String.valueOf(x);
		String yCoord = String.valueOf(y);
		String point = xCoord + "," + yCoord;

		return explored.contains(point);

	}

	public static void main(String args[]) {

		long start = System.currentTimeMillis();

		Main test = new Main();

		// System.out.println(test.isValid(991, -1));

		// test.markExplored(-5, -7);
		// System.out.println(test.explored(-5, -7));
		long time = System.currentTimeMillis() - start;

		System.out.println(test.count() + " time is " + time + " ms");

	}

}

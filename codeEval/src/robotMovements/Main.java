/**
 Robot Movements
 Challenge Description:
 A robot is located in the upper-left corner of a 4×4 grid. The robot can move either up, down, left, or right, but cannot go to the same location twice. The robot is trying to reach the lower-right corner of the grid. Your task is to find out the number of unique ways to reach the destination.

 Input sample:
 There is no input for this program.

 Output sample:
 Print out the number of unique ways for the robot to reach its destination. The number should be printed out as an integer ≥0.
 */
package RobotMovements;

import java.util.LinkedList;

public class Main {
	private int boardRows, boardCols, count;
	// count - counts the number of unique ways
	private boolean[] marked;

	public Main() {
		boardRows = 4;
		boardCols = 4;
		marked = new boolean[boardRows * boardCols];
		count = 0;
		
		// start from upper left corner whose index is 0 in 1-dimensional
		// coordinate system
		dfs(0, marked);
	}

	private void dfs(int v, boolean[] marked) {
		// v is the 1-dimensional index of lower right corner of 4x4 grid
		// here we reached the end
		if (v == 15) {
			count++;
			// backtrack
			return;
		}

		// mark explored
		marked[v] = true;

		// explore all adjacent cells
		for (int w : adj(v)) {
			if (!marked[w])
				dfs(w, marked);
		}
		// backtrack
		marked[v] = false;
	}

	public int getUniqueWays() {
		return count;
	}

	/**
	 * Returns all adjacent cells to cell specified in the argument.
	 * 
	 * @param toExplore
	 * @return
	 */
	private Iterable<Integer> adj(int toExplore) {
		LinkedList<Integer> adj = new LinkedList<Integer>();

		int row = from1Dto2D(toExplore)[0];
		int column = from1Dto2D(toExplore)[1];

		if (row > 0)                adj.add(from2Dto1D(row - 1, column));
		if (row < boardRows - 1)	adj.add(from2Dto1D(row + 1, column));
		if (column > 0)			    adj.add(from2Dto1D(row, column - 1));
		if (column < boardCols - 1) adj.add(from2Dto1D(row, column + 1));
		return adj;
	}

	// transforms coordinates from 1-dimensional coordinate system to
	// 2-dimensional
	private int[] from1Dto2D(int index) {
		int row = index / boardCols;
		int column = index % boardCols;
		if (row == 0) column = index;

		int[] toReturn = {row, column};
		return toReturn;
	}

	private int from2Dto1D(int row, int column) {
		if (row == 0)	return column;
		else			return column + row * boardCols;
	}

	public static void main(String args[]) {
		Main test = new Main();
		System.out.println(test.getUniqueWays());
	}
}

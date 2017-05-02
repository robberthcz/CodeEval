/**
 THE LABYRINTH
 CHALLENGE DESCRIPTION:
 You are given a text with a pseudographical schema of a labyrinth. Walls are marked with asterisk symbols (*). Your job is to write a program that finds the shortest way from the upper entrance to the lower exit and prints out the labyrinth schema containing a path marked with plus symbols (+).

 INPUT SAMPLE:
 A text file with labyrinth schema.
 ************************* *************************
 *                                   * *           *
 * * *** *** ******************* ***** * * * * * ***
 * * *   * *   *   * * *                 * * * *   *
 ***** * * *** * *** * * *** *** * ***** *** *******
 *     * *   * *     *   * * *   *     * * *       *
 *** ******* * ***** *** * * ******* * *** * *** * *
 * *     *     *   *     *     *     * *       * * *
 * * *********** * ********* * ******* * *** * *****
 *     * *   * * *     *     * *   *   * *   *     *
 * ***** * *** * ***** *** *** * * * ******* ***** *
 * *     *   * * *       * * *   * * * *   *     * *
 * * ***** *** *** *** *** * ***** *** *** ***** ***
 *     *   * * *     * *       * *       * *     * *
 * * ***** * * * *** * *** ***** *** ***** *** * * *
 * * *           *   * *   *     *     *     * *   *
 * ******* ******* * *** ******* *** * * ********* *
 *   *       *     * *   *         * * * *     *   *
 *** * * ***** * ***** ******* ******* * * * * * ***
 *     *   *   *         *       * *   * * * * *   *
 *** * *** * *** ***** ******* * * * *** *** * *** *
 * * * * * * * *     * * *     *       *   * * * * *
 * * *** * * * *** *** * * ********* ***** * * * * *
 * * *   * * *     *   * *   *     *   *     * * * *
 * * * *** ******* ***** * ******* *** * *** *** * *
 * * *     *   *   *     * *     * * * *   *   * * *
 * ***** * * * *** * ***** ***** * * * ***** * * * *
 * *     * * * *     * *     *           * * *   * *
 * ***** * *** * ***** *********** ******* * * * * *
 *     * * * *             *   *     * * *   * * * *
 * * * *** * *** * ***** ***** ******* * *** * * * *
 * * *   * * *   *     * *             *     * * * *
 * ***** * * *********** ******* *** * ******* * * *
 * *     *   *   *     * *   *   * * *       * *   *
 * * * ********* * ***** * *** *** *** * ***** * ***
 * * *       *           *   * * *   * *   *   *   *
 * ******* ***** ******* * *** * * *** *** * *******
 *   *   *   *   *   *     *         * * * * * * * *
 * ***** * *** ***** * ******* * ***** * *** * * * *
 *     *           *     *     * * *   *   *     * *
 *** *** ********************* *** *** *** *** * * *
 *   *   *     *               * * *   *       *   *
 *** *** * ***** * ******* *** * * *** * *** ***** *
 *       *       *   *   * * *   *     *   * *   * *
 *** ***** ***** *** *** *** ***** * * *** *** * * *
 *       *   *   * * *       *   * * *   * *   *   *
 *** *** * ***** * ***** *** *** *** *** ******* ***
 *   *     *   *   *     * * * *     * * *     *   *
 * ***** *** ***** ******* * * *** *** * *** ***** *
 *   *                 *           *         *     *
 ************************* *************************

 OUTPUT SAMPLE:
 Print to stdout the labyrinth schema containing the shortest way out marked with ‘+’ symbols:
 *************************+*************************
 *                        +++++++    * *           *
 * * *** *** *******************+***** * * * * * ***
 * * *   * *   *   * * *    +++++        * * * *   *
 ***** * * *** * *** * * ***+*** * ***** *** *******
 *     * *   * *     *   * *+*   *     * * *       *
 *** ******* * ***** *** * *+******* * *** * *** * *
 * *     *     *   *     *  +  *     * *       * * *
 * * *********** * *********+* ******* * *** * *****
 *     * *   * * *     *  +++* *   *   * *   *     *
 * ***** * *** * ***** ***+*** * * * ******* ***** *
 * *     *   * * *       *+* *   * * * *   *     * *
 * * ***** *** *** *** ***+* ***** *** *** ***** ***
 *     *   * * *     * *  +    * *       * *     * *
 * * ***** * * * *** * ***+***** *** ***** *** * * *
 * * *           *   * *+++*     *     *     * *   *
 * ******* ******* * ***+******* *** * * ********* *
 *   *       *     * *+++*         * * * *     *   *
 *** * * ***** * *****+******* ******* * * * * * ***
 *     *   *   *+++++++  *       * *   * * * * *   *
 *** * *** * ***+***** ******* * * * *** *** * *** *
 * * * * * * * *+++  * * *     *       *   * * * * *
 * * *** * * * ***+*** * * ********* ***** * * * * *
 * * *   * * *    +*   * *   *     *   *     * * * *
 * * * *** *******+***** * ******* *** * *** *** * *
 * * *     *   *  +*     * *     * * * *   *   * * *
 * ***** * * * ***+* ***** ***** * * * ***** * * * *
 * *     * * * *+++  * *     *           * * *   * *
 * ***** * *** *+***** *********** ******* * * * * *
 *     * * * *  +++++++++  *   *     * * *   * * * *
 * * * *** * *** * *****+***** ******* * *** * * * *
 * * *   * * *   *     *+*      +++++++*     * * * *
 * ***** * * ***********+*******+*** *+******* * * *
 * *     *   *   *     *+*   *+++* * *+      * *   *
 * * * ********* * *****+* ***+*** ***+* ***** * ***
 * * *       *  +++++++++*   *+* *   *+*   *   *   *
 * ******* *****+******* * ***+* * ***+*** * *******
 *   *   *   *+++*   *     *  +      *+* * * * * * *
 * ***** * ***+***** * *******+* *****+* *** * * * *
 *     *+++++++    *     *    +* * *  +*   *     * *
 *** ***+*********************+*** ***+*** *** * * *
 *   *  +*     *+++++++++++++++* * *  +*       *   *
 *** ***+* *****+* ******* *** * * ***+* *** ***** *
 *  +++++*+++++++*   *   * * *   *  +++*   * *   * *
 ***+*****+***** *** *** *** ***** *+* *** *** * * *
 *  +++++*+  *   * * *       *   * *+*   * *   *   *
 *** ***+*+***** * ***** *** *** ***+*** ******* ***
 *   *  +++*   *   *     * * * *  +++* * *     *   *
 * ***** *** ***** ******* * * ***+*** * *** ***** *
 *   *                 *  +++++++++*         *     *
 *************************+*************************
 CONSTRAINTS:

 The size of a labyrinth is up to 101×101 cells. There can be more than one way to pass the labyrinth, but the shortest way is always unambiguous and never has branches.
 */
package Labyrinth;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	// char representation of the labyrinth
	private char[][] labyrinth;
	// contains indexes for adjacent cells, left, right, down, up
	private byte[][] adj;
	// marks explored cells
	private boolean[][] marked;

	public Main(char[][] labyrinth) {
		this.labyrinth = labyrinth;
		this.marked = new boolean[labyrinth.length][labyrinth[0].length];
		this.adj = new byte[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
		// find the starting position of our path through labyrinth, it is the
		// first ' ' we see, walls are marked '*'
		// starting row is obviously 0
		int startCol;
		for (startCol = 0; startCol < labyrinth[0].length; startCol++) {
			if (labyrinth[0][startCol] == ' ')
				break;
		}
		// queue containing cells, they are effectively ordered by number of
		// moves it took to get to that cell
		LinkedList<Cell> queue = new LinkedList<Cell>();
		// enqueue the starting cell (startCol, 0)
		// every cell is connected to number of moves it took to get to that
		// cell => starting position has 0 moves
		queue.add(new Cell(0, startCol, 0));
		bfs(queue);
		// print the result to console
		for (int i = 0; i < labyrinth.length; i++) {
			for (int j = 0; j < labyrinth[0].length; j++)
				System.out.print(labyrinth[i][j]);
			System.out.println();
		}
	}
	/**
	 * BFS algorithm used to explore labyrinth.
	 * 
	 * @param queue
	 */
	private void bfs(LinkedList<Cell> queue) {
		// while condition => only explore cells whose row is not equal to last
		// row, only the exit cell can be located at the last row
		while (queue.peekFirst().row != labyrinth.length - 1) {
			// remove from the queue the next cell with the least moves
			Cell next = queue.removeFirst();
			// add all adjacent cells to cell next, their number of moves is
			// incremented by 1
			queue.addAll(next.adj());
		}

		// lastCell is the exit from labyrinth
		Cell lastCell = queue.removeFirst();
		// backtrack the shortest path through labyrinth and mark it
		while (lastCell.previous != null) {
			// mark the shortest path
			labyrinth[lastCell.row][lastCell.col] = '+';
			// lastCell is now the cell from which we get to this lastCell
			lastCell = lastCell.previous;
		}
		// mark the starting cell
		labyrinth[lastCell.row][lastCell.col] = '+';
	}

	/**
	 * Represents 1 cell in labyrinth
	 * 
	 * @author Robb
	 *
	 */
	class Cell {
		byte row, col;
		// reference to cell, which was used to get to this cell
		Cell previous;
		// number of moves it took to get here
		int distTo;

		public Cell(int row, int col, int distTo) {
			this.row = (byte) row;
			this.col = (byte) col;
			this.distTo = distTo;
		}

		/**
		 * Returns all the adjacent cells to this cell, it checks whether
		 * adjacent cell is out of bound, wall or it was already marked.
		 * 
		 * @return
		 */
		public LinkedList<Cell> adj() {
			LinkedList<Cell> adjCells = new LinkedList<Cell>();

			for (byte[] rowcol : adj) {
				byte i = rowcol[0], j = rowcol[1];
				// this is not valid adjacent cell => out of bounds
				if (row + i < 0 || col + j < 0 || row + i >= labyrinth.length || col + j >= labyrinth[0].length)
					continue;
				// this cell is already explored or it is a wall
				if (marked[row + i][col + j] || labyrinth[row + i][col + j] == '*')
					continue;
				// the adjacent cell
				Cell cell = new Cell(row + i, col + j, distTo + 1);
				cell.previous = this;

				adjCells.add(cell);
				// mark the adjacent cell as explored
				marked[row + i][col + j] = true;
			}
			return adjCells;
		}
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/Labyrinth/input.txt"));
		String firstLine = textScan.nextLine();
		char[][] labyrinth = new char[firstLine.length()][firstLine.length()];

		labyrinth[0] = firstLine.toCharArray();
		for (int i = 1; i < firstLine.length(); i++)
			labyrinth[i] = textScan.nextLine().toCharArray();

		Main test = new Main(labyrinth);
	}
}

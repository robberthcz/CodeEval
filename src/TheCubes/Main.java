/**
 THE CUBES
 CHALLENGE DESCRIPTION:
 You are given a scheme of the labyrinth in the form of a cube with N levels of N × N elements. The walls are marked with asterisks ‘*’, and the floor — with spaces. The floor of different levels, except the lowest one, can have holes marked with ‘o’. The holes allow you to move to a lower level (if it is on the floor of the current level), to move to a higher level (if it is on the floor of the higher level), or to move over the hole on the current level.
 Below is the example of a labyrinth scheme for N=5:

 Level 1:
 *****
 *   *
 * ***
 *   *
 ** **

 Level 2:
 *****
 *  o*
 *o* *
 * * *
 *****

 Level 3:
 *****
 *   *
 * * *
 *o* *
 *****

 Level 4:
 *****
 * o *
 *** *
 *   *
 *****

 Level 5:
 ** **
 *o  *
 ***o*
 *o  *
 *****
 Find the number of steps in the shortest way from the entrance on the first floor to the exit on the last floor, including entrance and exit. Moving between levels is one step.

 INPUT SAMPLE:
 The first argument is a file that contains test cases, one test case per line. Each test case contains N number (the length of the cube edge) and scheme of the labyrinth, separated by semicolon. The scheme of the labyrinth is serialized: all elements are listed sequentially, starting from the top left corner of the first level.
 For example (the first test case represent the example provided above in challenge description):
 5;******   ** ****   *** ********  o**o* ** * ************   ** * **o*
 ************ o **** **   ******** ***o  ****o**o  ******
 5;****** * ** * **   *** ********o*o** *o**   ************   **o* ** *o
 ************   **** **   ******** *** o ****o**  o******
 7;********     ** *** ** * * ** * * **     **** *********** *   ** ***
 **   *o**o***o**oo   ****************  oo ** * * ** * * ** * * ** *o o
 ****************     ** * ****o* * ** *** **o    ****************     **
 *** **     ****** ** o  o**************** *oo ** *** **o  * ** *** **o
 *********** ****   * ** * * ** * *o** ***o**     ********

 OUTPUT SAMPLE:
 Print to stdout the minimum number of steps for each test case to get through the labyrinth. If it is impossible to pass the labyrinth — print 0.
 For example:
 15
 0
 39

 CONSTRAINTS:
 Number of test cases is 20.
 The length of the cube edge is from 5 to 11.
 Moving between levels is 1 step.
 There can more than one shortest way.
 */
package TheCubes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	private boolean[][][] cubes;
	private boolean[][][] changeLevel;
	private byte[][] adj;
	private boolean[][][] marked;
	private int N;
	private int exitRow, exitCol;

	public Main(int N, boolean[][][] cubes, boolean[][][] changeLevel) {
		this.cubes = cubes;
		this.changeLevel = changeLevel;
		this.N = N;

		marked = new boolean[cubes.length][cubes[0].length][cubes[0][0].length];
		adj = new byte[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
		LinkedList<Cell> queue = new LinkedList<Cell>();

		int startCol = 0, startRow = 0;

		for (int i = 0; i < N; i++) {
			if (cubes[0][i][0]) {
				exitRow = 0;
				exitCol = i;
			} else if (cubes[N - 1][i][0]) {
				exitRow = N - 1;
				exitCol = i;
			} else if (cubes[i][0][0]) {
				exitRow = i;
				exitCol = 0;
			} else if (cubes[i][N - 1][0]) {
				exitRow = i;
				exitCol = N - 1;
			}
			if (cubes[0][i][N - 1]) {
				startRow = 0;
				startCol = i;

			} else if (cubes[N - 1][i][N - 1]) {
				startRow = N - 1;
				startCol = i;
			} else if (cubes[i][0][N - 1]) {
				startRow = i;
				startCol = 0;
			} else if (cubes[i][N - 1][N - 1]) {
				startRow = i;
				startCol = N - 1;
			}
		}
		queue.add(new Cell(startRow, startCol, N - 1, 1));
		System.out.println(bfs(queue));
	}

	private int bfs(LinkedList<Cell> queue) {
		while (queue.size() > 0 && !isExit(queue.peekFirst())) {
			Cell next = queue.removeFirst();
			queue.addAll(next.adj());
		}
		if (queue.size() == 0)	return 0;
		else          			return queue.peekFirst().distTo;
	}

	private boolean isExit(Cell cell) {
		return cell.row == exitRow && cell.col == exitCol && cell.level == 0;
	}

	class Cell {
		byte row, col, level;
		Cell previous;
		int distTo;

		public Cell(int row, int col, int level, int distTo) {
			this.row = (byte) row;
			this.col = (byte) col;
			this.level = (byte) level;
			this.distTo = distTo;
		}

		public LinkedList<Cell> adj() {
			LinkedList<Cell> adjCells = new LinkedList<Cell>();

			if (changeLevel[row][col][level] && !marked[row][col][level - 1]) {
				adjCells.add(new Cell(row, col, level - 1, distTo + 1));
				marked[row][col][level - 1] = true;
			}
			if (level < N - 2 && changeLevel[row][col][level + 1]
					&& !marked[row][col][level + 1]) {
				adjCells.add(new Cell(row, col, level + 1, distTo + 1));
				marked[row][col][level + 1] = true;
			}
			for (byte[] rowcol : adj) {
				byte i = rowcol[0], j = rowcol[1];
				if (!isValid(i, j))
					continue;
				Cell cell = new Cell(row + i, col + j, level, distTo + 1);
				cell.previous = this;

				adjCells.add(cell);

				marked[row + i][col + j][level] = true;
			}
			return adjCells;
		}
		/**
		 * Returns true if the cell with a offset of (i,j) with regard to this
		 * cell is valid and we have not been there
		 * @param i
		 *            row offset
		 * @param j
		 *            column offset
		 * @return
		 */
		private boolean isValid(int i, int j) {
			if ((row + i < 0 || col + j < 0 || row + i >= N || col + j >= N)||
			   (marked[row + i][col + j][level]	|| !cubes[row + i][col + j][level]))	return false;
			return true;
		}
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/TheCubes/input.txt"));

		while (textScan.hasNextLine()) {
			String line[] = textScan.nextLine().split(";");
			int N = Integer.parseInt(line[0]);

			boolean[][][] cubes = new boolean[N][N][N];
			boolean[][][] changeLevel = new boolean[N][N][N];
			int id = 0;

			for (int d = 0; d < N; d++) {
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (line[1].charAt(id) == ' ')
							cubes[i][j][d] = true;
						if (line[1].charAt(id) == 'o') {
							cubes[i][j][d] = true;
							changeLevel[i][j][d] = true;
						}
						id++;
						// System.out.print(id+" ");
					}
				}
			}

			Main test = new Main(N, cubes, changeLevel);
		}
	}
}
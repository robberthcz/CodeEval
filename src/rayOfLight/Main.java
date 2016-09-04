/**
 Ray of Light
 Challenge Description:
 You are given a plan of a room with 10×10 cells size. The walls in the room are marked with a number sign ‘#’. They are specular and they reflect light. If a ray hits a corner of the room, its distribution stops:
 -------    -------
 -#####-    -#####-
 -  /\#-    -   /#-
 - / /#-    -  / #-
 -/ / #-    - /  #-
 /------    -/-----

 There can be columns and prisms in the room at a distance at least 1 cell from the walls.
 The columns are marked with the ‘o’ symbol. They absorb light. If a ray hits a column, its distribution stops:
 -------
 -     -
 -  o  -
 - /   -
 -/    -
 /------

 The prisms are marked with an asterisk ‘*’. They split a ray into three parts. One of them goes in the same direction as the original ray, two others are turned by ±90°:
 ------
 - \ /-
 -  * -
 - / \-
 -/   -
 /-----

 There is a hole in one of the walls. A ray of light is let into the room through the hole at an angle aliquot to 45° to the walls:
 ##########
 #        #
 #  o  o  #
 #    o o #
 # o   *o #
 # o o    #
 # * * *o #
 #        #
 #        #
 ###/######

 Show the path of light distribution using pseudo-graphics. Use slash ‘/’ to show the fragment turned to 45° or 225°, backslash ‘\’ to show the fragment turned to 315° or 135°, and ‘X’ symbol to show the fragment where two rays are crossed:
 ##########
 #\   /\  #
 # \o/ o\ #
 #  X o o\#
 # o \ *o/#
 # o o\ / #
 # * * *o #
 #    / \ #
 #   /   \#
 ###/######

 The maximum distance of light distribution is 20 cells, including the first and the last cells, but excluding the cells with prisms.

 Input sample:
 The first argument is a file with test cases. Each line contains serialized plan of a room, starting from the upper-left cell.
 For example:
 ###########        ##  o  o  ##    o o ## o   *o ## o o    ## * * *o ## ##        ####/######
 ###########        ##    * o ##  o     #/    o   ## o *    ##        ## ##        ###########

 Output sample:
 Show the path of light distribution on the plan, using pseudo-graphics. Print the result to stdout in a serialized way similarly to the input.
 For example:
 ###########\   /\  ## \o/ o\ ##  X o o\## o \ *o/## o o\ / ## * * *o ## / \ ##   /     \####/######
 ###########  /\ /\ ## /  * o\##/ o/ \ /#/  / o X ## o * / \##/ \ /  /##\ X  / ## \/ \/  ###########

 Constraints:
 1.A room size is 10×10 cells.
 2.The maximum distance of light distribution is 20 cells, including the first and the last cells, but excluding the cells with prisms.
 3.There are 40 test cases in the input.
 */
package rayOfLight;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * This was fucking tough. Some serious debugging needed even though the core of the algorithm is elegant and can handle 80% of test cases with ease, there were some test cases with multiple prisms that bothered the algorithm.
 *
 * Main warnings:
 * The total length of any ray is 20. If ray hits prism, than all three split rays extend the original ray, so their length must up to 20 minus the length of the original ray before splitting.
 * Another warning. Sometimes (like 5 test cases) prisms can be hit by more than one ray of different lengths.
 */
public class Main {
	// size of the room is 10x10
	private char[][] room;
	// the longest found split ray coming out of the prism located at given row and col
	private int[][] prismLength;
	private int MAX_REY_LENGTH = 20;

	public Main(char[][] room) {
		this.room = room;
		this.prismLength = new int[room.length][room.length];
		//printRoom();
		char light = ' ';
		boolean up = false;
		int i = 0, j = 0;

		outerFor : for (i = 0; i < room.length; i++) {
			for (j = 0; j < room[0].length; j++) {
				if (room[i][j] == '/' || room[i][j] == '\\') {
					light = room[i][j];
					room[i][j] = ' ';
					if ((light == '\\' && (i == 0 || j == 0)) || (light == '/' && (i == 0 || j == 9))) {
						up = false;
					} else
						up = true;
					break outerFor;
				}
			}
		}
		//System.out.println(up + " " + i + " " + j + " " + light);
		propagate(i, j, light, up, MAX_REY_LENGTH);
		//printRoom();
		printRoomCodeeval();

	}
	private void propagate( int row, int col, char light, boolean up, int length) {
		if (row > 9 || col > 9 || row < 0 || col < 0 || room[row][col] == 'o' || isCorner(row, col)
				|| length <= 0)
			return;
		int nextRow, nextCol;
		nextRow = up ? (row - 1) : (row + 1);

		if (light == '/') 	nextCol = up ? (col + 1) : (col - 1);
		else 				nextCol = up ? (col - 1) : (col + 1);
		// write the ray
		if (room[row][col] == ' ') {
			room[row][col] = light;
			propagate(nextRow, nextCol, light, up, length - 1);
		}
		// backtrack with a longer split ray from prism
		else if(room[row][col] == light || room[row][col] == 'X'){
			propagate(nextRow, nextCol, light, up, length - 1);
		}
		// hit prism
		else if (room[row][col] == '*') {
			// explored this with longer ray, no need to revisit
			// also to prevent StackOverflowException due to way we handle prisms
			if(length <= prismLength[row][col]) return;
			prismLength[row][col] = length;
			// sometimes a given prism can be hit by more than one ray (with different length) and hence can produce split rays of different lengths
			// we make sure that every such solution is recorded, especially the longest one
			if (room[row - 1][col - 1] == '*' && length > prismLength[row - 1][col - 1]) propagate(row - 1, col - 1, '\\', true, length);
			if (room[row - 1][col + 1] == '*' && length > prismLength[row - 1][col + 1]) propagate(row - 1, col + 1, '/', true, length);
			if (room[row + 1][col - 1] == '*' && length > prismLength[row + 1][col - 1]) propagate(row + 1, col - 1, '/', false, length);
			if (room[row + 1][col + 1] == '*' && length > prismLength[row + 1][col + 1]) propagate(row + 1, col + 1, '\\', false, length);

			// might lead to StackOverflowException, since we split rays in all four directions,
			// one of them being the original direction how got to the prism
			// this approach decreases significantly the complexity and number of input arguments to this function
			// we would have to remember the direction how we got to the current row and column
			if (room[row - 1][col - 1] != '*') propagate(row - 1, col - 1, '\\', true, length);
			if (room[row - 1][col + 1] != '*') propagate(row - 1, col + 1, '/', true, length);
			if (room[row + 1][col - 1] != '*') propagate(row + 1, col - 1, '/', false, length);
			if (room[row + 1][col + 1] != '*') propagate(row + 1, col + 1, '\\', false, length);
		}
		// hit the wall, comeback
		else if (room[row][col] == '#') {
			if (row == 0 || row == 9) {
				up = !up;
				nextRow = (row == 0) ? (row + 1) : (row - 1);
				propagate(nextRow, col, oppositeSlash(light), up, length);
			} else {
				nextCol = (col == 0) ? (col + 1) : (col - 1);
				propagate(row, nextCol, oppositeSlash(light), up, length);
			}
		}
		// rays intersected
		else if (room[row][col] == oppositeSlash(light)) {
			room[row][col] = 'X';
			propagate(nextRow, nextCol, light, up, length - 1);
		}
	}

	private boolean isCorner(int row, int col) {
		return (row == 0 || row == 9) && (col == 0 || col == 9);
	}

	/**
	 * Print the room to StdOut in an understandable way
	 */
	public void printRoom() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.print(room[i][j]);
			System.out.println();
		}
	}

	/**
	 * Print the room to StdOut, the solution is in the form as needed for CodeEval submitting
	 */
	public void printRoomCodeeval() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.print(room[i][j]);
		}
		System.out.println();
	}


	private char oppositeSlash(char slash) {
		if (slash == '/') return '\\';
		else return '/';
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/rayOfLight/input.txt"));

		while (textScan.hasNextLine()) {
			char[] seq = textScan.nextLine().toCharArray();
			char[][] room = new char[10][10];
			int id = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++)
					room[i][j] = seq[id++];
			}
			Main test = new Main(room);
			// test.printRoom();
		}
	}
}

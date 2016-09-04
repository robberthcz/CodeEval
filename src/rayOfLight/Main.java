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

public class Main {
	// size of the room is 10x10
	private char[][] room;

	public Main(char[][] room) {
		this.room = room;
		printRoom();
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
		propagate(i, j, light, up);
		printRoom();
		//printRoomCodeeval();

	}
	private void propagate(int row, int col, char light, boolean up) {
		if (room[row][col] == 'o' || isCorner(row, col)
				|| room[row][col] == 'X' || room[row][col] == light)
			return;

		int nextRow, nextCol;
		nextRow = up ? (row - 1) : (row + 1);

		if (light == '/') {
			nextCol = up ? (col + 1) : (col - 1);
		} else {
			nextCol = up ? (col - 1) : (col + 1);
		}

		if (room[row][col] == ' ') {
			room[row][col] = light;
			propagate(nextRow, nextCol, light, up);
		}
		else if (room[row][col] == '*') {
			if (room[row - 1][col - 1] != '*')
				propagate(row - 1, col - 1, '\\', true);
			if (room[row - 1][col + 1] != '*')
				propagate(row - 1, col + 1, '/', true);
			if (room[row + 1][col - 1] != '*')
				propagate(row + 1, col - 1, '/', false);
			if (room[row + 1][col + 1] != '*')
				propagate(row + 1, col + 1, '\\', false);
		}
		else if (room[row][col] == '#') {
			if (row == 0 || row == 9) {
				up = !up;
				nextRow = (row == 0) ? (row + 1) : (row - 1);
				propagate(nextRow, col, oppositeSlash(light), up);
			} else {
				nextCol = (col == 0) ? (col + 1) : (col - 1);
				propagate(row, nextCol, oppositeSlash(light), up);

			}
		} else if (room[row][col] == oppositeSlash(light)) {
			room[row][col] = 'X';
			propagate(nextRow, nextCol, light, up);
		}

	}
	private boolean isCorner(int row, int col) {
		return (row == 0 || row == 9) && (col == 0 || col == 9);
	}

	public void printRoom() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++)
				System.out.print(room[i][j]);
			System.out.println();
		}
	}

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

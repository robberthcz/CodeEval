package RayOfLight;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	// size of the room is 10x10
	private char[][] room;
	

	public Main(char[][] room) {

		this.room = room;

		// printRoom();
		char light = ' ';
		boolean up = false;
		int i = 0, j = 0;

		outerFor : for (i = 0; i < room.length; i++) {
			for (j = 0; j < room[0].length; j++) {

				if (room[i][j] == '/' || room[i][j] == '\\') {
					light = room[i][j];
					room[i][j] = ' ';

					if ((light == '\\' && (i == 0 || j == 0))
							|| (light == '/' && (i == 0 || j == 9))) {
						up = false;

					} else
						up = true;
					break outerFor;

				}
			}
		}
		System.out.println(up + " " + i + " " + j + " " + light);
		propagate(i, j, light, up);
		printRoom();

	}
	private void propagate(int row, int col, char light, boolean up) {
		if (room[row][col] == 'o' || isCorner(row, col)
				|| room[row][col] == 'X' || room[row][col] == light)
			return;

		int nextRow, nextCol;

		if (up)
			nextRow = row - 1;
		else
			nextRow = row + 1;

		if (light == '/') {
			if (up)
				nextCol = col + 1;
			else
				nextCol = col - 1;
		} else {
			if (up)
				nextCol = col - 1;
			else
				nextCol = col + 1;
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
				if (row == 0)
					nextRow = row + 1;
				else
					nextRow = row - 1;
				propagate(nextRow, col, oppositeSlash(light), up);

			} else {
				if (col == 0)
					nextCol = col + 1;
				else
					nextCol = col - 1;
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

	private char oppositeSlash(char slash) {

		if (slash == '/')
			return '\\';
		else
			return '/';
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("test-cases/rayOfLight.txt"));

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

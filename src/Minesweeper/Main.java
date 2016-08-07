package Minesweeper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * 
 * Challenge Description:
 * 
 * You will be given an M*N matrix. Each item in this matrix is either a '*' or
 * a '.'. A '*' indicates a mine whereas a '.' does not. The objective of the
 * challenge is to output a M*N matrix where each element contains a number
 * (except the positions which actually contain a mine which will remain as '*')
 * which indicates the number of mines adjacent to it. Notice that each position
 * has at most 8 adjacent positions e.g. left, top left, top, top right, right,
 * ...
 * 
 * Input sample:
 * 
 * Your program should accept as its first argument a path to a filename. Each
 * line in this file contains M,N, a semicolon and the M*N matrix in row major
 * form. E.g. 3,5;**.........*... 4,4;*........*......
 * 
 * Output sample:
 * 
 * Print out the new M*N matrix (in row major form) with each position(except
 * the ones with the mines) indicating how many adjacent mines are there. E.g.
 * 100332001*100 10022101*101110
 * 
 * @author Robb
 *
 */
public class Main {
	private boolean[][] matrix;

	public Main(boolean[][] matrix) {

		// true signifies a mine
		// false means no mine
		this.matrix = matrix;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {

				if (matrix[i][j])
					System.out.print('*');
				else
					System.out.print(adjacentMines(i, j));

			}
		}
		System.out.println();

	}

	// the number of adjacent cells which have mines in them
	private int adjacentMines(int row, int col) {
		int adjacentMines = 0;

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {

				// we don't want to explore the starting position
				if (i == 0 && j == 0)
					continue;

				int adjRow = row + i, adjCol = col + j;

				// adjacent position is invalid
				if (adjRow < 0 || adjCol < 0)
					continue;
				if (adjRow == matrix.length || adjCol == matrix[0].length)
					continue;

				// mine is in this position, incremented the value to be
				// returned
				if (matrix[adjRow][adjCol])
					adjacentMines++;

			}
		}
		return adjacentMines;

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("minesweeper.txt"));

		while (textScan.hasNextLine()) {
			String line[] = textScan.nextLine().split(";");

			int rows = Integer.parseInt(line[0].split(",")[0]);
			int cols = Integer.parseInt(line[0].split(",")[1]);

			boolean[][] matrix = new boolean[rows][cols];

			int id = 0;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if (line[1].charAt(id++) == '*')
						matrix[i][j] = true;
					else
						matrix[i][j] = false;
				}
			}

			Main test = new Main(matrix);
		}

	}

}

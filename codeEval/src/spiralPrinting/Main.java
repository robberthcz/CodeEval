/**
 CHALLENGE DESCRIPTION:
 Write a program to print a 2D array (n x m) in spiral order (clockwise)

 INPUT SAMPLE:
 Your program should accept as its first argument a path to a filename. The input file contains several lines. Each line is one test case. Each line contains three items (semicolon delimited). The first is 'n'(rows), the second is 'm'(columns) and the third is a single space separated list of characters/numbers in row major order. E.g.
 3;3;1 2 3 4 5 6 7 8 9

 OUTPUT SAMPLE:
 Print out the matrix in clockwise fashion, one per line, space delimited. E.g.
 1 2 3 6 9 8 7 4 5
 */
package SpiralPrinting;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
	private int nCols;
	private String[][] matrix;

    /**
	 * 
	 * @param nCols
	 *            matrix has this number of columns
	 * @param toPrint
	 *            matrix in string representation
	 */
	public Main(int nCols, String[] toPrint) {
		this.nCols = nCols;

		matrix = new String[toPrint.length / nCols][nCols];

		// transform the string representation to string-array representation of
		// the
		// matrix
		for (int i = 0; i < toPrint.length; i++) {
			int[] id = from1Dto2D(i);
			matrix[id[0]][id[1]] = toPrint[i];
		}

		printSpiralOrder(matrix);
	}
	/**
	 * Prints the matrix in spiral order.
	 * 
	 * @param toPrint
	 */
	public void printSpiralOrder(String[][] toPrint) {
		int startRow = 0, startCol = 0, endRow = toPrint.length - 1, endCol = nCols - 1;

		// total number of rows and columns
		int rows = toPrint.length, cols = toPrint[0].length;

		// spiral order is represented as single vector named output
		String[] output = new String[rows * cols];

		// identifies the index to which element of our matrix is assigned to in
		// output vector
		int id = 0;

		// spiral order => from the matrix you shave off top row, then right
		// most column, then bottom row, then left most column and continue the
		// with the remaining matrix

		// top row is indexed by startRow
		// right most column is indexed by endRow
		// bottom row is indexed by endRow
		// left most column is indexed by startCol
		while (startCol <= endCol && startRow <= endRow) {

			// save the top row
			for (int i = startCol; i <= endCol; i++)
				output[id++] = toPrint[startRow][i];

			// save the right most column
			for (int i = startRow + 1; i < endRow; i++)
				output[id++] = toPrint[i][endCol];

			// save the bottom row
			// first if condition => if true then we are done
			if (startRow != endRow) {
				for (int i = endCol; i >= startCol; i--)
					output[id++] = toPrint[endRow][i];
			}

			// save the left most column
			// first if condition => if true then we are done
			if (startCol != endCol) {
				for (int i = endRow - 1; i > startRow; i--)
					output[id++] = toPrint[i][startCol];
			}

			// continue with matrix that has shaved off top row, bottom row,
			// left most column and right most column
			startCol++;
			endCol--;
			startRow++;
			endRow--;
		}

		// print the result
		for (int i = 0; i < output.length; i++) {
			System.out.print(output[i]);
			// don't print space at the end
			if (i != output.length - 1)
				System.out.print(" ");
		}
		System.out.println();
	}
	/**
	 * Transforms indices from 1-dimensional system of coordinates to
	 * 2-dimensional
	 * 
	 * @param index
	 * @return array of coordinates {row, column}
	 */
	private int[] from1Dto2D(int index) {
		int row = index / nCols;
		int column = index % nCols;
		if (row == 0)
			column = index;

		int[] toReturn = {row, column};
		return toReturn;
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/SpiralPrinting/input.txt"));

		while (textScan.hasNextLine()) {
			String words[] = textScan.nextLine().split(";");
			// m is the number of columns
			int m = Integer.parseInt(words[1]);

			String matrix[] = words[2].split(" ");

			Main test = new Main(m, matrix);
		}
	}
}

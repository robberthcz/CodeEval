package LargestSubmatrix;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Challenge Description:
 * 
 * You have the matrix of positive and negative integers. Find a sub-matrix with
 * the largest sum of its elements. In this case sub-matrix means a continuous
 * rectangular part of the given matrix. There is no limitation for sub-matrix
 * dimensions. It only has to be rectangular.
 * 
 * Input sample:
 * 
 * Your program should accept as its first argument a path to a filename. Read
 * the matrix from this file. Example of the matrix is the following:
 * 
 * -1 -4 -5 -4 -5 8 -1 3 -2 1 3 2 1 5 6 -9
 * 
 * After some calculations you may find that the sub-matrix with the largest sum
 * of the input is: 8 -1 1 3 5 6
 * 
 * Output sample:
 * 
 * Print out the sum of elements for the largest sub-matrix. For the given input
 * it is: 22
 * 
 * Costraints: 1.Each element in the matrix is in range [-100, 100]. 2.Input
 * matrix has an equal number of rows and columns. 3.There are up to 20 rows and
 * columns in the input file.
 * 
 * 
 * The implementation is extended Kadane's algorithm with complexity of
 * O(n*n*n), it is DP algorithm.
 * 
 * @author Robb
 *
 */
public class Main {

	public Main() {

	}

	public static void largestSubMatrix(int[][] matrix) {

		// for every submatrix matrix[a...b][], where a and b are row numbers,
		// we need
		// to calculate the cumulative sum of the rows between a and b
		// including.
		// cumSum method reduces this problem to eventually subtracting two
		// numbers
		cumSum(matrix);

		// eventually sum of the largest sub-matrix of matrix[][]
		int totalMax = Integer.MIN_VALUE;

		// largest submatrix for matrix[a...b][]
		for (int a = 0; a < matrix.length; a++) {
			for (int b = a; b < matrix.length; b++) {

				// sum of largest submatrix of matrix[a...b][]
				int maxHere;

				// set-up starting value for maxHere
				if (a == b)
					maxHere = matrix[a][0];
				// a and b are different, but a is the starting row, so row b is
				// already sum of all the rows above it and itself
				else if (a == 0)
					maxHere = matrix[b][0];
				// row b is also sum of rows, which are before row a => we only
				// need cumulative sum for matrix[a...b][0], but we have
				// matrix[0...b][0]
				else
					maxHere = matrix[b][0] - matrix[a - 1][0];

				// here we finally apply Kadane's algo => we have
				// matrix[a...b][] and we need to find submatrix of the form
				// matrix[a...b][x...y] that has the largest sum
				for (int c = 1; c < matrix[0].length; c++) {

					//col now holds sum of matrix[a...b][c]
					int col;

					if (a == b)
						col = matrix[a][c];
					else if (a == 0)
						col = matrix[b][c];
					else
						col = matrix[b][c] - matrix[a - 1][c];

					// hard to explain, but the idea can be found on wikipedia
					// under Kadane's algorithm
					maxHere = Math.max(col, maxHere + col);
					
					// we are done with matrix[a...b][x...y], is this the
					// largest sum?
					totalMax = Math.max(totalMax, maxHere);

				}

			}
		}

		System.out.println(totalMax);

	}
	// calculates the cumulative sum of the given matrix, row of a
	// matrix is a sum of itself and a row above, applied recursively => last
	// row is the sum of itself and all the rows above it, O(n*n).
	private static void cumSum(int[][] matrix) {

		if (matrix.length == 1)
			return;

		for (int row = 1; row < matrix.length; row++) {
			for (int col = 0; col < matrix[0].length; col++)
				matrix[row][col] += matrix[row - 1][col];
		}
	}
	
	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/largestSubmatrix.txt"));

		String firstLine = textScan.nextLine();

		String[] firstRow = firstLine.split(" ");

		int[][] matrix = new int[firstRow.length][firstRow.length];

		for (int i = 0; i < firstRow.length; i++)
			matrix[0][i] = Integer.parseInt(firstRow[i]);

		firstLine = null;
		firstRow = null;

		int id = 1;
		while (textScan.hasNextLine()) {

			String[] nums = textScan.nextLine().split(" ");

			for (int i = 0; i < nums.length; i++)
				matrix[id][i] = Integer.parseInt(nums[i]);

			id++;
		}

		Main.largestSubMatrix(matrix);
		// the right answer is 22

	}

}

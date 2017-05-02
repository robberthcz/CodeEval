/**
 MINIMUM PATH SUM
 CHALLENGE DESCRIPTION:
 You are given an n*n matrix of integers. You can move only right and down. Calculate the minimal path sum from the top left to the bottom right

 INPUT SAMPLE:
 Your program should accept as its first argument a path to a filename. The first line will have the value of n(the size of the square matrix). This will be followed by n rows of the matrix. (Integers in these rows will be comma delimited). After the n rows, the pattern repeats. E.g.
 2
 4,6
 2,8
 3
 1,2,3
 4,5,6
 7,8,9

 OUTPUT SAMPLE:
 Print out the minimum path sum for each matrix. E.g.
 14
 21
 */
package MinimumPathSum;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public Main(int[][] matrix) {
		// the matrix is basically a rooted DAG, where weights are on vertices
		// => in this case matrix values

		// we travel from left top corner
		// to right bottom corner
		// we can move only right or down

		// we look sequentially at every position => check what is the least
		// costly way to get there => either from left or up
		// then rewrite the cost for current position, continue to next position
		// to right
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				// should skip starting position
				if (i == 0 && j == 0) continue;

				// cost of travelling from left and from up
				int fromLeft = Integer.MAX_VALUE, fromUp = Integer.MAX_VALUE;

				// cost of traveling to (i,j) from left
				if (j > 0) fromLeft = matrix[i][j - 1];
				if (i > 0) fromUp =  matrix[i - 1][j];

				// min cost to get to (i,j)
				matrix[i][j] += Math.min(fromLeft, fromUp);
			}
		}
		// print out the cost to get to right bottom
		System.out.println(matrix[matrix.length - 1][matrix.length - 1]);
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/MinimumPathSum/input.txt"));

		while (textScan.hasNextLine()) {
			String[] line = textScan.nextLine().split(",");

			if (line.length == 1) {
				int dim = Integer.parseInt(line[0]);
				int[][] matrix = new int[dim][dim];
                // parse the matrix
				for (int i = 0; i < dim; i++) {
                    matrix[i] = Arrays.stream(textScan.nextLine().split(",")).mapToInt(Integer::parseInt).toArray();
				}
				Main test = new Main(matrix);
			}
		}
	}
}

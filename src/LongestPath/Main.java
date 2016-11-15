/**
 LONGEST PATH
 CHALLENGE DESCRIPTION:
 You are given a 2D N×N matrix. Each element of the matrix is a letter: from ‘a’ to ‘z’. Your task is to find the length L of the longest path in which not a single letter is repeated. The path can start at any cell, the transfer to the next element can be vertical or horizontal.
 Example of a 5×5 matrix, where L=15:

 INPUT SAMPLE:
 The first argument is a file with test cases. Each line contains a serialized N×N matrix .
 For example:
 qttiwkajeerhdgpikkeaaabwl
 vavprkykiloeizzt
 skwajgaaxqpfcxmadpwaraksnkbgcaukbgli
 kaja
 bjzanjikh

 OUTPUT SAMPLE:
 Print to stdout the length of the longest path of unique elements for each test case, one per line.
 For example:
 15
 11
 16
 3
 7

 CONSTRAINTS:
 N is in a range from 2 to 6.
 The number of test cases is 20.
 */
package LongestPath;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	private char[] board;
	private int boardRows, boardCols;
	private int longest;

	public Main(String board) {
		boardRows = (int) Math.sqrt(board.length());
		boardCols = boardRows;
		longest = 0;
		this.board = board.toCharArray();
	}

	public int longestPath() {
		HashSet<Character> set = new HashSet<Character>();

		for (int i = 0; i < board.length; i++) {
			boolean[] marked = new boolean[board.length];
			dfs(set, i, 0, marked);
		}

		return longest;
	}

	private void dfs(HashSet<Character> set, int toExplore, int strID,	boolean[] marked) {
		if (set.contains(board[toExplore]))	return;
		else {
			set.add(board[toExplore]);
			if (set.size() > longest)
				longest = set.size();
		}

		marked[toExplore] = true;

		for (int j : adj(toExplore)) {
			if (!marked[j])
				dfs(set, j, strID + 1, marked);
		}

        // backtrack
        set.remove(board[toExplore]);
		marked[toExplore] = false;
	}

	private Iterable<Integer> adj(int toExplore) {
		LinkedList<Integer> adj = new LinkedList<Integer>();

		int row = from1Dto2D(toExplore)[0];
		int column = from1Dto2D(toExplore)[1];

		if (row > 0)                adj.add(from2Dto1D(row - 1, column));
		if (row < boardRows - 1)    adj.add(from2Dto1D(row + 1, column));
		if (column > 0)			    adj.add(from2Dto1D(row, column - 1));
		if (column < boardCols - 1)	adj.add(from2Dto1D(row, column + 1));

		return adj;
	}

	private int[] from1Dto2D(int index) {
		int row = index / boardCols;
		int column = index % boardCols;
		if (row == 0) column = index;

		int[] toReturn = {row, column};
		return toReturn;
	}

	private int from2Dto1D(int row, int column) {
		if (row == 0) return column;
		else          return column + row * boardCols;
	}

	public static void main(String args[]) throws IOException {
		BufferedReader textScan = new BufferedReader(new FileReader("src/LongestPath/input.txt"));

		String line;
		while ((line = textScan.readLine()) != null) {
			// String board = textScan.nextLine();
			Main test = new Main(line);
			System.out.println(test.longestPath());
		}
	}
}
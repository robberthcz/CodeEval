package WordSearch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Challenge Description:
 * 
 * Given a 2D board and a word, find if the word exists in the grid. The word
 * can be constructed from letters of sequentially adjacent cell, where adjacent
 * cells are those horizontally or vertically neighboring. The same letter cell
 * may not be used more than once.
 * 
 * Input sample:
 * 
 * The board to be used may be hard coded as: [ [ABCE], [SFCS], [ADEE] ]
 * 
 * Your program should accept as its first argument a path to a filename. Each
 * line in this file contains a word. E.g. ASADB
 * 
 * ABCCED
 * 
 * ABCF
 * 
 * 
 * Output sample:
 * 
 * Print out True if the word exists in the board, False otherwise. E.g.*
 * 
 * False
 * 
 * True
 * 
 * False
 * 
 * 
 * 
 * @author Robb
 *
 */
public class Main {

	private boolean found;
	private char[] board;
	private int boardRows, boardCols;

	public Main() {
		boardRows = 3;
		boardCols = 4;

		board = new char[]{'A', 'B', 'C', 'E', 'S', 'F', 'C', 'S', 'A', 'D',
				'E', 'E'};

	}

	public boolean boardContains(String S) {
		found = false;
		if (S.length() == 0)
			return true;

		for (int i = 0; i < board.length && !found; i++) {
			boolean[] marked = new boolean[board.length];
			dfs(S, i, 0, marked);
		}

		return found;

	}

	private void dfs(String S, int toExplore, int strID, boolean[] marked) {

		if (found || S.charAt(strID) != board[toExplore])
			return;
		else if (strID == S.length() - 1) {
			found = true;
			return;
		}
		marked[toExplore] = true;

		for (int j : adj(toExplore)) {
			if (!marked[j])
				dfs(S, j, strID + 1, marked);
		}
		marked[toExplore] = false;

	}

	private Iterable<Integer> adj(int toExplore) {
		LinkedList<Integer> adj = new LinkedList<Integer>();

		int row = from1Dto2D(toExplore)[0];
		int column = from1Dto2D(toExplore)[1];

		if (row > 0)
			adj.add(from2Dto1D(row - 1, column));
		if (row < boardRows - 1)
			adj.add(from2Dto1D(row + 1, column));
		if (column > 0)
			adj.add(from2Dto1D(row, column - 1));
		if (column < boardCols - 1)
			adj.add(from2Dto1D(row, column + 1));

		return adj;
	}

	private int[] from1Dto2D(int index) {
		int row = index / boardCols;
		int column = index % boardCols;
		if (row == 0)
			column = index;

		int[] toReturn = {row, column};
		return toReturn;
	}

	private int from2Dto1D(int row, int column) {

		if (row == 0)
			return column;
		else
			return column + row * boardCols;
	}

	public static void main(String args[]) throws FileNotFoundException {
		Main bs = new Main();

		Scanner wordScan = new Scanner(new FileReader("boardSearch.txt"));

		while (wordScan.hasNextLine()) {
			String word = wordScan.nextLine();

			if (bs.boardContains(word))
				System.out.println("True");
			else
				System.out.println("False");
		}

	}

}

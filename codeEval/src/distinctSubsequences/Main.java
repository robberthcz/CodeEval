/**
 DISTINCT SUBSEQUENCES
 CHALLENGE DESCRIPTION:
 A subsequence of a given sequence S consists of S with zero or more elements deleted. Formally, a sequence Z = z1z2..zk is a subsequence of X = x1x2...xm, if there exists a strictly increasing sequence of indicies of X such that for all j=1,2,...k we have Xij = Zj. E.g. Z=bcdb is a subsequence of X=abcbdab with corresponding index sequence <2,3,5,7>

 INPUT SAMPLE:
 Your program should accept as its first argument a path to a filename. Each line in this file contains two comma separated strings. The first is the sequence X and the second is the subsequence Z. E.g.
 babgbag,bag
 rabbbit,rabbit

 OUTPUT SAMPLE:
 Print out the number of distinct occurrences of Z in X as a subsequence E.g.
 5
 3

 */
package DistinctSubsequences;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
public class Main {
	private int count;

	public Main() {
		count = 0;
	}
	
	// typical LCS algorithm using dynamic programming
	public int lcs(String S, String T) {
		int n = S.length(), m = T.length();
		int[][] DP = new int[n + 1][m + 1];
		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (S.charAt(i - 1) == T.charAt(j - 1)) DP[i][j] = DP[i - 1][j - 1] + 1;
				else if (DP[i - 1][j] >= DP[i][j - 1])  DP[i][j] = DP[i - 1][j];
				else                    				DP[i][j] = DP[i][j - 1];
			}
		}
		reconstructLCS(DP, n, m, "", S, T);
		return count;
	}

	// due to memory reasons we did not use array to store the progression of DP
	// algorithm, it is easy to reconstruct the solution by assuming where the
	// previous algorithm could go "from" => the number of possible ways how to
	// reconstruct the solution is the number we want
	private void reconstructLCS(int[][] DP, int i, int j, String str, String S,
			String T) {
		String copy = str.substring(0, str.length());

		//reach the end of table, success or not?
		if ((i == 0 || j == 0) && copy.equals(T)) {
			count++;
			return;
		} else if (i == 0 || j == 0)
			return;

		//3 possible ways where to go
		if (S.charAt(i - 1) == T.charAt(j - 1)) {
			// we are going backwards, hence must append in the front
			// this operations is not constant!!
			String solution = String.valueOf(S.charAt(i - 1)) + copy;
			reconstructLCS(DP, i - 1, j - 1, solution, S, T);
		}		
		if (DP[i - 1][j] == DP[i][j]) reconstructLCS(DP, i - 1, j, copy, S, T);
		if (DP[i][j - 1] == DP[i][j]) reconstructLCS(DP, i, j - 1, copy, S, T);

	}
	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/DistinctSubsequences/input.txt"));

		while (textScan.hasNextLine()) {
			Main test = new Main();
			String line = textScan.nextLine();
			String words[] = line.split(",");
			System.out.println(test.lcs(words[0], words[1]));
		}
	}
}
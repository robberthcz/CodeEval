/**
 Longest Common Subsequence
 Challenge Description:
 You are given two sequences. Write a program to determine the longest common subsequence between the two strings (each string can have a maximum length of 50 characters). NOTE: This subsequence need not be contiguous. The input file may contain empty lines, these need to be ignored.

 Input sample:
 The first argument will be a path to a filename that contains two strings per line, semicolon delimited. You can assume that there is only one unique subsequence per test case. E.g.
 XMJYAUZ;MZJAWXU

 Output sample:
 The longest common subsequence. Ensure that there are no trailing empty spaces on each line you print. E.g.
 MJAU
 */
package LongestCommonSubsequence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

    /**
     * Typical DP algorithm for finding the longest common subsequency between two strings.
     * @param S
     * @param T
     * @return
     */
    public static Iterable<String> LCS(String S, String T) {
		int n = S.length(), m = T.length();
		int[][] DP = new int[n + 1][m + 1];

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (S.charAt(i - 1) == T.charAt(j - 1))	DP[i][j] = DP[i - 1][j - 1] + 1;
				else if (DP[i - 1][j] >= DP[i][j - 1])	DP[i][j] = DP[i - 1][j];
				else					                DP[i][j] = DP[i][j - 1];
			}
		}
		TreeSet<String> validStr = new TreeSet<String>();
		reconstructLCS(DP, n, m, validStr, "", S, T);
		return validStr;
	}

    /**
     * Using the table generated during the DP algorithm, we retrace the steps and find all solutions, which are possible.
     * @param DP
     * @param i
     * @param j
     * @param validStr
     * @param str
     * @param S
     * @param T
     */
	private static void reconstructLCS(int[][] DP, int i, int j,
		TreeSet<String> validStr, String str, String S, String T) {
		// we don't know the exact path, so have to guess
		String copy = str.substring(0, str.length());
        // reached start with appropriately sized result
		if ((i == 0 || j == 0) && str.length() == DP[S.length()][T.length()]) {
			validStr.add(copy);
			return;
		}
        // reached start, invalid
        else if (i == 0 || j == 0) return;
        // matched char
		if (S.charAt(i - 1) == T.charAt(j - 1)) {
			// we are going backwards, hence must append in the front
			// this operations is not constant!!
			String solution = String.valueOf(S.charAt(i - 1)) + copy;
			reconstructLCS(DP, i - 1, j - 1, validStr, solution, S, T);
		}
		if (DP[i - 1][j] == DP[i][j]) reconstructLCS(DP, i - 1, j, validStr, copy, S, T);
		if (DP[i][j - 1] == DP[i][j]) reconstructLCS(DP, i, j - 1, validStr, copy, S, T);
	}
	public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/LongestCommonSubsequence/input.txt"));

        while (textScan.hasNextLine()) {
			String line = textScan.nextLine();
			String words[] = line.split(";");
			for (String lcs : LCS(words[0], words[1]))
				System.out.println(lcs);
		}
	}
}

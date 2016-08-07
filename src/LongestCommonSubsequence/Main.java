package LongestCommonSubsequence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeSet;
/**
 * The well-known LCS problem, is identical to DistinctSubsequences problem,
 * which was already solved, hence no comments.
 * 
 * @author Robb
 *
 */
public class Main {

	public Main() {
	}

	public Iterable<String> LCS(String S, String T) {
		int n = S.length(), m = T.length();
		int[][] DP = new int[n + 1][m + 1];

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				if (S.charAt(i - 1) == T.charAt(j - 1))
					DP[i][j] = DP[i - 1][j - 1] + 1;
				else if (DP[i - 1][j] >= DP[i][j - 1])
					DP[i][j] = DP[i - 1][j];
				else
					DP[i][j] = DP[i][j - 1];

			}
		}
		TreeSet<String> validStr = new TreeSet<String>();

		reconstructLCS(DP, n, m, validStr, "", S, T);

		return validStr;
	}

	private void reconstructLCS(int[][] DP, int i, int j,
			TreeSet<String> validStr, String str, String S, String T) {
		// we don't know the exact path, so have to guess

		String copy = str.substring(0, str.length());

		if ((i == 0 || j == 0) && str.length() == DP[S.length()][T.length()]) {

			validStr.add(copy);
			return;
		} else if (i == 0 || j == 0)
			return;

		if (S.charAt(i - 1) == T.charAt(j - 1)) {
			// we are going backwards, hence must append in the front
			// this operations is not constant!!
			String solution = String.valueOf(S.charAt(i - 1)) + copy;
			reconstructLCS(DP, i - 1, j - 1, validStr, solution, S, T);
		}
		if (DP[i - 1][j] == DP[i][j])
			reconstructLCS(DP, i - 1, j, validStr, copy, S, T);
		if (DP[i][j - 1] == DP[i][j])
			reconstructLCS(DP, i, j - 1, validStr, copy, S, T);

	}
	public static void main(String[] args) throws FileNotFoundException {

		Main test = new Main();

		Scanner textScan = new Scanner(new FileReader("lcs.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();

			String words[] = line.split(";");

			for (String lcs : test.LCS(words[0], words[1]))
				System.out.println(lcs);

		}

	}
}

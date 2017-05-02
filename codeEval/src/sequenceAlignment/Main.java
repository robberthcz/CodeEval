package SequenceAlignment;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	private final int n, m;
	private final int[][] V, G, E, F;
	private final int openGap, extendGap;

	public Main(String x, String y) {
		n = x.length();
		m = y.length();

		V = new int[n + 1][m + 1];
		G = new int[n + 1][m + 1];
		E = new int[n + 1][m + 1];
		F = new int[n + 1][m + 1];

		openGap = -7;
		extendGap = -1;

		for (int i = 1, j = 1; i <= n || j <= m; j++, i++) {
			int gapSum = openGap + i * extendGap;
			if (i <= n) {
				V[i][0] = gapSum;
				E[i][0] = gapSum;
			}

			if (j <= m) {
				V[0][j] = gapSum;
				F[0][j] = gapSum;
			}

		}

		for (int i = 1; i <= n; i++) {
			for (int j = 1; j <= m; j++) {
				int matchPenalty;

				if (x.charAt(i - 1) == y.charAt(j - 1))
					matchPenalty = 3;
				else
					matchPenalty = -3;

				G[i][j] = V[i - 1][j - 1] + matchPenalty;
				E[i][j] = Math.max(E[i][j - 1] + extendGap, V[i][j - 1]
						+ extendGap + openGap);
				F[i][j] = Math.max(F[i - 1][j] + extendGap, V[i - 1][j]
						+ extendGap + openGap);

				V[i][j] = Math.max(G[i][j], Math.max(E[i][j], F[i][j]));

			}
		}

	}

	public int maxScore() {
		return V[n][m];
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner genomeScan = new Scanner(new FileReader("test-cases/optimalAlignment.txt"));

		while (genomeScan.hasNextLine()) {
			String line = genomeScan.nextLine();
			String[] genes = line.split(" ");
			genes[0].trim();
			genes[2].trim();

			Main salign = new Main(genes[0], genes[2]);
			System.out.println(salign.maxScore());

		}

	}

}

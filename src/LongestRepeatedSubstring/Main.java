package LongestRepeatedSubstring;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public Main() {
	}

	public String LRS(String S) {

		String suffix[] = new String[S.length()];
		int[] lcs = new int[S.length()];

		// create suffix array in linear time
		for (int i = 0; i < S.length(); i++)
			suffix[i] = S.substring(i, S.length()).split(" ")[0];

		Arrays.sort(suffix);

		for (int i = 0; i < S.length() - 1; i++)
			lcs[i] = lcs(suffix[i], suffix[i + 1]);

		String lrs = "";

		for (int i = 0; i < suffix.length - 1; i++) {
			lrs = lrs.trim();

			int smaller;
			if (suffix[i].length() < suffix[i + 1].length())
				smaller = i;
			else
				smaller = i + 1;

			int smallerLen = suffix[smaller].length();

			if (lcs[i] > lrs.length()) {
				
				// does it overlap or not
				if (Math.abs(suffix[i].length() - suffix[i + 1].length()) >= lcs[i])
					lrs = suffix[i].substring(0, lcs[i]);

				// it does overlap
				// valid fits can still be found
				else {
					int id = i + 1;
					while (lcs[id] >= lcs[i] && id < S.length()) {
						if (Math.abs(smallerLen - suffix[id].length()) >= lcs[i]) {
							lrs = suffix[i].substring(0, lcs[i]);
							break;
						}

						id++;
					}

				}

			}

		}
		return lrs;

	}
	private int lcs(String S, String T) {
		for (int i = 0; i < S.length() && i < T.length(); i++) {
			if (S.charAt(i) != T.charAt(i))
				return i;
		}

		if (S.length() < T.length())
			return S.length();
		else
			return T.length();

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner scan = new Scanner(new FileReader("repeatedSubstring.txt"));

		while (scan.hasNextLine()) {
			Main test = new Main();

			String line = scan.nextLine();
			String substring = test.LRS(line).trim();
			// System.out.println(line + " " + substring.length());

			if (substring.length() == 0)
				System.out.println("NONE");
			else
				System.out.println(substring);

		}
	}

}

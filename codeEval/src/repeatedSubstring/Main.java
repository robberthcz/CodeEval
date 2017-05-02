/**
 Repeated Substring
 Challenge Description:
 You are to find the longest repeated substring in a given text. Repeated substrings may not overlap. If more than one substring is repeated with the same length, print the first one you find.(starting from the beginning of the text).
 NOTE: The substrings can't be all spaces.

 Input sample:
 Your program should accept as its first argument a path to a filename. The input file contains several lines. Each line is one test case. Each line contains a test string. E.g.
 banana
 am so uniqe

 Output sample:
 For each set of input produce a single line of output which is the longest repeated substring. If there is none, print out the string NONE. E.g.
 an
 NONE
 */
package repeatedSubstring;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	public static String LRS(String S) {
		String suffix[] = new String[S.length()];
		// create suffix array in linear time
		for (int i = 0; i < S.length(); i++)
			suffix[i] = S.substring(i, S.length());
		Arrays.sort(suffix);

		String lrs = "";
		for (int i = 0; i < suffix.length - 1; i++) {
			String curLcp = lcp(suffix[i], suffix[i + 1]);
			// maxOverlap handles the aaaaaa test-case
			int maxOverlap = Math.max(suffix[i].length(), suffix[i + 1].length()) / 2;
			curLcp = curLcp.substring(0, Math.min(maxOverlap, curLcp.length()));
			// the substring cannot be all whitespaces
			if(curLcp.trim().equals(""))
				continue;
			// current substring must be either longer than past optimum
			// or of the same length as the past optimum and appear earlier in the test-case than the past optimum
			else if(curLcp.length() > lrs.length() || (curLcp.length() == lrs.length() && S.indexOf(curLcp, 0) < S.indexOf(lrs, 0)))
				lrs = curLcp;

		}
		return lrs;
	}
	
	private static String lcp(String S, String T) {
		int min = Math.min(S.length(), T.length());
		for (int i = 0; i < min; i++) {
			if (S.charAt(i) != T.charAt(i))
				return S.substring(0, i);
		}
		return S.substring(0, min);
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner scan = new Scanner(new FileReader("src/repeatedSubstring/input.txt"));

		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String lrp = LRS(line);

			if (lrp.length() == 0) System.out.println("NONE");
			else System.out.println(lrp);
		}
	}

}

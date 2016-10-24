/**
 String Substitution
 Challenge Description:
 Credits: This challenge was contributed by Sam McCoy
 Given a string S, and a list of strings of positive length, F1,R1,F2,R2,...,FN,RN, proceed to find in order the occurrences (left-to-right) of Fi in S and replace them with Ri. All strings are over alphabet { 0, 1 }. Searching should consider only contiguous pieces of S that have not been subject to replacements on prior iterations. An iteration of the algorithm should not write over any previous replacement by the algorithm.

 Input sample:
 Your program should accept as its first argument a path to a filename. Each line in this file is one test case. Each test case will contain a string, then a semicolon and then a list of comma separated strings. E.g.
 10011011001;0110,1001,1001,0,10,11

 Output sample:
 For each line of input, print out the string after substitutions have been made.eg.
 11100110

 For the curious, here are the transitions for the above example: 10011011001 => 10100111001 [replacing 0110 with 1001] => 10100110 [replacing 1001 with 0] => 11100110 [replacing 10 with 11]. So the answer is 11100110
 */
package StringSubstitution;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	private boolean[] marked;
	private String[] string;

	public Main(String[] string) {
		this.string = string;
		this.marked = new boolean[string.length];
	}

	public void substitute(String toReplace, String replaceWith) {
		for (int i = 0; i < string.length; i++) {
			if (marked[i])	continue;

			int j;
			for (j = i; j - i < toReplace.length() && j < string.length; j++) {
				String replacing = String.valueOf(toReplace.charAt(j - i));

				if (marked[j] || !string[j].equals(replacing)) {
					j = i;
					break;
				}
			}

			if (j - i == toReplace.length()) {
				replace(replaceWith, i, j);
			}
		}
	}

	private void replace(String replaceWith, int start, int end) {
		string[start] = replaceWith;
		marked[start] = true;

		for (int i = start + 1; i < end; i++) {
			string[i] = "-";
			marked[i] = true;
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < string.length; i++) {
			if (string[i].equals("-"))
				continue;
			sb.append(string[i]);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/StringSubstitution/input.txt"));

		while (textScan.hasNextLine()) {
			String[] line = textScan.nextLine().split(";");
     		Main test = new Main(line[0].split(""));
			String[] substitutions = line[1].split(",");

			for (int i = 0; i < substitutions.length; i += 2) {
				String toReplace = substitutions[i];
				String replaceWith = substitutions[i + 1];

				test.substitute(toReplace, replaceWith);
			}
			System.out.println(test);
		}
	}
}

package StringList;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Challenge Description:
 * 
 * Credits: Challenge contributed by Max Demian.
 * 
 * You are given a number N and a string S. Print all of the possible ways to
 * write a string of length N from the characters in string S, comma delimited
 * in alphabetical order.
 * 
 * Input sample:
 * 
 * The first argument will be the path to the input filename containing the test
 * data. Each line in this file is a separate test case. Each line is in the
 * format: N,S i.e. a positive integer, followed by a string (comma separated).
 * E.g. 1,aa 2,ab 3,pop
 * 
 * Output sample:
 * 
 * Print all of the possible ways to write a string of length N from the
 * characters in string S comma delimited in alphabetical order, with no
 * duplicates. E.g. a aa,ab,ba,bb ooo,oop,opo,opp,poo,pop,ppo,ppp
 * 
 * @author Robb
 *
 */
public class Main {
	StringBuilder lastWord;
	int len;

	public Main(String word, int len) {
		this.len = len;
		TreeSet<Character> set = new TreeSet<Character>();

		// extract only non-repeating chars
		for (int i = 0; i < word.length(); i++)
			set.add(word.charAt(i));

		char[] keypad = new char[set.size()];

		int id = 0;
		for (char c : set) {
			keypad[id++] = c;
		}

		// just to know when stop printing ","
		lastWord = new StringBuilder("");
		for (int i = 0; i < len; i++)
			lastWord.append(keypad[keypad.length - 1]);

		for (int i = 0; i < keypad.length; i++) {
			StringBuilder permutation = new StringBuilder("");
			dfs(0, i, keypad, permutation);
		}
		System.out.println();

	}

	private void dfs(int row, int column, char[] keypad,
			StringBuilder permutation) {

		permutation.append(keypad[column]);

		if (row == len - 1) {

			System.out.print(permutation);

			if (!permutation.toString().equals(lastWord.toString()))
				System.out.print(",");

			permutation = permutation.deleteCharAt(permutation.length() - 1);
			return;
		}

		for (int j = 0; j < keypad.length; j++) {
			dfs(row + 1, j, keypad, permutation);

		}
		permutation = permutation.deleteCharAt(permutation.length() - 1);

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("stringList.txt"));

		while (textScan.hasNextLine()) {

			String line = textScan.nextLine();

			String words[] = line.split(",");

			int len = Integer.parseInt(words[0]);

			Main test = new Main(words[1], len);
		}

	}
}
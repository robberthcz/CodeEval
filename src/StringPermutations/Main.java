package StringPermutations;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Challenge Description:
 * 
 * Write a program which prints all the permutations of a string in alphabetical
 * order. We consider that digits < upper case letters < lower case letters. The
 * sorting should be performed in ascending order.
 * 
 * Input sample:
 * 
 * Your program should accept a file as its first argument. The file contains
 * input strings, one per line.
 * 
 * For example: 
 * hat
 * 
 * abc
 * 
 * Zu6
 * 
 * 
 * Output sample:
 * 
 * Print to stdout the permutations of the string separated by comma, in
 * alphabetical order.
 * 
 * For example:
 * 
 * 
 * aht,ath,hat,hta,tah,tha
 * 
 * abc,acb,bac,bca,cab,cba
 * 
 * 6Zu,6uZ,Z6u,Zu6,u6Z,uZ6
 * 
 * 
 * 
 * @author Robb
 *
 */
public class Main {

	public static Iterable<String> genPermutation(String s, int size) {

		int N = s.length();
		Queue<String> toReturn = new LinkedList<String>();
		boolean[] marked = new boolean[N];
		char[] permutation = new char[N];
		char[] sChars = s.toCharArray();

		Arrays.sort(sChars);

		genPermutation(-1, size, permutation, marked, sChars, toReturn);

		return toReturn;

	}

	private static void genPermutation(int level, int size, char[] permutation,
			boolean[] marked, char[] str, Queue<String> toReturn) {

		if (level >= size - 1) {

			toReturn.add(String.valueOf(permutation));
			return;
		}

		for (int i = 0; i < str.length; i++) {
			if (!marked[i]) {

				permutation[++level] = str[i];
				marked[i] = true;

				genPermutation(level, size, permutation, marked, str, toReturn);

				marked[i] = false;
				level--;
			}
		}

	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner strings = new Scanner(new FileReader("stringPermutations.txt"));

		while (strings.hasNextLine()) {
			String word = strings.nextLine();

			Iterable<String> permutations = Main.genPermutation(word, 3);

			Iterator<String> iter = permutations.iterator();

			while (iter.hasNext()) {
				System.out.print(iter.next());
				if (iter.hasNext())
					System.out.print(",");

			}

			System.out.println();

		}

	}

}

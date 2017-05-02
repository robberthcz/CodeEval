/**
 Following Integer
 Challenge Description:
 Credits: This challenge has appeared in a past google competition
 You are writing out a list of numbers. Your list contains all numbers with exactly Di digits in its decimal representation which are equal to i, for each i between 1 and 9, inclusive. You are writing them out in ascending order. For example, you might be writing every number with two '1's and one '5'. Your list would begin 115, 151, 511, 1015, 1051. Given N, the last number you wrote, compute what the next number in the list will be. The number of 1s, 2s, ..., 9s is fixed but the number of 0s is arbitrary.

 Input sample:
 Your program should accept as its first argument a path to a filename. Each line in this file is one test case. Each test case will contain an integer n < 10^6. E.g.
 115
 842
 8000

 Output sample:
 For each line of input, generate a line of output which is the next integer in the list. E.g.
 151
 2048
 80000
 */
package FollowingInteger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * @author Robb
 */
public class Main {

	/**
	 * The number is given as string.
	 * @param N
	 * @return
	 */
	public static int followingInteger(String N) {
		int floor = Integer.parseInt(N);
		Integer next = 0;
		// get char representation of our input number
		char[] nums = N.toCharArray();
		// N is now the minimum possible number that could be arranged from
		// digits of input number
		Arrays.sort(nums);
		N = String.valueOf(nums);

		while (next == null || next <= floor) {
			for (int i = 0; i < N.length(); i++) {
				// the number is represented as a board and with dfs we traverse
				// it starting at different points
				// marked says what digits were already explored
				boolean[] marked = new boolean[N.length()];
				// set contains generated numbers, at some point it should
				// contain one bigger than the input
				TreeSet<Integer> set = new TreeSet<Integer>();
				dfs(N, marked, set, "", i);
				// look if there is valid number in the set and return it, else
				// continue looking
				next = set.higher(floor);
				if (next != null && next > floor)
					return next;
			}
			// the generated numbers will now contain one more zero
			N = "0" + N;
		}
		return next;
	}

	private static void dfs(String N, boolean[] marked, TreeSet<Integer> set, String number, int position) {
		// append current digit
		number += N.substring(position, position + 1);
		marked[position] = true;
		// we completed one round of dfs
		if (number.length() == N.length()) {
			set.add(Integer.parseInt(number));
		}
		// explore unmarked digits
		for (int i = 0; i < N.length(); i++) {
			if (!marked[i]) {
				dfs(N, marked, set, number, i);
			}
		}
	    // backtrack
		marked[position] = false;
		// number = number.substring(0, number.length() - 1);
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/FollowingInteger/input.txt"));

		while (textScan.hasNextLine()) {
			String N = textScan.nextLine().trim();
			System.out.println(Main.followingInteger(N));
		}
	}
}

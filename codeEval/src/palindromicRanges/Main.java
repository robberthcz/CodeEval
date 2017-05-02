/**
 Palindromic Ranges
 Challenge Description:
 A positive integer is a palindrome if its decimal representation (without leading zeros) is a palindromic string (a string that reads the same forwards and backwards). For example, the numbers 5, 77, 363, 4884, 11111, 12121 and 349943 are palindromes.
 A range of integers is interesting if it contains an even number of palindromes. The range [L, R], with L <= R, is defined as the sequence of integers from L to R (inclusive): (L, L+1, L+2, ..., R-1, R). L and R are the range's first and last numbers.
 The range [L1,R1] is a subrange of [L,R] if L <= L1 <= R1 <= R. Your job is to determine how many interesting subranges of [L,R] there are.

 Input sample:
 Your program should accept as its first argument a path to a filename. Each line in this file is one test case. Each test case will contain two positive integers, L and R (in that order), separated by a space. eg.
 1 2
 1 7
 87 88

 Output sample:
 For each line of input, print out the number of interesting subranges of [L,R] eg.
 1
 12
 1

 For the curious: In the third example, the subranges are: [87](0 palindromes), [87,88](1 palindrome),[88](1 palindrome). Hence the number of interesting palindromic ranges is 1
 */
package palindromicRanges;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	public Main(int lo, int hi) {
		boolean[] anagram = new boolean[hi - lo + 1];

        for (int i = lo; i <= hi; i++)
			anagram[i - lo] = isAnagram(i);

		int n = 0;
		for (int i = 0; i < anagram.length; i++) {
			boolean isOdd = anagram[i];
			if (!isOdd)	n++;

			for (int j = i + 1; j < anagram.length; j++) {
				if (isOdd == anagram[j]) isOdd = false;
				else                     isOdd = true;

                if (!isOdd)	n++;
			}
		}
		System.out.println(n);
	}

	private boolean isAnagram(int num) {
		String n = String.valueOf(num);

		int lo = 0, hi = n.length() - 1;
		while (lo < hi) {
			if (n.charAt(lo) != n.charAt(hi)) return false;
			lo++;
			hi--;
		}
		return true;
	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/PalindromicRanges/input.txt"));

		while (textScan.hasNextLine()) {
			String[] nums = textScan.nextLine().split(" ");

			int lo = Integer.parseInt(nums[0]);
			int hi = Integer.parseInt(nums[1]);

			Main test = new Main(lo, hi);
		}
	}
}

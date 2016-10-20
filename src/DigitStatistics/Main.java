/**
 Digit statistics
 Challenge Description:
 Given the numbers "a" and "n" find out how many times each digit from zero to nine is the last digit of the number in a sequence [ a, a2, a3, ... an-1, an ]

 Input sample:
 Your program should accept as its first argument a path to a filename. Each line of input contains two space separated integers "a" and "n" E.g:
 2 5

 Output sample
 For each line of input print out how many times the digits zero, one, two ... nine occur as the last digit of numbers in the sequence E.g:
 0: 0, 1: 0, 2: 2, 3: 0, 4: 1, 5: 0, 6: 1, 7: 0, 8: 1, 9: 0

 In this example, the sequence consists of numbers 2, 4, 8, 16 and 32. Among the last digits, the digit two occurs twice, and each of the digits four, six and eight occurs once.

 Constraints:
 1 ≤ n ≤ 1 000 000 000 000,
 2 ≤ a ≤ 9
 */
package DigitStatistics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * @author Robb
 *
 */
public class Main {
	private LinkedHashSet<Integer> lastDigs;

	public Main(int a, long n) {
		// the last digits of numbers in the above sequence repeat itself in a
		// cycle, this set contains these last digits, the order is important
		lastDigs = new LinkedHashSet<Integer>();
		// use only the last digit
		a = a % 10;
		int aCopy = a;

		while (!lastDigs.contains(aCopy)) {
			lastDigs.add(aCopy);
			aCopy = aCopy * a % 10;
		}
		// contains the number of times given number is last digit
		long[] lastDigits = new long[10];

		// how many times the cycle is repeated
		long nOfRepeats = n / lastDigs.size();
		// last cycle may be not be complete
		long leftover = n - nOfRepeats * lastDigs.size();

		for (int digit : lastDigs) {
            lastDigits[digit] = nOfRepeats;
		    // whether last incomplete cycle contains this processed last digit
			if (leftover > 0) {
				lastDigits[digit] += 1;
				// decrement the length of last incomplete cycle
				// every cycle is ordered the same way, like 1,2,3 1,2,3 1,2
				leftover--;
			}
		}

		// solution is submitted in the form that 0: 2, 1: 5 ... 9: 3
		StringBuilder toReturn = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			toReturn.append(i + ": " + lastDigits[i]);
			if (i != 9) toReturn.append(", ");
		}
		// submit the solutions
		System.out.println(toReturn);
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader(
				"src/DigitStatistics/input.txt"));

		while (textScan.hasNextLine()) {
			String words[] = textScan.nextLine().split(" ");

			int a = Integer.parseInt(words[0]);
			long n = Long.parseLong(words[1]);

			Main test = new Main(a, n);
		}
	}
}

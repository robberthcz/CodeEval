package DigitStatistics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * We are given numbers "a" and "n". We count how many times digits between 0
 * and 9 are last digits of numbers in a sequence (a to power of 1,..., a to
 * power of n-1, a to power of n)
 * 
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

		int aCopy = a % 10;

		while (!lastDigs.contains(aCopy)) {
			// System.out.println(aCopy);

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
			// signifies whether the last digit is in the last incomplete cycle
			int one;

			// whether last incomplete cycle contains this processed last digit
			if (leftover > 0) {
				one = 1;
				// decrement the length of last incomplete cycle
				// every cycle is ordered the same way, like 1,2,3 1,2,3 1,2
				leftover--;
			} else
				one = 0;

			lastDigits[digit] = nOfRepeats + one;;

		}

		// solution is submitted in the form that 0: 2, 1: 5 ... 9: 3
		StringBuilder toReturn = new StringBuilder();

		for (int i = 0; i < 10; i++) {
			toReturn.append(i + ": " + lastDigits[i]);
			if (i != 9)
				toReturn.append(", ");

		}

		// submit the solutions
		System.out.println(toReturn);

	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader(
				"test-cases/digitStatistics.txt"));

		while (textScan.hasNextLine()) {
			String words[] = textScan.nextLine().split(" ");

			int a = Integer.parseInt(words[0]);
			long n = Long.parseLong(words[1]);

			Main test = new Main(a, n);
		}

	}

}

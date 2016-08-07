package TextDollar;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Transforms digit representation of number to its text representation---------
 * 21 034 => TwentyOneThousandThirtyFour
 * 
 * 
 * @author Robb
 *
 *
 */
public class Main {

	private HashMap<Integer, String> map;

	public Main() {
		map = new HashMap<Integer, String>();
		// maps digit representation of basic numbers to their text
		// representation
		map.put(0, "Zero");
		map.put(1, "One");
		map.put(2, "Two");
		map.put(3, "Three");
		map.put(4, "Four");
		map.put(5, "Five");
		map.put(6, "Six");
		map.put(7, "Seven");
		map.put(8, "Eight");
		map.put(9, "Nine");
		map.put(10, "Ten");
		map.put(11, "Eleven");
		map.put(12, "Twelve");
		map.put(13, "Thirteen");
		map.put(14, "Fourteen");
		map.put(15, "Fifteen");
		map.put(16, "Sixteen");
		map.put(17, "Seventeen");
		map.put(18, "Eighteen");
		map.put(19, "Nineteen");
		map.put(20, "Twenty");
		map.put(30, "Thirty");
		map.put(40, "Forty");
		map.put(50, "Fifty");
		map.put(60, "Sixty");
		map.put(70, "Seventy");
		map.put(80, "Eighty");
		map.put(90, "Ninety");

	}
	/**
	 * Transforms number to its text represenation
	 * 
	 * @param N
	 *            number to be converted to text
	 * @return String in the form of "OneMillionTenThousandTwoHundredTwentyFour"
	 */
	public String numberToText(int N) {

		if (N == 0)
			return "";
		else if (N > 999_999) {
			// recursion in the form that - subtract million from the number N,
			// and build up the string to be returned by appending "Million"
			// divide then the number into two parts
			// first part represents the part higher than million
			// the second part represents the part of a number lower than the
			// million
			return numberToText(N / 1_000_000) + "Million"
					+ numberToText(N - (N / 1_000_000) * 1_000_000);
		} else if (N > 999) {
			return numberToText(N / 1_000) + "Thousand"
					+ numberToText(N - (N / 1_000) * 1_000);
		} else if (N > 99) {
			return numberToText(N / 100) + "Hundred"
					+ numberToText(N - (N / 100) * 100);
		} else if (N <= 19)
			return map.get(N);
		else {
			// here the number to be built up is between 19 and 99
			
			//first get the tens - 20,30,...
			int tens = (N / 10) * 10;
			
			//get the singles
			int ones = N - tens;

			return map.get(tens) + numberToText(ones);

		}

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("test-cases/textDollar.txt"));

		Main test = new Main();

		while (textScan.hasNextLine()) {

			int dollarAmount = Integer.parseInt(textScan.nextLine());

			// zero shouldn't be in input, but still...
			if (dollarAmount == 0) {
				System.out.println("ZeroDollars");
				continue;
			}

			System.out.print(test.numberToText(dollarAmount));

			// always assume plural quantities;
			System.out.println("Dollars");

		}

	}
}

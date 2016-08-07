package TextToNumber;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
/**
 * Transforms number represented by text into number represented by digits-----
 * one hundred thousand twenty four => 100024
 * 
 * @author Robb
 *
 *
 */
public class Main {
	// maps text representation of numbers to digit representation
	private HashMap<String, Integer> map;

	public Main() {

		map = new HashMap<String, Integer>();

		map.put("zero", 0);
		map.put("one", 1);
		map.put("two", 2);
		map.put("three", 3);
		map.put("four", 4);
		map.put("five", 5);
		map.put("six", 6);
		map.put("seven", 7);
		map.put("eight", 8);
		map.put("nine", 9);
		map.put("ten", 10);
		map.put("eleven", 11);
		map.put("twelve", 12);
		map.put("thirteen", 13);
		map.put("fourteen", 14);
		map.put("fifteen", 15);
		map.put("sixteen", 16);
		map.put("seventeen", 17);
		map.put("eighteen", 18);
		map.put("nineteen", 19);
		map.put("twenty", 20);
		map.put("thirty", 30);
		map.put("forty", 40);
		map.put("fifty", 50);
		map.put("sixty", 60);
		map.put("seventy", 70);
		map.put("eighty", 80);
		map.put("ninety", 90);
		map.put("thousand", 1_000);
		map.put("million", 1_000_000);
	}
	/**
	 * 
	 * @param numbers
	 *            String array of text representation of number, the form is
	 *            following -> one hundred twenty four {million | thousand}
	 * @return
	 */
	public int textToNumber(String[] numbers) {
		// NEG_CONST is used to account for negative numbers
		// if the number is negative we have to start at position 1
		// temp is used to build up the intermediate sums
		int start = 0, NEG_CONST = 1, temp = 0;
		int toReturn = 0;

		if (numbers[0].equals("negative")) {
			start = 1;
			NEG_CONST = -1;
		}
		// we have to build up the number first from millions, then from
		// thousands
		// each million or thousand can be up to hundreds
		// each hundred can be preceded by tens and singles
		// so temp variable first picks up singles, then tens and then hundreds
		for (int i = start; i < numbers.length; i++) {

			if (numbers[i].equals("million") || numbers[i].equals("thousand")) {
				toReturn += temp * map.get(numbers[i]);
				// we are done with building up the number by hundreds, tens and
				// singles
				temp = 0;
				continue;
			}
			// build up the number by hundred
			if (numbers[i].equals("hundred"))
				temp *= 100;
			// build up the number by tens or singles
			else
				temp += map.get(numbers[i]);
		}
		// temp now contains what is left after subtracting millions and
		// thousands
		toReturn += temp;

		return toReturn * NEG_CONST;
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("textToNumber.txt"));

		Main test = new Main();

		while (textScan.hasNextLine()) {

			String[] numbers = textScan.nextLine().trim().split(" ");

			System.out.println(test.textToNumber(numbers));
		}

	}

}

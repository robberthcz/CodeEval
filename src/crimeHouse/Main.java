package crimeHouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
	// current min number of people in the house
	private int count;
	// can never drop below zero
	// current number of masked people entered or left
	private int enteredMaskedCount, leftMaskedCount;
	// ids of people who entered or left without a mask
	private HashSet<Integer> entered, left;

	public Main(boolean[] enterEvent, int[] idEvent) {
		count = 0;
		enteredMaskedCount = 0;
		leftMaskedCount = 0;
		entered = new HashSet<Integer>();
		left = new HashSet<Integer>();
		// for each event
		for (int i = 0; i < enterEvent.length; i++) {
			if (enterEvent[i]) {
				// one more in the house
				++count;
				// masked person entered
				if (idEvent[i] == 0) {
					enteredMaskedCount++;
					continue;
				}
				// first check if person is not already in the house
				// that person also could have already left after entering
				// if already in the house and did not leave unmasked => had to be in a
				// mask when leaving
				if (!entered.add(idEvent[i]) && !left.remove(idEvent[i])) {
					leftMaskedCount--;
				}

			} else {
				// leaving for unmasked person counts only if he entered before
				// that or somebody masked entered beforehand
				// masked person leaving => always counts if possible
				if (enteredMaskedCount > 0 || entered.contains(idEvent[i])
						|| idEvent[i] == 0)
					count = Math.max(0, --count);
				// masked person left
				if (idEvent[i] == 0) {
					leftMaskedCount++;
					continue;
				}
				// check if person did not already leave beforehand
				// then check if person did not enter beforehand
				// then person had to enter as a masked person
				if (!left.add(idEvent[i]) && !entered.remove(idEvent[i])) {
					enteredMaskedCount--;
				}

			}

			// is it CRIME TIME?
			if (enteredMaskedCount < 0 || leftMaskedCount < 0) {
				System.out.println("CRIME TIME");
				count = -1;
				break;
			}

		}
		// if not CRIME TIME, print the min number of people in the house
		if (count != -1)
			System.out.println(count);

	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader(
				"test-cases/crimeHouse.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine().split(";")[1].trim();
			// System.out.println(line);
			String[] events = line.split("\\|");

			boolean[] entered = new boolean[events.length];
			int[] ids = new int[events.length];

			for (int i = 0; i < events.length; i++) {
				String[] event = events[i].split(" ");

				ids[i] = Integer.parseInt(event[1]);
				if (event[0].equals("E"))
					entered[i] = true;
				else
					entered[i] = false;
			}
			Main test = new Main(entered, ids);

		}
	}

}

package crimeHouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	// current min number of people in the house
	private int count;
	// can never drop below zero
	// current number of masked people entered or left
	private int enteredMaskedCount, leftMaskedCount;
	// ids of people who entered or left without a mask
	private HashMap<Integer, Integer> entered, left;
	private LinkedList<Integer> enteredMaskedT, leftMaskedT;

	public Main(boolean[] enterEvent, int[] idEvent) {
		count = 0;
		enteredMaskedCount = 0;
		leftMaskedCount = 0;

		entered = new HashMap<Integer, Integer>();
		left = new HashMap<Integer, Integer>();
		enteredMaskedT = new LinkedList<Integer>();
		leftMaskedT = new LinkedList<Integer>();
		// for each event
		for (int i = 0; i < enterEvent.length && enteredMaskedCount >= 0 && leftMaskedCount >= 0; i++) {
			if (enterEvent[i]) {
				// one more in the house
				++count;
				// masked person entered
				if (idEvent[i] == 0) {
					enteredMaskedCount++;
					enteredMaskedT.addFirst(i);
					continue;
				}
				// first check if person is not already in the house
				// that person also could have already left after entering
				// if already in the house and did not leave unmasked => had to be in a
				// mask when leaving
				if (entered.containsKey(idEvent[i]) && !left.containsKey(idEvent[i])) {
					leftMaskedCount--;
					if(leftMaskedT.size() == 0 || leftMaskedT.removeFirst() < entered.get(idEvent[i])){
						leftMaskedCount = - 1;
						break;
					}
				}
				entered.remove(idEvent[i]);
				entered.put(idEvent[i], i);
				left.remove(idEvent[i]);

			} else {
				// leaving for unmasked person counts only if he entered before
				// that or somebody masked entered beforehand
				// masked person leaving => always counts if possible
				if (enteredMaskedCount > 0 || entered.containsKey(idEvent[i])
						|| idEvent[i] == 0)
					count = Math.max(0, --count);
				// masked person left
				if (idEvent[i] == 0) {
					leftMaskedCount++;
					leftMaskedT.addFirst(i);
					continue;
				}
				// check if person did not already leave beforehand
				// then check if person did not enter beforehand
				// then person had to enter as a masked person
				if (left.containsKey(idEvent[i]) & !entered.containsKey(idEvent[i])) {
					enteredMaskedCount--;
					if(enteredMaskedT.size() == 0 || enteredMaskedT.removeFirst() < left.get(idEvent[i])){
						enteredMaskedCount = - 1;
						break;
					}
				}
				left.remove(idEvent[i]);
				left.put(idEvent[i], i);
				entered.remove(idEvent[i]);
			}



		}
		// is it CRIME TIME?
		if (enteredMaskedCount < 0 || leftMaskedCount < 0) {
			System.out.println("CRIME TIME");
		}
		else
			System.out.println(count);

	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader(
				"test-cases/crimeHouse.txt"));
		String exc = "";
		while (textScan.hasNextLine()) {
			String line = textScan.nextLine().split(";")[1].trim();
			exc += ";" + line + "\n";
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
		//throw new RuntimeException(exc);
	}

}

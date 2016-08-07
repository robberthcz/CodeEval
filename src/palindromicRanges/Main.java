package palindromicRanges;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	public Main(int lo, int hi) {

		boolean[] anagram = new boolean[hi - lo + 1];

		for (int i = lo; i <= hi; i++) {

			if (isAnagram(i))
				anagram[i - lo] = true;
			else
				anagram[i - lo] = false;

		}

		int n = 0;

		for (int i = 0; i < anagram.length; i++) {

			boolean isOdd = anagram[i];
			if (!isOdd)
				n++;

			for (int j = i + 1; j < anagram.length; j++) {

				if (isOdd == anagram[j])
					isOdd = false;
				else
					isOdd = true;

				if (!isOdd)
					n++;

			}

		}
		
		System.out.println(n);

	}
	private boolean isAnagram(int num) {
		String n = String.valueOf(num);

		int lo = 0, hi = n.length() - 1;

		while (lo < hi) {

			if (n.charAt(lo) != n.charAt(hi))
				return false;
			lo++;
			hi--;
		}

		return true;

	}

	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/palindromicRanges.txt"));

		while (textScan.hasNextLine()) {

			String[] nums = textScan.nextLine().split(" ");

			int lo = Integer.parseInt(nums[0]);
			int hi = Integer.parseInt(nums[1]);

			Main test = new Main(lo, hi);
		}

	}

}

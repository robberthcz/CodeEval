package FindMin;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.TreeMap;
/**
 * This problem was in one of the Facebook hackathons.
 * 
 * @author Robb
 *
 *         After sending smileys, John decided to play with arrays. Did you know
 *         that hackers enjoy playing with arrays? John has a zero-based index
 *         array, m, which contains n non-negative integers. However, only the
 *         first k values of the array are known to him, and he wants to figure
 *         out the rest.
 * 
 *         John knows the following: for each index i, where k <= i < n, m is
 *         the minimum non-negative integer which is *not* contained in the
 *         previous *k* values of m.
 * 
 *         For example, if k = 3, n = 4 and the known values of m are [2, 3, 0],
 *         he can figure out that m[3] = 1. John is very busy making the world
 *         more open and connected, as such, he doesn't have time to figure out
 *         the rest of the array. It is your task to help him. Given the first k
 *         values of m, calculate the nth value of this array. (i.e. m[n -
 *         1]).Because the values of n and k can be very large, we use a
 *         pseudo-random number generator to calculate the first k values of m.
 *         Given positive integers a, b, c and r, the known values of m can be
 *         calculated as follows: m[0] = a m = (b * m[i - 1] + c) % r, 0 < i < k
 *
 */
public class Main {
	private int[] m;
	private TreeMap<Integer, Integer> map;

	public Main(int n, int k, int a, int b, int c, int r) {
		// contains the current sequence of k values of m, it is TreeMap,
		// because some
		// values may be repeated, which creates problems in some degenerate
		// cases, so we count the number of occurences for each value
		map = new TreeMap<Integer, Integer>();
		
		//the whole sequence
		m = new int[n];

		// a is the first number in sequence
		m[0] = a;
		map.put(a, 1);

		// generate the initial sequence using the given random number generator
		for (int i = 1; i < k; i++) {
			// random number generator
			m[i] = (b * m[i - 1] + c) % r;

			insertValue(m[i]);

		}

		int min = 0;

		for (int i = k; i < n; i++) {

			// find the min in the keySet between 0 and infinity, very stupid
			// solution
			min = 0;
			while (map.containsKey(min))
				min++;

			m[i] = min;

			insertValue(m[i]);

			// sequence of k values we are processing moved one step to the
			// right => need to remove the m[i - k] number from the map
			int value = map.get(m[i - k]);
			if (value == 1)
				map.remove(m[i - k]);
			else
				map.put(m[i - k], --value);

		}

	}
	// inserts number in the sequence into our map
	private void insertValue(int m) {

		if (map.containsKey(m)) {
			// increment the occurences of the given value
			int value = map.get(m) + 1;
			map.put(m, value);
		} else
			map.put(m, 1);

	}

	// returns the number for solution submitting
	public int lastValue() {
		return m[m.length - 1];
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("test-cases/findMin.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();

			String words[] = line.split(",");
			int n = Integer.parseInt(words[0]);
			int k = Integer.parseInt(words[1]);
			int a = Integer.parseInt(words[2]);
			int b = Integer.parseInt(words[3]);
			int c = Integer.parseInt(words[4]);
			int r = Integer.parseInt(words[5]);

			Main findmin = new Main(n, k, a, b, c, r);
			System.out.println(findmin.lastValue());
			// 26,34,1,6,12,8,38,41,40,12 right answers to findMin.txt

		}

	}

}
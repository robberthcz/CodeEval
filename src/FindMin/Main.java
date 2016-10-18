/**
 FIND MIN
 CHALLENGE DESCRIPTION:
 Credits: This problem appeared in the Facebook Hacker Cup 2013 Hackathon.
 After sending smileys, John decided to play with arrays. Did you know that hackers enjoy playing with arrays? John has a zero-based index array, m, which contains n non-negative integers. However, only the first k values of the array are known to him, and he wants to figure out the rest.
 John knows the following: for each index i, where k <= i < n, m is the minimum non-negative integer which is *not* contained in the previous *k* values of m.
 For example, if k = 3, n = 4 and the known values of m are [2, 3, 0], he can figure out that m[3] = 1. John is very busy making the world more open and connected, as such, he doesn't have time to figure out the rest of the array. It is your task to help him. Given the first k values of m, calculate the nth value of this array. (i.e. m[n - 1]).Because the values of n and k can be very large, we use a pseudo-random number generator to calculate the first k values of m. Given positive integers a, b, c and r, the known values of m can be calculated as follows:
 m[0] = a
 m = (b * m[i - 1] + c) % r, 0 < i < k
 INPUT SAMPLE:
 Your program should accept as its first argument a path to a filename. Each line in this file contains 6 comma separated positive integers which are the values of n,k,a,b,c,r in order. E.g.
 78,51,3,5,5,51230
 186,75,68,16,539,312
 137,135,48,17,461,512
 98,22,6,30,524,100
 46,18,7,11,9,46
 OUTPUT SAMPLE:
 Print out the nth element of m for each test case e.g.
 26
 34
 1
 6
 12
 */
package FindMin;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
	private LinkedList<Integer> list;
	private TreeMap<Integer, Integer> map;

	public Main(int n, int k, int a, int b, int c, int r) {
		// contains the current sequence of k values of m, it is TreeMap,
		// because some
		// values may be repeated, which creates problems in some degenerate
		// cases, so we count the number of occurences for each value
		map = new TreeMap<Integer, Integer>();
		//the whole sequence
		list = new LinkedList<>();

		// a is the first number in sequence
		list.add(a);
		map.put(a, 1);

		// generate the initial sequence using the given random number generator
		for (int i = 1; i < k; i++) {
			// random number generator
			list.addLast((b * list.getLast() + c) % r);
			insertValue(list.getLast());
		}

		for (int i = k; i < n; i++) {
			// find the min in the keySet between 0 and infinity, very stupid
			// solution
			int min = 0;
			while (map.containsKey(min)) min++;
			list.addLast(min);

			insertValue(list.getLast());

			// sequence of k values we are processing moved one step to the
			// right => need to remove the first number from the map
			int value = map.get(list.getFirst());
			if (value == 1)	map.remove(list.removeFirst());
			else			map.put(list.removeFirst(), --value);
		}
	}
	// inserts number in the sequence into our map
	private void insertValue(int m) {
		if (map.containsKey(m))	map.put(m, map.get(m) + 1);
        else map.put(m, 1);
	}

	// returns the number for solution submitting
	public int lastValue() {
		return list.getLast();
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/FindMin/input.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();

			String words[] = line.split(",");
			int n = Integer.parseInt(words[0]);
			int k = Integer.parseInt(words[1]);
			int a = Integer.parseInt(words[2]);
			int b = Integer.parseInt(words[3]);
			int c = Integer.parseInt(words[4]);
			int r = Integer.parseInt(words[5]);

			Main test = new Main(n, k, a, b, c, r);
			System.out.println(test.lastValue());
			// 26,34,1,6,12,8,38,41,40,12 right answers to input.txt
		}
	}
}
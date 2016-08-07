package ClimbingStairs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Scanner;
/**
 * 
 * Challenge Description:
 * 
 * You are climbing a stair case. It takes n steps to reach to the top. Each
 * time you can either climb 1 or 2 steps. In how many distinct ways can you
 * climb to the top?
 * 
 * Input sample:
 * 
 * Your program should accept as its first argument a path to a filename. Each
 * line in this file contains a positive integer which is the total number of
 * stairs. Ignore all empty lines. E.g.
 * 
 * 
 * 
 * 10
 * 
 * 20
 * 
 * 
 * Output sample:
 * 
 * Print out the number of ways to climb to the top of the staircase. E.g.
 * 
 * 89
 * 
 * 10946
 * 
 * 
 * Constraints: The total number of stairs is <= 1000
 * 
 * @author Robb
 *
 */
public class Main {

	// climbing stairs problems reduces to calculating Fibonacci numbers
	// we use DP approach with time complexity of O(n) and space complexity of
	// O(1)

	//
	public static BigInteger climbStairs(int N) {
		if (N == 0)
			return BigInteger.valueOf(0);

		BigInteger a = BigInteger.valueOf(1);
		BigInteger b = BigInteger.valueOf(1);

		for (int i = 1; i < N; i++) {
			BigInteger current = a;

			a = b;
			b = b.add(current);
		}

		return b;

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("climbingStairs.txt"));

		while (textScan.hasNextLine()) {

			System.out.println(Main.climbStairs(Integer.parseInt(textScan
					.nextLine())));

		}

	}

}

package PrefixExpressions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * 
 * Challenge Description:
 * 
 * You are given a prefix expression. Write a program which evaluates it.
 * 
 * Input sample:
 * 
 * Your program should accept a file as its first argument. The file contains
 * one prefix expression per line.
 * 
 * For example:
 * 
 * 
 * + 2 3 4
 * 
 * Your program should read this file and insert it into any data structure you
 * like. Traverse this data structure and evaluate the prefix expression. Each
 * token is delimited by a whitespace. You may assume that sum ‘+’,
 * multiplication ‘*’ and division ‘/’ are the only valid operators appearing in
 * the test data.
 * 
 * Output sample:
 * 
 * Print to stdout the output of the prefix expression, one per line.
 * 
 * For example:
 * 
 * 
 * 20
 * 
 * 
 * 
 * 
 * @author Robb
 *
 *
 */
public class Main {

	public static int evalautePrefix(String[] prefix) {

		LinkedList<Double> stack = new LinkedList<Double>();

		// traverse the string array from right to left
		for (int i = prefix.length - 1; i >= 0; i--) {

			// if prefix[i] is number then save it
			if (isDigit(prefix[i])) {
				stack.add(Double.parseDouble(prefix[i]));
				continue;
			}

			// we pop the last two numbers on the stack if prefix[i] is not
			// number and
			// evaluate it using the prefix[i]
			double first = stack.removeLast();
			double second = stack.removeLast();

			// result of applying the prefix[i] to first and second doubles
			double toStack;

			if (prefix[i].equals("*"))
				toStack = first * second;
			else if (prefix[i].equals("+"))
				toStack = first + second;
			else
				toStack = first / second;

			// add the result back to stack
			stack.add(toStack);

		}
		// stack should have only 1 value,now
		// casting down is just for submit solution purposes
		int toReturn = stack.removeLast().intValue();

		return toReturn;

	}
	/**
	 * Decides whether the input string is numeric or not
	 * 
	 * @param prefix
	 * @return
	 */
	private static boolean isDigit(String prefix) {
		return !(prefix.equals("*") || prefix.equals("+") || prefix.equals("/"));
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("prefixExpressions.txt"));

		while (textScan.hasNextLine()) {
			String[] prefix = textScan.nextLine().split(" ");

			System.out.println(Main.evalautePrefix(prefix));
		}
	}

}

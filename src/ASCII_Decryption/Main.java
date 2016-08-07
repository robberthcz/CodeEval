package ASCII_Decryption;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
/**
 * 
 * You are an analyst at the Central Intelligence Agency, and you have
 * intercepted a top secret encrypted message which contains numbers. Each
 * number is obtained by taking an ASCII code of the original character and
 * adding some unknown constant N.
 * 
 * For example, you can encrypt the word 'test' with the condition that N = 11.
 * 
 * 'test' to ASCII -> 116 101 115 116 -> add N to each number-> 127 112 126 127
 * 
 * Based on previous intelligence reports, you know that the original message
 * includes two identical words consisting of X characters and you know the last
 * letter in the word.
 * 
 * Your challenge is to decrypt the message.
 * 
 * Input sample:
 * 
 * Your program should accept as its first argument a path to a filename.
 * 
 * Each line of input consists of three parts: length of a word, which is
 * repeated twice, the last letter of this word, and an encrypted message
 * separated with space:
 * 
 * 
 * 5 | s | 92 112 109 40 118 109 109 108 123 40 119 110 40 124 112 109 40 117
 * 105 118 129 40 119 125 124 127 109 113 111 112 40 124 112 109 40 118 109 109
 * 108 123 40 119 110 40 124 112 109 40 110 109 127 54 40 53 40 91 120 119 107
 * 115
 * 
 * 
 * Output sample:
 * 
 * For each line of input print out decrypted message:
 *
 * The needs of the many outweigh the needs of the few. - Spock
 * 
 * 
 * Due to memory concerns and nature of the problem, we can't use char array as
 * a data structure for solution (the encrypted chars could be of negative
 * value).
 * 
 * @author Robb
 * 
 */
public class Main {

	// 15 25 12 10 45 etc.
	// represents the char numbers as int array
	private int[] chars;
	private char lastChar;
	private int len;

	/**
	 * 
	 * @param sentence
	 *            input string representing a series of numbers
	 * @param lastChar
	 *            last char of the longest word
	 * @param len
	 *            length of the longest word in the sentence
	 */
	public Main(String sentence, char lastChar, int len) {
		String[] sentenceSplit = sentence.trim().split(" ");
		chars = new int[sentenceSplit.length];

		// pars the char numbers to our int array
		for (int i = 0; i < sentenceSplit.length; i++)
			chars[i] = Integer.parseInt(sentenceSplit[i]);

		this.lastChar = lastChar;
		this.len = len;

		int N = decrypt();

		StringBuilder message = new StringBuilder();
		for (int i = 0; i < chars.length; i++)
			message.append(Character.toChars(chars[i] - N));
		
		System.out.println(message);

	}

	/**
	 * Decrypts the message. Finds a given constant and returns it.
	 */
	private int decrypt() {
		// the unknown constant
		int N = 0;
		// set containing candidate words not yet repeated
		HashSet<String> set = new HashSet<String>();

		// looping through the chars array and looking for a word of size len
		// this word has to repeat itself and on at least one of the sides has
		// to have ' '
		// for each word we also calculate the N
		for (int i = 1; i < chars.length - (len - 1); i++) {

			N = chars[i + len - 1] - Integer.valueOf(lastChar);

			// checking for ' ' on both sides
			boolean isValid = (chars[i - 1] - N == ' ')
					|| (chars[i + len] - N == ' ');

			if (isValid) {

				String word = getWord(i);
				if (set.contains(word))
					return N;
				else
					set.add(word);
			}

		}

		// this code should not be reached
		return N;

	}
	/**
	 * Represent members of the char array chars[i....i+(len-1)] as string
	 * 
	 * @param i
	 * @return
	 */
	private String getWord(int i) {
		String toReturn = "";

		// not constant operation
		for (int j = i; j < len + i; j++)
			toReturn += String.valueOf(chars[j]);

		return toReturn;

	}
	public static void main(String args[]) throws FileNotFoundException {

		long start = System.currentTimeMillis();

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/ASCIIDecryption.txt"));

		while (textScan.hasNextLine()) {

			String words[] = textScan.nextLine().split("\\|");

			int len = Integer.parseInt(words[0].trim());
			char lastChar = words[1].trim().charAt(0);

			Main test = new Main(words[2].trim(), lastChar, len);

		}
		/*
		 * long cost = System.currentTimeMillis() - start; Runtime runtime =
		 * Runtime.getRuntime(); long memory = runtime.totalMemory() -
		 * runtime.freeMemory(); System.out.println("Used memory is KB: " +
		 * memory / (1024)); System.out.println(cost);
		 */
	}

}

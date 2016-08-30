/**
 Alphabet blocks
 Challenge Description:

 We all remember those childhood times when we learned how to use alphabet blocks to form different words, such as MOM, DAD, TRAIN, and others. We propose you to remind this time for a while and imagine yourself being a child.
 So, you have a set of alphabet blocks. There is a letter on each of the six faces of every block. Also, you have a word associated with your childhood that you want to form.
 Write a program that will verify if it is possible to form the necessary word out of the set. If yes, then print "True" to stdout; otherwise, print "False".
 You can choose only one letter from an alphabet block and place blocks in any order.

 Input sample:
 The first argument is a path to a file. Each line contains test cases that have three arguments separated by the pipe symbol "|".
 1. The first argument in the line is a number that shows how many blocks are in the set.
 2. The second one is a word that you want to form.
 3. The third one is a list of arrays of letters. One face of the alphabet block includes one letter from array.
 For example:
 There is an array of letters "ABCDEF". It refers to one toy block with the following faces:
 "A", "B", "C", "D", "E", "F".

 1 4 | DOG | UPZRHR INOYLC KXDHNQ BAGMZI
 2 6 | HAPPY | PKMFQP KTXGCV OSDMAJ SDSIMY OEPGLE JZCDHI
 3 5 | PLAIN | BFUBZD XMQBNM IDXVCN JCOIAM OZYAYH

 Output sample:
 Print "True" to stdout if you can form the necessary word, or "False" if you cannot do this.
 1 True
 2 True
 3 False

 Constraints:
 1.The word length is from 4 to 18 characters.
 2.Number of blocks in the set is always equal to or greater than the word length.
 3.You can take only one block from the set to form one letter in the word.
 4.The letters in the word and letters on the faces of one block may repeat: the word might be "MOM" or there might be a block with "AAAAAA" faces.
 5.The number of test cases is 40.
 */
package alphabetBlocks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {
	private HashMap<Character, Integer> wordBlock;
	private boolean found;

	public Main(String word, String[] cubes) {
		// represents chars and their frequencies of the given word
		wordBlock = new HashMap<Character, Integer>();
		found = false;

		for (int i = 0; i < word.length(); i++)	insertPiece(word.charAt(i), wordBlock);
		// parse the cubes into char arrays
		LinkedList<char[]> cubeList = new LinkedList<char[]>();
		for (int i = 0; i < cubes.length; i++) {
			char[] cube = parseCube(cubes[i]);
			if (cube != null) cubeList.add(cube);
		}
		char[][] cubesArray = new char[cubeList.size()][];
		int id = 0;
		for (char[] a : cubeList) cubesArray[id++] = a;
		// for each letter of the first cube
		for (int i = 0; !found && i < cubesArray[0].length; i++) {
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();
			dfs(0, i, cubesArray, map);
		}
		// print the result
		if (found) System.out.println("True");
		else System.out.println("False");
	}

	/**
	 *
	 * @param row current cube to be explored
	 * @param col field of current cube to be explored
	 * @param a the cubes as char array
	 * @param map currently formed word
	 */
	private void dfs(int row, int col, char[][] a,
			HashMap<Character, Integer> map) {
		if (found) return;
		else if (row == a.length) {
			found = isMatch(map);
			return;
		}
		// explore every letter j of current cube
		for (int j = 0; j < a[row].length; j++) {
			insertPiece(a[row][j], map);
			dfs(row + 1, j, a, map);
			// backtrack
			removePiece(a[row][j], map);
		}
	}

	private void insertPiece(char c, HashMap<Character, Integer> map) {
		if (map.containsKey(c)) {
			int val = map.get(c);
			map.put(c, ++val);
		} else	map.put(c, 1);
	}

	private void removePiece(char c, HashMap<Character, Integer> map) {
		int val = map.get(c);
		if (val == 1) map.remove(c);
		else map.put(c, --val);
	}

	/**
	 *
	 * @param map
	 * @return True if input map has appropriate chars and their frequencies that match the word to be formed
	 */
	private boolean isMatch(HashMap<Character, Integer> map) {
		if (!map.keySet().containsAll(wordBlock.keySet())) return false;
		for (char c : map.keySet()) {
			int mapVal = map.get(c);
			int wordBlockVal = wordBlock.get(c);
			if (mapVal < wordBlockVal) return false;
		}
		return true;
	}

	/**
	 * Transforms every cube so that it only contains the letters which the word to be formed has
	 * For instance, if the word to be formed is "MOM" and the cube is "OPMKTO", we transform the cube to "OMO"
	 * Some cubes, which do not have any such letters, are removed
	 * @param cube
	 * @return
	 */
	private char[] parseCube(String cube) {
		HashSet<Character> set = new HashSet<Character>();
		for (int i = 0; i < cube.length(); i++) {
			if (wordBlock.containsKey(cube.charAt(i))) set.add(cube.charAt(i));
		}
		if (set.size() == 0) return null;
		char[] chars = new char[set.size()];
		int id = 0;
		for (char c : set) chars[id++] = c;
		return chars;
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"src/alphabetBlocks/input.txt"));

		while (textScan.hasNextLine()) {
			String[] input = textScan.nextLine().split("\\|");
			String word = input[1].trim();
			String[] cubes = input[2].trim().split(" ");
			Main test = new Main(word, cubes);
		}

	}

}

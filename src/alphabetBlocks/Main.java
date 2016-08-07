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

		wordBlock = new HashMap<Character, Integer>();
		found = false;

		for (int i = 0; i < word.length(); i++)
			insertPiece(word.charAt(i), wordBlock);

		LinkedList<char[]> cubeList = new LinkedList<char[]>();

		for (int i = 0; i < cubes.length; i++) {
			char[] cube = parseCube(cubes[i]);
			if (cube != null)
				cubeList.add(cube);
		}

		char[][] cubesArray = new char[cubeList.size()][];

		int id = 0;
		for (char[] a : cubeList) {
			cubesArray[id++] = a;
		}

		for (int i = 0; !found && i < cubesArray[0].length; i++) {
			HashMap<Character, Integer> map = new HashMap<Character, Integer>();

			dfs(0, i, cubesArray, map);
		}

		if (found)
			System.out.println("True");
		else
			System.out.println("False");

	}
	private void dfs(int row, int col, char[][] a,
			HashMap<Character, Integer> map) {

		if (found)
			return;
		else if (row == a.length) {
			found = isMatch(map);
			return;
		}

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
		} else
			map.put(c, 1);
	}

	private void removePiece(char c, HashMap<Character, Integer> map) {

		int val = map.get(c);

		if (val == 1)
			map.remove(c);
		else
			map.put(c, --val);

	}

	private boolean isMatch(HashMap<Character, Integer> map) {

		if (!map.keySet().containsAll(wordBlock.keySet()))
			return false;

		for (char c : map.keySet()) {

			int mapVal = map.get(c);
			int wordBlockVal = wordBlock.get(c);

			if (mapVal < wordBlockVal)
				return false;

		}

		return true;

	}

	private char[] parseCube(String cube) {

		HashSet<Character> set = new HashSet<Character>();

		for (int i = 0; i < cube.length(); i++) {
			if (wordBlock.containsKey(cube.charAt(i)))
				set.add(cube.charAt(i));
		}
		if (set.size() == 0)
			return null;

		char[] chars = new char[set.size()];
		int id = 0;
		for (char c : set)
			chars[id++] = c;

		return chars;

	}
	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/alphabetBlocks.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();

			String[] input = line.split("\\|");

			String word = input[1].trim();

			String[] cubes = input[2].trim().split(" ");

			Main test = new Main(word, cubes);

		}

	}

}

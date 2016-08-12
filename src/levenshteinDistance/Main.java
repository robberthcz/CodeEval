package levenshteinDistance;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;

public class Main {

	public Main() {
	}

	private HashSet<String> getWildcardMatches(TST trie, String S){
		HashSet<String> set = new HashSet<String>();

		for(String neighbor : getWildcardNeighbors(S)){
			set.addAll(trie.matchedKeys(neighbor));
		}
		return set;
	}

	public HashSet<String> getNeighborsLevDistOne(TST trie, String S){
		HashSet<String> search = new HashSet<String>();
		HashSet<String> found = new HashSet<String>();
		if(trie.matchedKeys(S).size() == 0)
			return found;

		search.add(S);
		getNeighborsLevDistOne(trie, search, found);
		return found;
	}

	private void getNeighborsLevDistOne(TST trie, HashSet<String> search, HashSet<String> found){
		if(search.size() == 0)
			return;

		HashSet<String> neighbors = new HashSet<String>();
		for(String S : search){
			neighbors.addAll(getWildcardMatches(trie, S));
		}
		found.addAll(search);
		neighbors.removeAll(found);
		getNeighborsLevDistOne(trie, neighbors, found);
	}

	private Iterable<String> getWildcardNeighbors(String S){
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < S.length(); i++) {
			// remove 1 character
			String T1 = S.substring(0, i) + S.substring(i + 1, S.length());
			// substitute 1 character
			String T2 = S.substring(0, i) + "."
					+ S.substring(i + 1, S.length());
			// add 1 character
			String T3 = S.substring(0, i) + "." + S.substring(i, S.length());
			//System.out.println(T1 + " " + T2 + " " + T3);
			set.add(T1);
			set.add(T2);
			set.add(T3);
		}
		// add 1 character, last
		String T3 = S + ".";
		set.add(T3);
		return set;
	}

	static class TST {
		class Node {
			boolean isWord;
			Node left, right, mid;
			char c;

			public Node() {
				isWord = false;
			}
		}
		private Node root;

		public TST() {
		}

		public void put(String key) {
			root = put(root, key, 0);
		}

		private Node put(Node x, String key, int d) {
			char c = key.charAt(d);
			if (x == null) {
				x = new Node();
				x.c = c;
			}

			if (c < x.c)
				x.left = put(x.left, key, d);
			else if (c > x.c)
				x.right = put(x.right, key, d);
			else if (d < key.length() - 1)
				x.mid = put(x.mid, key, d + 1);
			else
				x.isWord = true;

			return x;
		}

		public HashSet<String> matchedKeys(String key) {
			HashSet<String> keys = new HashSet<String>();
			matchedKeys(root, key, 0, keys, "");
			return keys;
		}
		private void matchedKeys(Node x, String key, int d,
				HashSet<String> keys, String match) {
			if (x == null || d == key.length())
				return;

			char c = key.charAt(d);

			// matching the wildcard character
			if (c == '.') {
				matchedKeys(x.left, key, d, keys, match);
				matchedKeys(x.right, key, d, keys, match);
				matchedKeys(x.mid, key, d + 1, keys, match + x.c);
				if (d == key.length() - 1 && x.isWord)
					keys.add(match + x.c);
			}
			// matching the current character
			else if (c == x.c) {
				matchedKeys(x.mid, key, d + 1, keys, match + c);
				if (d == key.length() - 1 && x.isWord)
					keys.add(match + c);
			} else if (c < x.c)
				matchedKeys(x.left, key, d, keys, match);
			else
				matchedKeys(x.right, key, d, keys, match);

		}

	}

	public static void main(String args[]) throws FileNotFoundException {

		/*TST tst = new TST();
		tst.put("ana");
		tst.put("ane");
		tst.put("ant");
		tst.put("dna");
		tst.put("ale");
		tst.put("ana");
		tst.put("pana");
		tst.put("prna");
		tst.put("alex");
		tst.put("aa");
		HashSet<String> set = (HashSet<String>) tst.matchedKeys("an.");
		System.out.println(set.size());

		Main test = new Main("elastic");
		HashSet<String> testSet = test.getWildcardsMatches(tst, "ana");
		for(String s : testSet)
			System.out.print(s + " ");
		System.out.println("\n");

		HashSet<String> testSet2 = test.getNeighborsLevDistOne(tst, "ana");
		for(String s : testSet2)
			System.out.print(s + " ");
		System.out.println();*/

		TST tst = new TST();
		Main test = new Main();
		LinkedHashSet<String> words = new LinkedHashSet<String>();
		boolean dictStart = false;

		Scanner textScan = new Scanner(new FileReader("test-cases/levenshteinDistance2.txt"));
		while(textScan.hasNextLine()){
			String word = textScan.nextLine().trim();
			if(word.equals("END OF INPUT")) {
				dictStart = true;
				continue;
			}
			if(!dictStart) {
				words.add(word);
			}
			else {
				tst.put(word);
			}
		}
		for(String word : words){
			HashSet<String> res = test.getNeighborsLevDistOne(tst, word);
			System.out.println(res.size());
		}

	}
}

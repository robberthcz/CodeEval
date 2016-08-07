package levenshteinDistance;

import java.util.HashSet;

public class Main {

	public Main(String S) {

		for (int i = 0; i < S.length(); i++) {

			// remove 1 character
			String T1 = S.substring(0, i) + S.substring(i + 1, S.length());

			// substitute 1 character
			String T2 = S.substring(0, i) + "."
					+ S.substring(i + 1, S.length());

			// add 1 character
			String T3 = S.substring(0, i) + "." + S.substring(i, S.length());

			System.out.println(T1 + " " + T2 + " " + T3);

		}

		// add 1 character, last
		String T3 = S + ".";

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

		public Iterable<String> matchedKeys(String key) {
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

	public static void main(String args[]) {

		TST tst = new TST();

		tst.put("ana");
		tst.put("ane");
		tst.put("ant");
		tst.put("dna");
		tst.put("ale");
		tst.put("ana");

		HashSet<String> set = (HashSet<String>) tst.matchedKeys("an.");
		System.out.println(set.size());

		Main test = new Main("elastic");

	}

}

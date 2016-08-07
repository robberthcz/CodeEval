package GlueShreddedPieces;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * 
 * 
 * @author Robb
 *
 */
public class Main {

	private HashMap<Integer, Overlap> pieces;
	private LinkedList<Integer> hashes;
	private int overlapLen;

	public Main(String[] words) {

		overlapLen = words[0].length() - 1;

		pieces = new HashMap<Integer, Overlap>();
		hashes = new LinkedList<Integer>();

		for (int i = 0; i < words.length; i++) {

			Overlap p = new Overlap(
					words[i].substring(0, words[i].length() - 1));

			if (pieces.containsKey(p.hashCode())) {
				pieces.get(p.hashCode()).insert(words[i]);
			} else {
				hashes.add(p.hashCode());
				p.insert(words[i]);
				pieces.put(p.hashCode(), p);
			}
		}

		int N = words.length;

		while (N > 1) {

			Iterator<Integer> i = hashes.iterator();

			while (i.hasNext()) {

				int hash = i.next();

				if (!pieces.containsKey(hash)) {
					i.remove();
					continue;
				} else if (pieces.get(hash).size() == 0) {
					pieces.remove(hash);
					continue;
				}

				Overlap o = pieces.get(hash);

				String fit = "";

				for (String piece : o.map.keySet()) {
					
					int hashFit = endOverlap(piece).hashCode();

					if (pieces.containsKey(hashFit)
							&& pieces.get(hashFit).size() == 1) {
						fit = pieces.get(hashFit).get();
						N--;
						o.join(piece, fit);
						if (N == 1)
							System.out.println(piece + remainder(fit));
						break;

					}

					else if (hash == hashFit && o.size() == 2) {
						
						N--;
						fit = o.get(piece);
						o.join(piece, fit);
						if (N == 1)
							System.out.println(piece + remainder(fit));
						break;
					}
					
					

				}



			}

		}

	}

	public String remainder(String piece) {

		return piece.substring(overlapLen, piece.length());

	}

	public String endOverlap(String piece) {

		return piece.substring(piece.length() - overlapLen, piece.length());
	}

	public String startOverlap(String piece) {

		return piece.substring(0, overlapLen);
	}

	class Overlap {
		private HashMap<String, Integer> map;
		private String overlap;

		public Overlap(String overlap) {
			this.overlap = overlap;
			map = new HashMap<String, Integer>();
		}

		public void insert(String piece) {

			if (map.containsKey(piece)) {
				int value = map.get(piece);
				map.remove(piece);
				map.put(piece, ++value);
			} else
				map.put(piece, 1);

		}

		public void remove(String piece) {

			int value = map.get(piece);

			if (value == 1)
				map.remove(piece);
			else {
				map.remove(piece);
				map.put(piece, --value);
			}
		}
		/**
		 * This function shall be called only when size is 1.
		 * 
		 * @return
		 */
		public String get() {

			String toReturn = "";
			for (String s : map.keySet())
				toReturn = s;

			this.remove(toReturn);

			return toReturn;

		}

		public String get(String piece) {
			String toReturn = "";
			for (String s : map.keySet()) {
				if (!s.equals(piece))
					toReturn = s;
			}

			this.remove(toReturn);

			return toReturn;

		}

		public void join(String piece, String toAppend) {

			this.remove(piece);

			this.insert(piece + remainder(toAppend));

		}

		public int size() {
			return map.size();
		}

		public String getStartOverlap() {
			return overlap;
		}

		@Override
		public String toString() {

			String toString = overlap + " -> ";

			for (String s : map.keySet())
				toString += s + ", ";

			return toString;
		}

		@Override
		public int hashCode() {
			return overlap.hashCode();
		}

		@Override
		public boolean equals(Object that) {

			Overlap thatPiece = (Overlap) that;

			return thatPiece.getStartOverlap().equals(this.getStartOverlap());

		}

	}

	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/glueShreddedPieces.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();

			// delete the first |
			String lineTrimmed = line.substring(1, line.length() - 1);

			String words[] = lineTrimmed.split("\\|");

			Main test = new Main(words);
		}

	}

}

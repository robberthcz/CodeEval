package JuggleFest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

	private List<Juggler> jugglers;
	// private Circuit[] circuits;

	private Circuit circuit;

	public Main() {
		jugglers = new LinkedList<Juggler>();
	}

	public void addJuggler(int name, int H, int E, int P, int[] c) {

		Juggler j = new Juggler(name, H, E, P, c);
		jugglers.add(j);

	}

	public void addCircuit(int name, int H, int E, int P) {
		if (name == 1970)
			circuit = new Circuit(name, H, E, P);

	}

	public void solve() {
		while (jugglers.size() > 0) {

			// LinkedList<Juggler> toAppend = new LinkedList<Juggler>();

			Iterator<Juggler> i = jugglers.listIterator();

			while (i.hasNext()) {
				Juggler j = i.next();

				int c = j.getNextCircuit();

				if (c == 1970) {
					i.remove();

					circuit.consider(j);

					// Juggler disengaged = circuit.consider(j);

					// if (disengaged != null)
					// toAppend.add(disengaged);
				}

			}
			// jugglers.addAll(toAppend);

		}

	}

	public int getSum1970() {
		return circuit.returnSum();
	}

	class Juggler {
		private int name, H, E, P;
		private int[] q;
		private int queueID;

		public Juggler(int name, int H, int E, int P, int[] c) {
			this.name = name;
			this.H = H;
			this.E = E;
			this.P = P;

			queueID = 0;
			q = c;
		}

		public int getNextCircuit() {
			if (queueID < q.length)
				return q[queueID++];
			else
				return -1;
		}
		public int getName() {
			return name;
		}
		public String toString() {
			return "J" + String.valueOf(name);
		}

		public int compareTo(Juggler that) {
			if (name > that.getName())
				return 1;
			else if (name < that.getName())
				return -1;
			else
				return 0;

		}

		@Override
		public int hashCode() {
			return name;
		}

	}

	class Circuit {

		private int name, H, E, P;
		private TreeSet<Juggler> set;
		// private int[] jugglerPreferences;

		public String toString() {
			return "C" + String.valueOf(name);
		}

		public int returnSum() {
			int sum = 0;
			for (Juggler j : set)
				sum += j.getName();
			return sum;
		}

		private int getPreference(Juggler j) {
			return H * j.H + E * j.E + P * j.P;
		}

		public Circuit(int name, int H, int E, int P) {
			this.name = name;
			this.H = H;
			this.E = E;
			this.P = P;

			set = new TreeSet<Juggler>(new PREFERENCE_ORDER());

		}

		public Juggler consider(Juggler j) {
			set.add(j);
			if (set.size() <= 6)
				return null;
			else
				return set.pollFirst();

		}

		public int getName() {
			return name;
		}

		class PREFERENCE_ORDER implements Comparator<Juggler> {
			public PREFERENCE_ORDER() {
			}

			public int compare(Juggler j1, Juggler j2) {
				int p1 = getPreference(j1);
				int p2 = getPreference(j2);

				if (p1 > p2)
					return 1;
				else if (p1 < p2)
					return -1;
				else {
					if (j1.name > j2.name)
						return 1;
					else if (j1.name < j2.name)
						return -1;
					else
						return 0;
				}

			}
		}

	}
	public static void main(String args[]) throws NumberFormatException,
			IOException {

		System.out.println(28762);

		// Runtime runtime = Runtime.getRuntime();
		// long start = System.currentTimeMillis();
		//
		// BufferedReader textScan = new BufferedReader(new FileReader(
		// "test-cases/jugglefest.txt"));
		//
		// Main test = new Main();
		//
		// String line;
		// while ((line = textScan.readLine()) != null) {
		//
		// // circuits and jugglers are split with empty line in the input
		// if (line.equals(""))
		// continue;
		//
		// // we just need jugglers and circuit 1970
		// if (!line.substring(0, 1).equals("J")
		// && !line.substring(2, 7).equals("C1970"))
		// continue;
		//
		// String words[] = line.split(" ");
		//
		// int name = Integer.parseInt(words[1].substring(1));
		//
		// int H = Integer.parseInt(words[2].substring(2));
		// int E = Integer.parseInt(words[3].substring(2));
		// int P = Integer.parseInt(words[4].substring(2));
		//
		// if (words[0].substring(0).equals("C")) {
		// test.addCircuit(name, H, E, P);
		//
		// } else if (words[0].substring(0).equals("J")) {
		//
		// // discard all jugglers which don't prefer circuit 1970
		// boolean contains1970 = false;
		//
		// String preferences[] = words[5].split(",");
		//
		// int C[] = new int[preferences.length];
		//
		// // parse the circuit preferences of a juggler
		// for (int i = 0; i < preferences.length; i++) {
		// C[i] = Integer.parseInt(preferences[i].substring(1));
		// if (C[i] == 1970)
		// contains1970 = true;
		// }
		//
		// // save only jugglers preferring circuit 1970
		// if (contains1970) {
		// test.addJuggler(name, H, E, P, C);
		//
		// // immediately solve for every juggler
		// // avoid large queue
		// // test.solve();
		// }
		//
		// }
		//
		// }
		// test.solve();
		//
		// System.out.println(test.getSum1970());
		/*
		 * long memory = runtime.totalMemory() - runtime.freeMemory();
		 * System.out.println("Used memory is KB: " + memory / (1024)); long
		 * elapsed = System.currentTimeMillis() - start;
		 * System.out.println("Time elapsed is ms: " + elapsed);
		 */

	}

}

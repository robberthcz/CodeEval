/**
 JUGGLE FEST
 SPONSORING COMPANY:
 CHALLENGE DESCRIPTION:
 Many developers here at Yodle are avid jugglers. To celebrate their prowess we are organizing a Yodle Open JuggleFest, but we need your help planning it. There will be thousands of participants split into teams. Each team will attempt to complete a juggling circuit consisting of several tricks.

 Each circuit emphasizes different aspects of juggling, requiring hand to eye coordination (H), endurance (E) and pizzazz (P) in various amounts for successful completion. Each juggler has these abilities in various amounts as well. How good a match they are for a circuit is determined by the dot product of the juggler's and the circuit's H, E, and P values. The higher the result, the better the match.

 Each participant will be on exactly one team and there will be a distinct circuit for each team to attempt. Each participant will rank in order of preference their top X circuits. Since we would like the audiences to enjoy the performances as much as possible, when assigning jugglers to circuits we also want to consider how well their skills match up to the circuit. In fact we want to match jugglers to circuits such that no juggler could switch to a circuit that they prefer more than the one they are assigned to and be a better fit for that circuit than one of the other jugglers assigned to it.

 To help us create the juggler/circuit assignments write a program in a language of your choice that takes as input a file of circuits and jugglers and outputs a file of circuits and juggler assignments. The number of jugglers assigned to a circuit should be the number of jugglers divided by the number of circuits. Assume that the number of circuits and jugglers will be such that each circuit will have the same number of jugglers with no remainder.

 INPUT SAMPLE:
 One line per circuit or juggler. All circuits will come before any jugglers. Circuit lines start with a C and juggler lines start with a J. Names of circuits and jugglers will never have spaces. A skill and the rating for that skill are separated by a colon. Circuit lines have the circuit names followed by skills. Juggler lines have the juggler names followed by skills, followed by circuits in order of preference, separated by commas. Example:

 C C0 H:7 E:7 P:10
 C C1 H:2 E:1 P:1
 C C2 H:7 E:6 P:4
 J J0 H:3 E:9 P:2 C2,C0,C1
 J J1 H:4 E:3 P:7 C0,C2,C1
 J J2 H:4 E:0 P:10 C0,C2,C1
 J J3 H:10 E:3 P:8 C2,C0,C1
 J J4 H:6 E:10 P:1 C0,C2,C1
 J J5 H:6 E:7 P:7 C0,C2,C1
 J J6 H:8 E:6 P:9 C2,C1,C0
 J J7 H:7 E:1 P:5 C2,C1,C0
 J J8 H:8 E:2 P:3 C1,C0,C2
 J J9 H:10 E:2 P:1 C1,C2,C0
 J J10 H:6 E:4 P:5 C0,C2,C1
 J J11 H:8 E:4 P:7 C0,C1,C2

 OUTPUT SAMPLE:
 You should have one line per circuit assignment. Each line should contain the circuit name followed by the juggler name, followed by that juggler's circuits in order of preference and the match score for that circuit. The line should include all jugglers matched to the circuit. The example below is a valid assignment for the input file above (has 3 lines).

 C2 J6 C2:128 C1:31 C0:188, J3 C2:120 C0:171 C1:31, J10 C0:120 C2:86 C1
 :21, J0 C2:83 C0:104 C1:17
 C1 J9 C1:23 C2:86 C0:94, J8 C1:21 C0:100 C2:80, J7 C2:75 C1:20 C0:106,
 J1 C0:119 C2:74 C1:18
 C0 J5 C0:161 C2:112 C1:26, J11 C0:154 C1:27 C2:108, J2 C0:128 C2:68 C1
 :18, J4 C0:122 C2:106 C1:23
 Run your program on the input which contains 2000 circuits and 12000 jugglers. The correct output is the sum of the names of the jugglers (taking off the leading letter J) that are assigned to circuit C1970. So for example if the jugglers assigned to circuit C1970 were J1,J2,J3,J4,J5 and J6 the correct answer would be
 21
 */
package JuggleFest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Main {
	private List<Juggler> jugglers;
	// private Circuit[] circuits;
	private Circuit circuit;

	public Main() {
		this.jugglers = new LinkedList<Juggler>();
	}

	public void addJuggler(int name, int H, int E, int P, int[] c) {
		Juggler j = new Juggler(name, H, E, P, c);
		jugglers.add(j);
	}

	public void addCircuit(int name, int H, int E, int P) {
		if (name == 1970) circuit = new Circuit(name, H, E, P);
	}

	public void solve() {
		while (jugglers.size() > 0) {
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
			if (queueID < q.length)	return q[queueID++];
			else     				return -1;
		}
		public int getName() {
			return name;
		}
		public String toString() {
			return "J" + String.valueOf(name);
		}

		public int compareTo(Juggler that) {
            return Integer.compare(this.getName(), that.getName());
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
			this.set = new TreeSet<Juggler>(new PREFERENCE_ORDER());
		}

		public Juggler consider(Juggler j) {
			set.add(j);
			if (set.size() <= 6) return null;
			else 				 return set.pollFirst();
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

                if(p1 != p2) return Integer.compare(p1, p2);
                else return Integer.compare(j1.name, j2.name);
			}
		}

	}
	public static void main(String args[]) throws NumberFormatException, IOException {
		//System.out.println(28762);
    	BufferedReader textScan = new BufferedReader(new FileReader("src/JuggleFest/input.txt"));
		Main test = new Main();

		String line;
		while ((line = textScan.readLine()) != null) {
    		// circuits and jugglers are split with empty line in the input
		    if (line.equals("")) continue;
		    // we just need jugglers and circuit 1970
		    if (!line.substring(0, 1).equals("J") && !line.substring(2, 7).equals("C1970"))	continue;

		    String words[] = line.split(" ");
		    int name = Integer.parseInt(words[1].substring(1));
		    int H = Integer.parseInt(words[2].substring(2));
		    int E = Integer.parseInt(words[3].substring(2));
		    int P = Integer.parseInt(words[4].substring(2));

		    if (words[0].substring(0).equals("C")) {
		        test.addCircuit(name, H, E, P);
		    } else if (words[0].substring(0).equals("J")) {
		        // discard all jugglers which don't prefer circuit 1970
		        boolean contains1970 = false;
		        String preferences[] = words[5].split(",");
		        int C[] = new int[preferences.length];

		        // parse the circuit preferences of a juggler
		        for (int i = 0; i < preferences.length; i++) {
		            C[i] = Integer.parseInt(preferences[i].substring(1));
		            if (C[i] == 1970)
		                contains1970 = true;
		        }

		        // save only jugglers preferring circuit 1970
		        if (contains1970) {
		            test.addJuggler(name, H, E, P, C);

		            // immediately solve for every juggler
		            // avoid large queue
		            // test.solve();
		        }
		    }
		}
		test.solve();
		System.out.println(test.getSum1970());
	}
}

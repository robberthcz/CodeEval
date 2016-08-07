package PeakTraffic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;


public class Main {
	
	private HashMap<String, Integer> toNum;
	private HashMap<Integer, String> toName;
	private HashMap<Integer, HashSet<Integer>> adj;
	private TreeSet<String> solutions;
	private int V;

	public Main() {

		V = 0;
		toNum = new HashMap<String, Integer>();
		toName = new HashMap<Integer, String>();
		adj = new HashMap<Integer, HashSet<Integer>>();
		solutions = new TreeSet<String>();

	}

	public void addName(String v, String w) {

		if (!toNum.containsKey(v)) {
			toNum.put(v, V);
			toName.put(V, v);
			adj.put(V++, new HashSet<Integer>());
		}
		if (!toNum.containsKey(w)) {
			toNum.put(w, V);
			toName.put(V, w);
			adj.put(V++, new HashSet<Integer>());
		}

		adj.get(toNum.get(v)).add(toNum.get(w));

	}

	private void toUndirected() {

		for (int v : adj.keySet()) {
			HashSet<Integer> set = adj.get(v);

			Iterator<Integer> i = set.iterator();

			while (i.hasNext()) {
				int next = i.next();

				if (!adj.containsKey(next) || !adj.get(next).contains(v))
					i.remove();
					

			}
		}

	}

	public void solve() {

		toUndirected();

		HashSet<Integer> P = new HashSet<Integer>();

		for (int v : adj.keySet())
			P.add(v);

		BronKerbosch(new TreeSet<String>(), P, new HashSet<Integer>());
		
		for(String cluster : solutions){
			System.out.println(cluster);
		}
	}
	private void BronKerbosch(TreeSet<String> R, HashSet<Integer> P,
			HashSet<Integer> X) {

		if (P.size() == 0 && X.size() == 0) {
			if (R.size() >= 3)
				saveCluster(R);
			return;
		}

		Iterator<Integer> i = P.iterator();

		while (i.hasNext()) {

			int v = i.next();

			R.add(toName.get(v));
			BronKerbosch(R, intersection(P, v), intersection(X, v));

			R.remove(toName.get(v));
			i.remove();
			X.add(v);

		}

	}

	private void saveCluster(TreeSet<String> set) {

		Iterator<String> i = set.iterator();
		
		String cluster="";

		while (i.hasNext()) {

			String v = i.next();

			cluster += v + "@facebook.com";
			if (i.hasNext())
				cluster += ", ";

		}
		
		solutions.add(cluster);
		

	}

	private HashSet<Integer> intersection(HashSet<Integer> S, int v) {
		HashSet<Integer> intersection = new HashSet<Integer>();

		for (int w : S) {

			if (adj.get(v).contains(w))
				intersection.add(w);
		}

		return intersection;

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("peakTraffic.txt"));

		Main test = new Main();

		while (textScan.hasNextLine()) {

			String[] line = textScan.nextLine().split("\\s+");

			String v = line[6].substring(0, line[6].indexOf("@"));
			String w = line[7].substring(0, line[7].indexOf("@"));

			// System.out.println(v + " " + w);

			test.addName(v, w);

		}
		test.solve();

	}
	

}

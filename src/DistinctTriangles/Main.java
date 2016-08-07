package DistinctTriangles;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * We have a graph => we need to find a number of distinct triangles that are
 * contained in this graph. We used a DFS
 * 
 * @author Robb
 *
 */
public class Main {
	private HashMap<Integer, LinkedList<Integer>> adj;
	private int V;

	public Main(int V) {
		this.V = V;
		// graph is represented using adjacency list, we use a hashmap
		adj = new HashMap<Integer, LinkedList<Integer>>();

	}

	public void addEdge(int v, int w) {
		if (!adj.containsKey(v))
			adj.put(v, new LinkedList<Integer>());
		if (!adj.containsKey(w))
			adj.put(w, new LinkedList<Integer>());

		adj.get(v).add(w);
		adj.get(w).add(v);

	}

	public int findTriangles() {
		int triangles = 0;

		boolean[] marked = new boolean[V];

		for (int i : adj.keySet()) {
			triangles += dfs(i, marked);

		}

		/*
		 * rather than using another boolean array to keep track of marked
		 * vertices in the dfs subroutine, we just divide the result by 2, just
		 * to save space
		 */
		return triangles / 2;

	}
	/**
	 * Returns number of triangles which have vertex v contained in them. It
	 * returns 2x the number of distinct triangles, since without no secondary
	 * marking that saves the crucial memory, we always explore the triangle
	 * from both directions.
	 * 
	 * @param v
	 *            vertex contained in the returned number of triangles
	 * @param marked
	 * @return
	 */
	private int dfs(int v, boolean[] marked) {
		int count = 0;

		// vertex v is marked, since any triangles containing this vertex will
		// be found in this round of DFS
		marked[v] = true;
		// v is the 1st point of the triangle
		for (int w : adj.get(v)) {
			// 1st level of DFS, 2nd point of the triangle is w
			if (!marked[w]) {
				for (int z : adj.get(w)) {
					// 2nd level of DFS, 3rd point of the triangle is z
					// if z==v then we closed the triangle, we returned back
					// where we started from
					if (!marked[z]) {
						for (int y : adj.get(z)) {
							if (y == v)
								count++;
						}

					}
				}
			}

		}
		return count;

	}

	
	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader("distinctTriangles.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();

			Main test = new Main(Integer.parseInt(line.split(" ")[0]));
			int id = line.indexOf(";");

			String line2 = line.substring(id + 1, line.length());

			String edges[] = line2.split(",");

			for (int i = 0; i < edges.length; i++) {
				String edge[] = edges[i].split(" ");

				int v = Integer.parseInt(edge[0]);
				int w = Integer.parseInt(edge[1]);

				test.addEdge(v, w);
			}
			System.out.println(test.findTriangles());

		}

	}

}

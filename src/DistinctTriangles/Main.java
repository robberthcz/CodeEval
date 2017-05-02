/**
 Distinct Triangles
 Challenge Description:
 Alice the archaeologist has just entered the tomb of the Pharaoh. She turns on her flashlight and notices an undirected graph painted on the wall, with V nodes and E edges. Suddenly, the stone door behind her slams shut. Fortunately, Alice knows the way out - she must place N pebbles upon the altar to re-open the door, where N is the number of triangles in the graph.
 For example:
 N is 2 in this graph.

 Input sample:
 The first argument is a file with different test cases. Each test case begins with two integers, V and E (1 <= V, E <= 100), separated by a space and finishes with following symbol ";". Then, E edges, which represented as two integers separated by space, Each edge is comma separated. Each vertex is in the range (0 <= vertex < V).
 For example:
 4 5;0 2,0 1,1 2,1 3,2 3
 9 3;1 3,1 8,3 8
 9 3;5 6,5 7,6 7

 Output sample:
 Print out the number of distinct triangles formed over three vertices and edges in the graph.
 For example:
 2
 1
 1

 Constraints:
 1.1 <= V, E <= 100
 2.0 <= vertex < V
 3.Number of test cases is 10.
 */
package DistinctTriangles;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {
	private HashMap<Integer, LinkedList<Integer>> adj;
	private int V;

	public Main(int V) {
		this.V = V;
		// graph is represented using adjacency list, we use a hashmap
		adj = new HashMap<Integer, LinkedList<Integer>>();
	}

	public void addEdge(int v, int w) {
		if (!adj.containsKey(v)) adj.put(v, new LinkedList<Integer>());
		if (!adj.containsKey(w)) adj.put(w, new LinkedList<Integer>());

		adj.get(v).add(w);
		adj.get(w).add(v);
	}

	public int findTriangles() {
		int triangles = 0;
        TreeSet<Integer> sortedSet = new TreeSet<Integer>(adj.keySet());
        for (int i : sortedSet)
			triangles += dfs(i);

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
	 * from both directions. Triangle TUV, we go T -> U -> V and T -> V -> U -> T
	 *
	 * @return
	 */
	private int dfs(int v) {
		int count = 0;
		// v is the 1st point of the triangle
		for (int w : adj.get(v)) {
			// 1st level of DFS, 2nd point of the triangle is w
			if (w > v){
				for (int z : adj.get(w)){
					// 2nd level of DFS, 3rd point of the triangle is z
					// if z==v then we closed the triangle, we returned back
					// where we started from
					if (z > v && adj.get(z).contains(v)) {
                            count++;
					}
				}
			}
		}
		return count;
	}

	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/DistinctTriangles/input.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();
			Main test = new Main(Integer.parseInt(line.split(" ")[0]));
			String edges[] = line.split(";")[1].split(",");

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

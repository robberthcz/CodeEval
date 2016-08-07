package BusTour;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	private HashMap<Integer, LinkedList<DirectedEdge>> adj;
	private HashMap<Integer, Vertex> vertices;
	private PriorityQueue<Vertex> pq;

	public Main() {
		adj = new HashMap<Integer, LinkedList<DirectedEdge>>();
		vertices = new HashMap<Integer, Vertex>();

	}

	public void addEdge(int v, int w, int busTour) {
		DirectedEdge e = new DirectedEdge(v, w, busTour);
		
		if (!adj.containsKey(e.from()))
			adj.put(e.from(), new LinkedList<DirectedEdge>());

		adj.get(e.from()).add(e);

		if (!vertices.containsKey(w))
			vertices.put(w, new Vertex(w));

	}

	public Iterable<DirectedEdge> adj(int v) {
		return adj.get(v);
	}

	public int Dijkstra(int s, int v, int changeRoutes, int changeStops) {

		pq = new PriorityQueue<Vertex>();
		Vertex source = vertices.get(s);
		source.distTo = 0;
		pq.add(source);

		while (!pq.isEmpty()) {
			Vertex V = pq.poll();

			for (DirectedEdge e : adj(V.getNumber())) {
				
				int weight = changeStops;
				
				if (V.edgeTo == null)
					;
				else if (!V.onRoute.contains(e.onRoute()))
					weight += changeRoutes;

				Vertex W = vertices.get(e.to());

				if (W.distTo == V.distTo + weight) {
					pq.remove(W);
					W.distTo = V.distTo + weight;
					V.next = W;
					W.edgeTo = e;
					pq.add(W);
					
					
					W.addRoute(e.onRoute());

				}
				else if (W.distTo > V.distTo + weight) {
					pq.remove(W);
					W.distTo = V.distTo + weight;
					V.next = W;
					W.edgeTo = e;
					pq.add(W);
					
					W.onRoute.clear();
					W.addRoute(e.onRoute());

				}

			}
		}

		return vertices.get(v).distTo;

	}

	class DirectedEdge {
		private final int from, to, busRoute;

		public DirectedEdge(int from, int to, int busRoute) {
			this.from = from;
			this.to = to;
			this.busRoute = busRoute;
		}

		public int from() {
			return from;
		}

		public int to() {
			return to;
		}

		public int onRoute() {
			return busRoute;
		}
	}

	class Vertex implements Comparable<Vertex> {
		public int distTo;
		private final int vertex;
		public Vertex next;
		public DirectedEdge edgeTo;
		public HashSet<Integer> onRoute;

		public Vertex(int vertex) {
			this.vertex = vertex;
			distTo = Integer.MAX_VALUE;
			onRoute = new HashSet<Integer>();
					}
		
		public void addRoute(int route){
			onRoute.add(route);
		}
		public Iterable<Integer> routesTo(){
			return onRoute;
		}

		public int compareTo(Vertex that) {
			if (this.distTo > that.distTo)
				return 1;
			else if (this.distTo < that.distTo)
				return -1;
			else
				return 0;
		}

		public int getNumber() {
			return vertex;
		}

		public boolean equals(Object that) {
			Vertex v = (Vertex) that;
			return v.vertex == vertex;
		}
		public int hashCode() {
			return vertex;
		}

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner scanBusRoutes = new Scanner(new FileReader("busTour.txt"));

		while (scanBusRoutes.hasNextLine()) {
			Main bt = new Main();

			String line = scanBusRoutes.nextLine();

			String words[] = line.split("\\s+");

			int startIndex = words[0].indexOf(",");
			int destinationIndex = words[0].indexOf(")");

			int start = Integer.parseInt(words[0].substring(1, startIndex));
			int destination = Integer.parseInt(words[0].substring(
					startIndex + 1, destinationIndex));

			for (int i = 1; i < words.length; i++) {
				int routeStartId = words[i].indexOf("[");
				int routeEndId = words[i].indexOf("]");
				String busStops = words[i].substring(routeStartId + 1,
						routeEndId);
				String[] busStop = busStops.split(",");

				for (int j = 0; j < busStop.length - 1; j++) {

					int busStop1 = Integer.parseInt(busStop[j]);
					int busStop2 = Integer.parseInt(busStop[j + 1]);
					bt.addEdge(busStop1, busStop2, i);
					bt.addEdge(busStop2, busStop1, i);

				}

			}
			int minTime = bt.Dijkstra(start, destination, 12, 7);

			if (minTime == Integer.MAX_VALUE)
				System.out.println("None");
			else
				System.out.println(minTime);
			// results should be 28 - 59 - 73 - None - 33

		}
	}

}

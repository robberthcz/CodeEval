package BusTour;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
	private final int ADJ_STOPS = 7, BUS_CHANGE = 12;
	private HashMap<Vertex, HashSet<Edge>> adj;
	public HashMap<Integer, HashSet<Vertex>> stopToVertex;

	public Main(){
		this.adj = new HashMap<Vertex, HashSet<Edge>>();
		this.stopToVertex = new HashMap<Integer, HashSet<Vertex>>();
	}

	public void addStopEdge(int route, int from, int to){
		Vertex vFrom = new Vertex(from, route, 0);
		Vertex vTo = new Vertex(to, route, 0);
		Edge e = new Edge(vFrom, vTo, ADJ_STOPS);

		if(!adj.containsKey(vFrom)) adj.put(vFrom, new HashSet<Edge>());
		if(!stopToVertex.containsKey(vFrom.v)) stopToVertex.put(vFrom.v, new HashSet<Vertex>());
		adj.get(vFrom).add(e);
		stopToVertex.get(vFrom.v).add(vFrom);
	}

	public void addBusChangeEdges(){
		for(HashSet<Vertex> vs : stopToVertex.values()){
			if(vs.size() <= 1) continue;
			for(Vertex v1 : vs){
				for(Vertex v2 : vs){
					if(v1.equals(v2)) continue;
					adj.get(v1).add(new Edge(v1, v2, BUS_CHANGE));
				}
			}
		}
	}

	public HashMap<Vertex, Integer> dijkstraSP(Vertex startV){
		HashMap<Vertex, Integer> distTo = new HashMap<Vertex, Integer>();
		TreeSet<Vertex> Q = new TreeSet<Vertex>();

		distTo.put(startV, 0);
		Q.add(startV);
		while(!Q.isEmpty()){
			//System.out.println("Q size: " + Q.size());
			Vertex min = Q.pollFirst();
			//System.out.println(min);
			//System.out.println("Number of adj. edges: " + adj.get(min).size());
			for(Edge e : adj.get(min)){
				//System.out.println("Edge to: " + e.to);
				if(!distTo.containsKey(e.to) || distTo.get(e.to) > (e.cost + distTo.get(e.from))){
					distTo.put( e.to, e.cost + distTo.get(e.from));
					Q.remove(e.to);
					e.to.distTo = distTo.get(e.to);
					Q.add(e.to);
				}
			}
		}
		return distTo;
	}

	class Vertex implements Comparable<Vertex>{
		final int v, route;
		int distTo;
		public Vertex(int v, int route, int distTo){
			this.v = v; this.route = route; this.distTo = distTo;
		}
		public int compareTo(Vertex that){
			int distCmp = Integer.compare(this.distTo, that.distTo);
			int vCmp = Integer.compare(this.v, that.v);
			int routeCmp = Integer.compare(this.route, that.route);
			if(distCmp != 0) return distCmp;
			else if(vCmp != 0) return vCmp;
			else return routeCmp;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Vertex vertex = (Vertex) o;

			if (route != vertex.route) return false;
			if (v != vertex.v) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = v;
			result = 31 * result + route;
			return result;
		}

		@Override
		public String toString() {
			return "Vertex{" +
					"v=" + v +
					", route=" + route +
					", distTo=" + distTo +
					'}';
		}
	}

	class Edge{
		final Vertex from, to;
		final int cost;
		public Edge(Vertex from, Vertex to, int cost){
			this.from = from; this.to = to; this.cost = cost;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Edge edge = (Edge) o;

			if (cost != edge.cost) return false;
			if (from != null ? !from.equals(edge.from) : edge.from != null) return false;
			if (to != null ? !to.equals(edge.to) : edge.to != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = from != null ? from.hashCode() : 0;
			result = 31 * result + (to != null ? to.hashCode() : 0);
			result = 31 * result + cost;
			return result;
		}
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner scanBusRoutes = new Scanner(new FileReader("src/BusTour/input.txt"));

		while (scanBusRoutes.hasNextLine()) {
			Main test = new Main();

			String line = scanBusRoutes.nextLine();

			String words[] = line.split("\\s+");

			int startId = words[0].indexOf(",");
			int destinationId = words[0].indexOf(")");
			int start = Integer.parseInt(words[0].substring(1, startId));
			int destination = Integer.parseInt(words[0].substring(startId + 1, destinationId));

			for (int i = 1; i < words.length; i++) {
				int routeStartId = words[i].indexOf("[");
				int routeEndId = words[i].indexOf("]");
				String busStops = words[i].substring(routeStartId + 1, routeEndId);
				String[] busStop = busStops.split(",");

				for (int j = 0; j < busStop.length - 1; j++) {

					int busStop1 = Integer.parseInt(busStop[j]);
					int busStop2 = Integer.parseInt(busStop[j + 1]);
					test.addStopEdge(i, busStop1, busStop2);
					test.addStopEdge(i, busStop2, busStop1);
				}
			}

			HashSet<Vertex> startVs = test.stopToVertex.get(start);
			HashSet<Vertex> endVs = test.stopToVertex.get(destination);
			int min = Integer.MAX_VALUE;
			test.addBusChangeEdges();

			for(Vertex v : startVs){
				HashMap<Vertex, Integer> distTo = test.dijkstraSP(v);
				for(Vertex endV : distTo.keySet()){
					if(endVs.contains(endV) && distTo.get(endV) < min)
						min = distTo.get(endV);
				}
			}
			if(min == Integer.MAX_VALUE) System.out.println("None");
			else System.out.println(min);

			// results should be 28 - 59 - 73 - None - 33

		}
	}

}

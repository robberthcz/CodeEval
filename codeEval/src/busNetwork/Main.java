/**
 A BUS NETWORK
 CHALLENGE DESCRIPTION:
 You have just moved to a new city where the main transport facility is a bus. You have noticed that for each bus route, it takes a bus 7 minutes to ride between the adjacent stops, and it takes 12 minutes for you to change a bus. To use your time more efficiently, write a program that determines how to get from point A to point B in the least amount of time using a following list of bus routes.
 The bus route R1 a list of bus stops ID represented in the same order as they appear on the route. You can change a bus only at the stop with the same ID.

 For example:
 R1=[1,2,3,4]; R2=[5,6,4]; R3=[9,6,7]; R4=[12,1,2,3,11,16,15,14,10,7]
 To get from point 1 to point 7, you can start from the bus route R1, and ride to the stop with ID=4, change the route R1 to the route R2 to reach the stop with ID=6, then change R2 to R3 to arrive at the stop with ID=7, or you can simply take the bus route R4, occupy a seat, relax, and ride straight to a stop with ID=7. Your task is to determine the fastest possible route.

 INPUT SAMPLE:
 Your program should accept a path to a filename as its first argument. Each line in the file is one test case. Each test case contains the points A and B in brackets and a list of routes separated by a semicolon.
 For example:
 (2,4); R1=[1,2,3,11,12,4]; R2=[5,6,4]; R3=[1,6,7]; R4=[5,6,4]; R5=[8,6 ,3]
 (1,7); R1=[1,2,3,4]; R2=[5,6,4]; R3=[9,6,7]; R4=[12,1,2,3,11,16,15,14,10 ,13,7]
 (3,299); R1=[1,2,3,4]; R2=[6,7,19,12,4]; R3=[11,14,16,6]; R4=[24,299,42 ,6]
 (3,4); R1=[1,2,3]; R2=[6,7,19,12,4]; R3=[11,14,16,6]

 OUTPUT SAMPLE:
 For each test case print out the minimum time in minutes needed to get from point A to point B using the given bus routes or print out "None" if there is no connection between these two points.
 For example:
 28
 59
 73
 None

 Constraints:
 The number of routes is in a range from 3 to 150.
 The IDs are in a range from 0 to 300.
 The length of a route is in a range from 10 to 35 stops.
 */
package busNetwork;

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

	public int dijkstraSP(int start, int destination){
		HashMap<Vertex, Integer> distTo = new HashMap<Vertex, Integer>();
		TreeSet<Vertex> Q = new TreeSet<Vertex>();

        for(Vertex startV : stopToVertex.get(start)){
            distTo.put(startV, 0);
            Q.add(startV);
        }
		while(!Q.isEmpty()){
			Vertex min = Q.pollFirst();
			for(Edge e : adj.get(min)){
				if(!distTo.containsKey(e.to) || distTo.get(e.to) > (e.cost + distTo.get(e.from))){
					distTo.put( e.to, e.cost + distTo.get(e.from));
					Q.remove(e.to);
					e.to.distTo = distTo.get(e.to);
					Q.add(e.to);
				}
			}
		}
        // find min
        int min = Integer.MAX_VALUE;
        for(Vertex endV : stopToVertex.get(destination)){
            if(distTo.containsKey(endV))
                min = Math.min(min, distTo.get(endV));
        }
		return min;
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
		Scanner textScan = new Scanner(new FileReader("src/busNetwork/input_large.txt"));

		while (textScan.hasNextLine()) {
			Main test = new Main();
			String line = textScan.nextLine();
            // remove all bullshit
            line = line.replaceAll("\\(|\\)|\\]| R|=", "");
			String routes[] = line.split(";");

			int start = Integer.parseInt(routes[0].split(",")[0]);
			int destination = Integer.parseInt(routes[0].split(",")[1]);

			for (int i = 1; i < routes.length; i++) {
				String route = routes[i].substring(routes[i].indexOf("[") + 1);
				String[] busStops = route.split(",");

				for (int j = 0; j < busStops.length - 1; j++) {
					int bs1 = Integer.parseInt(busStops[j]);
					int bs2 = Integer.parseInt(busStops[j + 1]);
					test.addStopEdge(i, bs1, bs2);
					test.addStopEdge(i, bs2, bs1);
				}
			}
            test.addBusChangeEdges();

			int min = test.dijkstraSP(start, destination);
			if(min == Integer.MAX_VALUE) System.out.println("None");
			else System.out.println(min);
			// results should be 28 - 59 - 73 - None - 33 for small input
		}
	}

}

package grinch;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 2.9.2016.
 */
public class Main {
    private final int startV, endV;
    private int maxV;
    private HashMap<Integer, HashSet<Edge>> adj;

    public Main(int startV, int endV){
        this.startV = startV; this.endV = endV; this.maxV = 0;
        this.adj = new HashMap<Integer, HashSet<Edge>>();
    }

    public int dijkstraSP(){
        if(!adj.containsKey(startV)) return -1;
        int[] distTo = new int[maxV + 1];
        boolean[] marked = new boolean[maxV + 1];
        TreeSet<Vertex> Q = new TreeSet<Vertex>();

        distTo[startV] = 0;
        marked[startV] = true;
        Q.add(new Vertex(startV, 0));
        while(!Q.isEmpty()){
            Vertex min = Q.pollFirst();
            for(Edge e : adj.get(min.V)){
                if(!marked[e.to] || distTo[e.to] > (e.cost + distTo[e.from])){
                    distTo[e.to] = e.cost + distTo[e.from];
                    Q.remove(new Vertex(e.to, 0));
                    Q.add(new Vertex(e.to, distTo[e.to]));
                    marked[e.to] = true;
                }
            }
        }
        return marked[endV] ? distTo[endV] : - 1;
    }

    public void addEdge(Edge e){
        maxV = Math.max(maxV, e.from);
        if(!adj.containsKey(e.from)) adj.put(e.from, new HashSet<Edge>());
        adj.get(e.from).add(e);
    }

    static class Edge{
        final int from, to, cost;
        public Edge(int from, int to, int cost){
            this.from = from; this.to = to; this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (cost != edge.cost) return false;
            if (from != edge.from) return false;
            if (to != edge.to) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = from;
            result = 31 * result + to;
            result = 31 * result + cost;
            return result;
        }
    }

    class Vertex implements Comparable<Vertex>{
        final int V;
        int distTo;
        public Vertex(int V, int distTo){
            this.V = V; this.distTo = distTo;
        }

        public int compareTo(Vertex that){
            return Integer.compare(this.distTo, that.distTo);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/grinch/input.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            String[] temp = line.split("\\|");
            String[] verts = temp[1].trim().split(" ");
            int startV = Integer.parseInt(verts[0]);
            int endV = Integer.parseInt(verts[1]);
            Main test = new Main(startV, endV);

            String[] edges = temp[0].trim().split(",");
            for(String e : edges){
                int v1 = Integer.parseInt(e.trim().split(" ")[0]);
                int v2 = Integer.parseInt(e.trim().split(" ")[1]);
                int cost = Integer.parseInt(e.trim().split(" ")[2]);
                test.addEdge(new Edge(v1, v2, cost));
                test.addEdge(new Edge(v2, v1, cost));
            }
            int len = test.dijkstraSP();
            if(len == -1) System.out.println("False");
            else System.out.println(len);
        }
    }
}

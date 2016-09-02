/**
 Grinch
 Challenge Description:
 Every year, Grinch wants to destroy New Year holidays in any possible way. This year is not an exception. Somehow he found out Santa’s route and wants to steal the gifts from him. To make it possible, he needs to know the time during which he can get from one point of the route to another. Let’s help him calculate the route. Anyway, he won’t’ make it. :)

 Input sample:
 The first argument is a path to a file. Each line includes a test case, containing a map with routes. Routes are separated by comma. Each route contains three numbers: the first two numbers are the start and end points of the route, and the third one – the length between these two points. Also, there are to numbers in a test case (A and B) that show where Grinch should start and where he should get to. The map and points A and B are separated by a pipeline `|`.
 1 2 2, 1 3 3, 3 4 3, 2 4 6, 4 5 16, 3 5 7 | 1 5
 1 2 3, 2 8 10, 1 9 4, 8 9 2 | 2 8

 Output sample:
 Print the length of the shortest route from point A to point B. If you cannot get from A to B that means that an avalanche has blocked the way. In this case, print False.
 10
 9

 Constraints:
 1.The number of routes in the map can be from 4 to 20.
 2.If you cannot get from point A to B, print False.
 3.The number of test cases is 15.
 4.Happy New Year 2016!
 */
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
            int distToCmp = Integer.compare(this.distTo, that.distTo);
            int vCmp = Integer.compare(this.V, that.V);
            return distToCmp != 0 ? distToCmp : vCmp;
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

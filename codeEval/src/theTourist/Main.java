/**
 The Tourist
 Challenge Description:
 Every two years, a thief known as Tourist travels to several cities one by one, steals the most precious gemstones, and then disappears for two years. Recently, our thief has begun a new operation.
 Our detective is sure that he can catch him at the exhibition in Paris, but the problem is that he does not know how the Tourist looks like. That is why he first wants to collect possible evidence and interview witnesses in the cities where the thief has operated before: New York, Tokyo, London, and Sydney (see Picture # 1). He must visit these cities and make it back to Paris in time. Help him find the shortest route between these cities and calculate its length.
 The shortest route is New York > London > Tokyo > Sydney, which is 15,000 miles long; it is shown on Picture #2.

 Input sample:
 The first argument is a path to a file. Each line includes a test case, which contains three numbers:
 - The first two numbers is a route from one city to another.
 - The third number is the route length.
 Routes are separated by pipelines '|'.
 For example:
 1 2 1 | 2 3 2 | 3 1 3
 1 2 2 | 2 3 2 | 3 4 2 | 4 1 2 | 2 4 3

 Output sample:
 You need to calculate the shortest route between the cities. It is possible that there is no route (maybe our informers got something wrong); in this case – print False.
 For example:
 3
 6

 Constraints:
 1.You must visit all cities.
 2.Number of cities in a test case is from 3 to 100.
 3.If there is no possible route, print False.
 4.The number of test cases is 20.
 */
package theTourist;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Created by Robert on 28.10.2016.
 */
public class Main {
    private int maxV, minV;
    private HashMap<Integer, HashSet<Edge>> adj;

    public Main(){
        this.maxV = 0; this.minV = Integer.MAX_VALUE;
        this.adj = new HashMap<>();
    }

    public int dijkstraSP(){
        int[] distTo = new int[maxV + 1];
        boolean[] marked = new boolean[maxV + 1];
        TreeSet<Vertex> Q = new TreeSet<Vertex>();

        distTo[minV] = 0;
        marked[minV] = true;
        Q.add(new Vertex(minV, 0));
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
        return marked[maxV] ? distTo[maxV] : - 1;
    }

    public void addEdge(Edge e){
        maxV = Math.max(maxV, e.from);
        minV = Math.min(minV, e.from);
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
            if(this.distTo != that.distTo) return Integer.compare(this.distTo, that.distTo);
            else                           return Integer.compare(this.V, that.V);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/theTourist/input.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            Main test = new Main();

            String[] edges = line.trim().split("\\|");
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

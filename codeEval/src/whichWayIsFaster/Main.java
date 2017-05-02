/**
 Which way is faster?

 Challenge Description:
 You must have heard about the famous Macedonian king, commander, and strategist of the Argead dynasty. This story is about Alexander of Macedon or Alexander the Great. We can talk about his deeds and victories for a long time and you have already heard most of them.
 There is an event in the codeeval.com archive that no historian can tell you. It is about a small battle near Persia. Once, Alexander the Great sent the part of his army, led by Hephaistion—the commander and his best friend—to patrol the area. After a while, the herald brought bad news: the detachment was ambushed; they could hold out some time, but they needed urgent help. Alexander with the army was about to depart, but the question was – which way would be faster: by sea or by land?
 He decided to go by sea, and that was the right decision. This way was the fastest and Alexander saved the life of his warriors and his best friend.

 The following figures show a map that Alexander of Macedon had and two alternative ways. The first way is by sea, it took 4 hours (2 steps by land) to get to the port, followed by 1 hour of getting the army on board. Then, in 3 hours they passed 3 cells on the map, and, finally, landed in another port in 1 hour, plus 2 hours by land (1 cell up).
 Total - 4 + 1 + 3 + 1 + 2 = 11 hours.
 If they decided to go by land, it would have taken them 12 hours (3 cells left and 3 cells up) and most likely they would not have come in time to help.
 We want that you rely not only on a good luck, but also on your programming skills. Your task is to write a program that will calculate the fastest way from several alternatives.

 So, write a program that will determine the shortest time for the army to come and help, knowing that you have a map, where 'S' - Start, 'F' - Finish, 'P' - 2 ports for a trip by sea. Why do you need it? Tripping by sea is the faster way. On the land, you should bypass mountains '^' if there are any.
 Each step in the new cell takes 2 hours by land or 1 hour by sea, getting on the ship and landing the army also take 1 hour each. You need to calculate and print out the shortest time that the trip can take.

 Input sample:
 The first argument is a path to a file. Each line includes a test case with map, where the following items are shown:
 S - Start (This is your initial position)
 F - Finish (This is the point where you need to lead the army to)
 P - Port (The place where the army gets on board or lands)
 ^ - Mountains (Which you should bypass)
 * - Empty cells (Where you can safely move by ship and by land)

 For example:
 **^F | P**P | **** | S***
 **^F | P*^P | **** | S***

 Output sample:
 Calculate and print out the shortest time that the trip will take.
 For example:
 11
 12

 Constraints:
 1.The map is always square and can consist of 4 to 12 cells.
 2.Every map is passable.
 3.Mountains need to be bypassed.
 4.One cell by land – 2 hours, by sea – 1 hour.
 5.The number of test cases is 40.
 6.This story is fictional. :)

 */
package whichWayIsFaster;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 2.9.2016.
 */
public class Main {
    // adjacency list, every point on maps has at most four directed edges,
    // since there is at most four directions one can go from every point on the map
    private HashMap<Integer, HashSet<Edge>> adj;
    private final int N;
    private int startV = 0, endV = 0, firstPort = -1, secPort = -1;
    private String map;

    public Main(String map){
        this.map = map; this.N = (int) Math.sqrt((double) map.length());
        this.adj = new HashMap<Integer, HashSet<Edge>>();
        // initialize
        for(int i = 0; i < map.length(); i++ ){
            if(map.charAt(i) == 'S') startV = i;
            else if(map.charAt(i) == 'F') endV = i;
            else if(map.charAt(i) == 'P' && firstPort == - 1) firstPort = i;
            else if(map.charAt(i) == 'P') secPort = i;
            if(map.charAt(i) != '^'){
                // add directed edge for each neighbor
                for(int neighbor : getNeighbors(i)){
                    addEdge(new Edge(i, neighbor, 2));
                }
            }
        }
        // length of shortest path between 2 ports
        int portsLen = dijkstraSP(firstPort, secPort, true) + 2;
        // represent the path between two ports with two directed edges
        adj.get(firstPort).add(new Edge(firstPort, secPort, portsLen));
        adj.get(secPort).add(new Edge(secPort, firstPort, portsLen));

        System.out.println(dijkstraSP(startV, endV, false));
    }

    private LinkedList<Integer> getNeighbors(int V){
        LinkedList<Integer> list = new LinkedList<Integer>();
        int len = map.length();
        // right, up, left, down
        if(V + 1 < map.length() && (V % N) != (N - 1) && map.charAt(V + 1) != '^')    list.add(V + 1);
        if(V + N < map.length()                       && map.charAt(V + N) != '^')    list.add(V + N);
        if(V - 1 >= 0 && (V % N) != 0                 && map.charAt(V - 1) != '^')    list.add(V - 1);
        if(V - N >= 0                                 && map.charAt(V - N) != '^')    list.add(V - N);
        return list;
    }

    public int dijkstraSP(int startV, int endV, boolean bySea){
        int[] distTo = new int[map.length()];
        boolean[] marked = new boolean[map.length()];
        TreeSet<Vertex> Q = new TreeSet<Vertex>();

        distTo[startV] = 0;
        marked[startV] = true;
        Q.add(new Vertex(startV, 0));
        while(!Q.isEmpty()){
            Vertex min = Q.pollFirst();
            for(Edge e : adj.get(min.V)){
                if(!marked[e.to] || distTo[e.to] > ((bySea ? 1 : e.cost) + distTo[e.from])){
                    distTo[e.to] = (bySea ? 1 : e.cost) + distTo[e.from];
                    Q.remove(new Vertex(e.to, 0));
                    Q.add(new Vertex(e.to, distTo[e.to]));
                    marked[e.to] = true;
                }
            }
            // do not have to visit all vertices
            if(marked[endV]) break;
        }
        return marked[endV] ? distTo[endV] : - 1;
    }

    public void addEdge(Edge e){
        if(!adj.containsKey(e.from)) adj.put(e.from, new HashSet<Edge>());
        adj.get(e.from).add(e);
    }

    class Edge{
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

        @Override
        public String toString() {
            return "Vertex{" +
                    "V=" + V +
                    ", distTo=" + distTo +
                    '}';
        }

        public int compareTo(Vertex that){
            int distToCmp = Integer.compare(this.distTo, that.distTo);
            int vCmp = Integer.compare(this.V, that.V);
            return distToCmp != 0 ? distToCmp : vCmp;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/whichWayIsFaster/input.txt"));
        while(textScan.hasNextLine()){
            String line = textScan.nextLine().replaceAll(" ", "").replaceAll("\\|", "");
            //System.out.println(line);
            Main test = new Main(line);
        }
    }
}

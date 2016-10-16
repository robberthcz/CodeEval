/**
 Discount Offers
 Challenge Description:
 Our marketing department has just negotiated a deal with several local merchants that will allow us to offer exclusive discounts on various products to our top customers every day. The catch is that we can only offer each product to one customer and we may only offer one product to each customer.
 Each day we will get the list of products that are eligible for these special discounts. We then have to decide which products to offer to which of our customers. Fortunately, our team of highly skilled statisticians has developed an amazing mathematical model for determining how likely a given customer is to buy an offered product by calculating what we call the "suitability score" (SS). The top-secret algorithm to calculate the SS between a customer and a product is this:

 1. If the number of letters in the product's name is even then the SS is the number of vowels (a, e, i, o, u, y) in the customer's name multiplied by 1.5.
 2. If the number of letters in the product's name is odd then the SS is the number of consonants in the customer's name.
 3. If the number of letters in the product's name shares any common factors (besides 1) with the number of letters in the customer's name then the SS is multiplied by 1.5.
 Your task is to implement a program that assigns each customer a product to be offered in a way that maximizes the combined total SS across all of the chosen offers. Note that there may be a different number of products and customers. You may include code from external libraries as long as you cite the source.

 Input sample:
 Your program should accept as its only argument a path to a file. Each line in this file is one test case. Each test case will be a comma delimited set of customer names followed by a semicolon and then a comma delimited set of product names. Assume the input file is ASCII encoded. For example (NOTE: The example below has 3 test cases):

 Jack Abraham,John Evans,Ted Dziuba;iPad 2 - 4-pack,Girl Scouts Thin Mints,Nerf Crossbow
 Jeffery Lebowski,Walter Sobchak,Theodore Donald Kerabatsos,Peter Gibbons,Michael Bolton,Samir Nagheenanajar;Half & Half,Colt M1911A1,16lb bowling ball,Red Swingline Stapler,Printer paper,Vibe Magazine Subscriptions - 40 pack
 Jareau Wade,Rob Eroh,Mahmoud Abdelkader,Wenyi Cai,Justin Van Winkle,Gabriel Sinkin,Aaron Adelson;Batman No. 1,Football - Official Size,Bass Amplifying Headphones,Elephant food - 1024 lbs,Three Wolf One Moon T-shirt,Dom Perignon 2000 Vintage

 Output sample:
 For each line of input, print out the maximum total score to two decimal places. For the example input above, the output should look like this:
 21.00
 83.50
 71.25

 */
package discountOffers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

/**
 * This is the assignment problem with the goal of maximizing the cost of the matching.
 * We use the algorithm as described in pdf file, which is the shortest path algorithm to find the perfect matching of bipartite graph with the lowest weight. Even though we have to maximize the cost of matching, we can still use this algorithm.
 * We just have to find the edge with maximum cost, then for each edge we change the cost by adding the maximum cost and subtracting the original cost of the edge.
 * Also, the assignment problem assumes equal number of agents and tasks => but CodeEval test-cases will include varying number of customers and products, so ones has to add dummies. It took me a day to realize this!!!!
 * Created by Robert on 23.8.2016.
 */
public class Main {
    // customers = X, products = Y
    private final String[] customers, products;
    // the cost matrix
    private double[][] m;
    private HashSet<Character> vowels = new HashSet<Character>(){{ add('a'); add('e'); add('i'); add('o'); add('u');
        add('y');}};
    // adjacency list
    private HashMap<Integer, HashSet<Edge>> adj;
    // S connects to all v in X, T to all v in Y
    private final int S, T;
    private int[] pair, p;

    public Main(String[] customers, String[] products){
        this.customers = customers;
        this.products = products;
        this.m = new double[customers.length][products.length];
        this.adj = new HashMap<>();
        this.S = customers.length + products.length;
        this.T = S + 1;
        // allows us to change this problem to min-cost matching
        double max = 0;
        // pair[v1]=v2, pair[v2]=v1,  vertex v1 is matched to vertex v2
        this.pair = new int[T + 1];
        // compatible prices as described in the slides
        this.p = new int[T + 1];

        // calculate the cost matrix
        for(int i = 0; i < customers.length; i++){
            int numOfVows = getNumOfVowels(customers[i]);
            for(int j = 0; j < products.length; j++){
                if(products[j].length() % 2 == 0) m[i][j] = numOfVows * 1.5;
                else                              m[i][j] = customers[i].length() - numOfVows;
                if(gcd(customers[i].length(), products[j].length()) > 1) m[i][j] *= 1.5;
                // dummy product, zero cost
                if(products[j].length() == 0) m[i][j] = 0;

                if(m[i][j] > max) max = m[i][j];
            }
        }
        // 1 undirected edge, for each (x, y)
        // product numbers are moved to right by the number of customers
        for(int i = 0; i < customers.length; i++){
            for(int j = 0; j < products.length; j++){
                int cost = Double.valueOf(max*100 - m[i][j]*100).intValue();
                Edge e = new Edge(i, j + customers.length, cost);
                addEdge(e);
            }
        }
        // s to X, Y to t
        adj.put(S, new HashSet<>());
        adj.put(T, new HashSet<>());
        for(int i = 0; i < customers.length; i++){
            Edge e = new Edge(S, i, 0);
            adj.get(S).add(e);
        }
        for(int i = 0; i < products.length; i++){
            Edge e = new Edge(i + customers.length, T, 0);
            adj.get(e.x).add(e);
        }

        // find shortest path while the T can be reached
        int res = 1;
        Arrays.fill(pair, Integer.MAX_VALUE);
        while(res != -1){
            int[] distTo = new int[T + 1];
            Edge[] edgeTo = new Edge[T + 1];
            res = dijkstraSP(S, T, edgeTo, distTo);
            // update p
            for(int i = 0; i < p.length; i++) p[i] += distTo[i];
            // reassign pairs and matched/unmatched edges
            if(res != -1) reassign(edgeTo);
        }
        // get the max cost
        double sum = 0;
        for(int i = 0; i < customers.length; i++)
            if(pair[i] != Integer.MAX_VALUE) sum += m[i][pair[i] - customers.length];
        // print the result
        DecimalFormat df = new DecimalFormat("0.00");
        DecimalFormatSymbols decSep = new DecimalFormatSymbols();
        decSep.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(decSep);
        System.out.println(df.format(sum));
    }

    public int gcd(int p, int q) {
        if (q == 0) return p;
        else return gcd(q, p % q);
    }

    public int getNumOfVowels(String S){
        int count = 0;
        for(int i = 0; i < S.length(); i++)
            if(vowels.contains(S.charAt(i)))
                count++;
        return count;
    }

    /**
     * According to shortest path found, this function, rearranges the matched and unmatched edges on the shortest path.
     * @param edgeTo
     */
    private void reassign(Edge[] edgeTo){
        // how did we get to T from Y
        int y = edgeTo[T].x;
        while(y != S){
            // traveled by unmatched edge from x in X to y in Y, now it is matched
            int x = edgeTo[y].x;
            edgeTo[y].matched = true;

            // reassign pairs
            pair[x] = y;
            pair[y] = x;

            // traveled by matched edge from y in Y to x in X
            y = edgeTo[x].getTo(x);
            // y in Y will be paired with some x in X by the next edge on the shortest path
            pair[y] = Integer.MAX_VALUE;
            if(y != S) edgeTo[x].matched = false;
        }
    }

    public int dijkstraSP(int startV, int endV, Edge[] edgeTo, int[] distTo){
        boolean[] marked = new boolean[T + 1];
        TreeSet<Vertex> Q = new TreeSet<Vertex>();

        Arrays.fill(distTo, Integer.MAX_VALUE);
        distTo[startV] = 0;
        Q.add(new Vertex(startV, 0));
        while(!Q.isEmpty()){
            Vertex min = Q.pollFirst();
            // no decrease-key operation, but control for already polled vertices
            if(marked[min.V]) continue;
            marked[min.V] = true;

            for(Edge e : adj.get(min.V)){
                if(isNotViableEdge(min, e, pair)) continue;

                int from = e.getFrom(min.V);
                int to = e.getTo(min.V);
                int cost = e.cost;
                if(e.x < S && e.y < S) cost = p[e.x] + e.cost - p[e.y];
                if(e.matched && cost != 0) System.out.println("matched edge should have zero cost");
                if(cost < 0) System.out.println("no edge should have negative cost");

                if(distTo[to] > ( cost + distTo[from])){
                    distTo[to] = cost + distTo[from];
                    Q.add(new Vertex(to, distTo[to]));
                    edgeTo[to] = e;
                }
            }
        }

        return distTo[endV] != Integer.MAX_VALUE ? distTo[endV] : - 1;
    }

    /**
     * From X to Y => only travel by unmatched edge, from Y to X  only with matched edge.
     * From S only travel to unpaired x in X. From Y to T only travel from unmatched y in Y.
     * @param from
     * @param e
     * @param pair
     * @return
     */
    private boolean isNotViableEdge(Vertex from, Edge e, int[] pair){
        int to = e.getTo(from.V);
        // skip matched v in X, when coming from S
        return (from.V == S && pair[to] != Integer.MAX_VALUE) ||
        // matched edge from v in X, continue
        (isX(from.V) && e.matched) ||
        // unmatched v in Y, go to T only
        (isY(from.V) && pair[from.V] == Integer.MAX_VALUE && to != T) ||
        // matched v in Y, go to X by matched edge only
        (isY(from.V) && pair[from.V] != Integer.MAX_VALUE && !e.matched);
    }

    private boolean isX(int v){
        return v < customers.length;
    }

    private boolean isY(int v){
        return v >= customers.length && v < S;
    }

    public void addEdge(Edge e){
        if(!adj.containsKey(e.x)) adj.put(e.x, new HashSet<Edge>());
        adj.get(e.x).add(e);

        if(!adj.containsKey(e.y)) adj.put(e.y, new HashSet<Edge>());
        adj.get(e.y).add(e);
    }

    class Edge{
        // edge always starts at X and ends at Y
        // sometimes it functions as dir. edge from X to Y, or the opposite
        final int x, y, cost;
        boolean matched;
        public Edge(int x, int y, int cost){
            this.x = x; this.y = y; this.cost = cost;
            matched = false;
        }
        // v is the from vertex as viewed from the outside
        public int getFrom(int v){
            if(v == x || x == S) return x;
            else return y;
        }
        
        public int getTo(int v){
            if(v == y && y != T) return x;
            else return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (cost != edge.cost) return false;
            if (x != edge.x) return false;
            if (y != edge.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
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
        Scanner textScan = new Scanner(new FileReader("src/discountOffers/input.txt"));

        while(textScan.hasNextLine()){
            String line[] = textScan.nextLine().split(";");
            // no products or no customers or both
            if(line.length < 2){
                System.out.println("0.00");
                continue;
            }
            String[] customers = line[0].toLowerCase().replaceAll("[^a-z,]", "").split(",");
            String[] products = line[1].toLowerCase().replaceAll("[^a-z,]", "").split(",");

            // customers and products must be equal in number, fill with dummies
            int maxSize = Math.max(customers.length, products.length);
            String custFilled[] = new String[maxSize];
            String prodFilled[] = new String[maxSize];
            Arrays.fill(custFilled, "");
            Arrays.fill(prodFilled, "");
            for(int i = 0;i < customers.length; i++) custFilled[i] = customers[i];
            for(int i = 0;i < products.length; i++) prodFilled[i] = products[i];

            //Main test = new Main(customers, products);
            Main test = new Main(custFilled, prodFilled);
        }
    }
}

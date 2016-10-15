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
 * Created by Robert on 23.8.2016.
 */
public class Main {
    private final String[] customers, products;
    private double[][] m;
    private HashSet<Character> vowels = new HashSet<Character>(){{ add('a'); add('e'); add('i'); add('o'); add('u');
        add('y');}};
    private HashMap<Integer, HashSet<Edge>> adj;
    private final int S, T;

    public Main(String[] customers, String[] products){
        this.customers = customers;
        this.products = products;
        this.m = new double[customers.length][products.length];
        this.adj = new HashMap<>();
        this.S = customers.length + products.length;
        this.T = S + 1;

        double max = 0;

        for(int i = 0; i < customers.length; i++){
            int numOfVows = getNumOfVowels(customers[i]);
            for(int j = 0; j < products.length; j++){
                if(products[j].length() % 2 == 0) m[i][j] = numOfVows * 1.5;
                else                              m[i][j] = customers[i].length() - numOfVows;

                if(gcd(customers[i].length(), products[j].length()) > 1) m[i][j] *= 1.5;
                //System.out.println(m[i][j] + " " + customers[i] + " " + products[j]);
                if(m[i][j] > max) max = m[i][j];
            }
        }

        for(int i = 0; i < customers.length; i++){
            for(int j = 0; j < products.length; j++){
                int cost = Double.valueOf(max*100D - m[i][j]*100D).intValue();
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
            adj.get(e.from).add(e);
        }

        int[] pair = new int[T + 1];
        int[] p = new int[T + 1];
        Arrays.fill(pair, Integer.MAX_VALUE);
        int res = 1;
        while(res != -1){
            Edge[] edgeTo = new Edge[T + 1];
            res = dijkstraSP(S, T, edgeTo, pair, p);
            if(res != -1) reassign(pair, edgeTo);
        }

        double sum = 0;
        for(int i = 0; i < customers.length; i++){
            if(pair[i] != Integer.MAX_VALUE){
                sum += m[i][pair[i] - customers.length];
                //System.out.println(customers[i] + " -> " + products[pair[i]-customers.length] + " " + m[i][pair[i] - customers.length]);
            }

        }
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

    private void reassign(int[] pair, Edge[] edgeTo){
        int y = edgeTo[T].from;
        while(y != S){
            int x = edgeTo[y].from;
            edgeTo[y].matched = true;

            pair[x] = y;
            pair[y] = x;

            y = edgeTo[x].getTo(x);
            pair[y] = Integer.MAX_VALUE;
            if(y != S){
                edgeTo[x].matched = false;
            }
        }
    }

    public int dijkstraSP(int startV, int endV, Edge[] edgeTo, int[] pair, int[] p){
        int[] distTo = new int[T + 1];
        boolean[] marked = new boolean[T + 1];
        TreeSet<Vertex> Q = new TreeSet<Vertex>();

        distTo[startV] = 0;
        marked[startV] = true;
        Q.add(new Vertex(startV, 0));
        while(!Q.isEmpty()){
            Vertex min = Q.pollFirst();

            for(Edge e : adj.get(min.V)){
                int from = e.getFrom(min.V);
                int to = e.getTo(min.V);
                // skip matched v in X, when coming from S
                if(min.V == S && pair[to] != Integer.MAX_VALUE) continue;
                // matched edge from v in X, continue
                if(isX(min.V) && e.matched) continue;
                // unmatched v in Y, go to T only
                if(isY(min.V) && pair[min.V] == Integer.MAX_VALUE && to != T) continue;
                // matched v in Y, go to X by matched edge only
                if(isY(min.V) && pair[min.V] != Integer.MAX_VALUE && !e.matched) continue;

                int x = Math.min(from, to);
                int y = Math.max(from, to);
                int cost = e.cost;
                if(x < S && y < S) cost = p[x] + e.cost - p[y];
                if(e.matched && cost != 0) System.out.println("matched edge does not have zero cost");

                if(!marked[to] || distTo[to] > ( cost + distTo[from])){
                    distTo[to] = cost + distTo[from];
                    Q.remove(new Vertex(to, 0));
                    Q.add(new Vertex(to, distTo[to]));
                    marked[to] = true;
                    edgeTo[to] = e;
                }
            }
        }

        for(int i = 0; i < p.length; i++) p[i] += distTo[i];
        return marked[endV] ? distTo[endV] : - 1;
    }

    private boolean isX(int v){
        return v < customers.length;
    }

    private boolean isY(int v){
        return v >= customers.length && v < S;
    }

    public void addEdge(Edge e){
        if(!adj.containsKey(e.from)) adj.put(e.from, new HashSet<Edge>());
        adj.get(e.from).add(e);

        if(!adj.containsKey(e.to)) adj.put(e.to, new HashSet<Edge>());
        adj.get(e.to).add(e);
    }

    class Edge{
        final int from, to, cost;
        boolean matched;
        public Edge(int from, int to, int cost){
            this.from = from; this.to = to; this.cost = cost;
            matched = false;
        }
        
        public int getFrom(int v){
            if(v == from || from == S) return from;
            else return to;
        }
        
        public int getTo(int v){
            if(v == to && to != T) return from;
            else return to;
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
        Scanner textScan = new Scanner(new FileReader("src/discountOffers/input_test.txt"));

        while(textScan.hasNextLine()){
            String line[] = textScan.nextLine().split(";");
            if(line.length < 2){
                System.out.println("0.00");
                continue;
            }
            String[] customers = line[0].toLowerCase().replaceAll("[^a-z,]", "").split(",");
            String[] products = line[1].toLowerCase().replaceAll("[^a-z,]", "").split(",");

            Main test = new Main(customers, products);

        }
    }
}

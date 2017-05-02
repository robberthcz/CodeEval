/**
 Mars Networks
 Challenge Description:
 For the mission to Mars some areas are chosen, where soil, climate, and seismology investigations will be made. These areas are of size 10 000 × 10 000 meters. The probes are sent to all these areas. For the effective work, the probes in each area must be connected to high-speed data network.
 Since delivery to Mars is very expensive, you have to determine the minimum length of the optical fiber cable which connects probes to a network.

 Input sample
 The first argument is a file that contains the space-separated coordinates of the site’s probes, one line per site. X and Y coordinates are separated by comma.
 For example:
 500,8000 1000,9500 2000,8500 1000,7500 4500,7000 5500,6500 7000,7000
 2500,4000 1000,4000 1000,3000 3000,2500 2500,1000 3500,500 9000,6000
 8500,4500 7500,4000 9000,3500 10000,3000
 8028,5930 1835,5145 8537,9824 7623,7936 8031,1207
 9349,3367 395,3342 3588,3655 9235,2503 1075,6413 2394,8353
 9013,3937 7791,872 2417,3183
 3416,472 8093,7510 1709,4893 9999,6958 6761,2692 2530,6753

 Output sample:
 Print to stdout the minimum length of optical fiber cable for every site:
 26602
 15110
 15335
 9150
 17770

 If the length is not integer (for example, 9063. 114), you should round it upward (9064).
 Constraints:
 1.Number of sites is 20.
 2.Number of probes within one site can be from 3 to 250.
 3.There can be 2 or more probes with the same coordinates — in such case they do not need any cable to interconnect.
 */
package marsNetworks;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Euclidean minimum spanning tree problem. This reduces to the regular minimum spanning tree, but have to consider quadratic number of edges
 * O(nlog(n)) is achievable if one constructs first the Delaunay triangulation and then uses the edges from that triangulation to get a regular minimum spanning tree. However, the algorithms for this triangulation include of lot of overhead and are too involved. Naive approach is fast enough in this case. Constructing a triangulation would probably pay off with graphs of at least 500 vertices, we have at most 250.
 *
 * For the minimum spanning tree we use the Kruskal's minimum spanning tree algorithm, which is the most elegant.
 * For Kruskal's algorithm we use the union-find data structure implemented by the great Kevin Wayne and Robert Sedgewick.
 * The address is: http://algs4.cs.princeton.edu/15uf/WeightedQuickUnionUF.java.html
 * Created by Robert on 22.10.2016.
 */
public class Main {

    public Main(ArrayList<Edge> edges, int nOfPoints){
        double totalCost = 0;
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(nOfPoints);
        // sort according to weight
        Collections.sort(edges);

        // Kruskal algorithm
        for(Edge e : edges){
            if(!uf.connected(e.x, e.y)){
                totalCost += e.cost;
                uf.union(e.x, e.y);
            }
        }
        // round up the value
        System.out.println(Double.valueOf(totalCost).intValue() + 1);
    }

    class WeightedQuickUnionUF {
        private int[] parent;   // parent[i] = parent of i
        private int[] size;     // size[i] = number of sites in subtree rooted at i
        private int count;      // number of components

        public WeightedQuickUnionUF(int n) {
            count = n;
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        /**
         * Returns the number of components.
         *
         * @return
         */
        public int count() {
            return count;
        }

        /**
         * Returns the component identifier for the component containing site.
         *
         * @param  p the integer representing one object
         * @return the component identifier for the component containing site
         * @throws IndexOutOfBoundsException unless
         */
        public int find(int p) {
            validate(p);
            while (p != parent[p])
                p = parent[p];
            return p;
        }

        // validate that p is a valid index
        private void validate(int p) {
            int n = parent.length;
            if (p < 0 || p >= n) {
                throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n-1));
            }
        }

        /**
         * Returns true if the the two sites are in the same component.
         */
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        /**
         * Merges the component containing site {@code p} with the
         * the component containing site {@code q}.
         */
        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            // make smaller root point to larger one
            if (size[rootP] < size[rootQ]) {
                parent[rootP] = rootQ;
                size[rootQ] += size[rootP];
            }
            else {
                parent[rootQ] = rootP;
                size[rootP] += size[rootQ];
            }
            count--;
        }
    }

    static class Edge implements Comparable<Edge>{
        final int x, y;
        final double cost;
        public Edge(int x, int y, double cost){
            this.x = x; this.y = y; this.cost = cost;
        }

        @Override
        public int compareTo(Edge that){
            if(this.cost != that.cost) return Double.compare(this.cost, that.cost);
            else if(this.x != that.x)  return Integer.compare(this.x, that.y);
            else                       return Integer.compare(this.y, that.y);
        }
    }

    static class Point{
        final int x, y;

        Point(int x, int y){
            this.x = x; this.y = y;
        }

        public double distTo(Point that){
            int dX = Math.abs(this.x - that.x);
            int dY = Math.abs(this.y - that.y);
            return Math.sqrt(dX*dX + dY*dY);
        }

        @Override
        public String toString() {
            return "P{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            if (y != point.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/marsNetworks/input.txt"));

        while(textScan.hasNextLine()){
            String[] site = textScan.nextLine().split(" ");
            ArrayList<Edge> edges = new ArrayList<>(site.length * site.length);
            HashSet<Point> pSet = new HashSet<>();
            // parse points and delete duplicates
            for(int i = 0; i < site.length; i++) {
                String[] p = site[i].split(",");
                pSet.add(new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1])));
            }
            Point[] points = pSet.toArray(new Point[pSet.size()]);
            // save edge for each pair of distinct points
            for(int i = 0; i < points.length; i++){
                for(int j = i + 1; j < points.length; j++){
                    double d = points[i].distTo(points[j]);
                    edges.add(new Edge(i, j, d));
                }
            }
            Main test = new Main(edges, points.length);
        }
    }
}

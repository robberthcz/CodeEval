/**
 PEAK TRAFFIC
 CHALLENGE DESCRIPTION:
 Credits: This challenge is from the facebook engineering puzzles
 Facebook is looking for ways to help users find out which friends they interact with the most on the site. Towards that end, you have collected data from your friends regarding who they interacted with on the site. Each piece of data represents a desirable but one-way interaction between one user of Facebook towards another user of Facebook. By finding groups of users who regularly interact with one another, you hope to help users determine who among their friends they spend the most time with online.

 Being a popular user, you have collected a lot of data; so much that you cannot possibly process it by hand. But being a programmer of no small repute, you believe you can write a program to do the analysis for you. You are interested in finding clusters of users within your data pool; in other words, groups of users who interact among one another. A cluster is defined as a set of at least three users, where every possible permutation of two users within the cluster have both received and sent some kind of interaction between the two.

 With your program, you wish to analyze the collected data and find out all clusters within.

 INPUT SAMPLE:
 Your program should accept as its first argument a path to a filename. The input file consists of multiple lines of aggregated log data. Each line starts with a date entry, whose constituent parts are separated by single white spaces. The exact format of the date always follows the examples given below. Following the date is a single tab, and then the email address of the user who is performing the action. Following that email is another single tab and then finally the email of the Facebook user who receives the action. The last line of the file may or may not have a newline at its end.

 Thu Dec 11 17:53:01 PST 2008    a@facebook.com    b@facebook.com
 Thu Dec 11 17:53:02 PST 2008    b@facebook.com    a@facebook.com
 Thu Dec 11 17:53:03 PST 2008    a@facebook.com    c@facebook.com
 Thu Dec 11 17:53:04 PST 2008    c@facebook.com    a@facebook.com
 Thu Dec 11 17:53:05 PST 2008    b@facebook.com    c@facebook.com
 Thu Dec 11 17:53:06 PST 2008    c@facebook.com    b@facebook.com
 Thu Dec 11 17:53:07 PST 2008    d@facebook.com    e@facebook.com
 Thu Dec 11 17:53:08 PST 2008    e@facebook.com    d@facebook.com
 Thu Dec 11 17:53:09 PST 2008    d@facebook.com    f@facebook.com
 Thu Dec 11 17:53:10 PST 2008    f@facebook.com    d@facebook.com
 Thu Dec 11 17:53:11 PST 2008    e@facebook.com    f@facebook.com
 Thu Dec 11 17:53:12 PST 2008    f@facebook.com    e@facebook.com
 Every line in the input file will follow this format, you are guaranteed that your submission will run against well formed input files.

 OUTPUT SAMPLE:
 You must output all clusters detected from the input log file with size of at least 3 members. A cluster is defined as N >= 3 users on Facebook that have send and received actions between all possible permutations of any two members within the cluster.
 Your program should print to standard out, exactly one cluster per line. Each cluster must have its member user emails in alphabetical order, separated by a comma and a single space character each. There must not be a comma (or white space) after the final email in the cluster; instead print a single new line character at the end of every line. The clusters themselves must be printed to standard out also in alphabetical order; treat each cluster as a whole string for purposes of alphabetical comparisons. Do not sort the clusters by size or any other criteria.

 a@facebook.com, b@facebook.com, c@facebook.com
 d@facebook.com, e@facebook.com, f@facebook.com

 Finally, any cluster that is a sub-cluster (in other words, all users within one cluster are also present in another) must be removed from the output. For this case, your program should only print the largest super-cluster that includes the other clusters. Your program must be fast, efficient, and able to handle extremely large input files.
 */
package PeakTraffic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Cluster must be of size higher than 2.
 *
 * Important is to notice that graph resulting from the test-cases is extremely sparse, where 80% of vertices have
 * only 1 neighbor. Such vertices can be deleted, since they cannot produce cluster of size higher than 2.
 *
 * More advanced versions of Bron-Kerbosch (pivoting, degeneracy ordering) algorithm did not prove to be efficient for
 * smaller graphs (300
 * vertices)
 * and small clusters (3, 4, 5).
 *
 * Listing All Maximal Cliques in Large Sparse Real-World Graphs - Eppstein, Strash => this paper contains excellent
 * strategy to implement the set intersections and find a pivot in a very efficient manner and with constant memory. But
 * it would be hell to debug it, since it is very imperative solution.
 *
 * Linear algorithm for degeneracy ordering can be found here:
 *
 */
public class Main {
    private HashMap<Integer, HashSet<Integer>> adj;

    public Main() {
        this.adj = new HashMap<>();
    }

    public void addUndirEdge(int v, int w) {
        if (!adj.containsKey(v)) adj.put(v, new HashSet<Integer>());
        if (!adj.containsKey(w)) adj.put(w, new HashSet<Integer>());
        adj.get(v).add(w);
        adj.get(w).add(v);
    }

    /**
     * Returns a list of clusters with size at least 3.
     * @return
     */
    public LinkedList<LinkedList<Integer>> getClusters(){
        // remove all vertices which cannot form cluster
        adj.keySet().removeIf(k -> adj.get(k).size() <= 1);
        LinkedList<LinkedList<Integer>> acc = new LinkedList<>();
        BronKerbosch(acc, new LinkedList<Integer>(), new LinkedList<Integer>(adj.keySet()), new LinkedList<Integer>());
        // remove clusters of size lower than 3
        acc.removeIf(list -> list.size() <= 2);
        return acc;
    }

    public void BronKerbosch(LinkedList<LinkedList<Integer>> acc, LinkedList<Integer> R, LinkedList<Integer> P, LinkedList<Integer> X) {
        if (P.isEmpty() && X.isEmpty()) {
            acc.add((LinkedList<Integer>) R.clone());
            return;
        }
        while(P.size() > 0){
            int v = P.removeFirst();
            R.addLast(v);
            // P ∩ N(v)
            LinkedList<Integer> interP = getIntersection(P, v);
            // X ∩ N(v)
            LinkedList<Integer> interX = getIntersection(X, v);

            BronKerbosch(acc, R, interP, interX);

            X.addLast(v);
            R.removeLast();
        }
    }

    /**
     * Returns intersection of vertices in the list and adjacent vertices to vertex v
     * @param list
     * @param v
     * @return
     */
    private LinkedList<Integer> getIntersection(LinkedList<Integer> list, int v) {
        LinkedList<Integer> listInterV = new LinkedList<Integer>();
        for (int i : list) {
            if (adj.get(v).contains(i)) listInterV.addLast(i);
        }
        return listInterV;
    }

    public static void main(String args[]) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/PeakTraffic/input_large.txt"));
        Main test = new Main();
        // username to id
        HashMap<String, Integer> unameToId = new HashMap<>();
        // id to username
        HashMap<Integer, String> idToUname = new HashMap<>();
        int id = 0;
        // represents email ending which all users should share
        // this does not have to be true
        String email = "";

        while (textScan.hasNextLine()) {
            String[] line = textScan.nextLine().split("\\s+");
            String v = line[6].substring(0, line[6].indexOf("@"));
            String w = line[7].substring(0, line[7].indexOf("@"));
            if(id == 0) email = line[6].substring(line[6].indexOf("@"));

            if (!unameToId.containsKey(v)){
                idToUname.put(id, v);
                unameToId.put(v, id++);
            }
            if (!unameToId.containsKey(w)){
                idToUname.put(id, w);
                unameToId.put(w, id++);
            }
            test.addUndirEdge(unameToId.get(v), unameToId.get(w));
        }
        LinkedList<LinkedList<Integer>> clusters = test.getClusters();
        // for CodeEval submittion purposes
        TreeSet<String> results = new TreeSet<>();
        for(LinkedList<Integer> list : clusters){
            // string representation of this cluster
            LinkedList<String> result = new LinkedList<>();
            for(int i : list) result.addLast(idToUname.get(i) + email);
            // emails are to be sorted alphabetically
            Collections.sort(result);
            String toString = result.toString();
            results.add(toString.substring(1, toString.length() - 1));
        }
        // string representation of each cluster is to be also sorted alphabetically, not according to size
        for(String S : results) System.out.println(S);
    }
}
/**
 Package Problem
 Challenge Description:
 You want to send your friend a package with different things.
 Each thing you put inside the package has such parameters as index number, weight and cost.
 The package has a weight limit.
 Your goal is to determine which things to put into the package so that the total weight is less than or equal to the package limit and the total cost is as large as possible.
 You would prefer to send a package which weights less in case there is more than one package with the same price.

 Input sample:
 Your program should accept as its first argument a path to a filename. The input file contains several lines. Each line is one test case.
 Each line contains the weight that the package can take (before the colon) and the list of things you need to choose. Each thing is enclosed in parentheses where the 1st number is a thing's index number, the 2nd is its weight and the 3rd is its cost. E.g.
 81 : (1,53.38,$45) (2,88.62,$98) (3,78.48,$3) (4,72.30,$76) (5,30.18,$9) (6,46.34,$48)
 8 : (1,15.3,$34)
 75 : (1,85.31,$29) (2,14.55,$74) (3,3.98,$16) (4,26.24,$55) (5,63.69,$52) (6,76.25,$75) (7,60.02,$74) (8,93.18,$35) (9,89.95,$78)
 56 : (1,90.72,$13) (2,33.80,$40) (3,43.15,$10) (4,37.97,$16) (5,46.81,$36) (6,48.77,$79) (7,81.80,$45) (8,19.36,$79) (9,6.76,$64)

 Output sample:
 For each set of things that you put into the package provide a list (items’ index numbers are separated by comma). E.g.
 4
 -
 2,7
 8,9

 Constraints:
 1.Max weight that a package can take is ≤ 100
 2.There might be up to 15 items you need to choose from
 3.Max weight and cost of an item is ≤ 100
 */
package packageProblem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 26.8.2016.
 */
public class Main {
    private int W;
    private ArrayList<Item> items;

    public Main(int W) {
        this.W = W;
        items = new ArrayList<Item>();
    }

    public void solve(){
        Collections.sort(items);
        LinkedList<Node> Q = new LinkedList<Node>();
        // include first item
        if(items.get(0).w <= W) Q.add(new Node(0, items.get(0).val, items.get(0).w, 1 << (items.get(0).name - 1)));
        // not include first item
        Q.addLast(new Node(0, 0, 0, 0));
        int maxBest = 0;
        Node maxNode = null;

        while(Q.size() > 0){
            Node n = Q.removeFirst();
            // leaf was reached
            if(n.level == items.size() - 1){
                if(n.val > maxBest || (maxNode != null && n.val == maxBest && n.w < maxNode.w)){
                    maxBest = n.val;
                    maxNode = n;
                }
                continue;
            }
            Item curItem = items.get(n.level + 1);
            // node with the item at the current level omitted
            Node omitCurItem = new Node(n.level + 1, n.val, n.w, n.packs);
            // node which includes the current item
            Node inclCurItem = new Node(n.level + 1, n.val + curItem.val, n.w + curItem.w, n.packs | (1 << (curItem.name - 1)));
            // bounds according to fractional knapsack problem
            double inclBound = getBound(inclCurItem);
            double omitBound = getBound(omitCurItem);
            // bound solutions
            if(omitBound > maxBest) Q.add(omitCurItem);
            if(inclBound > maxBest && inclCurItem.w <= W) Q.addFirst(inclCurItem);
        }
        //System.out.println(maxBest);
        if(maxNode == null){
            System.out.println("-");
            return;
        }
        int j = 0;
        String binString = Integer.toBinaryString(maxNode.packs);
        //System.out.println(binString);
        for(int i = binString.length() - 1; i >= 0; i--){
            char c = binString.charAt(i);
            if(c == '1') System.out.print(j + 1);
            if(c == '1' && i > 0) System.out.print(",");
            j++;
        }
        System.out.println();
    }

    /**
     *
     * @param n
     * @return Bound computed according to fractional knapsack problem. Best possible result which could be achieved by exploring this node all of his children
     */
    private double getBound(Node n){
        int i = n.level + 1;
        double bound = n.val;
        double w = n.w;
        while(i < items.size() && (w + items.get(i).w) <= W){
            Item item = items.get(i);
            w += item.w;
            bound += item.val;
            i++;
        }
        // fill it up to W
        if(i < items.size() - 1) bound += ((((double) W) - w) / w)*( (double) items.get(i).val);
        return bound;
    }

    class Item implements Comparable<Item>{
        int name, val;
        double w;
        public Item(int name, double w, int val){
            this.name = name; this.w = w; this.val = val;
        }

        public int compareTo(Item that){
            double thisRatio = (double) val / w;
            double thatRatio = (double) that.val / that.w;
            // Items with bigger Value/Weight ratio to left
            return Double.compare(thisRatio, thatRatio)* (-1);
        }
    }

    class Node{
        int level, val;
        double w;
        // if bit at i-th position is 1, then i-th item was included on the path when reaching this node
        int packs;
        public Node(int level, int val, double w, int packs) {
            this.level = level;
            this.val = val;
            this.w = w;
            this.packs = packs;
        }
        @Override
        public String toString() {
            return "Node{" +
                    "level=" + level +
                    ", val=" + val +
                    ", w=" + w +
                    '}';
        }
    }

    public void addItem(int name, double w, int val){
        items.add(new Item(name, w, val));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/packageProblem/input.txt"));

        while(textScan.hasNextLine()){
            String[] line = textScan.nextLine().split(":");
            int W = Integer.parseInt(line[0].trim());
            String[] packages = line[1].trim().replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\$", "").split(" ");
            Main test = new Main(W);
            for(String p : packages){
                String[] tmp = p.trim().split(",");
                test.addItem(Integer.parseInt(tmp[0]), Double.parseDouble(tmp[1]), Integer.parseInt(tmp[2]));
            }
            test.solve();
        }
    }
}

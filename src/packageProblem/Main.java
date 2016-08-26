package packageProblem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Robert on 26.8.2016.
 */
public class Main {
    private int W;
    private ArrayList<Item> items;


    public Main(int W){
        this.W = W;
        items = new ArrayList<Item>();

        Collections.sort(items);
    }

    public void solve(){
        LinkedList<Node> Q = new LinkedList<Node>();

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

        public Node(int level, int val, double w) {
            this.level = level;
            this.val = val;
            this.w = w;
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
            System.out.println(packages[0]);
            Main test = new Main(W);
            for(String p : packages){
                String[] tmp = p.trim().split(",");
                test.addItem(Integer.parseInt(tmp[0]), Double.parseDouble(tmp[1]), Integer.parseInt(tmp[2]));
            }
        }
    }
}

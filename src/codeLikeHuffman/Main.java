package codeLikeHuffman;

import sun.reflect.generics.tree.Tree;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Robert on 8.9.2016.
 */
public class Main {

    public Main(String S){
        HashMap<Character, Integer> sFreqs = new HashMap<Character, Integer>();
        TreeSet<Node> set = new TreeSet<Node>();
        for(char c : S.toCharArray()){
            if(!sFreqs.containsKey(c)) sFreqs.put(c, 1);
            else sFreqs.put(c, sFreqs.get(c) + 1);
        }
        System.out.println(sFreqs.toString());

        for(char c : sFreqs.keySet()){
            int freq = sFreqs.get(c);
            Node n = new Node(freq, 1, String.valueOf(c), null, null);
            set.add(n);
        }

        while(set.size() > 1){
            Node fst = set.pollFirst();
            Node snd = set.pollFirst();
            System.out.println(fst);
            System.out.println(snd);
            Node n = new Node(fst.freq + snd.freq, Math.max(fst.level, snd.level) + 1, fst.letters + snd.letters, fst, snd);
            set.add(n);
        }
        TreeMap<String, String> codeTables = new TreeMap<String, String>();
        reconstructTree(set.pollFirst(), "", codeTables);
        System.out.println(codeTables);
    }

    public void reconstructTree(Node n, String path, TreeMap<String,String> codeTables){
        if(n.left == null && n.right == null){
            codeTables.put(n.letters, new String(path));
            return;
        }
        if(n.left != null) reconstructTree(n.left, path + "0", codeTables);
        if(n.right != null) reconstructTree(n.right, path + "1", codeTables);
    }

    class Node implements Comparable<Node>{
        String letters;
        int level, freq;
        Node left, right;
        public Node(int freq, int level, String letters, Node left, Node right){
            this.freq = freq; this.level = level;
            this.letters = letters;
            this.left = left;
            this.right = right;
        }

        public int compareTo(Node that){
            // nodes with highest frequency to left
            if(this.freq != that.freq) return Integer.compare(this.freq, that.freq);
            else if(this.level != that.level) return Integer.compare(that.level, this.level);
            else return this.letters.compareTo(that.letters);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "letters='" + letters + '\'' +
                    ", level=" + level +
                    ", freq=" + freq +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/codeLikeHuffman/input.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            Main test = new Main(line);


        }
    }
}

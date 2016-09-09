/**
 CODE LIKE HUFFMAN
 CHALLENGE DESCRIPTION:

 One of the first algorithms for efficient information coding was proposed by David A. Huffman in 1952. The idea of the algorithm is the following: knowing the estimated probability or frequency of occurrence for each possible value of the source symbol, we can describe the procedure for building variable-length code table for encoding a source symbol.
 This algorithm is currently used in many data compression applications, including this challenge.

 INPUT SAMPLE:
 The first argument is a path to a file. Each line includes a test case which contains letters from which you have to build a code table for each character using Huffman algorithm
 abc
 ilovecodeeval

 OUTPUT SAMPLE:
 Use this algorithm to build a code table for each character and print it in an alphabetical order in the following way.
 a: 10; b: 11; c: 0;
 a: 1000; c: 1001; d: 1010; e: 01; i: 1011; l: 110; o: 111; v: 00;
 CONSTRAINTS:

 The test case can include lowercase characters only.
 When building a binary tree, if the priority of items is the same, the sorting should be done in an alphabetical order, that is:

 If the priority of items is the same then Node has higher priority than symbol. If 2 Nodes have same priority then sorting should be done in an alphabetical order.
 Test case can include from 3 to 30 characters.
 The number of test cases is 40.
 */
package codeLikeHuffman;

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

        for(char c : sFreqs.keySet()){
            Node n = new Node(sFreqs.get(c), 1, String.valueOf(c), null, null);
            set.add(n);
        }
        // extract first two and merge them
        while(set.size() > 1){
            Node fst = set.pollFirst();
            Node snd = set.pollFirst();
            Node n = new Node(fst.freq + snd.freq, Math.max(fst.level, snd.level) + 1, fst.letters + snd.letters, fst, snd);
            set.add(n);
        }
        // reconstruct the path for each original character, left 0, right 1
        TreeMap<String, String> codeTables = new TreeMap<String, String>();
        reconstructPath(set.pollFirst(), "", codeTables);
        // for CodeEval submition purposes
        String result = codeTables.toString().replaceAll("=", ": ").replaceAll(",", ";");
        result = result.substring(1, result.length() - 1);
        System.out.println(result + ";");
    }

    private void reconstructPath(Node n, String path, TreeMap<String,String> codeTables){
        if(n.left == null && n.right == null){
            codeTables.put(n.letters, path);
            return;
        }
        if(n.left != null) reconstructPath(n.left, path + "0", codeTables);
        if(n.right != null) reconstructPath(n.right, path + "1", codeTables);
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
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/codeLikeHuffman/input.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            Main test = new Main(line);
        }
    }
}

/**
 Da Vyncy
 Challenge Description:
 You were reading The Da Vyncy Code, the translation of a famous murder mystery novel into Python. The Code is finally revealed on the last page. You had reached the second to last page of the novel, and then you went to take a bathroom break.

 While you were in the bathroom, the Illuminati snuck into your room and shredded the last page of your book. You had 9 backup copies of the book just in case of an attack like this, but the Illuminati shredded the last page from each of the those books, too. Then they propped up a fan, aimed it at the remains, and turned it on at high-speed.

 The last page of your book is now in tatters.

 However, there are many text fragments floating in the air. You enlist an undergraduate student for a 'summer research project' of typing up these fragments into a file. Your mission: reassemble the last page of your book.

 Problem Description
 =============
 (adapted from a problem by Julie Zelenski)
 Write a program that, given a set of fragments (ASCII strings), uses the following method (or a method producing identical output) to reassemble the document from which they came:

 At each step, your program searches the collection of fragments. It should find the pair of fragments with the maximal overlap match and merge those two fragments. This operation should decrease the total number of fragments by one. If there is more than one pair of fragments with a maximal overlap, you may break the tie in an arbitrary fashion.Fragments must overlap at their start or end. For example:
 - "ABCDEF" and "DEFG" overlap with overlap length 3
 - "ABCDEF" and "XYZABC" overlap with overlap length 3
 - "ABCDEF" and "BCDE" overlap with overlap length 4
 - "ABCDEF" and "XCDEZ" do *not* overlap (they have matching characters in the middle, but the overlap does not extend to the end of either string).

 Fear not - any test inputs given to you will satisfy the property that the tie-breaking order will not change the result, as long as you only ever merge maximally-overlapping fragments. Bonus points if you can come up with an input for which this property does not hold (ie, there exists more than 1 different final reconstruction, depending on the order in which different maximal-overlap merges are performed) -- if you find such a case, submit it in the comments to your code!

 All characters must match exactly in a sequence (case-sensitive). Assume that your undergraduate has provided you with clean data (i.e., there are no typos).

 Input sample:
 Your program should accept as its first argument a path to a filename. Each line in this file represents a test case. Each line contains fragments separated by a semicolon, which your assistant has painstakingly transcribed from the shreds left by the Illuminati. You may assume that every fragment has length at least 2 and at most 1022 (excluding the trailing newline, which should *not* be considered part of the fragment). E.g. Here are two test cases.

 O draconia;conian devil! Oh la;h lame sa;saint!
 m quaerat voluptatem.;pora incidunt ut labore et d;, consectetur, adipisci velit;olore magnam aliqua;idunt ut labore et dolore magn;uptatem.;i dolorem ipsum qu;iquam quaerat vol;psum quia dolor sit amet, consectetur, a;ia dolor sit amet, conse;squam est, qui do;Neque porro quisquam est, qu;aerat voluptatem.;m eius modi tem;Neque porro qui;, sed quia non numquam ei;lorem ipsum quia dolor sit amet;ctetur, adipisci velit, sed quia non numq;unt ut labore et dolore magnam aliquam qu;dipisci velit, sed quia non numqua;us modi tempora incid;Neque porro quisquam est, qui dolorem i;uam eius modi tem;pora inc;am al

 Output sample:
 Print out the original document, reassembled. E.g.

 O draconian devil! Oh lame saint!
 Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem.
 */
package daVyncy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * There are 3 types of overlap =>
 * - "ABCDEF" and "DEFG" => from left  to right, "ABCDEF" joined on left, "DEFG" joined on right
 * - "ABCDEF" and "XYZABC" => from right to left
 * - "ABCDEF" and "BCDE" => second is substring of the first
 *
 * RULE FOR JOINING: The text fragments have to be joined according to size of their overlap
 *
 * The last type of overlap does not have to be considered, every such substring can be removed without breaking the rule. Just think about it - if the first text fragment is indeed substring of second text fragment, then all text fragments sharing overlap with the first one, also has to overlap with the second one. Such overlap will be then at least as big or bigger.
 *
 * Second case will not be considered, since it is mirrors the first type of overlap.
 *
 * The challenge description says to search the collection of fragments at each step and find the fragments with maximal overlap and merge them - it is unnecessary to do that at each step.
 * It is sufficient to find overlaps between the initial set of text fragments. Merging of any two text fragments does not change the sizes overlaps between the untouched text fragments or between the merged fragment and any of the untouched text fragments at that step.
 *
 * To determine the size of the overlap between  two text fragments we use the Knuth-Morris-Pratt algorithm for substring search. DFA is generated according to text fragment on the right and then the left text fragment is used as the text.
 * After iterating through chars of the text using DFA, the resulting state of DFA is the size of maximal overlap between the two text fragments. Complexity is O(max(N,MR)).
 *
 * Created by Robert on 29.10.2016.
 */
public class Main {
    // DFAs for each text fragment in the words arrays
    private int[][][] dfa;
    // holds Pairs of text fragments POSSIBLE to be merged, ordered according to size of the overlap
    private TreeSet<Pair> Q;
    // text fragments
    private String[] words;
    // Pairs of text fragments to be merged
    private LinkedList<Pair> solution;

    public Main(String[] words){
        this.words = words;
        this.dfa = new int[words.length][][];
        this.Q = new TreeSet<>();
        this.solution = new LinkedList<>();
        // boolean arrays to keep track if the text fragment, was already joined on left, or on right
        boolean[] joinedRight = new boolean[words.length];
        boolean[] joinedLeft = new boolean[words.length];

        Comparator<String> LONGER_STR_FST = new Comparator<String>()
        {
            @Override
            public int compare(String s1, String s2)
            {
                return Integer.compare(s1.length(), s2.length())*(-1);
            }
        };

        // sort text fragments so that larger ones are first => to deal with substrings
        Arrays.sort(words, LONGER_STR_FST);
        // cache DFAs for each text fragment
        for(int i = 0; i < words.length; i++){
            dfa[i] = KMP(words[i]);
        }
        // control each pair of text fragments for overlap
        for(int i = 0; i < words.length; i++){
            for(int j = 0; j < words.length; j++){
                // text fragment on the left or right cannot be substring
                if(i == j || joinedLeft[i]) continue;

                int overlap = getOverlapSize(words[i], words[j], dfa[j]);

                if(overlap > 0 && overlap != words[j].length())
                    Q.add(new Pair(i, j, overlap));
                // words[j] is substring of words[i]
                else if(overlap == words[j].length()){
                    joinedLeft[j] = true;
                    joinedRight[j] = true;
                }
            }
        }
        // MERGE text fragments
        while(!Q.isEmpty()){
            Pair max = Q.pollLast();
            // this max cannot be part of solution, one of the text fragments was already merged on the side of this overlap
            if(joinedLeft[max.left] || joinedRight[max.right]){
                continue;
            }
            joinedLeft[max.left] = true;
            joinedRight[max.right] = true;
            solution.addLast(max);
        }
        // pairs in solutions are in no particular order
        // the text fragments need to be merged from the beginning
        LinkedList<Pair> sorted = sortSolution();

        // transform the pairs to the actual text
        StringBuilder sb = new StringBuilder();
        sb.append(words[sorted.getFirst().left]);

        for(Pair p : sorted)
            sb.append(words[p.right].substring(p.overlap));

        System.out.println(sb.toString());
    }

    /**
     * Pairs in solution are sorted like this: (1, 11), (8, 7), (7, 1), (11, 5) => (8, 7), (7, 1), (1, 11), (11, 5)
     * Any solution has unique ordering such as this.
     * @return
     */
    private LinkedList<Pair> sortSolution(){
        LinkedList<Pair> sorted = new LinkedList<>();
        // pairs in solution indexed by its left id and right id
        Pair[] left = new Pair[words.length];
        Pair[] right = new Pair[words.length];

        solution.stream().forEach(p -> {left[p.left] = p; right[p.right] = p;});

        Pair p = solution.getFirst();
        // recreate the text up to the end
        sorted.addLast(p);
        while(left[p.right] != null){
            p = left[p.right];
            sorted.addLast(p);
        }
        // recreate the text up to the beginning
        p = solution.getFirst();
        while(right[p.left] != null){
            p = right[p.left];
            sorted.addFirst(p);
        }
        return sorted;
    }

    // represents pair of text fragments to be joined
    class Pair implements Comparable<Pair>{
        // left, right - indices to words array
        // right - text fragment whose prefix is suffix of the left text fragment
        // overlap - max size of such prefix-suffix of the two text fragments
        final int left, right, overlap;

        public Pair(int left, int right, int overlap){
            this.left = left;
            this.right = right;
            this.overlap = overlap;
        }

        public int compareTo(Pair that){
            if(this.overlap != that.overlap) return Integer.compare(this.overlap, that.overlap);
            else if(this.left != that.left)  return Integer.compare(this.left, that.left);
            else                             return Integer.compare(this.right, that.right);
        }
    }

    /**
     * If pattern is substring of text, then the returned value is the length of pattern, else it returns the max length of prefix of pattern which is also a suffix of the text. That is, text: "abrakadabra", pattern: "abraxas" => overlap is 4, since text ends in "abra" and pattern starts with "abra" as well.
     * @param text
     * @param pattern
     * @param dfa
     * @return
     */
    private int getOverlapSize(String text, String pattern, int[][] dfa){
        // number of matched chars in pattern
        int n = 0;
        for(char c : text.toCharArray()){
            n = dfa[c][n];
            if(n == pattern.length()) return n;
        }
        return n;
    }

    /**
     * Returns DFA - Deterministic finite state automaton for our pattern. The size of our alphabet is 128.
     * The time is proportional to RM.
     * @param pattern
     * @return
     */
    private int[][] KMP(String pattern){
        int[][] dfa = new int[128][pattern.length()];
        // character match at i, regex
        for(int i = 0; i < pattern.length(); i++){
            dfa[pattern.charAt(i)][i] = i + 1;
        }
        // character mismatch at i
        for(int i = 1, dfaPrefix = 0; i < pattern.length(); i++){
            // simulate pattern[1...(i-1)] on already constructed part of DFA
            // by cacheing the value from previous iteration and only incrementing for the pattern[(i-1)]
            if(i > 1) dfaPrefix = dfa[pattern.charAt(i - 1)][dfaPrefix];

            // we have pattern[1...(i-1)] and pattern[i]
            // here we account for the pattern[i], which is the mismatch
            for(int c = 0; c < 128; c++)
                if(dfa[c][i] == 0) dfa[c][i] = dfa[c][dfaPrefix];
        }
        return dfa;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/daVyncy/input.txt"));

        while(textScan.hasNextLine()){
            String[] words = textScan.nextLine().split(";");
            Main test = new Main(words);
        }
    }
}

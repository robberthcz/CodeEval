/**
 PLAY WITH DNA
 CHALLENGE DESCRIPTION:
 This challenge is related to bioinformatics. To help in a DNA research, you have to write an algorithm that finds all the occurrences of a DNA segment in a given DNA string. But, it would be too easy for you. So, write an algorithm with the maximum N number of allowed mismatches. By mismatch we mean the minimum of the total number of substitutions, deletions, and insertions that must be performed on a DNA slice to completely match a given segment. You need to compare the DNA slices with the same length as a given pattern (for example, the segments 'AGTTATC' -> 'AGTATGC' have only 2 mismatches).

 For the DNA string 'CGCCCGAATCCAG' and the segment 'CCC', the first match with the maximum 1 allowed mismatch is 'CGC', the second one is 'GCC', the third one is 'CCC', and so on.

 CCC -> CGC # One mismatch
 CCC -> GCC # One mismatch
 CCC -> CCC # 0 mismatch
 For the given segment 'CCC', the DNA string 'CGCCCGAATCCAG', and the maximum allowed mismatch '1', the list of the matches is 'CGC GCC CCC CCG TCC CCA'.

 INPUT SAMPLE:
 Your program should accept a path to a filename as its first argument. Each line in the file contains a segment of DNA, the maximum number of allowed mismatches N, and a DNA string separated by a single space.
 CCC 1 CGCCCGAATCCAG
 GCGAG 2 CCACGGCCTATGTATTTGCAAGGATCTGGGCCAGCTAAATCAGCACCCCTGGAACGGCAAG
 GTTCATTTTGTTGCGCGCATAG
 CGGCGCC 1 ACCCCCGCAGCCATATGTCCCCAGCTATTTAATGAGGGCCCCGAACACGGGGAGTCTTA
 CACGATCTGCCCTGGAATCGC

 OUTPUT SAMPLE:
 Print out all the occurrences of a segment S in a DNA string in the order from the best match (separated by a single space) taking into account the number of allowed mismatches. In case of several segments with the equal number of matches, print them in alphabetical order. If there is no such a case, print out 'No match'.
 CCC CCA CCG CGC GCC TCC
 GCAAG GCAAG GCCAG GCGCG GCGCA GCTAA
 No match

 CONSTRAINTS:
 The length of a DNA string is in a range from 100 to 300.
 N is in a range from 0 to 40.
 The length of a segment S is in a range from 3 to 50.
 */
package playWithDNA;

/**
 *
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
    private String pattern, text;
    private int k;
    private HashMap<String, Integer> matchToK;

    public Main(String pattern, int k, String text){
        this.pattern = pattern;
        this.text = text;
        this.k = k;
        //this.k = 2;
       // this.pattern = "CACD";
        //this.text = "BCBACBBB";
        this.matchToK = new HashMap<>();

        int[][] score = DP( this.pattern, this.text);
        LinkedList<String> results = new LinkedList<>();

        printArray(score);

        for(int i = 1; i < score[0].length; i++){
            if(score[score.length - 1][i] > this.k)
                continue;
            Set<String> set = new HashSet<>();
            reconstruct(score, score.length - 1, i, 0, "", set);
            if(!set.isEmpty()){
                for(String S : set){
                    matchToK.put(S, score[score.length - 1][i]);
                }
                results.addAll(set);
            }
        }

        Collections.sort(results, new MISMATCH_ORDER());
        String res = results.toString();
        res = res.substring(1, res.length() - 1).replaceAll(",", "");
        if(res.equals(""))
            res = "No match";
        System.out.println(res);
    }

    public final class MISMATCH_ORDER implements Comparator<String>{
        public int compare(String S, String T){
            int kS = matchToK.get(S);
            int kT = matchToK.get(T);

            if(kS != kT) return Integer.compare(kS, kT);
            else return S.compareTo(T);
        }
    }

    private void printArray(int[][] a){
        for (int[] row : a)
        {
            System.out.println(Arrays.toString(row));
        }
    }

    private int[][] DP(String pattern, String text){
        int[][] score = new int[pattern.length() + 1][text.length() + 1];

        for(int i = 0; i < score.length; i++){
            score[i][0] = i;
        }

        for(int j = 1; j < score[0].length; j++){
            for(int i = 1; i < score.length; i++){
                int match = pattern.charAt(i-1) == text.charAt(j-1) ? 0 : 1;
                score[i][j] = Math.min(score[i-1][j-1] + match, Math.min(score[i-1][j] + 1, score[i][j-1] + 1));

            }
        }
        return score;
    }

    private void reconstruct(int[][] a, int row, int col, int len, String tmp, Set<String> set){
        //System.out.println("Row: " + row + ", col: " + col + ", a: " + a[row][col]);
        if(row == 0){
            //System.out.println(len + " " + pattern.length());
            if(len == pattern.length())
                set.add(tmp);
            return;
        }
        else if(row <= 0 || col <= 0)
            return;

        int match = (pattern.charAt(row - 1) == text.charAt(col - 1)) ? 0 : 1;

        if (a[row - 1][col - 1] + match == a[row][col]) reconstruct(a, row - 1, col - 1, len+1, text.charAt(col-1) + tmp, set);
        if(a[row - 1][col] + 1 == a[row][col])          reconstruct(a, row-1, col, len, tmp, set);
        if(a[row][col-1] + 1 == a[row][col])            reconstruct(a, row, col-1, len+1, text.charAt(col-1) + tmp, set);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/playWithDNA/input_large.txt"));

        while(textScan.hasNextLine()){
            String line[] = textScan.nextLine().trim().split(" ");

            String pattern = line[0];
            int k = Integer.parseInt(line[1]);
            String text = line[2];

            Main test = new Main(pattern, k, text);
        }
    }
}

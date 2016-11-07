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
import java.math.BigInteger;
import java.util.*;

public class Main {
    private String pattern, text;
    private int k;

    public Main(String pattern, int k, String text){
        this.pattern = pattern;
        this.text = text;
        this.k = k;
        String S = "GATTACA";
        String T = "GCATGCU";

        int[][] score = NW( S, T);
        printArray(score);
        System.out.println();
        score = NW(S, T.substring(1) + 'C');
        printArray(score);
        //String match = reconstruct(score, S, T, score.length - 1, score[0].length - 1,"");
        //System.out.println(match);
        //System.out.println(getMismatches(S, match));
    }

    private void printArray(int[][] a){
        for (int[] row : a)
        {
            System.out.println(Arrays.toString(row));
        }
    }

    private int[][] NW(String S, String T){
        int[][] score = new int[S.length() + 1][T.length() + 1];

        for(int i = 1; i < score.length; i++){
            score[i][0] = i * (-1);
        }

        for(int i = 1; i < score[0].length; i++){
            score[0][i] = i * (-1);
        }

        for(int i = 1 ; i < score.length; i++){
            for(int j = 1; j < score[0].length; j++){
                int match = (S.charAt(i - 1) == T.charAt(j - 1)) ? 1 : -1;
                score[i][j] = Math.max(score[i-1][j-1] + match, Math.max(score[i-1][j] - 1, score[i][j-1] - 1));
                //System.out.println("i: " + i + ", j: " + j + ", score: " + score[i][j]);
            }
        }

        return score;
    }

    private String reconstruct(int[][] score, String S, String T, int row, int col, String tmp){
        if(row == 0 && col == 0) return tmp;
        int match = (S.charAt(row - 1) == T.charAt(col - 1)) ? 1 : -1;

        if      (score[row - 1][col - 1] + match == score[row][col]) return reconstruct(score, S, T, row-1, col-1, T.charAt(col - 1) + tmp);
        else if (score[row - 1][col] - 1 == score[row][col])         return reconstruct(score, S, T, row-1, col, '-' + tmp);
        else                                                         return reconstruct(score, S, T, row, col-1, '+' + tmp);
    }

    private int getMismatches(String S, String temp){
        int mismatches = 0;
        int j = 0;
        for(int i = 0; i < temp.length(); i++){
            if(S.charAt(j) == temp.charAt(i))
                mismatches++;

            if(temp.charAt(i) != '+')
                j++;
        }
        return mismatches;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/playWithDNA/input_large.txt"));

        Main test = new Main("", 0,"");

        /*while(textScan.hasNextLine()){
            String line[] = textScan.nextLine().trim().split(" ");

            String pattern = line[0];
            int k = Integer.parseInt(line[1]);
            String text = line[2];

            Main test = new Main(pattern, k, text);

            String res = test.search().toString();
            res = res.substring(1, res.length() - 1).replaceAll(",", "");
            if(res.equals(""))
                res = "No match";
            System.out.println(res);
        }*/
    }
}

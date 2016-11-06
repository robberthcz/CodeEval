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
    private String pattern, text;      // the pattern  // needed only for Las Vegas
    private int k, blockSize, numOfBlocks;
    private long[] patHash, textHash;
    private int m;           // pattern length
    private long q;          // a large prime, small enough to avoid long overflow
    private int R;           // radix
    private long RM;         // R^(M-1) % Q

    public Main(String pattern, int k, String text) {
        this.pattern = pattern;
        this.text = text;
        this.k = k;
        R = 256;
        m = pattern.length();
        q = longRandomPrime();

        this.blockSize = (k == 0) ? m : Math.max(m / (2 * k), 1);
        this.numOfBlocks = m / blockSize;

        // precompute R^(m-1) % q for use in removing leading digit
        RM = 1;
        for (int i = 1; i <= blockSize-1; i++)
            RM = (R * RM) % q;

        this.patHash = new long[numOfBlocks];
        this.textHash = new long[numOfBlocks];

        for(int i = 0; i < numOfBlocks; i++){
            int blockEnd = (i == numOfBlocks - 1) ? m : (i + 1) * blockSize;
            patHash[i] = hash(pattern.substring(i * blockSize, blockEnd));
            textHash[i] = hash(text.substring(i * blockSize, blockEnd));
        }
    }


    private long hash(String key) {
        long h = 0;
        for (int j = 0; j < key.length(); j++)
            h = (R * h + key.charAt(j)) % q;
        return h;
    }

    private int getHashMismatches(){
        int mismatches = 0;
        for(int i = 0; i < textHash.length; i++){
            if(textHash[i] != patHash[i])
                mismatches++;
        }
        return mismatches;
    }

    private int getMismatches(String textSubstr){
        int mismatches = 0;
        for(int i = 0; i < pattern.length(); i++){
            if(textSubstr.charAt(i) != pattern.charAt(i))
                mismatches++;
        }
        return mismatches;
    }

    public List<String> search() {
        int n = text.length();
        LinkedList<String> list = new LinkedList<String>();

        // check for match at offset 0
        if (getHashMismatches() <= k && getMismatches(text.substring(0, m)) <= k)
            list.add(text.substring(0, m));

        // check for hash match; if hash match, check for exact match
        for (int i = m; i < n; i++) {

            for(int j = 0; j < textHash.length; j++){
                int nextChar = (j == textHash.length - 1) ? i : (i-m + (j+1)*blockSize);
                // Remove leading digit, add trailing digit, check for match.
                textHash[j] = (textHash[j] + q - RM*text.charAt(i-m + j*blockSize) % q) % q;
                textHash[j] = (textHash[j]*R + text.charAt(nextChar)) % q;
            }

            if (getHashMismatches() <= k && getMismatches(text.substring(i - m + 1, i + 1)) <= k)
                list.add(text.substring(i - m + 1, i + 1));
        }

        Collections.sort(list, new MISMATCH_CMP());
        return list;
    }

    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    public final class MISMATCH_CMP implements Comparator<String>{
        public int compare(String S, String T){
            int kS = getMismatches(S);
            int kT = getMismatches(T);

            if(kS != kT) return Integer.compare(kS, kT);
            else return S.compareTo(T);
        }
    }



    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/playWithDNA/input_large.txt"));

        while(textScan.hasNextLine()){
            String line[] = textScan.nextLine().trim().split(" ");

            String pattern = line[0];
            int k = Integer.parseInt(line[1]);
            String text = line[2];

            Main test = new Main(pattern, k, text);

            String res = test.search().toString();
            res = res.substring(1, res.length() - 1);
            if(res.equals(""))
                res = "No match";
            System.out.println(res);
        }
    }
}

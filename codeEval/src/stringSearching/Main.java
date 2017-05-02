/**
 String Searching
 Challenge Description:
 You are given two strings. Determine if the second string is a substring of the first (Do NOT use any substr type library function). The second string may contain an asterisk(*) which should be treated as a regular expression i.e. matches zero or more characters. The asterisk can be escaped by a \ char in which case it should be interpreted as a regular '*' character. To summarize: the strings can contain alphabets, numbers, * and \ characters.

 Input sample:
 Your program should accept as its first argument a path to a filename. The input file contains two comma delimited strings per line. E.g.
 Hello,ell
 This is good, is
 CodeEval,C*Eval
 Old,Young

 Output sample:
 If the second string is indeed a substring of the first, print out a 'true'(lowercase), else print out a 'false'(lowercase), one per line. E.g.
 true
 true
 true
 false
 */
package stringSearching;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * We use the Knuth-Morris-Pratt algorithm from Algorithms, Robert Sedgewick.
 * This problem brings slight addition in that we also have to account for regular expressions - asterisk * matches zero or more characters.
 * If asterisk * is escaped in pattern, than we have to match asterisk and not use it as a regular expression.
 * Asterisk * can be escaped only in pattern obviously, in the text asterisk * is not escaped and acts as a normal character from the beginning.
 * Created by Robert on 23.10.2016.
 */
public class Main {

    /**
     * Returns DFA - Deterministic finite state automaton for our pattern. The size of our alphabet is 128.
     * The time is proportional to RM.
     * @param pattern
     * @return
     */
    private static int[][] KMP(String pattern){
        int[][] dfa = new int[128][pattern.length()];
        // character match at i, regex
        for(int i = 0; i < pattern.length(); i++){
            dfa[pattern.charAt(i)][i] = i + 1;
            // * matches zero or more characters
            if(pattern.charAt(i) == '*'){
                for(int j = 0; j < 128; j++){
                    // * matches to next char in pattern
                    dfa[j][i] = i + 1;
                    // next char in pattern => his state does not change unless there is match
                    if(i + 1 < pattern.length()) dfa[j][i + 1] = i + 1;
                }
            }
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

    /**
     * Returns DFA - Deterministic finite state automaton for our pattern.
     * @param text
     * @param pattern
     * @return
     */
    public static boolean isSubstr(String text, String pattern){
        int[][] dfa = KMP(pattern);
        // number of matched chars in pattern
        int n = 0;

        for(char c : text.toCharArray()){
            n = dfa[c][n];
            if(n == pattern.length()) return true;
        }
        return false;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/stringSearching/input.txt"));

        while(textScan.hasNextLine()){
            String[] line = textScan.nextLine().split(",");
            // deal with escaped asterisk in the pattern
            line[0] = line[0].replace("*", "="); // text
            line[1] = line[1].replace("\\*", "="); // pattern

            System.out.println(isSubstr(line[0], line[1]));
        }
    }
}

/**
 Less Money, More Problems
 Challenge Description:

 This challenge appeared in Google Code Jam competition, licensed under Creative Commons Attribution License

 Up until today, the nation you live in has used D different positive integer denominations of coin for all transactions. Today, the queen got angry when a subject tried to pay his taxes with a giant sack of low-valued coins, and she just decreed that no more than C coins of any one denomination may be used in any one purchase. For instance, if C = 2 and the existing denominations are 1 and 5, it is possible to buy something of value 11 by using two 5s and one 1, or something of value 12 by using two 5s and two 1s, but it is impossible to buy something of value 9 or 17.

 You cannot directly challenge the queen's decree, but you happen to be in charge of the mint, and you can issue new denominations of coin. You want to make it possible for any item of positive value at most V to be purchased under the queen's new rules. (Note that this may not necessarily have been possible before the queen's decree.) Moreover, you want to introduce as few new denominations as possible, and your final combined set of pre-existing and new denominations may not have any repeats.

 What is the smallest number of new denominations required?

 Input sample:
 The first argument is a path to a file. Each line of the input file contains one test case. Each test case consists of three parts separated by pipe symbol: 1) an integer C; 2) an integer V; 3) sorted list of space separated integers - all current denominations.

 1 | 3 | 1 2
 1 | 6 | 1 2 5
 2 | 3 | 3
 1 | 100 | 1 5 10 25 50 100

 Output sample:
 For each test case, output one line containing the minimum number of new denominations required, as described above.
 0
 1
 1
 3

 Constraints:
 1.The number of test cases is 40.
 2.1 ≤ C ≤ 100.
 3.1 ≤ V ≤ 109.
 4.The number of current denominations is in range from 1 to 100.
 */
package lessMoneyMoreProblems;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Robert on 24.8.2016.
 * Took some ideas from https://code.google.com/codejam/contest/4244486/dashboard#s=a&a=2
 * I would just do it with a lot more complexity, but I did realize two of the three most important properties to solve the problem
 */
public class Main {

    public static int solve(int C, long V, LinkedList<Long> initDenoms){
        int newDenomsCount = 0;
        // current value to which our denominations need to sum up to
        long curVal = 1;

        while(curVal <= V){
            // new denomination needed
            if(initDenoms.size() == 0 || initDenoms.peekFirst() > curVal){
                newDenomsCount++;
                // with new denomination, to each of the sums already reproduce-able by existing denominations,
                // we add the value of the new denomination and expand the set of reproduce-able sums
                // we need all 1<= sum<= V
                // so we increase the boundary by (value_of_new_denomination*possible_number_of_coins_of_each_denomination)
                curVal += C * curVal;
            }
            // use one of the initial denominations
            else{
                curVal += C * initDenoms.removeFirst();
            }
        }
        return newDenomsCount;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/lessMoneyMoreProblems/input.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            //System.out.println(line);
            String[] vals = line.split("\\|");
            int C = Integer.parseInt(vals[0].trim());
            long V = Long.parseLong(vals[1].trim());
            LinkedList<Long> initDenoms = new LinkedList<Long>();

            for(String num : vals[2].trim().split(" ")) initDenoms.addLast(Long.parseLong(num.trim()));
            //System.out.println("V: " + V + " C: " + C);
            //System.out.println(curDenoms.toString());
            System.out.println(solve(C, V, initDenoms));
        }
    }
}

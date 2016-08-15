package runningForPresident;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Robert on 14.8.2016.
 */
public class Main {

    public static int parseNum(String line){
        return Integer.parseInt(line.split(":")[1].trim());
    }

    public static String parseIssue(String line){
        return line.split(":")[0].trim();
    }

    public static String stateToString(int stateId){
        String toString = "";


        return toString;
    }

    public static void main(String[] args) throws FileNotFoundException {
        HashMap<Integer, String> costToIssue = new HashMap<Integer, String>();
        HashMap<String, Integer> issueToId = new HashMap<String, Integer>();
        Scanner textScan = new Scanner(new FileReader("test-cases/runningForPresident.txt"));

        int numOfStates = 51;
        int numOfIssues = parseNum(textScan.nextLine());
        // columns => states, rows => issue, last column => sums for each issue, first row => n. of votes for the state
        int[][] voteTable = new int[numOfIssues + 1][numOfStates + 1];

        // skip empty line
        textScan.nextLine();
        // parse issues
        for(int i = 0; i < numOfIssues; i++){
            String line = textScan.nextLine();
            String issue = parseIssue(line);
            costToIssue.put(parseNum(line), issue);
            issueToId.put(issue, i);
        }

        for(int state = 0; state < numOfStates; state++){
            // skip empty line
            textScan.nextLine();
            // parse states
            for(int i = 0; i < numOfIssues + 2; i++){
                String line = textScan.nextLine();
                // skip name of the state
                if(i == 0)
                    continue;
                else if(i == 1){
                    int numOfVotes = parseNum(line);
                    voteTable[0][state] = numOfVotes;
                    continue;
                }
                String issue = parseIssue(line);
                int votesForIssue = parseNum(line);
                int id = issueToId.get(issue);
                voteTable[id + 1][state] = votesForIssue;
                // add to total sum
                voteTable[id+1][numOfStates] += votesForIssue;
            }
        }
        System.out.println(Arrays.deepToString(voteTable));

        for(int i = 0; i < numOfIssues; i ++)
            System.out.println(voteTable[i + 1][numOfStates]);
    }
}

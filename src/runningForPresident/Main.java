/*
Running for president
        Challenge Description:

        You're planning your party's campaign strategy to win the Presidency of the United States.
        To win, your candidate has to navigate many controversial issues, which influence voters, such as healthcare, immigration, education, energy, independence, jobs, taxes, environment, and so on. Each of these is important, but also expensive to address.

        Because the United States presidential election is determined by states casting votes in the Electoral College, you need minimum 270 of the total 538 Electoral College votes to win.

        While not completely accurate, for this challenge we use the following criteria:
        •States may or may not value an issue.
        •States value each issue differently.
        • Winning 51% of any state gives you all of that state's votes. If you have less than 51 %, you won't receive any votes.
        Your task is to put together a winning strategy by identifying the fewest issues you must address to win 270 votes.

        Input sample:
        Your program should accept a file as its first argument. The file content is the following:

        Social issues: 9

        Healthcare: 33995797
        Immigration: 2089699
        Education: 37182280
        Energy independence: 1344134
        ...
        Wealth inequality: 99237127
        Increase military spending: 44066575

        Mississippi
        Votes: 6
        Creating jobs: 1
        Jobs: 0
        ...
        Increase military spending: 0
        Education: 1
        Energy independence: 0

        Oklahoma
        Votes: 7
        Creating jobs: 1
        Jobs: 2
        ...
        Increase military spending: 0
        Education: 1
        Energy independence: 2
        ...
        Maine
        Votes: 4
        Creating jobs: 0
        Jobs: 0
        ...
        Increase military spending: 0
        Education: 1
        Energy independence: 3

        The first line identifies the total number of potential issues, the second—information about costs for each program, and the third — information about each state, separated by spacing. Each state has a name, number of votes, and a list of issues that you can choose in each state. Each of these issues is valued based on the number of votes it can get in each state. Remember, you must get the majority of votes in each state to win.

        Output sample:

        Print out the list of issues you want to cover in you electoral program in alphabetical order.

        For example:

        Energy independence
        Healthcare
        Immigration
        Increase military spending

        Remember, that your task is to create a program with the fewest number of issues. If there are several variants of program with the fewest number of issues, then you need to choose the program with minimum costs.
*/


package runningForPresident;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 14.8.2016.
 */
public class Main {
    private int[][] voteTable;
    private String[] idToIssue;
    private HashMap<String, Integer> issueToCost;
    private int numOfIssues, numOfStates;

    public Main(int[][] voteTable, HashMap<String, Integer> issueToCost, String[] idToIssue, int numOfIssues, int numOfStates){
        this.voteTable = voteTable;
        this.idToIssue = idToIssue;
        this.issueToCost = issueToCost;
        this.numOfIssues = numOfIssues;
        this.numOfStates = numOfStates;

        //System.out.println(idToIssue.toString());
        LinkedList<Integer> minCostList = null;
        for(int i = 1; i <= numOfIssues; i++){
            int minCost = Integer.MAX_VALUE;
            for(LinkedList<Integer> list : genCombinations(i)){
                int total = 0;
                for(int j = 0; j < numOfStates; j++)
                    total += getVotes(list, j);
                int listCost = issuesToCost(list);
                //System.out.println(list.toString() + " VOTES: "  + total + " COST: " + listCost);
                //find min
                if(total >= 270 && listCost < minCost){
                    minCostList = list;
                    minCost = listCost;
                }
            }
            if(minCostList != null) {
                //System.out.println("Minimum cost list: " + minCostList.toString() + " COST: " + minCost);
                break;
            }
        }
        TreeSet<String> sortedSet = new TreeSet<String>();
        for(int id : minCostList) sortedSet.add(idToIssue[id]);
        for(String s : sortedSet) System.out.println(s);
    }

    private int issuesToCost(LinkedList<Integer> issuesId){
        int total = 0;
        for(int id : issuesId)
            total += issueToCost.get(idToIssue[id]);
        return total;
    }

    public String stateToString(int stateId){
        String toString = "";
        toString += "STATE " + stateId + "\n";
        toString += "Number of votes: " + voteTable[0][stateId] + "\n";
        for(int i = 0; i < numOfIssues; i++)
            toString += idToIssue[i] + ": " + voteTable[i + 1][stateId] + "\n";
        return toString;
    }

    private int getVotes(LinkedList<Integer> issues, int stateId){
        int totalVotes = voteTable[0][stateId];
        int votesForIssues = 0;
        for(int issue : issues)
            votesForIssues += voteTable[issue + 1][stateId];
        // to receive all votes for the state, need to have majority of votes
        return ( (Double.valueOf(votesForIssues) / Double.valueOf(totalVotes) ) > 0.50) ? totalVotes : 0;
    }

    private LinkedList<LinkedList<Integer>> genCombinations(int n){
        int[] arr = new int[numOfIssues];
        for(int i = 0; i < numOfIssues; i++)
            arr[i] = i;
        LinkedList<LinkedList<Integer>> acc = new LinkedList<LinkedList<Integer>>();
        LinkedList<Integer> curr = new LinkedList<Integer>();
        genCombinations(arr, n, 0, acc, curr);
        return acc;
    }

    private void genCombinations(int[] arr, int n, int start, LinkedList<LinkedList<Integer>> acc, LinkedList<Integer> curr){
        if(curr.size() == n){
            //System.out.println(curr.toString() + " " + curr.size());
            acc.add((LinkedList<Integer>) curr.clone());
            return;
        }
        for(int i = start; i < arr.length; i++){
            curr.add(arr[i]);
            genCombinations(arr, n, i + 1, acc, curr);
            curr.removeLast();
        }
    }
    public static int parseNum(String line){
        return Integer.parseInt(line.split(":")[1].trim());
    }

    public static String parseIssue(String line){
        return line.split(":")[0].trim();
    }

    public static void main(String[] args) throws FileNotFoundException {
        final int numOfStates, numOfIssues;
        HashMap<String, Integer> issueToCost, issueToId;
        int[][] voteTable;
        String[] idToIssue;

        Scanner textScan = new Scanner(new FileReader("test-cases/runningForPresident.txt"));

        numOfStates = 51;
        numOfIssues = parseNum(textScan.nextLine());
        issueToCost = new HashMap<String, Integer>();issueToId = new HashMap<String, Integer>();
        idToIssue = new String[numOfIssues];
        // columns => states, rows => issue, last column => sums for each issue, first row => n. of votes for the state
        voteTable = new int[numOfIssues + 1][numOfStates + 1];

        // skip empty line
        textScan.nextLine();
        // parse issues and their costs
        for(int i = 0; i < numOfIssues; i++){
            String line = textScan.nextLine();
            String issue = parseIssue(line);
            issueToCost.put(issue, parseNum(line));
            issueToId.put(issue, i);
            idToIssue[i] = issue;
        }

        for(int state = 0; state < numOfStates; state++){
            // skip empty line and name of state
            textScan.nextLine();
            textScan.nextLine();
            // save number of total votes for this state
            int numOfVotes = parseNum(textScan.nextLine());
            voteTable[0][state] = numOfVotes;
            // parse states
            for(int i = 0; i < numOfIssues; i++){
                String line = textScan.nextLine();
                String issue = parseIssue(line);
                int votesForIssue = parseNum(line);
                int id = issueToId.get(issue);
                voteTable[id + 1][state] = votesForIssue;
                // add to total sum
                voteTable[id+1][numOfStates] += votesForIssue;
            }
        }

        Main test = new Main(voteTable, issueToCost, idToIssue, numOfIssues, numOfStates);
    }
}

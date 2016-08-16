package runningForPresident;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 14.8.2016.
 */
public class Main {
    private int[][] voteTable;
    private HashMap<String, Integer> issueToId;
    private HashMap<Integer, String> idToIssue;
    private HashMap<String, Integer> issueToCost;
    private int numOfIssues, numOfStates;

    public Main(int[][] voteTable, HashMap<String, Integer> issueToCost, HashMap<String, Integer> issueToId, final HashMap<Integer, String> idToIssue, int numOfIssues, int numOfStates){
        this.voteTable = voteTable;
        this.issueToId = issueToId;
        this.idToIssue = idToIssue;
        this.issueToCost = issueToCost;
        this.numOfIssues = numOfIssues;
        this.numOfStates = numOfStates;

        LinkedList<LinkedList<Integer>> combs = new LinkedList<LinkedList<Integer>>();
        combs = genCombinations(2);

        /*for(LinkedList<Integer> list : combs)
            System.out.print(list.toString() + ",");

        LinkedList<Integer> list = new LinkedList<Integer>();
        list.add(3);
        System.out.println();
        System.out.println(getVotes( list, 2));
        System.out.println(getVotes( list, 1));
        */
        System.out.println(idToIssue.toString());
        LinkedList<Integer> minCostList = null;
        for(int i = 1; i <= numOfIssues; i++){
            int minCost = Integer.MAX_VALUE;
            for(LinkedList<Integer> list : genCombinations(i)){
                int total = 0;
                for(int j = 0; j < numOfStates; j++)
                    total += getVotes(list, j);
                int listCost = issuesToCost(list);
                System.out.println(list.toString() + " VOTES: "  + total + " COST: " + listCost);
                if(total >= 270 && listCost < minCost){
                    minCostList = list;
                    minCost = listCost;
                }
            }
            if(minCostList != null) {
                System.out.println("Minimum cost list: " + minCostList.toString() + " COST: " + minCost);
                break;
            }
        }
        TreeSet<String> sortedSet = new TreeSet<String>();
        for(int id : minCostList) sortedSet.add(idToIssue.get(id));
        for(String s : sortedSet) System.out.println(s);
    }

    private int issuesToCost(LinkedList<Integer> issuesId){
        int total = 0;
        for(int id : issuesId)
            total += issueToCost.get(idToIssue.get(id));
        return total;
    }

    public static int parseNum(String line){
        return Integer.parseInt(line.split(":")[1].trim());
    }

    public static String parseIssue(String line){
        return line.split(":")[0].trim();
    }

    public String stateToString(int stateId){
        String toString = "";
        toString += "STATE " + stateId + "\n";
        toString += "Number of votes: " + voteTable[0][stateId] + "\n";
        for(int i = 0; i < numOfIssues; i++)
            toString += idToIssue.get(i) + ": " + voteTable[i + 1][stateId] + "\n";

        return toString;
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

    private int getVotes(LinkedList<Integer> issues, int stateId){
        int totalVotes = voteTable[0][stateId];
        int votesForIssues = 0;
        for(int issue : issues)
            votesForIssues += voteTable[issue + 1][stateId];
        return ( (Double.valueOf(votesForIssues) / Double.valueOf(totalVotes) ) > 0.50) ? totalVotes : 0;
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


    public static void main(String[] args) throws FileNotFoundException {
        HashMap<String, Integer> issueToCost = new HashMap<String, Integer>();
        HashMap<String, Integer> issueToId = new HashMap<String, Integer>();
        HashMap<Integer, String> idToIssue = new HashMap<Integer, String>();
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
            issueToCost.put(issue, parseNum(line));
            issueToId.put(issue, i);
            idToIssue.put(i, issue);
        }

        for(int state = 0; state < numOfStates; state++){
            // skip empty line and name of state
            textScan.nextLine();
            textScan.nextLine();
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
        System.out.println(Arrays.deepToString(voteTable));

        for(int i = 0; i < numOfIssues; i ++)
            System.out.println(voteTable[i + 1][numOfStates]);

        Main test = new Main(voteTable, issueToCost, issueToId, idToIssue, numOfIssues, numOfStates);
        //System.out.println(test.stateToString(0));
        //System.out.println(test.stateToString(48));
        //System.out.println(test.stateToString(50));

    }
}

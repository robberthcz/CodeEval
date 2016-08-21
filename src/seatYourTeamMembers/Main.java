package seatYourTeamMembers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 19.8.2016.
 */
public class Main {
    // number of available places in the office
    private int N;
    private HashMap<Integer, HashSet<Integer>> adj;
    private HashSet<Integer> workers, seats;
    private int[] pair;

    public Main(int N){
        this.N = N;
        adj = new HashMap<Integer, HashSet<Integer>>();
        workers = new HashSet<Integer>();
        seats = new HashSet<Integer>();
        pair = new int[2*N + 1];
    }

    public boolean allMatched(){
        boolean allMatched = true;
        for(int worker : workers) if(pair[worker] == 0) allMatched = false;
        return allMatched;
    }

    public void addWorkerToSeat(int worker, int seat){
        int seatPlusN = seat + N;
        if(!adj.containsKey(worker)) adj.put(worker, new HashSet<Integer>());
        if(!adj.containsKey(seatPlusN)) adj.put(seatPlusN, new HashSet<Integer>());

        workers.add(worker);
        seats.add(seatPlusN);
        adj.get(worker).add(seatPlusN);
        adj.get(seatPlusN).add(worker);
    }

    private boolean bfs(){
        int[] edgeTo = new int[2*N + 1];
        HashSet<Integer> workersSet = new HashSet<Integer>();
        Arrays.fill(edgeTo, Integer.MIN_VALUE);
        edgeTo[0] = 0;
        LinkedList<Integer> Q = new LinkedList<Integer>();
        for(int worker : workers){
            if(pair[worker] == 0){
                Q.add(worker);
                workersSet.add(worker);
                edgeTo[worker] = 0;
                edgeTo[0] = 0;
            }
        }
        while(Q.size() > 0){
            int worker = Q.removeFirst();
            for(int seat : adj.get(worker)){
                if(pair[worker] == seat) continue;

                if(pair[seat] == 0){
                    edgeTo[seat] = worker;
                    reassign(seat, edgeTo);
                    return true;
                }
                else if(pair[seat] != 0){
                    if(workersSet.contains(pair[seat])) continue;
                    Q.add(pair[seat]);
                    workersSet.add(pair[seat]);
                    edgeTo[pair[seat]] = seat;
                    edgeTo[seat] = worker;
                }
            }
        }
        return false;
    }

    private void reassign(int freeSeat, int[] edgeTo){
        int e = freeSeat;
        while(edgeTo[e] != 0){
            pair[e] = edgeTo[e];
            pair[edgeTo[e]] = e;
            // next edge
            e = edgeTo[e];
            // skip matched edge
            e = edgeTo[e];
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("test-cases/seatYourTeamMembers.txt"));
        //String exc = "";

        while(textScan.hasNextLine()){
            String line = textScan.nextLine().replaceAll(" ", "").replaceAll("\\[", "");
            //exc += textScan.nextLine() + "\n";
            //System.out.println(line);
            String[] spl = line.split(";");
            int N = Integer.parseInt(spl[0]);
            Main test = new Main(N);
            String[] edges = spl[1].split("\\],");
            for(String s : edges){
                s = s.replaceAll("\\]", "");
                int worker = Integer.parseInt(s.split(":")[0]);
                //System.out.println(worker);
                String[] seats = s.split(":")[1].split(",");
                for(String seat : seats){
                    int seatInt = Integer.parseInt(seat);
                    //System.out.println(seat);
                    test.addWorkerToSeat(worker, seatInt);
                }

            }

            while(test.bfs());
            if(test.allMatched()) System.out.println("Yes");
            else System.out.println("No");


        }
        //throw new RuntimeException(exc);
    }
}

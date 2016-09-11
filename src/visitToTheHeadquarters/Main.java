/**
 VISIT TO THE HEADQUARTERS
 CHALLENGE DESCRIPTION:
 The Department of Security has a new headquarters. The building has several floors, and on each floor there are rooms numbered XXYY, where XX stands for the floor number, YY for the room number, and XX > 0; YY <= 10. The building has paternoster – a passenger elevator, which consists of several cabins that move slowly up and down without stopping. From time to time, the agents visit the headquarters. During the visit they go to see several rooms and stay in each room for some time. Due to the security reasons, only one agent can be in the room at the same time. The same rule applies to the elevators, only one agent can be in the elevator at the same time. The visits are planned to be completed within one day. Each agent visits the headquarters once a day.
 The agent enters the building on the 1st floor, passes the reception, and visits the rooms according to his/her list. Agents always visit the rooms in the increasing room number. The agents form a linear hierarchy according to the personal codes that were assigned to them. Agents with higher seniority have smaller codes in lexicographical order. The two agents cannot have the same code.
 If there are more than one agent who wants to enter a room, or an elevator, they have to form a queue. The queue is formed according to the codes. The higher the seniority of the agent, the closer to the top of the queue he stands. Agents can enter the elevator every 5 seconds. There is a queue for up and down directions of the elevator on each floor. After visiting the last room in the headquarters, the agent uses elevator to the first floor (if necessary) and exits the building.
 The time necessary to move from one point in the headquarters to another is set as follows:
 Entering the building (passing the reception and reaching the elevator or a room on the first floor) takes 10 seconds.
 Exiting the building (stepping out of the elevator or a room on the first floor and passing the reception) takes 10 seconds.
 On the same floor, the way from the elevator to the room (or to the queue in front of the room), or from the room to the elevator (or to the queue in front of the elevator), or from one room to another (or to the queue in front of the room) takes 10 seconds.
 The way up or down from one floor to another using an elevator takes 10 seconds.
 Write a program that determines time when the agent leaves the headquarters.

 INPUT SAMPLE:
 Your program should accept a filename as its first argument. Each line of the file contains the description of the agent visit. The description of each visit consists of agent's one character code (C), time (T) when the agent enters the headquarters, and the room numbers (XXYY) followed by the time in seconds (S) that agent may spend in the room. E.g.
 C T XXYY S XXYY S XXYY S ...
 The time when the agent enters the headquarters is in the HH:MM:SS format. E.g.

 A 09:00:00 0203 5 0210 10 0305 5 0604 10 0605 10 0901 10 0908 10
 B 09:00:25 0205 10 0404 5 0501 5 0602 5 0703 5 0807 5
 C 09:00:45 0109 10 0110 5 0207 5 0208 10 0401 10 0510 5
 D 09:01:15 0310 5 0404 5 0503 10 0603 5 0604 5 0704 10 0708 5 0910 5 1005 10
 OUTPUT SAMPLE:
 For each test case print out the time when agent entered the headquarters and the time when agent left the headquarters in the HH:MM:SS format separated by a single space. E.g.
 09:00:00 09:05:50
 09:00:25 09:05:40
 09:00:45 09:04:40
 09:01:15 09:08:15
 CONSTRAINTS:

 S is in range [5, 100]
 C is in range [A, Z]
 */
package visitToTheHeadquarters;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Rob on 9/11/2016.
 */
public class Main {
    static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

    public static void solve(List<Agent> agents, Map<Integer, Boolean> isRoomFree){
        TreeSet<Edge> Q = new TreeSet<Edge>();
        for(Agent agent : agents) Q.add(agent.edges.removeFirst());

        while(!Q.isEmpty()){
            Edge e = Q.pollFirst();
            if(e.from == e.to){
                isRoomFree.put(e.from, true);
                e.agent.cur.add(Calendar.SECOND, e.cost);
                if(e.agent.edges.size() != 0) Q.add(e.agent.edges.removeFirst());
            }
            else if(e.to >= 100 && isRoomFree.get(e.to)){
                makeQueueWait(e, Q, 5);
                e.agent.cur.add(Calendar.SECOND, e.cost);
                isRoomFree.put(e.to, false);
                if(e.agent.edges.size() != 0) Q.add(e.agent.edges.removeFirst());
            }
            else if(e.to >= 100 && !isRoomFree.get(e.to)){
                e.agent.cur.add(Calendar.SECOND, 5);
                Q.add(e);
            }
            else if(e.to < 100){
                makeQueueWait(e, Q, 5);
                e.agent.cur.add(Calendar.SECOND, e.cost);
                if(e.agent.edges.size() != 0) Q.add(e.agent.edges.removeFirst());
            }

        }

        for(Agent agent : agents) System.out.println(sdf.format(agent.start.getTime()) + " " + sdf.format(agent.cur.getTime()));
    }

    private static void makeQueueWait(Edge e, TreeSet<Edge> Q, int cost){
        if(Q.size() == 0) return;
        while(Q.first().to == e.to && Q.first().getTime() == e.getTime()){
            Edge first = Q.pollFirst();
            first.agent.cur.add(Calendar.SECOND, cost);
            Q.add(first);
        }
    }


    static class Agent{
        int seniority;
        Calendar start, cur;
        LinkedList<Edge> edges;

    }

    static class Edge implements Comparable<Edge>{
        int from, to, cost;
        Agent agent;

        public Edge(int from, int to, int cost, Agent agent) {
            this.from = from;
            this.to = to;
            this.cost = cost;
            this.agent = agent;
        }

        public int compareTo(Edge that){
            if(this.getTime() != that.getTime()) return Long.compare(this.getTime(), that.getTime());
            else if(this.from == this.to) return -1;
            return Integer.compare(this.agent.seniority, that.agent.seniority);
        }

        public long getTime(){
            return cost + agent.cur.getTimeInMillis() / 1000;
        }

        @Override
        public String toString() {
            return "E{ " + from + " -> " + to +
                    ", " + cost +
                    '}';
        }
    }

    public static boolean isSameFloor(int room1, int room2){
        return (room1 / 100) == (room2 / 100);
    }

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        Scanner textScan = new Scanner(new FileReader("src/visitToTheHeadquarters/input_large.txt"));
        ArrayList<Agent> agents = new ArrayList<Agent>();
        HashMap<Integer, Boolean> isRoomFree = new HashMap<Integer, Boolean>();

        while(textScan.hasNextLine()){
            String[] line = textScan.nextLine().trim().split(" ");

            LinkedList<Integer> rooms = new LinkedList<Integer>();
            LinkedList<Integer> costs = new LinkedList<Integer>();
            Agent agent = new Agent();
            // seniority
            agent.seniority = (int) line[0].charAt(0);
            // start
            Date date = sdf.parse(line[1]);
            agent.start = Calendar.getInstance();
            agent.start.setTime(date);
            agent.cur = Calendar.getInstance();
            agent.cur.setTime(date);
            agent.edges = new LinkedList<Edge>();

            for(int i = 2; i < line.length - 1; i += 2){
                int room = Integer.parseInt(line[i]);
                int cost = Integer.parseInt(line[i + 1]);
                rooms.addLast(room);
                costs.addLast(cost);
                isRoomFree.put(room, true);
            }
            //System.out.println(rooms);
            //System.out.println(costs);
            int lastRoom = 0;
            for(int i = 0; i < rooms.size(); i++){
                int room = rooms.get(i);
                int cost = costs.get(i);
                if(i == 0 && isSameFloor(room, 100)){
                    Edge e1 = new Edge(0, room, 10, agent);
                    Edge e2 = new Edge(room, room, cost, agent);
                    agent.edges.addLast(e1);
                    agent.edges.addLast(e2);
                    lastRoom = room;
                    continue;
                }
                else if(i == 0 && !isSameFloor(room, 100)){
                    Edge e1 = new Edge(0, 1, 10, agent);
                    int nFloors = room / 100 - 1;
                    Edge e2 = new Edge(1, room, nFloors*10 + 10, agent);
                    Edge e3 = new Edge(room, room, cost, agent);
                    agent.edges.addLast(e1);
                    agent.edges.addLast(e2);
                    agent.edges.addLast(e3);
                    lastRoom = room;
                    continue;
                }

                if(!isSameFloor(lastRoom, room)){
                    int nFloors = Math.abs(lastRoom / 100 - room / 100);
                    Edge e1 = new Edge(lastRoom, lastRoom / 100, 10, agent);
                    Edge e2 = new Edge(lastRoom / 100, room, nFloors*10 + 10, agent);
                    Edge e3 = new Edge(room, room, cost, agent);
                    agent.edges.addLast(e1);
                    agent.edges.addLast(e2);
                    agent.edges.addLast(e3);
                    lastRoom = room;
                }
                else{
                    Edge e1 = new Edge(lastRoom, room, 10, agent);
                    Edge e2 = new Edge(room, room, cost, agent);
                    agent.edges.addLast(e1);
                    agent.edges.addLast(e2);
                    lastRoom = room;
                }

                if(i == rooms.size() - 1 && isSameFloor(room, 100)){
                    Edge e = new Edge(room, 0, 10, agent);
                    agent.edges.addLast(e);
                }
                else if(i == rooms.size() - 1 && !isSameFloor(room, 100)){
                    int nFloors = Math.abs(1 - room / 100);
                    Edge e1 = new Edge(room, room / 100, 10, agent);
                    Edge e2 = new Edge(room / 100, 0, nFloors*10 + 10, agent);
                    agent.edges.addLast(e1);
                    agent.edges.addLast(e2);
                }
            }
            //System.out.println(agent.edges);
            agents.add(agent);

        }
        solve(agents, isRoomFree);
    }
}

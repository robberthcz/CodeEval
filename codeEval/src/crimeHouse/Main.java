/**
 Crime House
 Challenge Description:
 This challenge appeared in Google Code Jam competition, licensed under Creative Commons Attribution License

 While working for the police, you've identified a house where people go to commit crimes, called Crime House. One day, you set up a camera over the door of the house and record a video.

 You don't know how many people were in Crime House at the start of the day, but you can see people enter and leave through the front door. Unfortunately, because the people entering and leaving Crime House are criminals, sometimes they wear masks; and you aren't quite sure if the front door is the only way in or out.

 Sometimes you can guess who was wearing a mask. If criminal #5 entered the house, then someone wearing a mask left, then criminal #5 entered the house again, then either the person wearing the mask was criminal #5, or there is another way out of Crime House.
 At the end of the day, when Crime House has closed its doors for the night, you watch your video. Because you're an optimist, you want to figure out if it's possible that there are no other entrances or exits from crime house; and if so, you want to figure out the minimum number of people who could be in Crime House at the end of the day.

 Input sample:
 The first argument is a path to a file. Each line of the input file contains one test case. Each test case starts with a single integer N, the number of times people pass through the front door of Crime House in the day. Next, after the semicolon, follows N events, separated by a pipe. Each event contains information about one person entering or leaving Crime House through the front door.

 That information consists of a single character, E or L, followed by a space and then an integer id. If the first character is E, that indicates someone entered Crime House through the front door; if it's L, someone left through the front door. If id is greater than zero, the person with that identifier entered or left Crime House. If id is zero, then the person who entered or left Crime House was wearing a mask, and we don't know who he or she was.
 3; E 5|L 0|E 5
 2; L 1|L 1
 4; L 1|E 0|E 0|L 1
 7; L 2|E 0|E 1|E 2|E 0|E 3|L 4
 13; L 4|L 1|L 2|E 0|L 1|E 0|L 2|E 0|L 2|E 0|E 0|L 1|L 4

 Output sample:
 For each test case, output one line containing the answer. If it's possible that there are no other entrances or exits from Crime House, then the answer should be the minimum number of people who could be in Crime House at the end of the day. If that's impossible, it should be "CRIME TIME".
 1
 CRIME TIME
 1
 4
 0

 Constraints:
 1.The number of test cases is 20.
 2.Ids of criminals are in range from 0 to 2000 inclusive.
 3.The number of events is in range from 1 to 15 inclusive.
 */
package crimeHouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {


	public static int process(boolean[] enteredArr, int[] ids) {
        HashSet<Integer> inside = new HashSet<>();
        int random = 10_000_000;

        HashMap<Integer, Integer> knownEvents = new HashMap<>();
        for(int i : ids){
            if(i != 0){
                if(!knownEvents.containsKey(i)) knownEvents.put(i, 1);
                else knownEvents.put(i, knownEvents.get(i) + 1);
            }
        }

        for(int i = 0; i < enteredArr.length; i++){
            int id = ids[i];
            boolean entered = enteredArr[i];
            // unmasked entered
            if(id != 0 && entered){
                if(knownEvents.get(id) > 1) knownEvents.put(id, knownEvents.get(id) - 1);
                else knownEvents.remove(id);
                if(!inside.add(id)) return -1;
            }
            // unmasked left
            else if(id != 0 && !entered){
                if(knownEvents.get(id) > 1) knownEvents.put(id, knownEvents.get(id) - 1);
                else knownEvents.remove(id);
                if(!inside.remove(id)) return -1;
            }
            // masked entered
            else if(entered){
                boolean success = false;

                for(int l : nextToLeave(i, enteredArr, ids)){
                    if(!inside.contains(l)){
                        inside.add(l);
                        success = true;
                        break;
                    }
                }
                if(!success) inside.add(random++);
            }
            // masked left
            else{
                boolean success = false;
                int k = i;
                int next = findNextUnmasked(k, true, enteredArr, ids);
                while(next != -1){
                    if (inside.contains(next)){
                        inside.remove(next);
                        success = true;
                        break;
                    }
                    next = findNextUnmasked(++k, true, enteredArr, ids);
                }
                if(success) continue;

                int noNext = -1;
                for (int x : inside){

                    if(!knownEvents.containsKey(x)){
                        noNext = x;
                        break;
                    }
                }

                if(noNext != -1){
                    inside.remove(noNext);
                    continue;
                }

                for(int j = enteredArr.length - 1; j > i; j--){
                    int last = findNextUnmasked(j, false, enteredArr, ids);
                    if(inside.contains(last)){
                        inside.remove(last);
                        success = true;
                        break;
                    }
                }

                if(!success) return -1;
            }

        }
        return inside.size();
	}

    private static int findNextUnmasked(int i, boolean entered, boolean[] enteredArr, int[] ids){
        HashSet<Integer> visited = new HashSet<>();

        for(int j = i + 1; j < enteredArr.length; j++){
            if(enteredArr[j] == entered && !visited.contains(ids[j]) && ids[j] != 0) return ids[j];
            else if(enteredArr[j] == !entered && ids[j] != 0) visited.add(ids[j]);
        }
        return -1;
    }

    private static List<Integer> nextToLeave(int i, boolean[] enteredArr, int[] ids){
        List<Integer> list = new LinkedList<>();
        HashSet<Integer> visited = new HashSet<>();

        for(int j = i + 1; j < enteredArr.length; j++){
            if(enteredArr[j] && ids[j] != 0) visited.add(ids[j]);
            else if(!enteredArr[j] && ids[j] != 0 && !visited.contains(ids[j])) list.add(ids[j]);
        }
        return list;
    }

 	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("src/CrimeHouse/input_large.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine().split(";")[1].trim();
			String[] events = line.split("\\|");

			LinkedList<Boolean> enteredList = new LinkedList<>();
			LinkedList<Integer> ids = new LinkedList<>();

			for (int i = 0; i < events.length; i++) {
				String[] event = events[i].split(" ");
                ids.addLast(Integer.parseInt(event[1]));
				enteredList.addLast(event[0].equals("E") ? true : false);
			}

            int res = -1;
            for(int i = 0; i < events.length; i++){
                boolean[] enteredArr = new boolean[events.length + i];
                int[] idsArr = new int[events.length + i];

                for(int j = 0; j < enteredArr.length; j++){
                    if(j < i) enteredArr[j] = true;
                    else{
                        enteredArr[j] = enteredList.get(j - i);
                        idsArr[j] = ids.get(j - i);
                    }
                }

                res = process(enteredArr, idsArr);
                if(res != -1) {
                    break;
                }
            }

            if(res == -1) System.out.println("CRIME TIME");
            else          System.out.println(res);
		}
	}
}

package crimeHouse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
	// current min number of people in the house
	private int count;
	private HashMap<Integer, Integer> entered, left;
	private LinkedHashSet<Event> events, eventsCopy;

	public Main(boolean[] enterEvent, int[] idEvent) {
		entered = new HashMap<Integer, Integer>();
		left = new HashMap<Integer, Integer>();
		events = new LinkedHashSet<Event>();
		eventsCopy = new LinkedHashSet<Event>();
		count = 0;

		for(int i = 0; i < enterEvent.length; i++){
			events.add(new Event(idEvent[i], enterEvent[i], i));
		}
		eventsCopy.addAll(events);

		for(Event e : events){
			if(e.isEntering() && e.getId() != 0 && !entered.containsKey(e.getId())){
				entered.put(e.getId(), e.getTime());
				if(left.containsKey(e.getId())){
					//removeEvent(new Event(e.getId(), false, left.get(e.getId())));
					left.remove(e.getId());
				}
			}
			else if(e.isEntering() && e.getId() != 0){
				Event match = findEvent(0, false, entered.get(e.getId()), e.getTime());
				if(match == null){
					System.out.println("CRIME TIME");
					return;
				}
				removeEvent(match);
				removeEvent(new Event(e.getId(), true, entered.get(e.getId())));
				entered.remove(e.getId());
				entered.put(e.getId(), e.getTime());
			}

			if(!e.isEntering() && e.getId() != 0 && !left.containsKey(e.getId())){
				left.put(e.getId(), e.getTime());
				if(entered.containsKey(e.getId())){
					removeEvent(e);
					removeEvent(new Event(e.getId(), true, entered.get(e.getId())));
					entered.remove(e.getId());
				}
			}
			else if(!e.isEntering() && e.getId() != 0){
				Event match = findEvent(0, true, left.get(e.getId()), e.getTime());
				if(match == null){
					System.out.println("CRIME TIME");
					return;
				}
				removeEvent(match);
				removeEvent(new Event(e.getId(), false, left.get(e.getId())));
				removeEvent(e);
				left.remove(e.getId());
				left.put(e.getId(), e.getTime());
			}
		}
		int enteredMaskedCount = 0, leftMaskedCount = 0;
		for(Event e : eventsCopy){
			//System.out.print("[" + e + " " + count + " " + enteredMaskedCount );
			if(e.isEntering() && e.getId() != 0){
				count++;
			}
			else if(e.isEntering() && e.getId() == 0){
				count++;
				enteredMaskedCount++;
			}
			else if(!e.entering && e.getId() != 0){
				if(enteredMaskedCount > 0){
					enteredMaskedCount--;
					count = Math.max(0, --count);
				}
			}
			else if(!e.entering && e.getId() == 0){
				count = Math.max(0, --count);
				enteredMaskedCount = Math.max(0, --enteredMaskedCount);
			}
		}

		System.out.println(count);
	}

	private boolean removeEvent(Event e){
		return eventsCopy.remove(e);
	}

	private Event findEvent(int id, boolean entering, int time1, int time2){
		for(Event e : eventsCopy){
			if(e.getId() == id && e.isEntering() == entering && e.getTime() < time2 && e.getTime() > time1){
				return e;
			}
		}
		return null;
	}

	class Event{
		private final int id;
		private final boolean entering;
		private final int time;

		public Event(int id, boolean entering, int time) {
			this.id = id;
			this.entering = entering;
			this.time = time;
		}

		@Override
		public String toString() {
			if(isEntering()){
				return "E{" + id +
						"," + entering +
						"," + time +
						'}';
			}
			else{
				return "L{" + id +
						"," + entering +
						"," + time +
						'}';
			}

		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Event event = (Event) o;

			if (entering != event.entering) return false;
			if (id != event.id) return false;
			if (time != event.time) return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = id;
			result = 31 * result + (entering ? 1 : 0);
			result = 31 * result + time;
			return result;
		}

		public int getId() {
			return id;
		}

		public boolean isEntering() {
			return entering;
		}

		public int getTime() {
			return time;
		}

	}

	public static void main(String[] args) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader(
				"test-cases/crimeHouse.txt"));
		String exc = "";
		while (textScan.hasNextLine()) {
			String line = textScan.nextLine().split(";")[1].trim();
			exc += ";" + line + "\n";
			// System.out.println(line);
			String[] events = line.split("\\|");

			boolean[] entered = new boolean[events.length];
			int[] ids = new int[events.length];

			for (int i = 0; i < events.length; i++) {
				String[] event = events[i].split(" ");

				ids[i] = Integer.parseInt(event[1]);
				if (event[0].equals("E"))
					entered[i] = true;
				else
					entered[i] = false;
			}
			Main test = new Main(entered, ids);

		}
		//throw new RuntimeException(exc);
	}

}

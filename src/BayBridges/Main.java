package BayBridges;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;
/**
 * 
 * 
 * Challenge Description:
 * 
 * A new technological breakthrough has enabled us to build bridges that can
 * withstand a 9.5 magnitude earthquake for a fraction of the cost. Instead of
 * retrofitting existing bridges which would take decades and cost at least 3x
 * the price we're drafting up a proposal rebuild all of the bay area's bridges
 * more efficiently between strategic coordinates outlined below.
 * 
 * You want to build the bridges as efficiently as possible and connect as many
 * pairs of points as possible with bridges such that no two bridges cross. When
 * connecting points, you can only connect point 1 with another point 1, point 2
 * with another point 2.
 * 
 * At example given on the map we should connect all the points except points
 * with number 4.
 * 
 * Input sample:
 * 
 * Your program should accept as its first argument a path to a filename. Input
 * example is the following
 * 
 *  1 2 3 4 5 6
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 1: ([37.788353, -122.387695], [37.829853, -122.294312])
 * 
 * 2: ([37.429615, -122.087631], [37.487391, -122.018967])
 * 
 * 3: ([37.474858, -122.131577], [37.529332, -122.056046])
 * 
 * 4: ([37.532599,-122.218094], [37.615863,-122.097244])
 * 
 * 5: ([37.516262,-122.198181], [37.653383,-122.151489])
 * 
 * 6: ([37.504824,-122.181702], [37.633266,-122.121964])
 * 
 * 
 * Each input line represents a pair of coordinates for each possible bridge.
 * 
 * Output sample:
 * 
 * You should output bridges in ascending order.
 * 
 * 
 * 1
 * 
 * 2
 * 
 * 3
 * 
 * 5
 * 
 * 6
 * 
 * @author Robb
 *
 */
public class Main {

	// bridges are named with integers, this is only for CodeEval submit
	// solution
	private TreeSet<Integer> names;
	// bridges sorted in x-ascending order according to given end-point
	private PriorityQueue<Bridge> pq;

	public Main() {
		names = new TreeSet<Integer>();
		pq = new PriorityQueue<Bridge>();
	}

	// adds a new bridge
	public void addBridge(int name, double x1, double y1, double x2, double y2) {
		pq.add(new Bridge(name, x1, y1, x2, y2));
		names.add(name);
	}

	// uses a sweeping line according to x-coordinate
	public void solve() {

		// bridges which MAY intersect each other and are currently processed
		HashSet<Bridge> currBridges = new HashSet<Bridge>();
		// set of bridges which DO intersect each other
		TreeSet<Bridge> intersectedBridges = new TreeSet<Bridge>(
				new INTERSECT_ORDER());

		while (pq.size() > 0) {

			// remove and return next bridge
			Bridge b = pq.poll();

			if (!b.isLeftEnd()) {

				// we reached the end of the bridge, remove it from
				// currentBridges, no intersection possible anymore
				currBridges.remove(b);
				// log only bridges that have SOME intersections
				if (b.intersections.size() > 0)
					intersectedBridges.add(b);
				continue;
			}

			b.setRightEnd();
			// add the bridge back into pq for processing and wait till we hit
			// bridge's ending point
			pq.add(b);

			// check the bridge b if it intersects with any of the current
			// bridges
			for (Bridge bridge : currBridges) {

				if (b.intersects(bridge)) {

					// log the intersection on both sides
					b.intersections.add(bridge);
					bridge.intersections.add(b);

				}

			}

			// b is being now processed until right end is hit
			currBridges.add(b);

		}

		removeIntersections(intersectedBridges);

	}
	/**
	 * Removes the minimum number of bridges that that intersect each other.
	 * 
	 * @param intersectedBridges
	 */
	private void removeIntersections(TreeSet<Bridge> intersectedBridges) {
		// always select bridge with the most intersections and delete it,
		// continue until there are not intersections
		while (intersectedBridges.last().intersections.size() > 0) {

			Bridge b = intersectedBridges.pollLast();
			names.remove(b.name);

			// the intersection must be logged on both sides, so we have to
			// remove it from the given bridge and add it back to set
			for (Bridge bridge : b.intersections) {

				intersectedBridges.remove(bridge);
				bridge.intersections.remove(b);
				intersectedBridges.add(bridge);
			}
		}

	}

	/**
	 * Prints the maximum set of bridges which do not intersect each other.
	 */
	public void printNames() {
		for (Integer name : names)
			System.out.println(name);

	}

	/**
	 * Sorts the bridges according the number of intersections they have with
	 * other bridges.
	 * 
	 * @author Robb
	 *
	 */
	class INTERSECT_ORDER implements Comparator<Bridge> {

		public int compare(Bridge b1, Bridge b2) {
			if (b1.intersections.size() > b2.intersections.size())
				return 1;
			else if (b1.intersections.size() < b2.intersections.size())
				return -1;
			else
				return Integer.compare(b1.name, b2.name);

		}

	}

	/**
	 * Just for solution submitting, we have to return the names of bridges in
	 * ascending order.
	 * 
	 * @author Robb
	 *
	 */
	class NAME_ORDER implements Comparator<Bridge> {

		public int compare(Bridge b1, Bridge b2) {
			return Integer.compare(b1.name, b2.name);
		}

	}

	class Bridge implements Comparable<Bridge> {
		public final Point leftPoint, rightPoint;
		// is the bridge indexed by left-end point in pq or not
		private boolean leftEnd;
		// contains bridge which intersect with this bridge
		public HashSet<Bridge> intersections;
		// name of the bridge is an integer
		public final int name;

		public Bridge(int name, double x1, double y1, double x2, double y2) {

			this.name = name;
			intersections = new HashSet<Bridge>();
			// make sure left-end point has lower x-coordinate
			if (x2 < x1) {
				rightPoint = new Point(x1, y1);
				leftPoint = new Point(x2, y2);
			} else {
				leftPoint = new Point(x1, y1);
				rightPoint = new Point(x2, y2);
			}

			// start by processing the bridge with left-end point
			leftEnd = true;

		}

		@Override
		public int hashCode() {
			return name;
		}
		public String toString() {
			return Integer.toString(name) + " " + leftEnd;
		}

		public void setRightEnd() {
			leftEnd = false;
		}
		public boolean isLeftEnd() {
			return leftEnd;
		}

		/**
		 * Returns the x-coordinate of left or right point, depending what
		 * end-point is being processed.
		 * 
		 * @return
		 */
		public double getX() {
			if (leftEnd)
				return leftPoint.xCoord;
			else
				return rightPoint.xCoord;
		}

		/**
		 * Compares the bridges based on the x-coordinate of the current
		 * end-point.
		 */
		public int compareTo(Bridge that) {

			return Double.compare(getX(), that.getX());

		}
		/**
		 * Whether this bridge intersects that bridge. Some degenerate cases are
		 * not considered
		 * 
		 * @param that
		 * @return
		 */
		public boolean intersects(Bridge that) {
			Point a = leftPoint;
			Point b = rightPoint;
			Point c = that.leftPoint;
			Point d = that.rightPoint;

			if (ccw(a, c, d) == ccw(b, c, d))
				return false;
			else if (ccw(a, b, c) == ccw(a, b, d))
				return false;
			else
				return true;
		}
		/**
		 * This follows from Sedgewick's Point2D class from algs4. Typical CCW
		 * function from computational geometry.
		 * 
		 * @param a
		 * @param b
		 * @param c
		 * @return Points a->b->c => {clockwise, collinear, counterclockwise} =
		 *         {-1, 0, 1}
		 */
		private int ccw(Point a, Point b, Point c) {
			double ccw = (b.xCoord - a.xCoord) * (c.yCoord - a.yCoord)
					- (b.yCoord - a.yCoord) * (c.xCoord - a.xCoord);

			if (ccw < 0)
				return -1;
			else if (ccw > 0)
				return 1;
			else
				return 0;
		}
	}

	class Point {
		public final double xCoord, yCoord;

		public Point(double xCoord, double yCoord) {
			this.xCoord = xCoord;
			this.yCoord = yCoord;

		}

	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/bayBridges.txt"));

		Main test = new Main();

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();
			int name = Integer.parseInt(line.substring(0, line.indexOf(":")));

			int id1 = line.indexOf("[");
			int id2 = line.lastIndexOf("]");

			line = line.substring(id1 + 1, id2);
			String points[] = line.split(",");

			points[1] = points[1].substring(0, points[1].length() - 1).trim();
			points[2] = points[2].trim().substring(1);
			points[3] = points[3].substring(0, points[3].length() - 1).trim();

			double x1 = Double.parseDouble(points[0]), y1 = Double
					.parseDouble(points[1]), x2 = Double.parseDouble(points[2]), y2 = Double
					.parseDouble(points[3]);

			test.addBridge(name, x1, y1, x2, y2);

		}
		test.solve();
		test.printNames();
		// 1,2,3,5,6 right answer

		/*
		 * Runtime runtime = Runtime.getRuntime(); long memory =
		 * runtime.totalMemory() - runtime.freeMemory();
		 * System.out.println("Used memory is KB: " + memory / (1024));
		 */
	}

}

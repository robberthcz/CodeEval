package ClosestPair;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
/**
 * Closest pair of points problem. Complexity is O(n*log(n)). The complexity is
 * achieved by mergesorting inside the recursive calls, otherwise the naive
 * implementation is O(n*log(n)*log(n)).
 * 
 * @author Robb
 *
 */
public class Main {
	private Point[] pointsX;
	// lowest distance found so far
	private double min;
	private int inserted;
	// the closest points reference
	private Point best1, best2;

	public Main(int N) {
		pointsX = new Point[N];
		inserted = 0;

	}

	public void addPoint(int x, int y) {
		Point p = new Point(x, y);
		pointsX[inserted++] = p;

	}

	public double ClosestPair() {
		min = Double.POSITIVE_INFINITY;

		Arrays.sort(pointsX, X_ORDER);

		// pointsY will be recursively sorted according to Y eventually
		Point pointsY[] = new Point[inserted];

		// temp array for mergesorting and other stuff
		Point temp[] = new Point[inserted];

		for (int i = 0; i < inserted; i++) {
			pointsY[i] = pointsX[i];
			// System.out.println(pointsX[i]);
		}

		ClosestPair(pointsX, pointsY, temp, 0, pointsX.length - 1);

		return best1.distanceTo(best2);
	}

	// find closest pair of points in Px[lo..hi]
	private double ClosestPair(Point[] Px, Point[] Py, Point[] temp, int lo,
			int hi) {
		if (hi <= lo)
			return Double.POSITIVE_INFINITY;

		int mid = (hi - lo) / 2 + lo;

		// compute closest pair with both endpoints in left subarray or both in
		// right subarray
		double d1 = ClosestPair(Px, Py, temp, lo, mid);
		double d2 = ClosestPair(Px, Py, temp, mid + 1, hi);

		double dMin = Math.min(d1, d2);

		// Py[lo...hi] are sorted according to y-coordinate after this
		mergesort(Py, temp, lo, hi);

		// check whether closest pair is located in different subarrays, that is
		// with endpoints in left subarrays and right subarrays.
		double dSplit = ClosestSplitPair(Px, Py, temp, lo, mid, hi, dMin);

		return dSplit;

	}

	// Checks whether the closest of points are split pair, that is with
	// endpoints in left and right subarrays.
	private double ClosestSplitPair(Point[] Px, Point[] Py, Point[] temp,
			int lo, int mid, int hi, double delta) {

		Point leftmost = Px[mid];

		double leftInterval = leftmost.X() - delta;
		double rightInterval = leftmost.X() + delta;

		// deltaPoints counts how many points are within the required interval
		int deltaPoints = 0;

		for (int i = lo; i <= hi; i++) {
			if (Py[i].X() > leftInterval && Py[i].X() < rightInterval) {
				temp[deltaPoints++] = Py[i];
			}

		}

		// proof of the algorithm shows that we only need to check for 7 points
		// to
		// the right at most for valid point
		for (int i = 0; i < deltaPoints - 1; i++) {
			for (int j = 0; j < 7 && (i + j + 1) < deltaPoints; j++) {
				double dist = temp[i].distanceTo(temp[i + j + 1]);

				// did we find global min?
				if (dist < min) {
					best1 = temp[i];
					best2 = temp[i + j + 1];
					min = dist;
				}
				// did we find min for this recursive call?
				if (dist < delta)
					delta = dist;

			}
		}
		// returns found min of this recursive call
		return delta;

	}
	/**
	 * Mergesorts the Py array.
	 * 
	 * @param Py
	 * @param temp
	 * @param lo
	 * @param hi
	 */
	private void mergesort(Point[] Py, Point[] temp, int lo, int hi) {
		// this mergesort is not stable, but that's not important here
		// follows from Sedgewick's Algorithms book
		int mid = (hi - lo) / 2 + lo;

		for (int i = lo; i <= hi; i++)
			temp[i] = Py[i];

		int i = lo;
		int j = mid + 1;

		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				Py[k] = temp[j++];
			else if (j > hi)
				Py[k] = temp[i++];
			else if (temp[i].Y() < temp[j].Y())
				Py[k] = temp[i++];
			else
				Py[k] = temp[j++];

		}

	}

	// has to be inner due to submit-requirements
	class Point {
		private final int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;

		}

		public int X() {
			return x;
		}
		public int Y() {
			return y;
		}

		public double distanceSquaredTo(Point that) {
			double dx = this.x - that.x;
			double dy = this.y - that.y;
			return dx * dx + dy * dy;
		}
		public String toString() {
			return "(" + x + ", " + y + ")";
		}

		public double distanceTo(Point that) {
			return Math.sqrt(distanceSquaredTo(that));
		}

		public boolean equals(Object that) {
			Point p = (Point) that;

			return x == p.X() && y == p.Y();
		}
	}
	/**
	 * Returns comparator, which sorts the points according to their
	 * X-coordinate
	 */
	public static final Comparator<Point> X_ORDER = new xorder();

	private static class xorder implements Comparator<Point> {
		public int compare(Point p, Point q) {
			if (p.x < q.x)
				return -1;
			else if (p.x > q.x)
				return 1;
			else
				return 0;
		}
	}

	/**
	 * Returns comparator, which sorts the points according to their
	 * Y-coordinate
	 */
	public static final Comparator<Point> Y_ORDER = new yorder();

	private static class yorder implements Comparator<Point> {
		public int compare(Point p, Point q) {
			if (p.y < q.y)
				return -1;
			else if (p.y > q.y)
				return 1;
			else
				return 0;
		}
	}

	public static void main(String args[]) throws FileNotFoundException {

		Scanner pointsScan = new Scanner(new FileReader("points.txt"));

		while (pointsScan.hasNextLine()) {
			String firstLine = pointsScan.nextLine();

			int N = Integer.parseInt(firstLine);
			// if N=0 then no more points to deal with
			if (N == 0)
				break;

			Main test = new Main(N);

			for (int i = 0; i < N; i++) {
				String line = pointsScan.nextLine();
				String[] words = line.split("\\s+");

				int x = Integer.parseInt(words[0]);
				int y = Integer.parseInt(words[1]);

				test.addPoint(x, y);

			}

			double minDistance = test.ClosestPair();

			if (minDistance >= 10000)
				System.out.println("INFINITY");
			else {
				System.out.format("%.4f%n", minDistance);

			}
			// /36.2215, 1.414, 1.00, 35.1283 right answers

		}

	}
}

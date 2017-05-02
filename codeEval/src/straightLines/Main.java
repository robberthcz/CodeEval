package straightLines;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

	public Main(Point[] points) {

		int sLines = 0;

		Point[] temp = new Point[points.length];
		copyArray(points, temp);

		for (int i = 0; i < points.length; i++) {

			copyArray(temp, points);
			swap(points, 0, i);

			Arrays.sort(points, points[0].SLOPE_ORDER);

			// for (int k = 0; k < points.length; k++)
			// System.out.println(points[k] + " "
			// + points[0].slopeTo(points[k]));
			//
			// System.out.println();

			int collPoints = 2;
			for (int j = 1; j < points.length - 1; j++) {

				double slope1 = points[0].slopeTo(points[j]);
				double slope2 = points[0].slopeTo(points[j + 1]);

				if (slope1 == slope2) {
					collPoints++;
					if (j == points.length - 2
							&& !points[0].constainsSlope(slope1)) {
						sLines++;
						saveSlopes(points, j + 1, collPoints, slope1);
					}
				} else if (slope1 != slope2) {
					if (collPoints > 2 && !points[0].constainsSlope(slope1)) {
						sLines++;
						saveSlopes(points, j, collPoints, slope1);

					}
					collPoints = 2;
				}
			}

		}

		System.out.println(sLines);

	}

	private void saveSlopes(Point[] points, int endId, int collPoints,
			double slope) {

		points[0].insertSlope(slope);

		// System.out.println("saving slopes");
		// System.out.println(points[startId] + " " + slope);

		for (int i = endId, j = collPoints - 1; j > 0; j--, i--) {
			points[i].insertSlope(slope);
			// System.out.println(points[i] + " " + slope);
		}
		// System.out.println();

	}

	private void copyArray(Point[] from, Point[] to) {

		for (int i = 0; i < from.length; i++)
			to[i] = from[i];

	}

	private void swap(Point[] a, int lo, int hi) {

		Point temp = a[lo];
		a[lo] = a[hi];
		a[hi] = temp;

	}
	
	static class Point implements Comparable<Point> {

		private HashSet<Double> slopes;

		// compare points by slope
		public final Comparator<Point> SLOPE_ORDER = new slopecomparator(); // YOUR
																			// DEFINITION
																			// HERE

		private final class slopecomparator implements Comparator<Point> {
			public int compare(Point q, Point r) {
				if (q == null)
					throw new java.lang.NullPointerException();
				if (r == null)
					throw new java.lang.NullPointerException();

				double slope1 = Point.this.slopeTo(q);
				double slope2 = Point.this.slopeTo(r);

//				System.out.println(Point.this + " " + q + " " + slope1 + " " + r
//						+ " " + slope2);

				if (slope1 > slope2)
					return 1;
				if (slope1 < slope2)
					return -1;
				else
					return 0;
			}
		}

		private final byte x; // x coordinate
		private final byte y; // y coordinate

		// create the point (x, y)
		public Point(byte x, byte y) {
			this.x = x;
			this.y = y;

			slopes = new HashSet<Double>();
		}

		public void insertSlope(double slope) {
			slopes.add(slope);
		}

		public boolean constainsSlope(double slope) {

			return slopes.contains(slope);
		}

		// slope between this point and that point
		public double slopeTo(Point that) {
			if (that == null)
				throw new java.lang.NullPointerException();

			if (this.x == that.x && this.y != that.y)
				return Double.POSITIVE_INFINITY;
			if (this.x == that.x)
				return Double.NEGATIVE_INFINITY;
			if (this.y == that.y)
				return 0.0;

			else
				return (double) (that.y - this.y) / (that.x - this.x);

		}

		// is this point lexicographically smaller than that one?
		// comparing y-coordinates and breaking ties by x-coordinates
		public int compareTo(Point that) {
			if (that == null)
				throw new java.lang.NullPointerException();

			if (this.y > that.y)
				return 1;
			else if (this.y == that.y && this.x > that.x)
				return 1;
			else if (this.y == that.y && this.x == that.x)
				return 0;
			else
				return -1;

		}

		// return string representation of this point
		public String toString() {
			/* DO NOT MODIFY */
			return "(" + x + ", " + y + ")";
		}
	}

	public static void main(String[] args) throws FileNotFoundException {

		Scanner textScan = new Scanner(new FileReader(
				"test-cases/straightLines.txt"));

		while (textScan.hasNextLine()) {

			String[] parse = textScan.nextLine().trim().split("\\|");

			Point[] points = new Point[parse.length];

			for (int i = 0; i < parse.length; i++) {

				String[] parsePoint = parse[i].trim().split(" ");

				points[i] = new Point(Byte.parseByte(parsePoint[0]),
						Byte.parseByte(parsePoint[1]));
			}

			Main test = new Main(points);

		}

	}

}

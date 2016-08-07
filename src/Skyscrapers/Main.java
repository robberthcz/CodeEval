package Skyscrapers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;
/**
 * You are to design a program which helps you in drawing the skyline of a city.
 * You are given the locations of the buildings represented by triples (L, H, R)
 * where L and R are left and right coordinates of the building and H is the
 * height of the building. All buildings are rectangular in shape and they are
 * standing on a very flat surface. E.g.
 * 
 * 
 * On the first diagram the buildings are represented by the following triples:
 * (1,2,3); (2,4,6); (4,5,5); (7,3,11); (9,2,14); (13,7,15); (14,3,17)
 * 
 * The drawing line as shown on the second diagram is described by the following
 * sequence: 1 2 2 4 4 5 5 4 6 0 7 3 11 2 13 7 15 3 17 0
 * 
 * Input sample:
 * 
 * Your program should accept as its first argument a path to a filename. Each
 * line in this file is one test case. Each test case will contain the list of
 * triples semicolon separated. E.g.

 * 
 * (1,2,3);(2,4,6);(4,5,5);(7,3,11);(9,2,14);(13,7,15);(14,3,17)
 * 
 * (2,22,3);(6,12,10);(15,6,21)
 * 
 * (1,2,6);(9,23,22);(22,6,24);(8,14,19);(23,12,30)

 * 
 * Output sample:
 * 
 * The output must describe the drawing line as a vector
 * (X1,H1,X2,H2,X3,H3,X3,Xn-1,Hn-1,Xn) where X is a x-coordinate of a point
 * where the line is changing its direction from horizontal to vertical, and H
 * is a height of the vertical line. You're drawing continuously by starting at
 * the bottom of the first left building and finishing at the bottom of the
 * right building. So for each test case print out the drawing line in a way as
 * it is shown below.
 
 * 1 2 2 4 4 5 5 4 6 0 7 3 11 2 13 7 15 3 17 0
 * 
 * 2 22 3 0 6 12 10 0 15 6 21 0
 * 
 * 1 2 6 0 8 14 9 23 22 6 23 12 30 0
 * 
 
 * Notice that the elimination of hidden lines is one of the problems that
 * appear in CAD (computer-aided design).
 * 
 * Constraints: H in range (1, 100), max(x-coordinate) <= 10000, number of
 * buildings <= 1000
 * 
 * @author Robb
 *
 */
public class Main {

	private PriorityQueue<Building> pq;

	public Main() {
		pq = new PriorityQueue<Building>();

	}

	public void addBuilding(int xStart, int xEnd, int height) {
		pq.add(new Building(xStart, xEnd, height));
	}

	public void solve() {
		int xLast = Integer.MAX_VALUE, heightLast = Integer.MAX_VALUE;

		TreeSet<Building> set = new TreeSet<Building>(new HEIGHT_ORDER());

		while (pq.size() > 0) {
			Building b = pq.poll();
			// System.out.println(b + " " + b.isLeftEnd());

			int xNext = b.getX(), heighest;

			if (b.isLeftEnd()) {

				b.setRightEnd();
				set.add(b);
				pq.add(b);
				heighest = set.last().getHeight();

			} else {
				set.remove(b);
				if (set.size() == 0)
					heighest = 0;
				else
					heighest = set.last().getHeight();

			}

			if (xNext == xLast || heighest == heightLast)
				continue;
			System.out.print(xNext + " " + heighest);
			if (pq.size() > 0)
				System.out.print(" ");
			xLast = xNext;
			heightLast = heighest;

		}
		System.out.println();

	}

	class Building implements Comparable<Building> {

		private boolean leftEnd;
		private final int xStart, xEnd, height;

		public Building(int xStart, int xEnd, int height) {
			this.xStart = xStart;
			this.xEnd = xEnd;
			this.height = height;
			leftEnd = true;

		}
		public String toString() {
			return "(" + xStart + "," + height + "," + xEnd + ")";
		}

		public boolean isLeftEnd() {
			return leftEnd;
		}
		public void setRightEnd() {
			leftEnd = false;
		}

		public int compareTo(Building that) {
			int thisX = getX();
			int thatX = that.getX();

			if (thisX > thatX)
				return 1;
			else if (thisX < thatX)
				return -1;
			else {
				// make sure we remove from pq the higher building from
				// buildings
				// with the same X

				if (!leftEnd && !that.isLeftEnd()) {
					if (getHeight() < that.getHeight())
						return -1;
					else if (getHeight() > that.getHeight())
						return 1;
				}
				if (leftEnd && !that.isLeftEnd())
					return -1;
				else if (!leftEnd && that.isLeftEnd())
					return 1;

				if (getHeight() > that.getHeight())
					return -1;
				else if (getHeight() < that.getHeight())
					return 1;

				else
					return 0;
			}

		}

		public int getX() {
			if (leftEnd)
				return xStart;
			else
				return xEnd;
		}
		public int getHeight() {
			return height;
		}

	}

	class HEIGHT_ORDER implements Comparator<Building> {
		public int compare(Building b1, Building b2) {
			int h1 = b1.getHeight(), h2 = b2.getHeight();

			if (h1 > h2)
				return 1;
			else if (h1 < h2)
				return -1;
			else {
				if (b1.getX() > b2.getX())
					return 1;
				else if (b1.getX() < b2.getX())
					return -1;
				else if (b1.isLeftEnd() && !b2.isLeftEnd())
					return 1;
				else if (!b1.isLeftEnd() && b2.isLeftEnd())
					return -1;
				else
					return 0;

			}
		}
	}

	public static void main(String args[]) throws FileNotFoundException {
		Scanner textScan = new Scanner(new FileReader("skyscrapers.txt"));

		while (textScan.hasNextLine()) {
			String line = textScan.nextLine();
			String buildings[] = line.split(";");

			Main test = new Main();

			for (int i = 0; i < buildings.length; i++) {
				String toParse = buildings[i].trim();

				String numbers[] = toParse.substring(1, toParse.length() - 1)
						.split(",");
				int xStart = Integer.parseInt(numbers[0]);
				int height = Integer.parseInt(numbers[1]);
				int xEnd = Integer.parseInt(numbers[2]);

				test.addBuilding(xStart, xEnd, height);
			}
			test.solve();
		}
	}

}

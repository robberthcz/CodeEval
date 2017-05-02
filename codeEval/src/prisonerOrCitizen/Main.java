/**
 Prisoner or Citizen
 Challenge Description:
 No matter where you are, there are always good and bad people everywhere, those who have never broken the law, and those who are constantly abusing it.
 The goal of our challenge is not to persuade you to obey the laws, become better, or vice versa. Everyone has a right to make his own life choice.
 Your task is to find out where a person is—in jail or at large—depending on the coordinates.

 Input sample:
 The first argument is a path to a file. Each line includes a test case with the coordinates of jail and a person, separated by a pipeline '|'. All coordinates are in the (x, y) format. Coordinates of a jail, which form a jail square, are separated by comma ','.
 Coordinates of a prison should be united in the provided sequence (as shown on the picture).
 1 1, 1 4, 3 4, 3 2 | 2 3
 1 1, 3 2, 1 4, 3 4 | 3 3
 1 1, 1 3, 3 3, 3 1 | 1 2

 Output sample:
 Find out where a person is—in jail or at large—depending on the coordinates. If in jail, print Prisoner; otherwise, print Citizen.
 Prisoner
 Citizen
 Prisoner

 Constraints:
 1.All coordinates are from 0 to 10 and cannot be negative.
 2.Coordinates of a prison should be united in the provided sequence (as shown on the picture).
 3.The number of coordinates of a prison can be from 3 to 12.
 4.If coordinates of a person fall into the prison line, print Prisoner (see input sample # 3).
 5.The number of test cases is 20.

 */
package prisonerOrCitizen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Source of the algorithm is http://geomalgorithms.com/a03-_inclusion.html
 * Created by Robert on 21.9.2016.
 */
public class Main {
    /**
     * Implementation based on Crossing Number method. It counts the number of times a ray starting at Point P crosses the polygon boundary edges. If this number is even - point is outside, if odd - point is inside.
     * The challenge is solved, but there is still a hole in the algorithm - the point may lie on line segment, which is not horizontal or vertical, we do not check for that.
     * @param lineSegs
     * @param P
     * @return
     */
    public static boolean isPointInPolygon(LinkedList<LineSeg> lineSegs, Point P){
        // any intersection must be to the right of Point p
        Point rayEnd = new Point(Integer.MAX_VALUE, P.y);
        LineSeg ray = new LineSeg(P, rayEnd);

        int cn = 0;

        for(LineSeg l : lineSegs){
            Point leftP = l.leftPoint;
            Point rightP = l.rightPoint;
            // skip lines not viable for crossing
            if(rightP.x < P.x || Math.min(leftP.y, rightP.y) > P.y)
                continue;
            // check endpoints
            else if(l.isEndpoint(P))
                return true;
                // does it lie on vertical line
            else if(l.isVertical() && P.x == leftP.x && P.y >= leftP.y && P.y <= rightP.y){
                return true;
            }
            // does it lie on horizontal line
            else if(l.isHorizontal() && P.y == leftP.y && P.x >= leftP.x && P.x <= rightP.x){
                return true;
            }
            // skip horizontal lines for cross checking
            else if(l.isHorizontal()){
                continue;
            }
            // check intersection
            else if(ray.intersects(l)
                    // exclude right point of upward edge
                    && !(l.isUpward() && P.y == rightP.y)
                    // exclude left point of downward edge
                    && !(!l.isUpward() && P.y == leftP.y))
                cn++;
        }

        return cn % 2 == 1;
    }

    static class Point {
        public final int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public double getX(){
            return (double) x;
        }

        public double getY(){
            return (double) y;
        }

         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;

             Point point = (Point) o;

             if (x != point.x) return false;
             if (y != point.y) return false;

             return true;
         }
     }

    static class LineSeg{
        final Point leftPoint, rightPoint;

        public LineSeg(Point p1, Point p2){
            if(p1.x <= p2.x){
                leftPoint = p1;
                rightPoint = p2;
            }
            else{
                leftPoint = p2;
                rightPoint = p1;
            }
        }

        public boolean isEndpoint(Point p){
            return p.equals(leftPoint) || p.equals(rightPoint);
        }

        public boolean isUpward(){
            return leftPoint.y <= rightPoint.y;
        }

        public boolean isHorizontal(){
            return leftPoint.y == rightPoint.y;
        }

        public boolean isVertical(){
            return leftPoint.x == rightPoint.x;
        }

        public boolean intersects(LineSeg that) {
            Point a = leftPoint;
            Point b = rightPoint;
            Point c = that.leftPoint;
            Point d = that.rightPoint;

            if (ccw(a, c, d) == ccw(b, c, d)) return false;
            else if (ccw(a, b, c) == ccw(a, b, d)) return false;
            else return true;
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
            double ccw = (b.getX() - a.getX()) * (c.getY() - a.getY())
                    - (b.getY() - a.getY()) * (c.getX() - a.getX());
            if (ccw < 0) return -1;
            else if (ccw > 0) return 1;
            else return 0;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/prisonerOrCitizen/input_large.txt"));

        while(textScan.hasNextLine()){
            LinkedList<LineSeg> lineSegs = new LinkedList<>();
            LinkedList<Point> points = new LinkedList<>();
            String line[] = textScan.nextLine().replaceAll(",| \\|", "").split(" ");

            for(int i = 0; i < line.length - 2; i += 2){
                Point p = new Point(Integer.parseInt(line[i]), Integer.parseInt(line[i + 1]));
                points.addLast(p);
            }
            // generate line segments from points
            LineSeg last = new LineSeg(points.getFirst(), points.getLast());
            lineSegs.add(last);
            while(points.size() > 1){
                Point first = points.removeFirst();
                LineSeg l = new LineSeg(first, points.getFirst());
                lineSegs.add(l);
            }
            // point source
            Point P = new Point(Integer.parseInt(line[line.length - 2]), Integer.parseInt(line[line.length - 1]));

            boolean in = isPointInPolygon(lineSegs, P);
            if(in) System.out.println("Prisoner");
            else   System.out.println("Citizen");

        }
    }
}
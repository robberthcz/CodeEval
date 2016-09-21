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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Robert on 21.9.2016.
 */
public class Main {



     static class Point {
        public final int xCoord, yCoord;

        public Point(int xCoord, int yCoord) {
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }

        public double getX(){
            return (double) xCoord;
        }

        public double getY(){
            return (double) yCoord;
        }

         @Override
         public String toString() {
             return "Point{" +
                     "xCoord=" + xCoord +
                     ", yCoord=" + yCoord +
                     '}';
         }

         @Override
         public boolean equals(Object o) {
             if (this == o) return true;
             if (o == null || getClass() != o.getClass()) return false;

             Point point = (Point) o;

             if (xCoord != point.xCoord) return false;
             if (yCoord != point.yCoord) return false;

             return true;
         }

         @Override
         public int hashCode() {
             int result = xCoord;
             result = 31 * result + yCoord;
             return result;
         }
     }

    static class LineSeg{
        final Point leftPoint, rightPoint;

        public LineSeg(Point p1, Point p2){
            if(p1.xCoord <= p2.xCoord){
                leftPoint = p1;
                rightPoint = p2;
            }
            else{
                leftPoint = p2;
                rightPoint = p1;
            }
        }

        public boolean isUpward(){
            return leftPoint.yCoord <= rightPoint.yCoord;
        }

        public boolean isHorizontal(){
            return leftPoint.yCoord == rightPoint.yCoord;
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

        @Override
        public String toString() {
            return "LineSeg{" +
                    "leftPoint=" + leftPoint +
                    ", rightPoint=" + rightPoint +
                    '}';
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/prisonerOrCitizen/input.txt"));

        while(textScan.hasNextLine()){
            LinkedList<LineSeg> lineSegs = new LinkedList<>();
            LinkedList<LineSeg> lineSegsHoriz = new LinkedList<>();
            LinkedList<Point> points = new LinkedList<>();
            String line[] = textScan.nextLine().replaceAll(",| \\|", "").split(" ");
            //System.out.println(Arrays.toString(line));

            for(int i = 0; i < line.length - 2; i += 2){
                Point p = new Point(Integer.parseInt(line[i]), Integer.parseInt(line[i + 1]));
                points.addLast(p);
            }

            LineSeg ls = new LineSeg(points.getFirst(), points.getLast());
            if(!ls.isHorizontal()) lineSegs.add(ls);
            //System.out.println(lineSegs.getFirst());
            while(points.size() > 1){
                Point first = points.removeFirst();
                LineSeg l = new LineSeg(first, points.getFirst());
                if(!l.isHorizontal()) lineSegs.add(l);
                else                  lineSegsHoriz.add(l);
                //System.out.println(lineSegs.getLast());
            }
            Point p = new Point(Integer.parseInt(line[line.length - 2]), Integer.parseInt(line[line.length - 1]));
            Point rayEnd = new Point(Integer.MAX_VALUE, p.yCoord);
            LineSeg ray = new LineSeg(p, rayEnd);
            //System.out.println("\n" + ray);

            int count = 0;

            for(LineSeg l : lineSegs){
                if((l.leftPoint.xCoord == ray.leftPoint.xCoord
                        && l.leftPoint.xCoord == l.rightPoint.xCoord)
                        || l.leftPoint.equals(ray.leftPoint) || l.rightPoint.equals(ray.leftPoint)){
                    count = 1;
                    break;
                }
                else if(ray.intersects(l)
                        && !(l.isUpward() && ray.leftPoint.yCoord == l.rightPoint.yCoord)
                        && !(!l.isUpward() && ray.leftPoint.yCoord == l.leftPoint.yCoord))
                    count++;
            }

            for(LineSeg lHor : lineSegsHoriz){
                if(lHor.leftPoint.equals(ray.leftPoint) || lHor.rightPoint.equals(ray.leftPoint)
                        || (ray.leftPoint.yCoord == lHor.leftPoint.yCoord && ray.leftPoint.xCoord >= lHor.leftPoint.xCoord && ray.leftPoint.xCoord <= lHor.rightPoint.xCoord)){
                    count = 1;
                    break;
                }
            }

            if(count % 2 == 0) System.out.println("Citizen");
            else               System.out.println("Prisoner");
        }
    }
}
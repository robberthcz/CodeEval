/**
 Where is Wi-Fi
 Challenge Description:
 The car with the Wi-Fi radar is driving along the streets. It collects information about the accessible Wi-Fi hotspots in some definite places by determining MAC-address and azimuth angle to the hotspot. Also, the city map is available. It provides the list of buildings and coordinates of polygons vertices that form their outlines.
 Your task is to determine in which buildings hotspots are located.

 Input sample:
 The first argument is a path to a file that contains the city map and the Wi-Fi radar log. The city map is separated from the Wi-Fi radar log by an empty line.
 The city map is represented as a list of buildings, one building per line. Each line of city map data starts with a building name and is followed by coordinates of vertices that form its outline. Coordinates are pairs X and Y separated by semicolon ‘;’. Coordinates are separated by space.

 Each line of the Wi-Fi radar log starts with coordinates X and Y separated by semicolon ‘;’ of radar’s current position. If any hotspot was detected radar coordinates are followed by its MAC-address and azimuth angle (in degrees) separated by semicolon ‘;’.
 B001 14.88;8.94 14.88;33.23 25.29;33.23 25.29;15.88 32.23;15.88 32.23;8.94 14.88;8.94
 B002 14.88;33.23 14.88;43.64 49.58;43.64 49.58;26.29 39.17;26.29 39.17;33.23 14.88;33.23
 ... some lines skipped ...
 B010 63.45;50.58 70.39;50.58 70.39;43.64 63.45;43.64 63.45;50.58

 56.51;5.47 56-4c-18-eb-13-8b;59.3493 88-fe-14-a4-aa-2a;303.0239
 42.64;5.47 88-fe-14-a4-aa-2a;0.0000
 28.76;5.47 88-fe-14-a4-aa-2a;56.9761
 14.88;5.47 88-fe-14-a4-aa-2a;71.9958
 11.41;15.88
 11.41;29.76
 ... some lines skipped ...
 56.51;64.45 f9-aa-de-15-28-46;277.5946 de-c2-8e-34-08-17;214.6952

 Output sample:
 Print to stdout names of buildings which have Wi-Fi hotspots. The order should be alphabetical.
 B003
 B005
 B007

 Constraints:
 1.0° azimuth direction is the same as +Y axis direction
 2.Hotspots can be located both inside and outside of the building
 3.There can be more than one hotspot in the building
 4.Hotspots are immovable during all measurements
 */
package whereIsWifi;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * More in explanation.jpg. The most important point here is to think about the right triangle and tangens. With only two points and their azimuth angles, you can compute their intersection.
 * Created by Robert on 3.10.2016.
 */
public class Main {

    /**
     * Almost the same implementation as in challenge Prisoner or Citizen.
     * Here we don't consider the possibility of point ending up on the boundary of the polygon. This is due to points being defined by doubles and the fact that triangulation is only precise up to 0.1.
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
        public boolean isUpward(){
            return leftPoint.y <= rightPoint.y;
        }

        public boolean isHorizontal(){
            return compareDouble(leftPoint.y, rightPoint.y) == 0;
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
            double ccw = (b.x - a.x) * (c.y - a.y)
                    - (b.y - a.y) * (c.x - a.x);
            if (ccw < 0) return -1;
            else if (ccw > 0) return 1;
            else return 0;
        }
    }

    static class Point{
        final double x, y;
        public Point(double x, double y){
            this.x = x; this.y = y;
        }
    }

    static class Building{
        final String name;
        final LinkedList<LineSeg> lines;
        public Building(String name){
            this.name = name;
            this.lines = new LinkedList<>();
        }
    }
    // represents single hotspot measurement: angle and the current position of the car
    static class Tuple{
        final Point pos;
        final double azimuth;
        public Tuple(Point pos, double azimuth){
            this.pos = pos; this.azimuth = azimuth;
        }
    }

    private static int getQuadrant(double angle){
        int toInt = Double.valueOf(angle).intValue();
        if(0 < toInt && toInt < 90)         return 1;
        else if(90 < toInt && toInt < 180)  return 2;
        else if(180 < toInt && toInt < 270) return 3;
        else if(270 < toInt && toInt < 360) return 4;
        else return -1;
    }

    private static double normalizeAngle(double angle, int q){
        if(q == 1)      return angle;
        else if(q == 2) return 180D - angle;
        else if(q == 3) return angle - 180D;
        else if(q == 4) return 360D - angle;
        return 0D;
    }

    /**
     *  Based on the knowledge of two points in space and two line segments coming out of these two points at certain angles, this function determines the intersection of these two line segments. Explained in explanation.jpg.
     * @param t1
     * @param t2
     * @return
     */
    public static Point triangulateHotspot(Tuple t1, Tuple t2){
        double azi1 = t1.azimuth;
        double azi2 = t2.azimuth;
        int q1 = getQuadrant(t1.azimuth);
        int q2 = getQuadrant(t2.azimuth);
        // azimuth is either 0, 90, 180, 270 => tan could be 0
        // we also avoid computing special cases
        if(q1 == -1){
            // add a small amount so that azimuth lies strictly in one of the quadrants
            azi1 = azi1 + 0.0001D;
            // recompute the quadrant
            q1 = getQuadrant(azi1);
        }
        if(q2 == -1){
            azi2 = azi2 + 0.0001D;
            q2 = getQuadrant(azi2);
        }
        // quadrants 1, 3 => both coordinates of hotspot are either smaller or bigger => no need for adjustment
        double i = 1D, k = 1D;
        // size of the angle within a given  quadrant
        double theta1 = normalizeAngle(azi1, q1);
        double theta2 = normalizeAngle(azi2, q2);
        // adjust for 2 cases where
        // quadrant 2: hotspot is below the car and to the right => x coordinate of hotspot is strictly bigger, y is smaller
        // quadrant 4: hotspot is above the car to the left => y coordinate of hotspot is strictly bigger, x is smaller
        if(q1 == 2 || q1 == 4) i = -1D;
        if(q2 == 2 || q2 == 4) k = -1D;
        // constants m and n
        double m = i * Math.tan(Math.toRadians(theta1));
        double n = k / Math.tan(Math.toRadians(theta2));

        double c2 = (t2.pos.y - n * m * t1.pos.y + n * t1.pos.x - n  * t2.pos.x) / (1 - n * m);
        double c1 = m * (c2 - t1.pos.y) + t1.pos.x;

        return new Point(c1, c2);
    }

    private static int compareDouble(double d1, double d2){
        double dif = d1 - d2;
        if(dif > 0.00001) return 1;
        else if(dif < 0.00001) return -1;
        else return 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/whereIsWifi/input_large.txt"));
        // for each hotspot, it contains the available logs represented as tuples
        // hotspot is keyed by hashcode of its string representation
        HashMap<Integer, LinkedList<Tuple>> macToLog = new HashMap<>();
        LinkedList<Building> buildings = new LinkedList<>();

        // parse buildings
        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            // no more buildings
            if(line.equals("")) break;

            String nameAndPoints[] = line.split(" ");
            Building b = new Building(nameAndPoints[0]);
            Point[] points = new Point[nameAndPoints.length - 1];

            for(int i = 1; i < nameAndPoints.length; i++){
                String[] point = nameAndPoints[i].split(";");
                points[i - 1] = new Point(Double.parseDouble(point[0]), Double.parseDouble(point[1]));
            }

            for(int i = 0; i < points.length - 1; i++){
                b.lines.add(new LineSeg(points[i], points[i + 1]));
            }
            buildings.add(b);
        }

        // Wi-Fi radar log
        while(textScan.hasNextLine()){
            String[] radarlog = textScan.nextLine().split(" ");
            // current position of the car
            String curPosStr[] = radarlog[0].split(";");
            Point curPos = new Point(Double.parseDouble(curPosStr[0]), Double.parseDouble(curPosStr[1]));

            for(int i = 1; i < radarlog.length; i++){
                String[] macAndAngle = radarlog[i].split(";");
                String MAC = macAndAngle[0];
                double angle = Double.parseDouble(macAndAngle[1]);
                // found new MAC address
                if(!macToLog.containsKey(MAC.hashCode())) macToLog.put(MAC.hashCode(), new LinkedList<Tuple>());
                // two measurements suffice
                else if(macToLog.get(MAC.hashCode()).size() > 2) continue;

                // add new measurement for this MAC address
                macToLog.get(MAC.hashCode()).add(new Tuple(curPos, angle));
            }
        }

        TreeSet<String> buildsWithHotspots = new TreeSet<>();
        for(int key : macToLog.keySet()){
            LinkedList<Tuple> log = macToLog.get(key);

            Point hotspot = triangulateHotspot(log.get(0), log.get(1));
            // find buildings with this hotspot and add them to set
            for(Building b : buildings){
                if(isPointInPolygon(b.lines, hotspot))
                    buildsWithHotspots.add(b.name);
            }

        }
        // for CodeEval submittion purposes
        // in alphabetical order, print the names of buildings with hotspots in them
        for(String b : buildsWithHotspots) System.out.println(b);
    }
}

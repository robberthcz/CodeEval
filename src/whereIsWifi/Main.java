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
 * Created by Robert on 3.10.2016.
 */
public class Main {

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
        double x, y;
        public Point(double x, double y){
            this.x = x; this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (Double.compare(point.x, x) != 0) return false;
            if (Double.compare(point.y, y) != 0) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(x);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(y);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }
    }

    static class Building{
        String name;
        LinkedList<LineSeg> lines;
        public Building(String name){
            this.name = name;
            this.lines = new LinkedList<>();
        }
    }

    static class Tuple implements Comparable<Tuple>{
        Point pos;
        double azimuth;
        public Tuple(Point pos, double azimuth){
            this.pos = pos; this.azimuth = azimuth;
        }

        public int compareTo(Tuple that){
            int aziCmp = Double.compare(this.azimuth, that.azimuth);
            if(aziCmp != 0) return aziCmp;
            int xCmp = Double.compare(this.pos.x, that.pos.x);
            if(xCmp != 0) return xCmp;
            int yCmp = Double.compare(this.pos.y, that.pos.y);
            return yCmp;
        }

        @Override
        public String toString() {
            return "Tuple{" +
                    "pos=" + pos +
                    ", azimuth=" + azimuth +
                    '}';
        }
    }

    private static int getQuadrant(double angle){
        int toInt = Double.valueOf(angle).intValue();
        if(0 < toInt && toInt < 90) return 1;
        else if(90 < toInt && toInt < 180) return 2;
        else if(180 < toInt && toInt < 270) return 3;
        else if(270 < toInt && toInt < 360) return 4;
        else return -1;
    }

    public static Point triangulateHotspot(Tuple t1, Tuple t2){
        int q1 = getQuadrant(t1.azimuth);
        int q2 = getQuadrant(t2.azimuth);
        // second point is either 0, 90, 180, 270 => tan could be 0
        if(q2 == -1) return null;
        
        double i = 1D, k = 1D;
        double theta1 = 0D, theta2 = 0D;
        if(q1 == 1){
            theta1 = t1.azimuth;
        }
        else if(q1 == 2){
            theta1 = 180D - t1.azimuth;
            i = -1D;
        }
        else if(q1 == 3){
            theta1 = t1.azimuth - 180D;
        }
        else if(q1 == 4){
            theta1 = 360D - t1.azimuth;
            i = -1D;
        }

        if(q2 == 1){
            theta2 = t2.azimuth;
        }
        else if(q2 == 2){
            theta2 = 180D - t2.azimuth;
            k = -1D;
        }
        else if(q2 == 3){
            theta2 = t2.azimuth - 180D;
        }
        else if(q2 == 4){
            theta2 = 360D - t2.azimuth;
            k = -1D;
        }

        double m = i * Math.tan(Math.toRadians(theta1));
        double n = k / Math.tan(Math.toRadians(theta2));

        double c2 = (t2.pos.y - n * m * t1.pos.y + n * t1.pos.x - n  * t2.pos.x) / (1 - n * m);
        double c1 = m * (c2 - t1.pos.y) + t1.pos.x;

        return new Point(c1, c2);
    }

    public static int compareDouble(double d1, double d2){
        double dif = d1 - d2;
        if(dif > 0.00001) return 1;
        else if(dif < 0.00001) return -1;
        else return 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/whereIsWifi/input_large.txt"));
        HashMap<Integer, LinkedList<Tuple>> macToLog = new HashMap<>();
        LinkedList<Building> buildings = new LinkedList<>();

        // parse buildings
        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            // no more buildings
            if(line.equals("")) break;

            String nameAndCoords[] = line.split(" ");
            Building b = new Building(nameAndCoords[0]);
            Point[] points = new Point[nameAndCoords.length - 1];

            for(int i = 1; i < nameAndCoords.length; i++){
                String[] coord = nameAndCoords[i].split(";");
                points[i - 1] = new Point(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));
            }

            for(int i = 0; i < points.length - 1; i++){
                b.lines.add(new LineSeg(points[i], points[i + 1]));
            }
            buildings.add(b);

                        //System.out.println(b);
        }

        // Wi-Fi radar log
        while(textScan.hasNextLine()){
            String[] radarlog = textScan.nextLine().split(" ");
            // current position of the car
            String curPosStr[] = radarlog[0].split(";");
            Point curPos = new Point(Double.parseDouble(curPosStr[0]), Double.parseDouble(curPosStr[1]));

            for(int i = 1; i < radarlog.length; i++){
                String[] macAndAngle = radarlog[i].split(";");
                if(!macToLog.containsKey(macAndAngle[0].hashCode()))
                    macToLog.put(macAndAngle[0].hashCode(), new LinkedList<Tuple>());
                macToLog.get(macAndAngle[0].hashCode()).add(new Tuple(curPos, Double.parseDouble(macAndAngle[1])));
            }
        }
        TreeSet<String> buildsWithHotspots = new TreeSet<>();
        for(int key : macToLog.keySet()){
            LinkedList<Tuple> log = macToLog.get(key);
            //Collections.sort(log);
            //System.out.println(log);

            Point hotspot = null;
            for(int i = 0; i < log.size() - 1 && hotspot == null; i++){
                hotspot = triangulateHotspot(log.get(i), log.get(i + 1));
                //System.out.println(hotspot);
            }
            // find buildings with this hotspot
            for(Building b : buildings){
                if(isPointInPolygon(b.lines, hotspot))
                    buildsWithHotspots.add(b.name);
            }

        }
        for(String b : buildsWithHotspots) System.out.println(b);
    }
}

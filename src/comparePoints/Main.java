package comparePoints;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Robert on 18.8.2016.
 */
public class Main {

    static class Point{
        private final int x, y;

        public Point(int x, int y){
            this.x = x; this.y = y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
    }

    public static String getRelativePosition(Point me, Point friend){
        // friend is HERE
        String NS = "";
        String WE = "";
        if(me.getX() == friend.getX() && me.getY() == friend.getY())
            return "here";

        // NORTH
        if(friend.getY() >  me.getY())
            NS = "N";
        // SOUTH
        else if(friend.getY() <  me.getY())
            NS = "S";

        // EAST
        if( friend.getX() > me.getX())
            WE = "E";
        else if(friend.getX() < me.getX())
            WE = "W";
        return NS + WE;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("test-cases/comparePoints.txt"));

        while(textScan.hasNextLine()){
            String[] coords = textScan.nextLine().split(" ");
            Point me = new Point(Integer.parseInt(coords[0].trim()), Integer.parseInt(coords[1].trim()));
            Point friend = new Point(Integer.parseInt(coords[2].trim()), Integer.parseInt(coords[3].trim()));
            System.out.println(getRelativePosition(me, friend));
        }
    }
}

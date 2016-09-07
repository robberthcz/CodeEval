package flight370;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * Created by Robert on 6.9.2016.
 */
public class Main extends DefaultHandler {
    private final double PIx = 3.141592653589793;
    private String tmpValue;
    private SimpleDateFormat sdf;
    private Placemark curPmark;
    private LinkedList<Placemark> pmarks;
    private LinkedList<Region> regions;

    public Main(BufferedReader br, LinkedList<Region> regions){
        this.pmarks = new LinkedList<Placemark>();
        this.regions = regions;
        this.curPmark = null;
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        parseDocument(br);
    }

    public void printResults(){
        for(Region r : regions){
            if(r.inRegion.size() == 0){
                System.out.println("None");
                continue;
            }
            //System.out.print(r + " ");
            int maxConfirms = r.inRegion.last().numOfConfirms;
            String toReturn = "";
            for(Placemark p : r.inRegion){
                if(p.numOfConfirms == maxConfirms){
                    toReturn += (p.name + ", ");
                }
            }
            System.out.println(toReturn.substring(0, toReturn.length() - 2));
        }
    }

    private void parseDocument(BufferedReader br) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            parser.parse(new InputSource(br), this);
        } catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
    }

    private double toRadians(double coord){
        return coord * PIx / 180;
    }

    public double getDistance(Region r, Placemark p){
        int R = 6371000;
        double pmarkLat = toRadians(p.lat);
        double rLat = toRadians(r.lat);
        double dLat = toRadians(r.lat - p.lat);
        double dLon = toRadians(r.lon - p.lon);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(pmarkLat) * Math.cos(rLat) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public boolean isInRegion(Region r, Placemark p){
        return (r.d * 1000) > ((int) getDistance(r, p));
    }

    class Placemark implements Comparable<Placemark>{
        String name;
        int id;
        double lat, lon;
        Date date;
        int msecs;
        int numOfConfirms;

        @Override
        public String toString() {
            return "Placemark{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    ", date=" + date +
                    ", msecs=" + msecs +
                    ", numOfConfirms=" + numOfConfirms +
                    '}';
        }

        public int compareTo(Placemark that){
            int cmp = Integer.compare(this.numOfConfirms, that.numOfConfirms);
            if(cmp != 0) return cmp;
            int cmpDate = this.date.compareTo(that.date);
            if(cmpDate != 0) return cmpDate;
            cmp = Integer.compare(this.msecs, that.msecs);
            if (cmp != 0) return cmp;
            else return Integer.compare(this.id, that.id);
        }
    }

    static class Region{
        int d;
        double lat, lon;
        TreeSet<Placemark> inRegion;

        public Region(int d, double lat, double lon) {
            this.d = d;
            this.lat = lat;
            this.lon = lon;
            this.inRegion = new TreeSet<Placemark>();
        }

        @Override
        public String toString() {
            return "Region{" +
                    "d=" + d +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }

    @Override
    public void startElement(String s, String s1, String elementName, Attributes attributes) throws SAXException {
        tmpValue = "";
        if (elementName.equalsIgnoreCase("placemark")) {
            curPmark = new Placemark();
        }
    }
    @Override
    public void endElement(String s, String s1, String element) throws SAXException {
        if (element.equalsIgnoreCase("placemark")) {
            if(curPmark.name != null){
                pmarks.add(curPmark);
                for(Region r : regions){
                    if(isInRegion(r, curPmark)) r.inRegion.add(curPmark);
                }
            }
            curPmark = null;
        }
        if (element.equalsIgnoreCase("name") && curPmark != null) {
            curPmark.name = tmpValue;
            curPmark.id = Integer.parseInt(tmpValue.split("ID ")[1]);
        }

        if (element.equalsIgnoreCase("coordinates")&& curPmark != null) {
            // filter badly formed placemarks
            if(!tmpValue.contains(" ")){
                String[] coords = tmpValue.split(",");
                curPmark.lat = Double.parseDouble(coords[0]);
                curPmark.lon = Double.parseDouble(coords[1]);
            }
        }

        if (element.equalsIgnoreCase("description")&& curPmark != null) {
            int id1 = tmpValue.indexOf("Confirmation: ");
            int id2 = tmpValue.indexOf(" people");
            if(id1 > 0 && id2 > 0){
                String confirms = tmpValue.substring(id1 + 17, id2 - 4);
                curPmark.numOfConfirms = Integer.parseInt(confirms);
            }
        }

        if(element.equalsIgnoreCase("when")&& curPmark != null){
            try {
                curPmark.date = sdf.parse(tmpValue);
            } catch (ParseException e) {
                System.out.println("date parsing error");
            }
            // parse milliseconds, not saved to date
            curPmark.msecs = Integer.parseInt(tmpValue.split("\\.")[1]);
        }
    }
    @Override
    public void characters(char[] ac, int i, int j) throws SAXException {
        tmpValue = new String(ac, i, j);
    }


    public static void main(String[] args) throws IOException {
        BufferedReader brTemp = new BufferedReader(new FileReader("src/flight370/input.txt"));
        LinkedList<Region> regions = new LinkedList<Region>();
        int numOfRegions = 0;
        while(true){
            String line = brTemp.readLine();
            if(line.startsWith("<")) break;
            numOfRegions++;
            String[] temp = line.split("; \\(|, |\\)");
            regions.add(new Region(Integer.parseInt(temp[0]), Double.parseDouble(temp[1]), Double.parseDouble(temp[2])));
        }

        BufferedReader br = new BufferedReader(new FileReader("src/flight370/input.txt"));
        for(int i = 1; i <= numOfRegions; i++) br.readLine();
        Main test = new Main(br, regions);
        test.printResults();
    }
}

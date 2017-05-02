/**
 Flight 370
 Challenge Description:
 You have probably heard about the tragedy that happened to one of the Malaysian airplanes. Many countries support the search mission with ships, planes, and rescue services. There is also a web app which allows people to help with the search. It is called Tomnod. Thousands of volunteers are browsing the satellite images to explore the Earth and solve the real-world problems. They mark the items they have found with the special tags. You can also join the search by going to http://www.tomnod.com/ and signing in. All the search results regarding the Malaysian flight are in an XML file that you can download here. Each item found and marked on the map is represented by the following XML section:

 <Placemark id='4889783'> <name>Oil Slick ID 4889783</name><Snippet maxLines='0'></Snippet> <type>Oil Slick</type> <description><![CDATA[ <img src='https://s3.amazonaws.com/explorationlab/chips/08458a6f42a780af66c05d0ef1385558.jpg' height=400 width=400><br/> <table width=400><tr><td width=40%>CrowdRank: <b>100%</b><br/>Confirmation: <b>2</b> people<br/> <td align='right' valign='bottom'>Image © DigitalGlobe 2014<br><br><a href='http://tomnod.com/nod/challenge/mh370_indian_ocean/map/983378'>See this point on <img src='http://www.tomnod.com/nod/images/logo_small.png' width=100></a> </tr</table> ]]></description> <styleUrl>#tag_type_130</styleUrl> <TimeStamp><when>2014-03-27 18:14:25.234888</when></TimeStamp> <Point><coordinates>79.834979564799994,6.97011026235</coordinates></Point> </Placemark>XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXAn item is called a placemark. It has a name, a type, a point with coordinates, a timestamp when it was marked, and a description section with the confirmation in it.

 You are given a point on the Earth surface, represented by a longitude and a latitude, and a radius in kilometers. Your task is to print out the name of the placemark that was confirmed by the largest amount of people in the given area.

 Input sample:
 Your program should accept a path to a filename as its first argument. The first N lines of the file contain the regions, one per line. Other lines contain the XML code itself starting with the following line:
 <?xml version='1.0' encoding='UTF-8'?>
 For example:
 170; (96.289565663600001, 4.0044008079599998)
 873; (92.749414588199997, 11.692508007600001)
 98; (105.364957907, 6.6474719752600002)
 20; (80.185393420699995, 7.5700396347199996)
 <?xml version='1.0' encoding='UTF-8'?>
 <kml xmlns='http://www.opengis.net/kml/2.2'>
 <Document> ... Document body ... </Document> </kml>

 Output sample:
 For each test case print out the name of the placemark that was confirmed by the largest amount of people. If there is more than one placemark with the same maximum amount of confirmations, print out the names ordered by timestamp starting from the most recent ones, separated by comma and a single space. In case the timestamps are the same, print out placemarks ordered by Id. Print out 'None' in case there is no placemark in a given area.
 For example:
 1. Other ID 4398192, Other ID 4402502, Other ID 4437554, Other ID 4437645, Oil Slick ID 4519894
 2. Wreckage ID 6757595
 3. Other ID 3835555
 4. None

 Constraints:
 1.R is in a range from 20 to 3000.
 2.N is in a range from 10 to 15
 */
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
 * The formulate for determining distance is here: http://www.movable-type.co.uk/scripts/latlong.html
 * Also, we used http://janmatuschek.de/LatitudeLongitudeBoundingCoordinates
 * to compute the so-called bounding square for each region. That is, minimum and maximum coordinates, so that every
 * point inside our circle-region has to be also inside a square, which bounds this circle-region.
 * It frees you up from using expensive trigonometric computations.
 * However, it reduced the computation costs by 10% only. The biggest speed-up is to pre-compute the coordinates in
 * radians.
 *
 * The toughest part to figure out is reading the input. The XML is located after first the N lines, where N is the
 * number of regions. The idea was to use the first Scanner with the first InputStream to find the N and then with the
 * second scanner and the second InputStream skip first N lines and then feed the InputStream to SAXParser.
 * However, the Scanner buffers the input and hence cannot be used for that. It eats up the XML even if we read
 * only the first N lines.
 * The solution is to create InputSource from BufferedReader, which was used before that to read the first N lines.
 * This works, but it took time to find out about the existence of InputSource
 */
public class Main extends DefaultHandler {
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

    public static double toRadians(double coord){
        return coord * Math.PI / 180;
    }

    /**
     * The formula taken from http://www.movable-type.co.uk/scripts/latlong.html Thanks to the author, really helped.
     * @param r
     * @param p
     * @return
     */
    public double getDistance(Region r, Placemark p){
        int R = 6371;
        double pmarkLat = p.lat;
        double rLat = r.lat;
        double dLat = r.lat - p.lat;
        double dLon = r.lon - p.lon;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(pmarkLat) * Math.cos(rLat) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public boolean isInRegion(Region r, Placemark p){
        // first check if it is inside square boundary
        // then check real distance
        return r.minLat <= p.lat && p.lat <= r.maxLat
                && r.minLon <= p.lon && p.lon <= r.maxLon
                && ((r.d) > ((int) getDistance(r, p)));
    }


    class Placemark implements Comparable<Placemark>{
        String name;
        int id, msecs, numOfConfirms;
        double lat, lon;
        Date date;

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
        double lat, lon, minLat, maxLat, minLon, maxLon;
        TreeSet<Placemark> inRegion;

        public Region(int d, double lon, double lat) {
            this.d = d;
            this.lat = lat;
            this.lon = lon;
            this.inRegion = new TreeSet<Placemark>();
            // calculating the boundary square
            double r = (double) d / (double) 6371;
            //double latT = Math.asin(Math.sin(lat) / Math.cos(r));
            double dLon = Math.asin(Math.sin(r) / Math.cos(lat));
            this.minLat = lat - r;
            this.maxLat = lat + r;
            this.minLon = lon - dLon;
            this.maxLon = lon + dLon;
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
        // end of current Placemark reached
        if (element.equalsIgnoreCase("placemark")) {
            if(curPmark.name != null){
                pmarks.add(curPmark);
                for(Region r : regions){
                    if(isInRegion(r, curPmark)) r.inRegion.add(curPmark);
                }
            }
            curPmark = null;
        }
        // save the name and id of current placemark
        else if (element.equalsIgnoreCase("name") && curPmark != null) {
            curPmark.name = tmpValue;
            curPmark.id = Integer.parseInt(tmpValue.split("ID ")[1]);
        }
        // save the coordinates of current placemark
        else if (element.equalsIgnoreCase("coordinates")&& curPmark != null) {
            // filter badly formed placemarks
            if(!tmpValue.contains(" ")){
                String[] coords = tmpValue.split(",");
                curPmark.lon = toRadians(Double.parseDouble(coords[0]));
                curPmark.lat = toRadians(Double.parseDouble(coords[1]));
            }
        }
        // parse the number of confirmations from the tag description
        else if (element.equalsIgnoreCase("description")&& curPmark != null) {
            int id1 = tmpValue.indexOf("Confirmation: ");
            int id2 = tmpValue.indexOf(" people");
            if(id1 > 0 && id2 > 0){
                String confirms = tmpValue.substring(id1 + 17, id2 - 4);
                curPmark.numOfConfirms = Integer.parseInt(confirms);
            }
        }
        // parse the date
        else if(element.equalsIgnoreCase("when")&& curPmark != null){
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
        // compute the number of regions in the input
        while(true){
            String line = brTemp.readLine();
            if(line.startsWith("<")) break;
            numOfRegions++;
            String[] temp = line.split("; \\(|, |\\)");
            regions.add(new Region(Integer.parseInt(temp[0]), toRadians(Double.parseDouble(temp[1])), toRadians(Double.parseDouble(temp[2]))));
        }
        // read the XML
        BufferedReader br = new BufferedReader(new FileReader("src/flight370/input.txt"));
        // skip regions
        for(int i = 1; i <= numOfRegions; i++) br.readLine();
        Main test = new Main(br, regions);
        test.printResults();
    }
}

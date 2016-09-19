/**
 Discount Offers
 Challenge Description:
 Our marketing department has just negotiated a deal with several local merchants that will allow us to offer exclusive discounts on various products to our top customers every day. The catch is that we can only offer each product to one customer and we may only offer one product to each customer.
 Each day we will get the list of products that are eligible for these special discounts. We then have to decide which products to offer to which of our customers. Fortunately, our team of highly skilled statisticians has developed an amazing mathematical model for determining how likely a given customer is to buy an offered product by calculating what we call the "suitability score" (SS). The top-secret algorithm to calculate the SS between a customer and a product is this:

 1. If the number of letters in the product's name is even then the SS is the number of vowels (a, e, i, o, u, y) in the customer's name multiplied by 1.5.
 2. If the number of letters in the product's name is odd then the SS is the number of consonants in the customer's name.
 3. If the number of letters in the product's name shares any common factors (besides 1) with the number of letters in the customer's name then the SS is multiplied by 1.5.
 Your task is to implement a program that assigns each customer a product to be offered in a way that maximizes the combined total SS across all of the chosen offers. Note that there may be a different number of products and customers. You may include code from external libraries as long as you cite the source.

 Input sample:
 Your program should accept as its only argument a path to a file. Each line in this file is one test case. Each test case will be a comma delimited set of customer names followed by a semicolon and then a comma delimited set of product names. Assume the input file is ASCII encoded. For example (NOTE: The example below has 3 test cases):

 Jack Abraham,John Evans,Ted Dziuba;iPad 2 - 4-pack,Girl Scouts Thin Mints,Nerf Crossbow
 Jeffery Lebowski,Walter Sobchak,Theodore Donald Kerabatsos,Peter Gibbons,Michael Bolton,Samir Nagheenanajar;Half & Half,Colt M1911A1,16lb bowling ball,Red Swingline Stapler,Printer paper,Vibe Magazine Subscriptions - 40 pack
 Jareau Wade,Rob Eroh,Mahmoud Abdelkader,Wenyi Cai,Justin Van Winkle,Gabriel Sinkin,Aaron Adelson;Batman No. 1,Football - Official Size,Bass Amplifying Headphones,Elephant food - 1024 lbs,Three Wolf One Moon T-shirt,Dom Perignon 2000 Vintage

 Output sample:
 For each line of input, print out the maximum total score to two decimal places. For the example input above, the output should look like this:
 21.00
 83.50
 71.25

 */
package discountOffers;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Robert on 23.8.2016.
 */
public class Main {
    private final String[] customers, products;
    private double[][] m;
    private HashSet<Character> vowels = new HashSet<Character>(){{ add('a'); add('e'); add('i'); add('o'); add('u');
        add('y');}};

    public Main(String[] customers, String[] products){
        this.customers = customers;
        this.products = products;
        this.m = new double[customers.length][products.length];

        for(int i = 0; i < customers.length; i++){
            int numOfVows = getNumOfVowels(customers[i]);
            for(int j = 0; j < products.length; j++){
                if(products[j].length() % 2 == 0) m[i][j] = numOfVows * 1.5;
                else                              m[i][j] = customers[i].length() - numOfVows;

                if(gcd(customers[i].length(), products[j].length()) > 1) m[i][j] *= 1.5;
                //System.out.println(m[i][j] + " " + customers[i] + " " + products[j]);
            }
        }

    }

    private void auction(LinkedList<Integer> unassignedC, HashMap<Integer, Integer> prodToCustomer, double[][] prices,
                         double
            eps){
        int first = unassignedC.removeFirst();

        double max = Double.MIN_VALUE;
        int maxId = Integer.MIN_VALUE;
        // find object for this customer with max value
        for(int i = 0; i < m[first].length; i++){
            double cur = (m[first][i] - prices[first][i]) - eps;
            if(cur > max){
                max = cur;
                maxId = i;
            }
        }
        double sndMax = Double.MIN_VALUE;
        int sndMaxId = Integer.MIN_VALUE;
        // find object for this customer with the second max value
        for(int i = 0; i < m[first].length; i++){
            double cur = (m[first][i] - prices[first][i]) - eps;
            if(cur > sndMax && cur != max){
                sndMax = cur;
                sndMaxId = i;
            }
        }
        double incr = max - sndMax + eps;
        prices[first][maxId] += incr;
        if(prodToCustomer.containsKey(maxId))
            unassignedC.addLast(prodToCustomer.get(maxId));
        prodToCustomer.put(maxId, first);
    }

    public int gcd(int p, int q) {
        if (q == 0) return p;
        else return gcd(q, p % q);
    }

    public int getNumOfVowels(String S){
        int count = 0;
        for(int i = 0; i < S.length(); i++)
            if(vowels.contains(S.charAt(i)))
                count++;
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/discountOffers/input.txt"));

        while(textScan.hasNextLine()){
            String line[] = textScan.nextLine().split(";");
            String[] customers = line[0].toLowerCase().replaceAll("[^a-z,]", "").split(",");
            String[] products = line[1].toLowerCase().replaceAll("[^a-z,]", "").split(",");

            Main test = new Main(customers, products);
        }
    }
}

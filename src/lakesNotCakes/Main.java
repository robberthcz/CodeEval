/*      LAKES, NOT CAKES
        CHALLENGE DESCRIPTION:

        It was a dark and stormy night when Alice was wandering in the black forest. The rain fell in torrents into deep lakes scattered all over the area… Wait! Lakes… forest…? Really? Well, yeah, it’s not a true horror story, but it is fits our challenge perfectly. So, you have a map of the forest. You are sure there are some lakes in there, but you do not know their number.
        Write a program that will count how many lakes are in the forest. We count all adjacent o symbols as one lake (by adjacent we mean symbols that are located one cell up, down, left, right, or diagonally from the needed symbol).

        INPUT SAMPLE:

        The first argument is a path to a file. Each line includes a test case, which contains a map of the forest of different size. Forest areas are separated by a vertical bar |.
        # - forest
        o - lake

        For example:


        
        1
        2
        o # o | # # # | o # o
        o # o | # o # | o # o
        OUTPUT SAMPLE:

        Print the number of lakes for each test case.

        For example:


        
        1
        2
        4
        1
        CONSTRAINTS:

        A forest may be of different width and height from 3 to 30 symbols.
        Diagonal symbols are also counted as adjacent.
        The number of test cases is 40.
*/
package lakesNotCakes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Rob on 8/7/2016.
 */
public class Main {

    public Main(boolean[][] lakes){
        boolean[][] marked = new boolean[lakes.length][lakes[0].length];
        int lakeCount = 0;
        for(int i = 0; i < lakes.length; i++){
            for(int j = 0; j < lakes[0].length; j++){
                if(!marked[i][j] && lakes[i][j]){
                    lakeCount++;
                    explore(lakes, marked, i, j);
                }
            }
        }
        System.out.println(lakeCount);
    }

    private void explore(boolean[][] lakes, boolean[][] marked, int row, int col){
        if(!lakes[row][col] || marked[row][col])
            return;
        else{
            marked[row][col] = true;
            for(int i = -1; i <= 1 ; i++){
                for(int j = -1; j <= 1; j++){
                    if(i == 0 && j == 0)
                        continue;
                    else if(row + i < 0 || col + j < 0)
                        continue;
                    else if(row + i >= lakes.length || col + j >= lakes[0].length)
                        continue;
                    explore(lakes, marked, row + i, col + j);

                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader(
                "test-cases/lakesNotCakes.txt"));

        while (textScan.hasNextLine()) {
            String line = textScan.nextLine();
            //System.out.println(line);
            String[] rows = line.replaceAll(" ", "").split("\\|");
            boolean[][] lakes = new boolean[rows.length][rows[0].length()];
            for(int i = 0; i < rows.length; i++){
                for(int j = 0; j < rows[0].length(); j++){
                    if(rows[i].charAt(j) == 'o')
                        lakes[i][j] = true;
                    //System.out.println(lakes[i][j]);
                }

            }
            Main test = new Main(lakes);
        }
    }
}

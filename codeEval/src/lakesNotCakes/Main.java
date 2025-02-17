/**
 Lakes, not cakes
 Challenge Description:

 It was a dark and stormy night when Alice was wandering in the black forest. The rain fell in torrents into deep lakes scattered all over the area… Wait! Lakes… forest…? Really? Well, yeah, it’s not a true horror story, but it is fits our challenge perfectly. So, you have a map of the forest. You are sure there are some lakes in there, but you do not know their number.
 Write a program that will count how many lakes are in the forest. We count all adjacent o symbols as one lake (by adjacent we mean symbols that are located one cell up, down, left, right, or diagonally from the needed symbol).

 Input sample:
 The first argument is a path to a file. Each line includes a test case, which contains a map of the forest of different size. Forest areas are separated by a vertical bar |.
 # - forest
 o - lake

 For example:
 1 o # o | # # # | o # o
 2 o # o | # o # | o # o

 Output sample:
 Print the number of lakes for each test case.
 For example:
 1 4
 2 1

 Constraints:
 1.A forest may be of different width and height from 3 to 30 symbols.
 2.Diagonal symbols are also counted as adjacent.
 3.The number of test cases is 40.

 */
package lakesNotCakes;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Rob on 8/7/2016.
 */
public class Main {

    public Main(boolean[][] lakes){
        int lakeCount = 0;
        for(int i = 0; i < lakes.length; i++){
            for(int j = 0; j < lakes[0].length; j++){
                if(lakes[i][j]){
                    lakeCount++;
                    exploreNotRec(lakes, i, j);
                }
            }
        }
        System.out.println(lakeCount);
    }

    private void explore(boolean[][] lakes, int row, int col){
        if(!lakes[row][col])
            return;
        else{
            lakes[row][col] = false;
            for(int i = -1; i <= 1 ; i++){
                for(int j = -1; j <= 1; j++){
                    if(i == 0 && j == 0)
                        continue;
                    else if(row + i < 0 || col + j < 0)
                        continue;
                    else if(row + i >= lakes.length || col + j >= lakes[0].length)
                        continue;
                    explore(lakes, row + i, col + j);

                }
            }
        }
    }

    private void exploreNotRec(boolean[][] lakes, int row, int col){
        LinkedList<Integer> rowId = new LinkedList<Integer>();
        LinkedList<Integer> colId = new LinkedList<Integer>();

        rowId.addFirst(row);
        colId.addFirst(col);

        while(!rowId.isEmpty() && !colId.isEmpty()){
            int rowFirst = rowId.removeFirst();
            int colFirst = colId.removeFirst();
            //System.out.println(rowFirst);
            //System.out.println(colFirst);

            lakes[rowFirst][colFirst] = false;

            for(int i = -1; i <= 1 ; i++){
                for(int j = -1; j <= 1; j++){
                    if(i == 0 && j == 0)
                        continue;
                    else if(rowFirst + i < 0 || colFirst + j < 0)
                        continue;
                    else if(rowFirst + i >= lakes.length || colFirst + j >= lakes[0].length)
                        continue;
                    if(lakes[rowFirst + i][colFirst + j]){
                        rowId.addFirst(rowFirst + i);
                        colId.addFirst(colFirst + j);
                    }

                }
            }
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader(
                "src/lakesNotCakes/input.txt"));

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

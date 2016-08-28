/**
 Too unique
 Challenge Description:
 You are given a matrix of size N×M elements, filled with lowercase ASCII letters from ‘a’ to ‘z’. Find the max size of rectangular contiguous submatrix of unique (i.e. non repeated within a given submatrix) elements. Find all submatrices of unique elements of this size and replace their elements with asterisks ‘*’.

 Input sample:
 The first argument is a file that contains the input matrix. E.g.
 rzqicaiiaege
 ccwnulljybtu
 jxtxupauwuah
 oqikzgqrzpdq
 vblalwdjbdwn
 ahjeencuclbo

 Output sample:
 Print to stdout the result of the matrix with replaced elements, where all elements of the biggest submatrixes of unique elements are replaced with asterisks ‘*’. E.g.
 rzqicaiiae**
 ccwnulljyb**
 jxtx***uwu**
 oqik****zp**
 vbla****bd**
 ahje****cl**

 Constraints:
 1.The size of matrix in the input is 60×20 elements.
 */
package tooUnique;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Robert on 18.8.2016.
 */
public class Main {
    private char[][] mat;
    // at each position (i, j) there is saved another position (k, l) (as Coordinate object),
    // which together determine the largest contiguous submatrix of unique elements starting at position (i, j) and ending at (k, l)
    private Coordinate[][] coords;

    public Main(char[][] mat){
        this.mat = mat;
        this.coords = new Coordinate[mat.length][mat[0].length];
        int maxSubmatSize = 0;

        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[0].length; j++){
                Coordinate cur = new Coordinate(i, j);
                // max found by first expanding row-wise
                Coordinate max1 = getMaxSizeCoord(true, mat, new Coordinate(i, j), i, j, mat.length - 1, mat[0].length - 1, new HashSet<Character>());
                // max found by first expanding column-wise
                Coordinate max2 = getMaxSizeCoord(false, mat, new Coordinate(i, j), i, j, mat.length - 1, mat[0].length - 1, new HashSet<Character>());
                Coordinate max = (cur.submatSize(max1) >= cur.submatSize(max2)) ? max1 : max2;

                this.coords[i][j] = max;
                if(cur.submatSize(max) > maxSubmatSize)
                    maxSubmatSize = cur.submatSize(max);
                //System.out.println(cur + " => " + max);
                //System.out.println("Size of submatrix: " + cur.submatSize(max));
            }
        }
        //System.out.println("Max size of submatrix with unique elements: " + maxSubmatSize);
        for(int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords[0].length; j++) {
                Coordinate cur = new Coordinate(i, j);
                // fill all submatrices of max size with '*'
                if(cur.submatSize(coords[i][j]) == maxSubmatSize){
                    for(int k = cur.coordX(); k <= coords[i][j].coordX(); k++){
                        for(int l = cur.coordY(); l <= coords[i][j].coordY(); l++)
                            mat[k][l] = '*';
                    }
                }
            }
        }
        // print the result
        for(int i = 0; i < mat.length; i++){
            for(int j = 0; j < mat[0].length; j++)
                System.out.print(mat[i][j]);
            System.out.println();
        }
    }

    /**
     * @param rowWiseFirst specifies if search for max submatrix  of unique elements starts first by expanding row-wise
     * @param mat
     * @param start starting Coordinate from which all recursive calls originate
     * @param startRow row from which current search starts
     * @param startCol column from which current search starts
     * @param endRow row number bounding current search
     * @param endCol column number bounding current search
     * @param chars set containing unique characters from matrix "mat" found during our search
     * @return
     */
    private Coordinate getMaxSizeCoord(boolean rowWiseFirst, char[][] mat, Coordinate start, int startRow, int startCol, int endRow, int endCol, HashSet<Character> chars){
        if(startRow >= mat.length || startCol >= mat[0].length || !chars.add(mat[startRow][startCol]) || startRow > endRow || startCol > endCol){
            return null;
        }
        int maxRow = startRow, maxCol = startCol;
        // expand row-wise first
        if(rowWiseFirst){
            for(int i = startRow + 1; i <= endRow && chars.add(mat[i][startCol]); i++) maxRow = i;
            for(int j = startCol + 1; j <= endCol && chars.add(mat[startRow][j]); j++) maxCol = j;
        }
        // expand column-wise first
        else{
            for(int j = startCol + 1; j <= endCol && chars.add(mat[startRow][j]); j++) maxCol = j;
            for(int i = startRow + 1; i <= endRow && chars.add(mat[i][startCol]); i++) maxRow = i;
        }
        Coordinate maxRowWise = new Coordinate(maxRow, startCol);
        Coordinate maxColWise = new Coordinate(startRow, maxCol);
        Coordinate maxRecurse = getMaxSizeCoord(rowWiseFirst, mat, start, startRow + 1, startCol + 1, maxRow, maxCol, chars);
        int rowMaxSubSize = maxRowWise.submatSize(start);
        int colMaxSubSize = maxColWise.submatSize(start);
        int recMaxSubSize = start.submatSize(maxRecurse);

        if(rowMaxSubSize >= Math.max(colMaxSubSize, recMaxSubSize)) return maxRowWise;
        else if(colMaxSubSize >= Math.max(rowMaxSubSize, recMaxSubSize)) return maxColWise;
        else return maxRecurse;
    }

    class Coordinate{
        private final int x, y;

        public Coordinate(int x, int y){
            this.x = x; this.y = y;
        }
        public int submatSize(Coordinate that){
            if(that == null)
                return Integer.MIN_VALUE;
            else
                return (Math.abs(this.coordX() - that.coordX()) + 1)*(Math.abs(this.coordY() - that.coordY()) + 1);
        }
        public int coordX(){
            return x;
        }
        public int coordY(){
            return y;
        }
        @Override
        public String toString() {
            return "Coord{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/tooUnique/input.txt"));
        // for CodeEval input
        // int rows = 20, cols = 60;
        int rows = 6, cols = 12;
        char[][] mat = new char[rows][cols];

        for(int i = 0; i < rows; i++){
            String line = textScan.nextLine();
            for(int j = 0; j < cols; j++)
                mat[i][j] = line.charAt(j);
        }
        Main test = new Main(mat);
    }
}

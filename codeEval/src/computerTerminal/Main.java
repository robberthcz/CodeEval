/**
 Computer Terminal
 Challenge Description:
 In this problem you are writing the software for a small terminal with a 10-row, 10-column display (perhaps for a point-of-sale terminal). Rows and columns are numbered 0 through 9. The character that introduces a control sequence is ^, the circumflex. The character (or in one case, the two characters) immediately following the control sequence introducer will direct your software in performing its special functions.

 Here is the complete list of control sequences you will need to interpret:
 ^c - clear the entire screen; the cursor row and column do not change
 ^h - move the cursor to row 0, column 0; the image on the screen is not changed
 ^b - move the cursor to the beginning of the current line; the cursor row does not change
 ^d - move the cursor down one row if possible; the cursor column does not change
 ^u - move the cursor up one row, if possible; the cursor column does not change
 ^l - move the cursor left one column, if possible; the cursor row does not change
 ^r - move the cursor right one column, if possible; the cursor row does not change
 ^e - erase characters to the right of, and including, the cursor column on the cursor's row; the cursor row and column do not change
 ^i - enter insert mode
 ^o - enter overwrite mode
 ^^ - write a circumflex (^) at the current cursor location, exactly as if it was not a special character; this is subject to the actions of the current mode (insert or overwrite)
 ^DD - move the cursor to the row and column specified; each D represents a decimal digit; the first D represents the new row number, and the second D represents the new column number

 No illegal control sequences will ever be sent to the terminal. The cursor cannot move outside the allowed screen locations (that is, between row 0, column 0 and row 9, column 9).
 When a normal character (not part of a control sequence) arrives at the terminal, it is displayed on the terminal screen in a manner that depends on the terminal mode. When the terminal is in overwrite mode (as it is when it is first turned on), the received character replaces the character at the cursor's location. But when the terminal is in insert mode, the characters to the right of and including the cursor's location are shifted right one column, and the new character is placed at the cursor's location; the character previously in the rightmost column of the cursor's row is lost.
 Regardless of the mode, the cursor is moved right one column, if possible.

 Input sample:
 Your program should accept as its first argument a path to a filename. Input example is the following
 ^h^c
 ^04^^
 ^13/ \^d^b  /   \
 ^u^d^d^l^l^l^l^l^l^l^l^l
 ^r^r^l^l^d<CodeEval >^l^l^d/^b \
 ^d^r^r^66/^b  \
 ^b^d   \ /
 ^d^l^lv^d^b===========^i^94O123456
 789^94A=======^u^u^u^u^u^u^l^l\^o^b^r/

 Output sample:
 Print results in the following way.
     ^
    / \
   /   \
  /     \
 <CodeEval>
  \     /
   \   /
    \ /
     v
 ====A=====
 */
package computerTerminal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Robert on 2.9.2016. The code is self-explanatory.
 */
public class Main {

    public static char[][] process(String ctrlSeq, char[][] screen, int row, int col, boolean overWriteMode){
        if(ctrlSeq.length() == 0) return screen;
        else if(ctrlSeq.startsWith("^")){
            ctrlSeq = ctrlSeq.substring(1);
            char c = ctrlSeq.charAt(0);
            char cNext = ctrlSeq.charAt(1);
            ctrlSeq = ctrlSeq.substring(1);
            if(c == 'c') process(ctrlSeq, clearScreen(screen), row, col, overWriteMode);
            else if(c == 'h') process(ctrlSeq, screen, 0, 0, overWriteMode);
            else if(c == 'b') process(ctrlSeq, screen, row, 0, overWriteMode);
            else if(c == 'd') process(ctrlSeq, screen, Math.min( 9,++row), col, overWriteMode);
            else if(c == 'u') process(ctrlSeq, screen, Math.max(0, --row), col, overWriteMode);
            else if(c == 'l') process(ctrlSeq, screen, row, Math.max(0, --col), overWriteMode);
            else if(c == 'r') process(ctrlSeq, screen, row, Math.min(9, ++col), overWriteMode);
            else if(c == 'e') process(ctrlSeq, erase(screen, row, col), row, col, overWriteMode);
            else if(c == 'i') process(ctrlSeq, screen, row, col, false);
            else if(c == 'o') process(ctrlSeq, screen, row, col, true);
            else if(c == '^') process(ctrlSeq, write(c, screen, row, col, overWriteMode), row, Math.min(9, ++col), overWriteMode);
            else process(ctrlSeq.substring(1), screen, Character.getNumericValue(c), Character.getNumericValue(cNext), overWriteMode);
        }
        else process(ctrlSeq.substring(1), write(ctrlSeq.charAt(0), screen, row, col, overWriteMode), row, Math.min(9, ++col), overWriteMode);
        return screen;
    }

    public static void printScreen(char[][] screen){
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++)
                System.out.print(screen[i][j]);
            System.out.println();
        }
    }

    private static char[][] write(char c, char[][] screen, int row, int col, boolean overWriteMode){
        if(overWriteMode) screen[row][col] = c;
        else{
            for(int i = col; i < 9; i++) screen[row][i + 1] = screen[row][i];
            screen[row][col] = c;
        }
        return screen;
    }

    private static char[][] erase(char[][] screen, int row, int col){
        for(int i = col; i < 10; i++) screen[row][i] = ' ';
        return screen;
    }

    private static char[][] getNewScreen(){
        char[][] screen = new char[10][10];
        return clearScreen(screen);
    }

    private static char[][] clearScreen(char[][] screen){
        for(int i = 0; i < 10; i++)
            for(int j = 0; j < 10; j++)
                screen[i][j] = ' ';
        return screen;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/computerTerminal/input.txt"));
        String ctrlSeq = "";
        while(textScan.hasNextLine()){
            ctrlSeq += textScan.nextLine();
        }
        //System.out.println(ctrlSeq);
        printScreen(process(ctrlSeq, getNewScreen(), 0, 0, true));
    }
}

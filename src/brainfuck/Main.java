package brainfuck;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Robert on 11.8.2016.
 */
public class Main {

    public Main(String program){
        StringBuilder sb = new StringBuilder();
        int[] cells = new int[1000];
        int pos = 0, cellId = cells.length/2;

        while(pos < program.length()){
            char c = program.charAt(pos);
            int val = cells[cellId];
            // ++++++++++
            if(c == '+'){
                cells[cellId] = (cells[cellId] +  1) % 256;

            }
            // ----------
            else if(c == '-'){
                cells[cellId] -= 1;
                if(cells[cellId] < 0)
                    cells[cellId] = 255;
            }
            // >>>>>>>>>>
            else if(c == '>'){
                cellId++;
            }
            // <<<<<<<<<<
            else if(c == '<'){
                cellId = Math.max(0, --cellId);
            }
            // .........
            else if(c == '.'){
                sb.append(Character.toChars(val));
            }
            // [][][][]][]
            else if((c == '[' && val != 0) || (c == ']' && val == 0)){
            }
            // [[[[[[[[[[
            else if(c == '[' ){
                pos = matchBrace(pos, program);
            }
            // ]]]]]]]]]]
            else if(c == ']'){
                pos = matchBrace(pos, program);
            }
            pos++;
            //val = cells[cellId];
            //System.out.println("Position: " + pos + "; Char: " + c + "; CELL ID: " + cellId + "; Value: " + val + "; Result: " + sb.toString());

        }
        System.out.println(sb.toString());

    }

    private int matchBrace(int pos, String program){
        char c = program.charAt(pos);
        char opC;
        if(c == '[')
            opC = ']';
        else
            opC = '[';
        int numOfOps = 0;

        while(numOfOps > 0 || program.charAt(pos) != opC){
            if(c == '[')
                pos++;
            else
                pos--;

            char curC = program.charAt(pos);
            if(curC == c)
                numOfOps++;
            else if(curC == opC)
                numOfOps--;
        }
        return pos;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("test-cases/brainfuck.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            //System.out.println(line);
            Main test = new Main(line);
        }
    }
}

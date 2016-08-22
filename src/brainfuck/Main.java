/**
Brainf*ck
Challenge Description:
Looking for something utterly mind-blowing? Look no further, you hit the right place!
 This challenge is inextricably linked with Brainf*ck, the most famous esoteric programming language invented by Urban Müller. Is this the first time you hear about Brainf*ck? Find out more Brainf*ck (wiki)

Brainf*ck consists of only 8 basic commands:
> - move to the next cell;
< - move to the previous cell;
+ - increment the value in the current cell by 1;
- - decrement the value of the current cell by 1;
. - output the value of the current cell;
, - input the value outside and store it in the current cell;
[ - if the value of the current cell is zero, move forward on the text to the program to] taking into account nesting;
] - if the value of the current cell is not zero, go back on the text of the program to [considering nesting;

So, you should write a program that will get the code on the Brainf*ck language, calculate this code, and display the final result of the program. It can be a simple output of letters or a complex cycle; in any case, your program should handle it.

Input sample:
The first argument is a path to a file. Each line includes a test case where there is a program written on the Brainf*ck language.
For example:

1 +[--->++<]>+++.[->+++++++<]>.[--->+<]>----.

2 ++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<++++
+++++++++++.>.+++.------.--------.>+.

Output sample:

You should print the result of the program that you will get.

For example:

1 Yo!
2 Hello world!

Constraints:
1.Programs can include the code of any complexity.
2.The number of test cases is 10.

 */
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
        int[] cells = new int[200];
        int[] braces = new int[program.length()];
        int pos = 0, cellId = cells.length/2;

        // precompute matching braces  positions
        for(int i = 0; i < program.length(); i++)
            braces[i] = matchBrace(i, program);

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
            // [[[[[[[[[[ ]]]]]]]]]]
            else if((c == '[' && val == 0 ) || (c == ']' && val != 0)){
                pos = braces[pos];
            }
            pos++;
        }
        System.out.println(sb.toString());

    }

    private int matchBrace(int pos, String program){
        char c = program.charAt(pos);
        if(c != '[' && c != ']')
            return 0;
        char opC = (c == '[') ? ']' : '[';
        int numOfOps = 0;

        while(numOfOps >= 0 || program.charAt(pos) != opC){
            pos = (c == '[') ? ++pos : --pos;
            char curC = program.charAt(pos);
            numOfOps = (curC == c) ? ++numOfOps : numOfOps;
            numOfOps = (curC == opC) ? --numOfOps : numOfOps;
        }
        return pos;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("test-cases/brainfuck.txt"));

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            Main test = new Main(line);
        }
    }
}
/**
 Advanced Calculator
 Challenge Description:
 The goal of this challenge is to create an advanced calculator.
 The following operations should be supported with their order (operator precedence):
 Pi        Pi number
 e         Euler's number
 sqrt()    Square root
 cos()     Cosine (using radians as an argument)
 sin()     Sine (using radians as an argument)
 tan()     Tangent (using radians as an argument)
 lg()      Decimal logarithm
 ln()      Natural logarithm
 1   ()        Brackets
 2   ||        Absolute value, e.g. |-5 - 10|
 3   !         Factorial
 4   -         Unary minus
 5   ^         Exponent
 6   mod       Modulus divide, e.g. 5 mod 2 = 1 (only integers will be supplied here)
 7   *, /      Multiply, Divide (left-to-right precedence)
 8   +, -      Add, Subtract (left-to-right precedence)

 Input sample:
 Your program should accept as its first argument a path to a filename. The input file contains several lines. Each line is one test case. Each line contains mathematical expression. E.g.
 250*14.3
 3^6 / 117
 (2.16 - 48.34)^-1
 (59 - 15 + 3*6)/21
 lg(10) + ln(e)
 15*5 mod 2

 Output sample:
 For each set of input produce a single line of output which is the result of calculation.
 3575
 6.23077
 −0.02165
 2.95238
 2
 15

 Note: Don't use any kind of eval function.

 Constraints:
 Each number in input expression is greater than -20,000 and less than 20,000.
 Each output number is greater than -20,000 and less than 20,000.
 If output number is a float number it should be rounded to the 5th digit after the dot.
 E.g 14.132646 gets 14.13265, 14.132644 gets 14.13264, 14.132645 gets 14.13265.

 If output number has less than 5 digits after the dot you don't need to add zeros.
 E.g. you need to print 16.34 (and not 16.34000) in case the answer is 16.34.
 And you need to print 16 (and not 16.00000) in case the answer is 16.
 */
package advancedCalculator;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Robert on 13.9.2016.
 */
public class Main {
    DecimalFormat df = new DecimalFormat("0.#####");
    private HashSet<String> isFunction;
    private HashMap<String, Integer> opToPrecedence;

    public Main(String[] tokens){
        Arrays.toString(tokens);
        this.isFunction = new HashSet<String>(){{add("sin"); add("cos"); add("sqrt"); add("tan"); add("lg"); add
                ("ln"); add("!"); add("abs"); add("u");}};
        this.opToPrecedence = new HashMap<String, Integer>(){{put("!", 5); put("^", 4); put("mod", 3); put("/", 2); put
                ("*", 2); put("+", 1); put("-", 1);}};

        LinkedList<String> infix = getInfix(tokens);
        //System.out.println(infix);
        double result = Math.round(evalInfix(infix) * 100000.0)/ 100000.0;
        System.out.println(df.format(result));

    }

    public LinkedList<String> getInfix(String[] tokens){
        LinkedList<String> output = new LinkedList<String>();
        LinkedList<String> opStack = new LinkedList<String>();

        for(String T : tokens){
            //System.out.println(T);
            //System.out.println(output);
            //System.out.println(opStack);
            if(isNumber(T))
                output.addLast(T);
            else if(T.equals(")")){
                while(!opStack.getFirst().equals("("))
                    output.addLast(opStack.removeFirst());
                // remove the (
                opStack.removeFirst();
                if(!opStack.isEmpty() && isFunction.contains(opStack.getFirst()))
                    output.addLast(opStack.removeFirst());
            }
            else if(opStack.isEmpty() || T.equals("(") || isFunction.contains(T)
                    || !opToPrecedence.containsKey(opStack.getFirst())
                    || ( opToPrecedence.get(T) > opToPrecedence.get(opStack.getFirst())))
                opStack.addFirst(T);

            else if(opToPrecedence.containsKey(opStack.getFirst()) && opToPrecedence.get(T) <= opToPrecedence.get(opStack.getFirst())){
                while(opStack.size() > 0 && opToPrecedence.containsKey(opStack.getFirst()) && opToPrecedence.get(T)
                        <= opToPrecedence.get(opStack.getFirst())){
                    output.addLast(opStack.removeFirst());
                }
                opStack.addFirst(T);
            }
        }
        while(!opStack.isEmpty())
            output.addLast(opStack.removeFirst());
        return output;
    }



    public double evalInfix(LinkedList<String> output){
        evalInfix(output, 0);
        return Double.parseDouble(output.getFirst());
    }

    private void evalInfix(LinkedList<String> list, int id){
        if(list.size() == 1)
            return;
        int fstOp = 0;
        while(true){
            if(!isNumber(list.get(fstOp)))
                break;
            fstOp++;
        }
        String op = list.get(fstOp);
        //System.out.println(op);
        if(isFunction.contains(op)){
            double fst = Double.parseDouble(list.get(fstOp - 1));
            list.set(fstOp - 1, String.format("%.20f", getFunctionResult(op, fst)));
            list.remove(fstOp);
            evalInfix(list, fstOp - 1);
        }
        else{
            double fst = Double.parseDouble(list.get(fstOp - 2));
            double snd = Double.parseDouble(list.get(fstOp - 1));
            //System.out.println("first val: " + fst + " sec. val: " + snd + " result: " + combineValues(op, fst, snd));
            list.set(fstOp - 2, String.format("%.20f", combineValues(op, fst, snd)));
            list.remove(fstOp - 1);
            list.remove(fstOp - 1);
            evalInfix(list, fstOp);
        }
    }

    public Double getFunctionResult(String op, double val){
        if(op.equals("sin")){
            return Math.sin(val);
        }
        else if(op.equals("cos")){
            return Math.cos(val);
        }
        else if(op.equals("sqrt")){
            return Math.sqrt(val);
        }
        else if(op.equals("tan")){
            return Math.tan(val);
        }
        else if(op.equals("ln")){
            return Math.log(val);
        }
        else if(op.equals("lg")){
            return Math.log10(val);
        }
        else if(op.equals("!")){
            return (double) factorial((int) val);
        }
        else if(op.equals("abs")){
            return Math.abs(val);
        }
        else if(op.equals("u")){
            return val*(-1);
        }
        return val;
    }

    public Double combineValues(String op, double fstVal, double sndVal){
        if(op.equals("+")){
            return fstVal + sndVal;
        }
        else if(op.equals("-")){
            return fstVal - sndVal;
        }
        else if(op.equals("*")){
            return fstVal * sndVal;
        }
        else if(op.equals("/")){
            return fstVal / sndVal;
        }
        else if(op.equals("^")){
            return Math.pow(fstVal, sndVal);
        }
        else if(op.equals("mod")){
            int fstInt = (int) fstVal;
            int sndInt = (int) sndVal;
            /*int res = fstInt % sndInt;
            if(res < 0)
                res += sndInt;*/
            int res = Math.floorMod(fstInt, sndInt);
            return (double) res;
        }
        return Double.MAX_VALUE;
    }

    private boolean isNumber(String str){
        for (int i = 0; i < str.length(); i++){
            char c = str.charAt(i);
            if(c == '-' && i == 0 && str.length() > 1);
            else if (!Character.isDigit(c) && c != '.') return false;
        }
        return true;
    }

    public long factorial(int n) {
        long fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/advancedCalculator/input_large.txt"));

        HashSet<Character> isOperator = new HashSet<Character>(){{add('+'); add('-'); add('*'); add('/');}};

        while(textScan.hasNextLine()){
            String line = textScan.nextLine();
            /*if(line.equals("sin(cos(tan(lg(ln(13)))))")){
                System.out.println("0.01745");
                continue;
            }*/
            line = line.replaceAll("e", String.valueOf(Math.exp(1)));
            line = line.replaceAll("Pi", String.valueOf(Math.PI));
            //StringBuilder sb = new StringBuilder(line);
            int braceCount = 0;
            LinkedList<Integer> pipes = new LinkedList<Integer>();
            StringBuilder sb = new StringBuilder();
            sb.append(line.charAt(0));
            for(int i = 1; i < line.length(); i++){
                if(line.charAt(i) == '-'
                        && Character.isDigit(line.charAt(i - 1)) && Character.isDigit(line.charAt(i + 1)) ){
                    sb.append(' ');
                    sb.append(line.charAt(i));
                    sb.append(' ');
                }
                else if(line.charAt(i) == '-'
                        && line.charAt(i - 1) == ')' && Character.isDigit(line.charAt(i + 1)) ){
                    sb.append(' ');
                    sb.append(line.charAt(i));
                    sb.append(' ');
                }
                else if(line.charAt(i) == '-'
                        && Character.isDigit(line.charAt(i - 1)) && line.charAt(i + 1) == '(' ){
                    sb.append(' ');
                    sb.append(line.charAt(i));
                    sb.append(' ');
                }
                else{
                    sb.append(line.charAt(i));
                }

            }


            for(int i = 0; i < sb.length(); i++){
                char c = sb.charAt(i);
                if(c == '(') braceCount++;
                else if(c == ')') braceCount--;
                else if(c == '|'){
                    if(!pipes.isEmpty() && pipes.getFirst() == braceCount
                            && !(i > 0 && sb.charAt(i - 1) == '|' && i != sb.length() - 1)
                            && !(i > 1 && (isOperator.contains(sb.charAt(i - 2)) || isOperator.contains(sb.charAt(i -
                            1)) ) )){
                        sb.setCharAt(i, ')');
                        pipes.removeFirst();
                    }
                    else{
                        pipes.addFirst(braceCount);
                    }
                }
            }



            String expr = new String(sb);
            // + and - ops already have spaces between them and the numbers
            // unary minus
            expr = expr.replaceAll("-", "u");
            expr = expr.replaceAll(" u ", " - ");
            expr = expr.replaceAll("u", " u ");
            expr = expr.replaceAll("\\*", " * ").replaceAll("/", " / ").replaceAll("\\^", " ^ ")
                       .replaceAll("\\|", "abs(").replaceAll("\\(", " ( ")
                       .replaceAll("\\)", " ) ").replaceAll("!", " ! ").replaceAll("Pi", " Pi ");
            expr = expr.replaceAll("  ", " ").trim();
            expr = expr.replaceAll("  ", " ").trim();



            //System.out.println(line);
            String[] tokens = expr.split(" ");
            //System.out.println(Arrays.toString(tokens));
            Main test = new Main(tokens);

        }
    }
}

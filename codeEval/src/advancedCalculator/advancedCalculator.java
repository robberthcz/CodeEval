package advancedCalculator;

import java.io.*;
import java.util.Stack;
import java.util.ArrayList;
public class advancedCalculator{
    public static void main (String[] args) throws IOException{
        java.text.DecimalFormat nf = new java.text.DecimalFormat("#.#####");
        File file = new File("src/advancedCalculator/input_large.txt");
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        while ((line = in.readLine()) != null){
            if(line.equals(""))
                continue;
            boolean absolute=false;
            String newLine="";
            for(int i=0;i<line.length();i++){
                if(line.charAt(i)!=' '&&line.charAt(i)!='\t')
                    newLine+=line.charAt(i);
            }
            //System.out.println(newLine);
            ArrayList<String> prefixExp= new ArrayList<String>();
            Stack<Character> operatorStack=new Stack<Character>();
            int index=newLine.length()-1;
            while(index>=0){
                int headIndex=0;
                if(isOperator(newLine.charAt(index))){
                    if((newLine.charAt(index)=='-')&&(index==0||newLine.charAt(index-1)=='^'||newLine.charAt(index-1)=='*'||newLine.charAt(index-1)=='/'||newLine.charAt(index-1)=='('||newLine.charAt(index-1)=='|')){
                        prefixExp.add(Integer.toString(0));
                        prefixExp.add(Character.toString('-'));
                    }
                    else{
                        if(operatorStack.isEmpty())
                            operatorStack.push(newLine.charAt(index));
                        else{
                            if(operatorStack.peek()=='!')
                                prefixExp.add(Character.toString(operatorStack.pop()));
                            if(newLine.charAt(index)=='^'||newLine.charAt(index)=='!')
                                operatorStack.push(newLine.charAt(index));
                            else if(newLine.charAt(index)=='*'||newLine.charAt(index)=='/'){
                                while(!operatorStack.isEmpty()&&(operatorStack.peek()=='^'||operatorStack.peek()=='%'))
                                    prefixExp.add(Character.toString(operatorStack.pop()));
                                operatorStack.push(newLine.charAt(index));
                            }
                            else if(newLine.charAt(index)=='+'||newLine.charAt(index)=='-'){
                                while(!operatorStack.isEmpty()&&(operatorStack.peek()=='*'||operatorStack.peek()=='/'||operatorStack.peek()=='^'||operatorStack.peek()=='%'))
                                    prefixExp.add(Character.toString(operatorStack.pop()));
                                operatorStack.push(newLine.charAt(index));
                            }
                            else if(newLine.charAt(index)==')')
                                operatorStack.push(newLine.charAt(index));
                            else if(newLine.charAt(index)=='('){
                                while(operatorStack.peek()!=')')
                                    prefixExp.add(Character.toString(operatorStack.pop()));
                                operatorStack.pop();
                                if(index-4>=0&&newLine.substring(index-4,index).equals("sqrt")){
                                    prefixExp.add("s");
                                    index-=4;
                                }
                                if(index-3>=0&&newLine.substring(index-3,index).equals("sin")){
                                    prefixExp.add("i");
                                    index-=3;
                                }
                                if(index-3>=0&&newLine.substring(index-3,index).equals("cos")){
                                    prefixExp.add("c");
                                    index-=3;
                                }
                                if(index-3>=0&&newLine.substring(index-3,index).equals("tan")){
                                    prefixExp.add("t");
                                    index-=3;
                                }
                                if(index-2>=0&&newLine.substring(index-2,index).equals("lg")){
                                    prefixExp.add("g");
                                    index-=2;
                                }
                                if(index-2>=0&&newLine.substring(index-2,index).equals("ln")){
                                    prefixExp.add("n");
                                    index-=2;
                                }

                            }
                            else if(newLine.charAt(index)=='|'){
                                if(!absolute){
                                    operatorStack.push(newLine.charAt(index));
                                    absolute=true;
                                }
                                else{
                                    while(operatorStack.peek()!='|')
                                        prefixExp.add(Character.toString(operatorStack.pop()));
                                    prefixExp.add("|");
                                    operatorStack.pop();
                                    absolute=false;
                                }
                            }


                        }
                    }
                    index--;
                }
                else if(isMod(newLine, index)){
                    while(!operatorStack.isEmpty()&&(operatorStack.peek()=='^'))
                        prefixExp.add(Character.toString(operatorStack.pop()));
                    operatorStack.push('%');
                    index-=3;
                }
                else if(newLine.charAt(index)=='e'){
                    prefixExp.add(Double.toString(Math.E));
                    index--;
                }
                else if(isPi(newLine, index)){
                    prefixExp.add(Double.toString(Math.PI));
                    index-=2;
                }
                else{
                    headIndex=findNumIndex(newLine, index);
                    prefixExp.add(newLine.substring(headIndex, index+1));
                    index=headIndex-1;
                }
            }
            while(!operatorStack.isEmpty())
                prefixExp.add(Character.toString(operatorStack.pop()));

            //for(int i=0;i<prefixExp.size();i++)
            //	System.out.print(prefixExp.get(i)+" ");
            //System.out.println();
            Stack<Double> operandStack=new Stack<Double>();
            double num1;
            double num2;
            for(int i=0;i<prefixExp.size();i++){
                if(prefixExp.get(i).equals("^")){
                    num1=operandStack.pop();
                    num2=operandStack.pop();
                    operandStack.push(Math.pow(num1, num2));
                }
                else if(prefixExp.get(i).equals("!"))
                    operandStack.push(factor(operandStack.pop()));
                else if(prefixExp.get(i).equals("*")){
                    num1=operandStack.pop();
                    num2=operandStack.pop();
                    operandStack.push(num1*num2);
                }
                else if(prefixExp.get(i).equals("/")){
                    num1=operandStack.pop();
                    num2=operandStack.pop();
                    operandStack.push(num1/num2);
                }
                else if(prefixExp.get(i).equals("+")){
                    num1=operandStack.pop();
                    num2=operandStack.pop();
                    operandStack.push(num1+num2);
                }
                else if(prefixExp.get(i).equals("-")){
                    num1=operandStack.pop();
                    num2=operandStack.pop();
                    operandStack.push(num1-num2);
                }
                else if(prefixExp.get(i).equals("|")){
                    if(operandStack.peek()<0)
                        operandStack.push(0-operandStack.pop());
                }
                else if(prefixExp.get(i).equals("%")){
                    num1=operandStack.pop();
                    num2=operandStack.pop();
                    operandStack.push(num1%num2);
                }
                else if(prefixExp.get(i).equals("s")){
                    operandStack.push(Math.sqrt(operandStack.pop()));
                }
                else if(prefixExp.get(i).equals("i")){
                    operandStack.push(Math.sin(operandStack.pop()));
                }
                else if(prefixExp.get(i).equals("c")){
                    operandStack.push(Math.cos(operandStack.pop()));
                }
                else if(prefixExp.get(i).equals("t")){
                    operandStack.push(Math.tan(operandStack.pop()));
                }
                else if(prefixExp.get(i).equals("g")){
                    operandStack.push(Math.log10(operandStack.pop()));
                }
                else if(prefixExp.get(i).equals("n")){
                    operandStack.push(Math.log(operandStack.pop()));
                }
                else
                    operandStack.push(Double.parseDouble(prefixExp.get(i)));
            }
            if(Math.abs(operandStack.peek())<0.001)
                System.out.println(0);
            else
                System.out.println(nf.format(operandStack.pop()));
            //System.out.println();
        }
    }
    public static boolean isOperator(char c){
        return (c=='('||c==')'||c=='-'||c=='+'||c=='*'||c=='/'||c=='^'||c=='|'||c=='!');
    }
    public static boolean isMod(String str, int index){
        if(index-2<0)
            return false;
        return str.substring(index-2,index+1).equals("mod");
    }
    public static boolean isPi(String str, int index){
        if(index-1<0)
            return false;
        return str.substring(index-1,index+1).equals("Pi");
    }
    public static int findNumIndex(String str, int index){
        while((index!=0)&&((str.charAt(index-1)>=48&&str.charAt(index-1)<=57)||str.charAt(index-1)==46))
            index--;
        return index;
    }
    public static double factor(double i){
        double sum=i;
        i--;
        while(i>0){
            sum*=i;
            i--;
        }
        return sum;
    }
}
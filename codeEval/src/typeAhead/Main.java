/**
 Type Ahead
 Challenge Description:
 Your task is to build a type-ahead feature for an upcoming product.
 When the user enters a word or set of words, we want to be able to "predict" what they're going to type next with some level of accuracy. We've chosen to implement this using the N-Gram algorithm defined here.

 Your program should return a tuple of predictions sorted high to low based on the prediction score (upto a maximum of three decimal places, or pad with zeroes upto three decimal places i.e. 0.2 should be shown as 0.200), (if predictions share the same score, they are sorted alphabetically.) Words should be split by whitespace with all non-alphanumeric characters stripped off the beginning and end.
 Prediction scores are calculated like this: Occurrences of a word after an N-gram / total number of words after an N-gram e.g. for an N-gram of length 2:
 ONE TWO ONE TWO THREE TWO THREE

 "TWO" has the following predictions:
 THREE:0.666,ONE:0.333

 "THREE" occurred 2 times after a "TWO" and "ONE" occurred 1 time after a "TWO", for a total of 3 occurrences after a "TWO".

 Your program will run against the following text, ignoring all punctuation i.e. Hardcode it into your program:
 Mary had a little lamb its fleece was white as snow;
 And everywhere that Mary went, the lamb was sure to go.
 It followed her to school one day, which was against the rule;
 It made the children laugh and play, to see a lamb at school.
 And so the teacher turned it out, but still it lingered near,
 And waited patiently about till Mary did appear.
 "Why does the lamb love Mary so?" the eager children cry; "Why, Mary loves the lamb, you know" the teacher did reply."

 Input sample:
 Your program should accept as its first argument a path to a filename.The input file contains several lines. Each line is one test case. Each line contains a number followed by a string, separated by a comma. E.g.
 2,the
 The first number is the n-gram length. The second string is the text printed by the user and whose prediction you have to print out.

 Output sample:
 For each set of input produce a single line of output which is the predictions for what the user is going to type next. E.g.
 lamb,0.375;teacher,0.250;children,0.125;eager,0.125;rule,0.125
 */
package typeAhead;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.*;

/**
 * This is complicated only because of the bad challenge description.
 * N-gram - the number (N-1) determines the number of words for which the predictions need to be found, Nth word is
 * the prediction, together they form the N-gram
 * Created by Robert on 9.9.2016.
 */
public class Main {
    DecimalFormat df = new DecimalFormat("0.000");
    String TEXT = "Mary had a little lamb its fleece was white as snow; \n" +
        "And everywhere that Mary went, the lamb was sure to go. \n" +
        "It followed her to school one day, which was against the rule; \n" +
        "It made the children laugh and play, to see a lamb at school. \n" +
        "And so the teacher turned it out, but still it lingered near, \n" +
        "And waited patiently about till Mary did appear. \n" +
        "\"Why does the lamb love Mary so?\" the eager children cry; \"Why, Mary loves the lamb, you know\" the teacher did reply.\"";

    public Main(){
        // normalize the hardcoded text
        TEXT = TEXT.replaceAll("[^A-Za-z0-9 ]", "");
        //System.out.println(TEXT);
    }
    // just for elegant sorting of results needed for submittion
    class Elem implements Comparable<Elem>{
        String word;
        double freq;
        public Elem(String word, double freq) {
            this.word = word;
            this.freq = freq;
        }
        public int compareTo(Elem that){
            String thisFreq = df.format(this.freq);
            String thatFreq = df.format(that.freq);
            //System.out.println(this + " vs " + that + " " + thisFreq.compareTo(thatFreq) + " " + this.word.compareTo(that.word));
            if(!thisFreq.equals(thatFreq)) return thisFreq.compareTo(thatFreq)*(-1);
            else return this.word.compareTo(that.word);
        }
        public String toString(){
            return word + "," + df.format(freq).toString().replace(',', '.');
        }
    }

    public LinkedList<String> getWordsAfterNgram(String ngram){
        LinkedList<String> list = new LinkedList<String>();
        int i = 0;
        while(true){
            int id = TEXT.indexOf(ngram, i);
            // no match found or ngram is the last word
            if(id == - 1 || (id + ngram.length()) >= TEXT.length()) break;
            // make surch match is full word and not its substring
            else if(TEXT.charAt(id + ngram.length()) != ' ' || (id != 0 && TEXT.charAt(id - 1) != ' ')){
                ++i;
                continue;
            }
            String prefix = TEXT.substring(id + ngram.length() + 1);
            // the word after ngram
            String fstWord = prefix.substring(0, Math.min(prefix.indexOf(" "), TEXT.length()));
            list.add(fstWord);
            i = id + 1;
        }
        return list;
    }

    /**
     * Print the result according to CodeEval liking
     * @param list
     */
    public void printResult(LinkedList<String> list){
        HashMap<String, Integer> countMap = new HashMap<String, Integer>();
        TreeSet<Elem> elemsOrd = new TreeSet<Elem>();
        int total = list.size();
        for(String S : list){
            if(!countMap.containsKey(S)) countMap.put(S, 1);
            else countMap.put(S, countMap.get(S) + 1);
        }

        for(String S : countMap.keySet())
            elemsOrd.add(new Elem(S, (double) countMap.get(S) / (double) total));
        String toString = elemsOrd.toString().replaceAll(", ", ";");
        System.out.println(toString.substring(1, toString.length() - 1));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/typeAhead/input_large.txt"));
        Main test = new Main();

        while(textScan.hasNextLine()){
            String ngram = textScan.nextLine().split(",")[1];
            test.printResult(test.getWordsAfterNgram(ngram));
        }
    }
}

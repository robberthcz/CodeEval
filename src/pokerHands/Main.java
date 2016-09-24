/**
 Poker hands
 Challenge Description:
 In the card game poker, a hand consists of five cards and are ranked, from lowest to highest, in the following way:
 High Card: Highest value card.
 One Pair: Two cards of the same value.
 Two Pairs: Two different pairs.
 Three of a Kind: Three cards of the same value.
 Straight: All cards are consecutive values.
 Flush: All cards of the same suit.
 Full House: Three of a kind and a pair.
 Four of a Kind: Four cards of the same value.
 Straight Flush: All cards are consecutive values of same suit.
 Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.
 The cards are valued in the order:
 2, 3, 4, 5, 6, 7, 8, 9, Ten, Jack, Queen, King, Ace.

 If two players have the same ranked hands then the rank made up of the highest value wins; for example, a pair of eights beats a pair of fives. But if two ranks tie, for example, both players have a pair of queens, then highest cards in each hand are compared; if the highest cards tie then the next highest cards are compared, and so on.

 Input sample:
 Your program should accept as its first argument a path to a filename. Each line in this file contains 2 hands (left and right). Cards and hands are separated by space. E.g.
 6D 7H AH 7S QC 6H 2D TD JD AS
 JH 5D 7H TC JS JD JC TS 5S 7S
 2H 8C AD TH 6H QD KD 9H 6S 6C
 JS JH 4H 2C 9H QH KC 9D 4D 3S
 TC 7H KH 4H JC 7D 9S 3H QS 7S

 Output sample:
 Print out the name of the winning hand or "none" in case the hands are equal. E.g.
 left
 none
 right
 left
 right
 */
package pokerHands;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Robert on 13.8.2016.
 */
public class Main {
    // 0111 0001 0000 0000 0001 ...
    // A    K    Q    J    TEN
    long v4;
    // 0000 0000 0000 0
    // A ... ..  6 .. 2
    // 13 nums for each suit
    long v13;
    long ROYAL_FLUSH = 0b1111100000000;


    public Main(int[] vals, boolean isSameSuit){
        this.v4 = 0;
        this.v13 = 0;

        for(int val : vals){
            addVal13(val);
            addVal4(val);
        }
        System.out.println(Arrays.toString(vals));
        System.out.println(v4 % 15);
    }

    public void addVal4(int id){
        long valBitsOn = (15L << (id << 2)) & v4;
        v4 = ((valBitsOn << 1) | v4) | (1L << (id << 2));
    }

    public void addVal13(int id){
        v13 = v13 | (1 << id);
    }

    public boolean isConseq(){
        return ((v13 >>> Long.numberOfTrailingZeros(v13)) & 31) == 31;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/pokerHands/input.txt"));

        HashMap<Character, Integer> valToId = new HashMap<Character, Integer>(){{
            put('2', 0); put('3', 1); put('4', 2); put('5', 3); put('6', 4); put('7', 5);
            put('8', 6); put('9', 7); put('T', 8); put('J', 9); put('Q', 10); put('K', 11);
            put('A', 12);}};

        while(textScan.hasNextLine()){
            String[] cards = textScan.nextLine().split(" ");
            int[] leftVals = new int[5];
            int[] rightVals = new int[5];
            boolean leftSameSuit = true, rightSameSuit = true;
            for(int i = 0; i < 5; i++){
                // parse left hand
                leftVals[i] = valToId.get(cards[i].charAt(0));
                // parse right hand
                rightVals[i] = valToId.get(cards[i + 5].charAt(0));

                if(i > 0){
                    leftSameSuit = leftSameSuit && cards[i].charAt(1) == cards[i - 1].charAt(1);
                    rightSameSuit = rightSameSuit && cards[i + 5].charAt(1) == cards[(i + 5) - 1].charAt(1);
                }
            }

            Main leftHand = new Main(leftVals, leftSameSuit);
            Main rightHand = new Main(rightVals, rightSameSuit);
            System.out.println();
        }
        /*
        left
        none
        right
        left
        right
        */

    }
}

/**
 Poker hands
 Challenge Description:
 In the card game poker, a hand consists of five cards and are ranked, from lowest to highest, in the following way:
 High Card: Highest value card.                                                 0
 One Pair: Two cards of the same value.                                         1
 Two Pairs: Two different pairs.                                                2
 Three of a Kind: Three cards of the same value.                                3
 Straight: All cards are consecutive values.                                    4
 Flush: All cards of the same suit.                                             5
 Full House: Three of a kind and a pair.                                        6
 Four of a Kind: Four cards of the same value.                                  7
 Straight Flush: All cards are consecutive values of same suit.                 8
 Royal Flush: Ten, Jack, Queen, King, Ace, in same suit.                        9
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
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Robert on 13.8.2016.
 * Main source: http://www.codeproject.com/Articles/569271/A-Poker-hand-analyzer-in-JavaScript-using-bit-math
 * The bit hacks v4 and v13 are taken from the above source. It is just genius from the author. Especially the modulo operation on v4 is fantastic, it showed me and taught me how modulo operation works at a bit level. I am not going to explain it here, understand it by yourself.
 *
 * However, in this challenge you also have to deal with ties. For instance, if both player have Two Pairs, first you compare value of the highest pair, if same, the value of lower pairs, if still same, then you compare the highest value cards.
 */
public class Main {
    // 0111 0001 0000 0000 0001 ...
    // A    K    Q    J    TEN
    long v4;

    // 0000 0000 0000 0
    // A ... ..  6 .. 2
    // 13 nums for each value, suit not taken into account
    long v13;
    final long ROYAL_FLUSH = 0b1111100000000;

    // v4 % 15 = 6 => One Pair, 7 => Two Pairs, 9 => Three of a Kind, 10 => Full House, 1 => Four of a Kind
    // not all ranks can be determined with v4
    HashMap<Integer, Integer> modToRank = new HashMap<Integer, Integer>(){{ put(6, 1); put(7, 2); put(9, 3); put(10, 6); put(1, 7); put(5, -1);}};
    final boolean isSameSuit, isConseq;


    public Main(int[] vals, boolean isSameSuit){
        this.v4 = 0; this.v13 = 0; this.isSameSuit = isSameSuit;
        for(int val : vals){
            addVal13(val);
            addVal4(val);
        }
        this.isConseq = isConseq();
    }
    // write value of a card into v4
    public void addVal4(int id){
        long valBitsOn = (15L << (id << 2)) & v4;
        v4 = ((valBitsOn << 1) | v4) | (1L << (id << 2));
    }
    // write value of card into v13
    public void addVal13(int id){
        v13 = v13 | (1 << id);
    }
    // find out if we the hand has cards with consecutive values
    public boolean isConseq(){
        return ((v13 >>> Long.numberOfTrailingZeros(v13)) & 31) == 31;
    }

    /**
     * For purposes of determining ties between hands. Returns the n-th highest value of a pair (count = 2), three of a kind (count = 3), four of a kind (count = 4) or highest value card (count = 1) if located in this hand, else returns -1.
     * @param count
     * @param n
     * @return
     */
    public int getValueCount(int count, int n){
        int id = 0;
        int countToBin = (1 << count) - 1;
        int fst4 = (1 << 4) - 1;

        for(int i = 12; i >= 0; i--){
            // shift v4 by i*4 bits to right
            if((((v4 >>> (i << 2)) & fst4)) == countToBin) id++;
            if(id == n) return i;
        }
        return -1;
    }

    /**
     * Determines the rank of this hand.
     * @return Number representing the rank, 0 => High Card, 1 => One Pair, ... 9 => Royal Flash. As it is written in the description.
     */
    public int getRank(){
        int modRank = modToRank.get((int) (v4 % 15L));
        if(modRank != -1 && isSameSuit) modRank = Math.max(modRank, 5);
        if(modRank != -1) return modRank;
        if(v13 == ROYAL_FLUSH && isSameSuit) return 9;
        else if(isConseq && isSameSuit)      return 8;
        else if(isSameSuit)                  return 5;
        else if(isConseq)                    return 4;
        else                                 return 0;
    }

    public static int compareHands(Main leftHand, Main rightHand){
        int leftRank = leftHand.getRank();
        int rightRank = rightHand.getRank();
        if(leftRank != rightRank)               return Integer.compare(leftRank, rightRank);
        // TIES
        // Consecutive values
        if(leftRank == 8 || leftRank == 4)      return Long.compare(leftHand.v13, rightHand.v13);
        // Four of a kind
        if(leftRank == 7){
            int leftFour = leftHand.getValueCount(4, 1);
            int rightFour = rightHand.getValueCount(4, 1);
            if(leftFour != rightFour)           return Integer.compare(leftFour, rightFour);
        }
        // Three of a kind
        if(leftRank == 6 || leftRank == 3){
            int leftThree = leftHand.getValueCount(3, 1);
            int rightThree = rightHand.getValueCount(3, 1);
            if(leftThree != rightThree)          return Integer.compare(leftThree, rightThree);
        }
        // Pair
        if(leftRank == 6 || leftRank == 2 || leftRank == 1){
            int leftPair = leftHand.getValueCount(2, 1);
            int rightPair = rightHand.getValueCount(2, 1);
            if(leftPair != rightPair)             return Integer.compare(leftPair, rightPair);
        }
        // 2nd Pair
        if(leftRank == 2){
            int leftPair = leftHand.getValueCount(2, 2);
            int rightPair = rightHand.getValueCount(2, 2);
            if(leftPair != rightPair)              return Integer.compare(leftPair, rightPair);
        }
        // highest value card
        for(int i = 0; i < 5; i++) {
            int leftVal = leftHand.getValueCount(1, i + 1);
            int rightVal = rightHand.getValueCount(1, i + 1);
            if (leftVal == -1 || rightVal == -1) break;
            if (leftVal != rightVal)                return Integer.compare(leftVal, rightVal);
        }
        return 0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner textScan = new Scanner(new FileReader("src/pokerHands/input.txt"));
        // value of card => id
        HashMap<Character, Integer> valToId = new HashMap<Character, Integer>(){{
            put('2', 0); put('3', 1); put('4', 2); put('5', 3); put('6', 4); put('7', 5);
            put('8', 6); put('9', 7); put('T', 8); put('J', 9); put('Q', 10); put('K', 11);
            put('A', 12);}};

        while(textScan.hasNextLine()) {
            String[] cards = textScan.nextLine().split(" ");
            int[] leftVals = new int[5];
            int[] rightVals = new int[5];
            boolean leftSameSuit = true, rightSameSuit = true;
            for (int i = 0; i < 5; i++) {
                // parse left hand
                leftVals[i] = valToId.get(cards[i].charAt(0));
                // parse right hand
                rightVals[i] = valToId.get(cards[i + 5].charAt(0));
                // find if cards are from the same suit
                if (i > 0) {
                    leftSameSuit = leftSameSuit && cards[i].charAt(1) == cards[i - 1].charAt(1);
                    rightSameSuit = rightSameSuit && cards[i + 5].charAt(1) == cards[(i + 5) - 1].charAt(1);
                }
            }
            Main leftHand = new Main(leftVals, leftSameSuit);
            Main rightHand = new Main(rightVals, rightSameSuit);

            int res = compareHands(leftHand, rightHand);
            if (res == 1)       System.out.println("left");
            else if (res == -1) System.out.println("right");
            else                System.out.println("none");
        }
    }
}

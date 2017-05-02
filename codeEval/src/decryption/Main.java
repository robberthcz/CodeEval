/**
 Decryption
 Challenge Description:
 For this challenge you are given an encrypted message and a key. You have to determine the encryption and encoding technique and then print out the corresponding plaintext message. You can assume that the plaintext corresponding to this message, and all messages you must handle, will be comprised of only the characters A-Z and spaces; no digits or punctuation.

 Input sample:
 There is no input for this program. The encrypted message and key is:
 message: "012222 1114142503 0313012513 03141418192102 0113 2419182119021713 06131715070119",
 keyed_alphabet: "BHISOECRTMGWYVALUZDNFJKPQX"

 Output sample:
 Print out the plaintext message. (in CAPS)
 */
package decryption;

import java.util.HashMap;

/**
 * Created by Robert on 6.9.2016.
 */
public class Main {

    public static void main(String[] args){
        String keyed_alphabet = "BHISOECRTMGWYVALUZDNFJKPQX";
        String message = "012222  1114142503  0313012513  03141418192102  0113  2419182119021713  06131715070119";
        HashMap<Character, Integer> keyToPos = new HashMap<Character, Integer>();

        for(int i = 0; i < keyed_alphabet.length(); i++)
            keyToPos.put(keyed_alphabet.charAt(i), i);

        String dec_message = "";

        for(int i = 0; i < message.length(); i += 2){
            String cur = message.substring(i, i + 2);
            if(cur.equals("  ")) dec_message += " ";
            else{
                int num = Integer.parseInt(cur);
                dec_message += (char) ('A' + keyToPos.get( (char) ('A' + num)));
            }
        }
        System.out.println(dec_message);

    }
}

/**
 IP PACKAGE
 CHALLENGE DESCRIPTION:
 А novice hacker decided to play a trick on your friends and changed addresses in the header of an IP packet, you need to fix all the packets and put the correct address to help your friends.
 A package consists of a header IPV4 and a body of the package.
 You need to replace the IP source and destination IP address to the new addresses and print out the IP header under the condition that the package is valid. This means you should calculate checksum of IP header.

 INPUT SAMPLE:
 Your program should accept as its first argument a path to a filename.
 Each line contains new source ip address, new destination ip address and package in hex.
 190.168.0.96 190.168.0.96 45 04 05 dc b7 3a 40 00 2e 06 a6 df 36 f1 f0
 fd c0 a8 00 67 01 bb e7 eb 2e 05 72 e4 01 40 93 41 80 10 00 88 9d e5 00
 00 01 01 08 0a 28 d8 76 c4 00 03 65 45 47 cf bc 5c 01 99 c6 52 91 6f 5f
 df 69 24 a0 f9 b5 6c dc a2 f7 18 db 07 b8 18 dc 90 1a c4 bf 66 e1 33 f2
 73 e6 22 ca 3c e9 bc 15 c1 b5 01 41 99 1d 25 eb ee 60 7d df 08 80 a7 98
 25 cc 86 6d 19 83 69 e8 4c 5c c7 9f 4f 5b 93 a5 a2 14 4d 4c 3d 6c 85 fb
 4f 45 2e 1e ee 33 11 f5 bf 1d f2 67 e0 30 d1 54 18 57 f0 f4 66 a4 d6 be
 08 f2 db f4 2f 0a f9 50 81 03 80 7e 25 43 d9 ff 50 55 53 7a ab 9c 4f 5c
 55 a0 cb 49 23 9a e7 9b 7c 2f 29 ce 6b 95 85 b2 be 58 2b de cc 59 0d 27
 4b 85 0b df b5 15 4f c0 8f 04 9b 94 c0 59 b0 98 76 a3 0f 1c 00 53 36 6a
 f4 87 d0 68 28 90 eb a0 3f 9d e0 2b a9 54 a5 45 42 53 1e 01 31 37 09 b8
 bf 8a 07 a0 b6 11 17 43 79 5e 1a 16 59 84 7d 97 5d fb 38 d9 37 e5 c0 6d
 0a 9b d6 18 a9 dc 35 fe fb df ee ef 1e ce 28 9d db 28 6c 0f 33 e1 dd 76
 6d 88 4b 88 f2 92 ef 75 62 90 07 72 8d 93 35 0b 2e a3 de 89 20 10 63 07
 a3 24 ab 1a cd 10 af 01 b3 6f fc fc 80 7c 11 88 27 dc 6d 4d db 45 4e 49
 13 db 0c 4c 40 84 72 cf 45 35 d8 f4 ac 8e 60 dd b8 18 4e 71 0b 0f 07 9f
 69 8c 30 4f 56 7a 0a 77 c0 02 40 b6 0e 27 af 17 d2 df b7 95 43 35 c6 b6
 48 6f ee 0c 7a db 38 04 63 0e e7 c8 92 a5 c5 30 db 7d 86 c7 52 8e b1 1a
 20 12 36 85 ef 60 6f 00 91 4b df df 34 3e ff 32 31 b3 58 c3 63 b8 73 76
 9d 15 bd c5 4c 7e 26 b6 0c 19 c5 f8 a5 e6 e2 24 48 9f 78 07 2f d7 d7 03
 5b 8a 91 8c 0e cd 47 01 bc 9c f4 29 4d dd 1d a4 85 9f 1a e2 53 8a 80 2d
 1e 2a 78 ea 44 31 88 23 c8 99 c7 d6 f0 20 0f b3 a9 75 2e d5 8e a5 b9 9a
 23 a0 65 f5 8e b4 d4 cf 9f a9 a5 6b a7 bf 22 7f 82 a2 4b 6e 33 2f a4 18
 7c 35 36 d5 65 00 00 49 77 b8 7b 71 90 1f 16 3b de 84 18 70 cd e0 4b 57
 83 96 56 2d 0e 3d 1c 4e df e5 2a 9f 2d 08 fc 26 eb 80 3d 55 b1 13 7c c3
 48 b0 cd 3b 9d 21 55 62 31 5d 57 dd a8 1a 44 60 ca 47 d0 df 8c b8 53 d5
 89 b2 e6 8e e8 92 ad 97 d1 1e 8c 8f 93 d5 f1 b0 a0 28 b7 5d fc 81 11 80
 2c 31 77 7a 1b c3 fc dd cf 27 ae fc 00 dc 64 79 53 5f 67 3b be d4 26 e6
 dc 36 dc 2a 70 cf 50 86 36 34 d6 aa 74 94 5d 12 0f 18 26 58 38 74 fb f0
 da 7c 5e 8e d7 33 c5 1c 2e 80 f5 2f 97 1d 34 2d 31 5e 0f 9b b3 40 ef be
 69 76 6b 59 85 f7 c5 df cc ec e3 32 a8 ef 7a 5c e0 4b 9b 9f 88 bf f1 d0
 61 23 39 ff 2f a4 b9 62 f3 3e ae 94 f6 ef dc e3 fc 1c 36 9c e9 38 3f 77
 9a 69 2e 27 15 b3 5f 85 9c 98 fe a4 d6 32 09 eb 0f c3 2c 5d 70 cf 55 68
 a3 f1 53 2f 65 1e 30 c7 92 77 e4 3b 7c 26 7b d4 a7 dc 64 fa 96 ce c3 9b
 03 ec a8 d4 3e eb 8e 89 5c f0 82 ae 70 7c 64 59 73 34 62 ab bc 65 7c a3
 cd 98 ea 69 1b 8b 7c a0 c5 3d 75 6b 69 bb 1d bf 14 75 fb 80 96 32 14 60
 93 6a d4 2e b7 b2 c9 de 64 af a6 69 ce c8 81 06 bd ec dc 90 07 48 93 68
 e4 c0 6b d2 ff 9b 84 1c 71 c8 5d a6 34 ae b9 85 46 2b 0f 40 ed 0d 5c d9
 08 b2 2e 60 e6 34 4e 73 31 a1 e7 c1 ac 65 da a5 69 53 02 62 a2 af f1 9b
 c7 42 40 33 29 cc d5 24 d1 3c 22 7d 27 c7 71 fd 81 14 0d fe 79 37 28 ef
 18 3f da f3 aa ee 5b 05 db a0 09 64 ed 84 0d 9c 86 5c 8c 70 d8 a4 78 61
 cd fa b7 90 20 2b 80 61 d6 b8 6b 42 a0 fd 22 25 54 50 69 98 c2 65 c9 36
 5a 52 23 9f ca 98 35 ad 3d 2a 08 6b 92 bc 74 76 c4 d5 20 a6 a1 31 b6 de
 70 10 71 6d 90 f6 35 15 fa 01 bf b7 f8 c6 af d7 d1 c0 74 7d 3c d4 36 ea
 d7 72 ed 27 0f 7c f3 e9 de 2f dc 38 94 34 d6 c5 2e cf 3a 49 5d fe ea ea
 50 be 5f 43 cb 46 36 e4 0a ae bc 77 50 6e 27 3a 2f 75 b6 36 8c aa cc 33
 46 ee c5 9c b7 4f 16 36 4b 43 92 9c 4a 84 4a d9 f5 87 3b 87 58 09 ff 8b
 93 7d 66 73 72 ae 48 84 f7 c5 21 71 0e d4 51 2f 55 c8 cd df 0a 15 7c cc
 51 77 6a aa 2b 6d 95 ff 5b de bd 8e e8 07 c6 60 96 de 81 2a 20 4a 18 5b
 eb 7a c1 dc be 27 df 37 49 5d 7c e4 83 07 fe ac 9b f2 f6 44 2f 60 b7 cc
 39 15 4a 54 4b 26 41 32 e7 20 99 92 8e 90 f9 64 b6 e9 2f 43 ce 56 c3 50
 9c a2 40 68 09 b4 bc 9e 4d 57 20 f0 72 3f e0 f6 b6 88 88 44 9b 3a 00 1c
 56 5f a2 28 b7 df 5f 95 35 1b 2a 72 d9 99 a1 67 f1 d7 c5 64 76 81 fc 68
 36 9a 92 6a 46 a5 83 43 69 79 ce b9 d3 b6 5d 97 e9 26 27 c4 71 fe ab f4
 56 62 c4 43 2d c8 aa 67 ec cb 32 dd df 5a b3 d0 cb a2 9a 45 13 64 6b 2c
 a9 2f 8b 64 ad 99 45 d2 d3 f9 d8 b0 88 88 80 69 2e 63 f0 12 1d 16 76 e1
 c8 08 a0 63 5c d5 55 82 4a b2 9d a4 3c b7 60 5c 0d d9 63 ea 43 68 2c f4
 d9 f1 ec 5e 6d e2 a1 0f 36 38 53 1a 5e ee b7 df ed 04 b8 86 3a 91 4c e9
 b6 02 ad b5 0d 60 c8 69 56 6f b2 0e 20 75 be e7 6b 2b 46 d9 01 dc f7 ec
 55 1e d6 43 ea 76 7d 59 df 5e 2f 77 fb cd a9 0e 53 2f 1e b2 32 6b 6a f1
 20 52 be d1 ab 23 ea 41 e4 d9 c8 06 e5 47 25 e6 0c 7f 07 21 3c 6f f1 6c
 c7 fb 04 19 98 f6 25 dc cb 99 38 59 f7 c9 fe 75 80 a6 24 a1 54 4e 04 d0
 90 dc 64 26 aa 28 34 6d 66 ff 6f e9 29 83 49 54 78 4a 39 54 79

 OUTPUT SAMPLE:
 For each line of input print out ip header with new destination and source ip address and valid checksum
 45 04 05 dc b7 3a 40 00 2e 06 11 cd be a8 00 60 be a8 00 60
 */
package ipPackage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Best resource on IP headers and calculating checksums is the following:
 * http://www.thegeekstuff.com/2012/05/ip-header-checksum
 * Plus, I also used the trick for summing numbers from here
 * https://en.wikipedia.org/wiki/IPv4_header_checksum
 * Be careful for right-bit-shift operator, the 1's bits on the right are carried over to the left
 * Created by Rob on 9/17/2016.
 */
public class Main {
    /**
     *  Returns bit representation of the given IP address.
     * @param ipAddress String representation of IP address as follows 192.168.16.1.
     * @return
     */
    public static int getIpAddress(String ipAddress){
        String[] parts = ipAddress.split("\\.");
        return Integer.parseInt(parts[0]) << 24 | Integer.parseInt(parts[1]) << 16
              | Integer.parseInt(parts[2]) << 8 | Integer.parseInt(parts[3]);
    }

    /**
     * Transforms hex string to binary
     * 4500 => 0100 0101 0000 0000
     * @param i
     * @return
     */
    public static String intToHex(int i){
        return Integer.toHexString((i >> 12) & 15) + Integer.toHexString((i >> 8) & 15)
             + Integer.toHexString((i >> 4) & 15) + Integer.toHexString(i & 15);
    }

    public static void main(String[] args) throws FileNotFoundException {
        final int first16bits = ((1 << 16) - 1);
        Scanner textScan = new Scanner(new FileReader("src/ipPackage/input.txt"));

        while(textScan.hasNextLine()){
            // holds the 10 IP header fields
            int[] ipHeader = new int[10];
            String[] line = textScan.nextLine().split(" ");

            int sourceIp = getIpAddress(line[0]);
            int destIp = getIpAddress(line[1]);

            for(int i = 0; i < 10; i += 2){
                String twoOcts = line[i + 2] + line[i + 3];
                ipHeader[i / 2] = Integer.parseInt(twoOcts.substring(0,1), 16) << 12
                        | Integer.parseInt(twoOcts.substring(1, 2), 16) << 8
                        | Integer.parseInt(twoOcts.substring(2,3), 16) << 4
                        | Integer.parseInt(twoOcts.substring(3,4), 16);
            }
            // save the binary representation of IP adresses
            ipHeader[6] = (sourceIp >> 16) & first16bits;
            ipHeader[7] = sourceIp & first16bits;
            ipHeader[8] = (destIp >> 16) & first16bits;
            ipHeader[9] = destIp & first16bits;
            // sum the fields in the IP header
            int sum = 0;
            for(int i : ipHeader) sum += i;
            // the sum must be represented in 16-bit form
            // hence add the carry to the first 16-bits of sum
            // if another carry is generated, add again
            while(((sum >> 16) & first16bits) != 0)
                sum = (sum & first16bits) + ((sum >> 16) & first16bits);
            // checksum is one's complement of the sum, just flip the bytes
            ipHeader[5] = ~sum;

            // print the result
            for (int i = 0; i < ipHeader.length; i++){
                if( i < 5) System.out.print(line[i*2 + 2] + " " + line[i*2 + 3]);
                else{
                    String field = intToHex(ipHeader[i]);
                    System.out.print(field.substring(0, 2) + " " + field.substring(2));
                }
                if(i < 9) System.out.print(" ");
            }
            System.out.println();
        }
    }
}

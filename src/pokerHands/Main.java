package pokerHands;

/**
 * Created by Robert on 13.8.2016.
 */
public class Main {

    public Main(){
        // 0111 0001 0000 0000 0001 ...
        // A    K    Q    J    TEN
        long r = 0;
        // 0000000000000 0000000000000 ....
        // 13 nums for each suit
        long s = 0;
    }

    public long addRank4(long r,  int rank){
        long rankBitsOn = (15 << (rank << 2)) & r;
        return ((rankBitsOn << 1) | r) | (1 << (rank << 2));
    }

    public long addRank13(long s, int rank){
        return s | (1 << rank);
    }

    public boolean isConseq(long s){
        long bits1to13 = ((s | (s >> 13)) | (s >> 26)) | (s >> 39);
        int rightmostBit = (int) (Math.log(s & -s)/Math.log(2));
        return ((bits1to13 >> rightmostBit) & 31) == 31;
    }

    public boolean isSameSuit(long s){
        long tmp = 0x01FFFL;
        return Long.bitCount(s & tmp) == 5 || Long.bitCount(s & (tmp << 13)) == 5 || Long.bitCount(s & (tmp << 26)) == 5 || Long.bitCount(s & (tmp << 39)) == 5;
    }

    public static void main(String[] args){

        Main test = new Main();
        long sNotConseq = 1179648;
        long sConseq = 2031616;
        System.out.println(test.isConseq(sNotConseq));
        System.out.println(test.isConseq(sConseq));

        long r = 0;
        r = test.addRank4(r, 1);
        r = test.addRank4(r, 1);
        r = test.addRank4(r, 1);
        r = test.addRank4(r, 3);
        r = test.addRank4(r, 3);
        System.out.println(Long.toBinaryString(r));
        System.out.println(Long.bitCount(r));

        long tmp = 0x01FL;
        System.out.println(test.isSameSuit(tmp));

        long notSameSuit = 14680076;
        System.out.println(test.isSameSuit(notSameSuit));

        System.out.println(Long.toBinaryString(test.addRank13(0,0)));
        System.out.println(Long.toBinaryString(test.addRank13(0, 1)));
        System.out.println(Long.toBinaryString(test.addRank13(0, 10)));

    }
}

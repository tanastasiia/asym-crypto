package ua.kpi.generators.util;

import java.math.BigInteger;
import java.util.Random;

public class Util {

    public final static Random random = new Random();

    /**
     * @param mask - max generated number
     * @return random non zero number;
     */
    public static long random(long min, long mask) {
        while (true) {
            long num = random.nextLong() & mask;
            if (num >= min) {
                return num;
            }
        }
    }

    public static BigInteger randomBigInteger(BigInteger min, BigInteger max) {
        while (true) {
            BigInteger num = new BigInteger(max.bitLength(), random).mod(max);
            if (num.compareTo(min) >= 0) {
                return num;
            }
        }
    }
}

package ua.kpi.generators.util;

import java.util.Random;

public class RandomNum {
    private final static Random random = new Random();

    /**
     * @param mask - max generated number
     * @return random non zero number;
     */
    public static long random(long mask) {
        while (true) {
            long num = random.nextLong() & mask;
            if (num != 0) {
                return num;
            }
        }
    }

    public static long random(long mask, long seed) {
        while (true) {
            long num = new Random(seed).nextLong() & mask;
            if (num != 0) {
                return num;
            }
        }
    }
}

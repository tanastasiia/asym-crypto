package ua.kpi.generators.util;

import java.util.Random;

public class Util {

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

    //не обращай пока внимания
    public static long random(long mask, long seed) {
        while (true) {
            long num = new Random(seed).nextLong() & mask;
            if (num != 0) {
                return num;
            }
        }
    }

    /**
     * @param number number to make string from
     * @param length length of string that must be
     * @return generated string from number: for example number=3, length=5; 3 = 101 in binary => result is string "00101"
     */
    public static String makeBinaryString(long number, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length - Long.toString(number, 2).length(); i++) {
            sb.append("0");
        }
        return length == 0 ? sb.toString() : sb.append(Long.toString(number, 2)).toString();
    }
}

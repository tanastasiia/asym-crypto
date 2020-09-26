package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class LehmerLow extends Lehmer {

    private final long mask8lowBits = (1L << 8) - 1;

    @Override
    public int[] generate(int byteLength) {

        long x = Util.random(maskM);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            x = (a * x + c) & maskM;
            bytes[i] = (int) ((x & mask8lowBits));
        }
        return bytes;

    }
}

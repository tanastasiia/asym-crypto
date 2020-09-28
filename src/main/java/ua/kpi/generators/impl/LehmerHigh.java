package ua.kpi.generators.impl;

import ua.kpi.generators.Lehmer;
import ua.kpi.generators.util.Util;

public class LehmerHigh extends Lehmer {

    @Override
    public int[] generate(int byteLength) {

        long mask8highBits = ((1L << 32) - 1) - ((1L << 24) - 1);
        long x = Util.random(1, maskM);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            bytes[i] = (int) ((x & mask8highBits) >> 24);
            x = (a * x + c) & maskM;
        }
        return bytes;

    }
}

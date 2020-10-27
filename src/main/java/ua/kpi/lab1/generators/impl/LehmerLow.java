package ua.kpi.lab1.generators.impl;

import ua.kpi.lab1.generators.Lehmer;
import ua.kpi.lab1.generators.util.Util;

public class LehmerLow extends Lehmer {

    @Override
    public int[] generate(int byteLength) {

        long mask8lowBits = 0b11111111; //(1L << 8) - 1;
        long x = Util.random(1, maskM);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            bytes[i] = (int) ((x & mask8lowBits));
            x = (a * x + c) & maskM;
        }
        return bytes;

    }
}

package ua.kpi.generators.impl;

import ua.kpi.generators.BM;
import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BMBit extends BM {
    @Override
    public int[] generate(int byteLength) {
        BigInteger t = Util.randomBigInteger(BigInteger.ZERO, p);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                bytei += (t.compareTo(q) < 0 ? 1 : 0) << j;
                t = a.modPow(t, p);
            }
            bytes[i] = bytei;
        }
        return bytes;
    }
}

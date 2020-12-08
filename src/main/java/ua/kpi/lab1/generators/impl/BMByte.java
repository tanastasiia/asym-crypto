package ua.kpi.lab1.generators.impl;

import ua.kpi.lab1.generators.BM;
import ua.kpi.util.Util;

import java.math.BigInteger;

public class BMByte extends BM {
    @Override
    public int[] generate(int byteLength) {

        BigInteger t = Util.randomBigInteger(BigInteger.ZERO, p);
        BigInteger byte256 = BigInteger.valueOf(256);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            bytes[i] = (int) t.multiply(byte256).divide(p.subtract(BigInteger.ONE)).longValueExact();
            t = a.modPow(t, p);

        }
        return bytes;
    }
}

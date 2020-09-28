package ua.kpi.generators.impl;

import ua.kpi.generators.BBS;
import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BBSByte extends BBS {

    private final BigInteger byte256 = BigInteger.valueOf(256);

    @Override
    public int[] generate(int byteLength) {

        BigInteger r = Util.randomBigInteger(BigInteger.valueOf(2), n);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            r = r.modPow(BigInteger.valueOf(2), n);
            bytes[i] = (int) r.mod(byte256).longValueExact();
        }
        return bytes;
    }
}

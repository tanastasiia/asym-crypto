package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BMBit extends BM {
    @Override
    public String generate(int bitLength) {
        BigInteger t = Util.randomBigInteger(BigInteger.ZERO, p);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitLength; i++) {
            sb.append(t.compareTo(q) < 0 ? "1" : "0");
            t = a.modPow(t, p);
        }
        return sb.toString();

    }
}

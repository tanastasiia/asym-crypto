package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BBSBit extends BBS {

    @Override
    public String generate(int bitLength) {
        BigInteger r = Util.randomBigInteger(BigInteger.valueOf(2), n);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitLength; i++) {
            r = r.modPow(BigInteger.valueOf(2), n);
            sb.append(r.testBit(0) ? "1" : "0");

        }
        return sb.toString();

    }
}

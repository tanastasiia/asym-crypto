package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BBSByte extends BBS {

    private final BigInteger byte256 = BigInteger.valueOf(256);

    @Override
    public String generate(int bitLength) {

        int byteLength = bitLength / 8 + (bitLength % 8 != 0 ? 1 : 0);

        BigInteger r = Util.randomBigInteger(BigInteger.valueOf(2), n);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteLength; i++) {
            r = r.modPow(BigInteger.valueOf(2), n);
            sb.append(Util.makeBinaryString(r.mod(byte256).longValueExact(), 8));
        }

        return sb.substring(0, bitLength);
    }
}

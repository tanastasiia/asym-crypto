package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BMByte extends BM {
    @Override
    public String generate(int bitLength) {

        int byteLength = bitLength / 8 + (bitLength % 8 != 0 ? 1 : 0);

        BigInteger t = Util.randomBigInteger(BigInteger.ZERO, p);
        BigInteger byte256 = BigInteger.valueOf(256);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteLength; i++) {
            long k = t.multiply(byte256).divide(p.subtract(BigInteger.ONE)).longValueExact();
            sb.append(Util.makeBinaryString(k, 8));
            t = a.modPow(t, p);
        }
        return sb.substring(0, bitLength);
    }
}

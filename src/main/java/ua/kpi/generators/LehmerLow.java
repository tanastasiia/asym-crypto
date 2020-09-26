package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class LehmerLow extends Lehmer {

    private final long mask8lowBits = (1L << 8) - 1;

    @Override
    public String generate(int bitLength) {
        int byteLength = bitLength / 8 + (bitLength % 8 != 0 ? 1 : 0);

        long x = Util.random(maskM);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteLength; i++) {
            x = (a * x + c) & maskM;
            sb.append(Util.makeBinaryString(x & 0xFF , 8));
            //sb.append(Util.makeBinaryString((x & mask8lowBits) >> 8 , 8));
        }
        return sb.substring(0, bitLength);

    }
}

package ua.kpi.generators;

import ua.kpi.generators.util.Util;

public class LehmerHigh extends Lehmer {

    private final long mask8highBits = ((1L << 32) - 1) - ((1L << 24) - 1);

    @Override
    public String generate(int bitLength) {

        //Lehmer generator generates bytes, not bits
        int byteLength = bitLength / 8 + (bitLength % 8 != 0 ? 1 : 0);

        long x = Util.random(maskM);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteLength; i++) {
            x = (a * x + c) & maskM;
            sb.append(Util.makeBinaryString((x & mask8highBits) >> 24, 8));
        }
        return sb.substring(0, bitLength);

    }
}

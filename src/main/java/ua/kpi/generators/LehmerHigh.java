package ua.kpi.generators;

import ua.kpi.generators.util.Util;

public class LehmerHigh extends Lehmer implements Generator {

    private final long mask8highBits = ((1L << 32) - 1) - ((1L << 24) - 1);

    @Override
    public String generate(int length) {

        int byteLength = length / 8 + (length % 8 != 0 ? 1 : 0);

        long x = Util.random(maskM);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteLength; i++) {
            x = (a * x + c) & maskM;
            sb.append(Util.makeBinaryString((x & mask8highBits) >> 24, 8));
        }
        return sb.substring(0, length);

    }
}

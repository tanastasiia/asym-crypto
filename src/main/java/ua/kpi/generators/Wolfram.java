package ua.kpi.generators;

import ua.kpi.generators.util.Util;

public class Wolfram implements Generator {

    private final long mask32bit = (1L << 32) - 1;

    @Override
    public String generate(int bitLength) {

        long r = Util.random(mask32bit);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitLength; i++) {
            sb.append(r & 1);
            r = cyclicShift32BitLeft(r) ^ (r | cyclicShift32BitRight(r));
        }
        return sb.toString();

    }

    private long cyclicShift32BitRight(long r) {
        long bit = r & 1;
        return (r >> 1) | (bit << 31);
    }

    private long cyclicShift32BitLeft(long r) {
        long bit = r & (1L << 31);
        return ((r << 1) & mask32bit) | (bit >> 31);
    }
}

package ua.kpi.generators;

import ua.kpi.generators.util.Util;

public class Wolfram implements Generator {

    private final long mask32bit = (1L << 32) - 1;

    @Override
    public int[] generate(int byteLength) {

        long r = Util.random(1, mask32bit);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                bytei += (int) (r & 1) << j;
                r = cyclicShift32BitLeft(r) ^ (r | cyclicShift32BitRight(r));
            }
            bytes[i] = bytei;
        }
        return bytes;

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

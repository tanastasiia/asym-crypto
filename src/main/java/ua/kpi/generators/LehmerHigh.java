package ua.kpi.generators;

import ua.kpi.generators.util.RandomNum;

public class LehmerHigh extends Lehmer implements Generator {

    private final long mask8highBits = (1L << 32 - 1) ^ (1L << 24 - 1);

    @Override
    public String generate(int length) {

        long x = RandomNum.random(maskM);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            x = (a * x + c) & maskM;
            sb.append(Long.toBinaryString(x & mask8highBits));
        }
        return sb.toString();

    }
}

package ua.kpi.generators;

import ua.kpi.generators.util.BBS;
import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class BBSBit extends BBS {

    @Override
    public int[] generate(int byteLength) {
        BigInteger r = Util.randomBigInteger(BigInteger.valueOf(2), n);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                r = r.modPow(BigInteger.valueOf(2), n);
                bytei += (r.testBit(0) ? 1 : 0) << j;
            }
            bytes[i] = bytei;
        }
        return bytes;

    }
}

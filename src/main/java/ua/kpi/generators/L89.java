package ua.kpi.generators;

import ua.kpi.generators.lfsr.LFSR;
import ua.kpi.generators.lfsr.LFSRBigInteger;
import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class L89 implements Generator {

    private final LFSRBigInteger L = new LFSRBigInteger(89, BigInteger.ZERO.setBit(0).setBit(51));

    @Override
    public int[] generate(int byteLength) {

        BigInteger state = Util.randomBigInteger(BigInteger.ONE, BigInteger.ZERO.setBit(L.getN()).subtract(BigInteger.ONE));

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                bytei += L.pop(state) << j;
                state = L.nextState(state);
            }
            bytes[i] = bytei;
        }
        return bytes;
    }
}

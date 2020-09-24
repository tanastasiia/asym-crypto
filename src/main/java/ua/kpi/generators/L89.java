package ua.kpi.generators;

import ua.kpi.generators.lfsr.LFSR;
import ua.kpi.generators.lfsr.LFSRBigInteger;
import ua.kpi.generators.util.Util;

import java.math.BigInteger;

public class L89 implements Generator {

    private final LFSRBigInteger L = new LFSRBigInteger(89, BigInteger.ZERO.setBit(0).setBit(51));

    @Override
    public String generate(int bitLength) {

        BigInteger state = Util.randomBigInteger(BigInteger.ONE, BigInteger.ZERO.setBit(L.getN()).subtract(BigInteger.ONE));

        //generating sequence
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitLength; i++) {
            sb.append(L.pop(state));
            state = L.nextState(state);
        }
        return sb.toString();
    }
}

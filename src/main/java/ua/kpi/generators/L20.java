package ua.kpi.generators;

import ua.kpi.generators.lfsr.impl.LFSRLong;

import java.math.BigInteger;

public class L20 implements Generator {

    private final LFSRLong L = new LFSRLong(20, Long.parseLong("100000000001000101", 1), 1);

    @Override
    public BigInteger generate(int length) {

        return null;
    }
}

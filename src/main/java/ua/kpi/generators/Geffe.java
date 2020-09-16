package ua.kpi.generators;

import ua.kpi.generators.lfsr.LFSR;
import ua.kpi.generators.lfsr.impl.LFSRLong;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Geffe implements Generator {

    private final LFSRLong L1 = new LFSRLong(11, Long.parseLong("101"), 1);
    private final LFSRLong L2 = new LFSRLong(9, Long.parseLong("11011"), 1);
    private final LFSRLong L3 = new LFSRLong(10, Long.parseLong("1001"), 1);

    @Override
    public BigInteger generate(int length) {

        //setting random initial states
        Random random = new Random();
        L1.setState((Math.abs(random.nextLong()) + 1) % L1.getN());
        L2.setState((Math.abs(random.nextLong()) + 1) % L2.getN());
        L3.setState((Math.abs(random.nextLong()) + 1) % L3.getN());

        //generating sequence
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < length; i++) {
            res = ((L3.pop() & L1.shift()) ^ ((L3.shift() + 1) & L2.shift())) == 1 ? res.setBit(i) : res;
        }
        return res;

    }
}

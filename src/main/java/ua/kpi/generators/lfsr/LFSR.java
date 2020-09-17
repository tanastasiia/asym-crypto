package ua.kpi.generators.lfsr;

import java.math.BigInteger;

public class LFSR {

    private final int n;
    private final long a;

    public LFSR(int n, long a) {
        this.n = n;
        this.a = a;
    }

    //generate bit
    public int pop(long state) {
        return (int) (1 & state);
    }

    private int nextBit(long state) {
        return Long.bitCount(state & a) & 1;
    }

    public long nextState(long state) {
        return (state >> 1) + ((long) nextBit(state) << (n - 1));
    }

    public int getN() {
        return n;
    }

}

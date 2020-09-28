package ua.kpi.generators.util;

import java.math.BigInteger;

public class LFSR {

    /**
     * size of the register
     */
    private final int n;
    /**
     * characteristic polynomial coefficients
     */
    private final long a;

    public LFSR(int n, long a) {
        this.n = n;
        this.a = a;
    }

    /**
     * @param state current state
     * @return generated bit
     */
    public int getBit(long state) {
        return (int) (1 & state);
    }

    /**
     * @param state current state
     * @return next state
     */
    public long nextState(long state) {
        return (state >> 1) + ((long) nextBit(state) << (n - 1));
    }

    /**
     * @param state current state
     * @return bit for the next state
     */
    private int nextBit(long state) {
        return Long.bitCount(state & a) & 1;
    }

    public int getN() {
        return n;
    }

}

package ua.kpi.generators.lfsr;

import java.math.BigInteger;

public class LFSRBigInteger {
    /**
     * size of the register
     */
    private final int n;
    /**
     * characteristic polynomial coefficients
     */
    private final BigInteger a;

    public LFSRBigInteger(int n, BigInteger a) {
        this.n = n;
        this.a = a;
    }

    public int pop(BigInteger state) {
        return state.testBit(0) ? 1 : 0;
    }

    public BigInteger nextState(BigInteger state) {
        return state.shiftRight(1).add(nextBit(state) == 1? BigInteger.ZERO.setBit(n-1) : BigInteger.ZERO);
        // return (state >> 1) + ((long) nextBit(state) << (n - 1));
    }

    private int nextBit(BigInteger state) {
        return state.and(a).bitCount() & 1;
        //return Long.bitCount(state & a) & 1;
    }

    public int getN() {
        return n;
    }

}

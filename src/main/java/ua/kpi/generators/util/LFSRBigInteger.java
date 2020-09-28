package ua.kpi.generators.util;

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

    /**
     * @param state current state
     * @return generated bit
     */
    public int getBit(BigInteger state) {
        return state.testBit(0) ? 1 : 0;
    }

    /**
     * @param state current state
     * @return next state
     */
    public BigInteger nextState(BigInteger state) {
        return state.shiftRight(1).add(nextBit(state) == 1 ? BigInteger.ZERO.setBit(n - 1) : BigInteger.ZERO);
    }

    /**
     * @param state current state
     * @return bit for the next state
     */
    private int nextBit(BigInteger state) {
        return state.and(a).bitCount() & 1;
    }

    public int getN() {
        return n;
    }

}

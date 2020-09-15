package ua.kpi.generators.lfsr.impl;


import ua.kpi.generators.lfsr.LFSR;

public class LFSRLong extends LFSR<Long> {

    private long state;

    public LFSRLong(int n, long a, long state) {
        this.n = n;
        this.a = a;
        this.state = state;
    }

    private int nextBit() {
        return Long.bitCount(state & a) & 1;
    }

    public int pop() {
        return (int) (state & 1);
    }

    public int shift() {
        int popped = (int) (state & 1);
        state = (state >> 1) + ((long) nextBit() << (n - 1));
        return popped;
    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }
}

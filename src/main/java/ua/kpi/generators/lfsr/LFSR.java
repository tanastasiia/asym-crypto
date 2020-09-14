package ua.kpi.generators.lfsr;

import java.math.BigInteger;

public abstract class LFSR<T> {
    protected int n;
    protected T a;

    public int getN() {
        return n;
    }

    public T getA() {
        return a;
    }

}

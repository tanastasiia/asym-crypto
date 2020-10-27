package ua.kpi.lab1.generators;

public abstract class Lehmer implements Generator {

    protected final long m = 1L << 32;
    protected final long a = (1L << 16) + 1;
    protected final long c = 119;

    protected final long maskM = (1L << 32) - 1;
}

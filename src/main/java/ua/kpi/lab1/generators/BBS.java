package ua.kpi.lab1.generators;

import java.math.BigInteger;

public abstract class BBS implements Generator {

    protected final BigInteger p = new BigInteger("D5BBB96D30086EC484EBA3D7F9CAEB07", 16);
    protected final BigInteger q = new BigInteger("425D2B9BFDB25B9CF6C416CC6E37B59C1F", 16);
    protected final BigInteger n = p.multiply(q);

}

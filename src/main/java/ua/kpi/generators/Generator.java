package ua.kpi.generators;

import java.math.BigInteger;

public interface Generator {

    /**
     * @param length length of generated sequence in bits
     * @return generated sequence
     */
    String generate(int length);
}

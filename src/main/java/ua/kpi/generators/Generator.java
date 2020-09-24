package ua.kpi.generators;

public interface Generator {

    /**
     * @param bitLength length of generated sequence in bits
     * @return generated sequence
     */
    String generate(int bitLength);
}

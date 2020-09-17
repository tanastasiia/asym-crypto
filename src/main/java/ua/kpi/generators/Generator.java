package ua.kpi.generators;

public interface Generator {

    /**
     * @param length length of generated sequence in bits
     * @return generated sequence
     */
    String generate(int length);
}

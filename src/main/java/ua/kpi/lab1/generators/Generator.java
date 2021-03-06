package ua.kpi.lab1.generators;

public interface Generator {

    /**
     * @param byteLength length of generated sequence in bytes
     * @return generated byte array
     */
    int[] generate(int byteLength);

    default String getName() {
        return this.getClass().getSimpleName();
    }
}

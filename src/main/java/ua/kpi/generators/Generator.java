package ua.kpi.generators;

import java.util.List;

public interface Generator {

    /**
     * @param byteLength length of generated sequence in bytes
     * @return generated sequence
     */
    int[] generate(int byteLength);
}

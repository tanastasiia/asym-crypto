package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;
import java.util.Random;

public class JavaRand implements Generator {
    @Override
    public int[] generate(int byteLength) {

        int byteMask = (1 << 8) - 1;

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            bytes[i] = (int) Util.random(byteMask);
        }
        return bytes;
    }
}

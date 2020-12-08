package ua.kpi.lab1.generators.impl;

import ua.kpi.lab1.generators.Generator;
import ua.kpi.util.Util;

public class JavaRand implements Generator {

    @Override
    public int[] generate(int byteLength) {

        int byteMask = 0b11111111; //(1 << 8) - 1;
        int[] bytes = new int[byteLength];

        for (int i = 0; i < byteLength; i++) {
            bytes[i] = (int) Util.random(0, byteMask);
        }
        return bytes;
    }
}

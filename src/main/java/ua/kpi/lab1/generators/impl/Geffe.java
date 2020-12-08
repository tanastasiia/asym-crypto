package ua.kpi.lab1.generators.impl;

import ua.kpi.lab1.generators.Generator;
import ua.kpi.util.LFSR;
import ua.kpi.util.Util;

public class Geffe implements Generator {

    private final LFSR L1 = new LFSR(11, 0b101L);
    private final LFSR L2 = new LFSR(9, 0b11011L);
    private final LFSR L3 = new LFSR(10, 0b1001L);

    @Override
    public int[] generate(int byteLength) {

        long state1 = Util.random(1, (1L << L1.getN()) - 1);
        long state2 = Util.random(1, (1L << L2.getN()) - 1);
        long state3 = Util.random(1, (1L << L3.getN()) - 1);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                bytei += ((L3.getBit(state3) & L1.getBit(state1)) ^ ((L3.getBit(state3) + 1) & L2.getBit(state2))) << j;
                state1 = L1.nextState(state1);
                state2 = L2.nextState(state2);
                state3 = L3.nextState(state3);
            }
            bytes[i] = bytei;
        }
        return bytes;

    }
}

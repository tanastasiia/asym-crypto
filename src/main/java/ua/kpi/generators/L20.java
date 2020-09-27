package ua.kpi.generators;

import ua.kpi.generators.lfsr.LFSR;
import ua.kpi.generators.util.Util;

public class L20 implements Generator {

    private final LFSR L = new LFSR(20, Long.parseLong("100000000001000101", 2));

    @Override
    public int[] generate(int byteLength) {

        //initial state
        long state = Util.random(1,(1L << L.getN()) - 1);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                bytei += L.pop(state) << j;
                state = L.nextState(state);
            }
            bytes[i] = bytei;
        }
        return bytes;
    }
}

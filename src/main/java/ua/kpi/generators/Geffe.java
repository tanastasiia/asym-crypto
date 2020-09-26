package ua.kpi.generators;

import ua.kpi.generators.lfsr.LFSR;
import ua.kpi.generators.util.Util;

public class Geffe implements Generator {

    private final LFSR L1 = new LFSR(11, Long.parseLong("101"));
    private final LFSR L2 = new LFSR(9, Long.parseLong("11011"));
    private final LFSR L3 = new LFSR(10, Long.parseLong("1001"));

    @Override
    public int[] generate(int byteLength) {

        //setting random initial states
        long state1 = Util.random((1L << L1.getN()) - 1);
        long state2 = Util.random((1L << L2.getN()) - 1);
        long state3 = Util.random((1L << L3.getN()) - 1);

        int[] bytes = new int[byteLength];
        for (int i = 0; i < byteLength; i++) {
            int bytei = 0;
            for (int j = 0; j < 8; j++) {
                bytei += ((L3.pop(state3) & L1.pop(state1)) ^ ((L3.pop(state3) + 1) & L2.pop(state2))) << j;
                state1 = L1.nextState(state1);
                state2 = L2.nextState(state2);
                state3 = L3.nextState(state3);
            }
            bytes[i] = bytei;
        }
        return bytes;

    }
}

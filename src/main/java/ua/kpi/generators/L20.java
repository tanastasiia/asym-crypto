package ua.kpi.generators;

import ua.kpi.generators.lfsr.LFSR;
import ua.kpi.generators.util.Util;

public class L20 implements Generator {

    private final LFSR L = new LFSR(20, Long.parseLong("100000000001000101", 2));

    @Override
    public String generate(int length) {

        StringBuilder sb = new StringBuilder();
        long state = Util.random((1L << L.getN()) - 1);
        for (int i = 0; i < length; i++) {
            sb.append(L.pop(state));
            state = L.nextState(state);
        }
        return sb.toString();
    }
}

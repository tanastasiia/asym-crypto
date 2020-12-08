package ua.kpi.lab2;

import ua.kpi.util.Util;

import java.math.BigInteger;

public class MillerRabinPrimalityTest {

    private final int k = 50;

    public boolean test(BigInteger a) {
        BigInteger TWO = BigInteger.valueOf(2);
        BigInteger THREE = BigInteger.valueOf(3);

        if (a.compareTo(TWO) == 0 | a.compareTo(THREE) == 0) {
            return true;
        }
        if (a.compareTo(TWO) < 0 | a.mod(TWO).equals(BigInteger.ZERO)) {
            return false;
        }

        BigInteger t = a.subtract(BigInteger.ONE);

        int s = 0;

        while (t.mod(TWO).equals(BigInteger.ZERO)) {
            t = t.divide(TWO);
            s += 1;
        }

        for (int i = 0; i < k; i++) {
            BigInteger m = Util.randomBigInteger(TWO, a);
            BigInteger x = m.modPow(t, a);
            if (x.equals(BigInteger.ONE) || x.equals(a.subtract(BigInteger.ONE))) {
                continue;
            }
            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(TWO, a);
                if (x.equals(BigInteger.ONE)) {
                    return false;
                }
                if (x.equals(a.subtract((BigInteger.ONE)))) {
                    break;
                }
            }
            if (r == s) {
                return false;
            }
        }
        return true;
    }
}


package ua.kpi.lab2;

import ua.kpi.lab1.generators.util.Util;

import javax.swing.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class MillerRabinPrimalityTest {
    private boolean True;
    private boolean False;
    private final int k = 50;
    public boolean test(BigInteger a) {
        BigInteger TWO = BigInteger.valueOf(2);
        BigInteger THREE = BigInteger.valueOf(3);
        BigInteger ZERO = BigInteger.valueOf(0);
        BigInteger ONE = BigInteger.valueOf(1);

        if (a.compareTo(TWO) == 0 | a.compareTo(THREE) == 0)
        {
            return True;
        }
        if (a.compareTo(TWO) == -1 | a.mod(TWO) == ZERO)
        {
            return False;
        }

        BigInteger t = a.subtract(ONE);

        int s = 0;

        while (t.mod(TWO) == ZERO)
        {
            t.divide(TWO);
            s += 1;
        }

        for (int i = 0; i < k; i++) {
            BigInteger m = Util.randomBigInteger(TWO,a);
            BigInteger x = m.modPow(t, a);
            if (x.equals(ONE) || x.equals(a.subtract(ONE)))
            {
                continue;
            }
            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(TWO, a);
                if (x.equals(ONE))
                {
                    return False;
                }
                if (x.equals(a.subtract((ONE))))
                {
                    break;
                }
            }
            if (r == s) {
                return False;
            }
            return True;
        }
        return True;
    }
}


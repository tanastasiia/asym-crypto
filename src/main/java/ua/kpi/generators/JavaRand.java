package ua.kpi.generators;

import ua.kpi.generators.util.Util;

import java.math.BigInteger;
import java.util.Random;

public class JavaRand implements Generator {
    @Override
    public String generate(int bitLength) {
        String num = (new BigInteger(bitLength, Util.random)).toString(2);
        int numLen = num.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitLength - numLen; i++) {
            sb.append("0");
        }
        sb.append(num);
        return sb.toString();
    }
}

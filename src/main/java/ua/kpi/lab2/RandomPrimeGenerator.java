package ua.kpi.lab2;

import ua.kpi.lab1.generators.Generator;
import ua.kpi.lab1.generators.impl.L89;

import java.math.BigInteger;

public class RandomPrimeGenerator {

    private final Generator gen = new L89();
    private final MillerRabinPrimalityTest mrTest = new MillerRabinPrimalityTest();

    public RandomPrimeGenerator() {
    }

    public BigInteger generatePrime(int byteLength) {
        BigInteger TWO = BigInteger.valueOf(2);
        int[] generatedSeq = gen.generate(byteLength);
        byte[] bytes = new byte[byteLength];

        for (int i = 0; i < generatedSeq.length; i++) {
            bytes[i] = (byte) generatedSeq[i];
        }

        BigInteger m = new BigInteger(bytes).abs();

        if (m.mod(TWO).equals(BigInteger.ZERO)) {
            m = m.add(BigInteger.ONE);
        }
        while (!mrTest.test(m)) {
            m = m.add(TWO);
        }
        return m;
    }
}
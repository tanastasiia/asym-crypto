package ua.kpi.lab2;

import ua.kpi.lab1.generators.Generator;
import ua.kpi.lab1.generators.impl.L89;

import java.math.BigInteger;

public class RandomPrimeGenerator {

    private Generator gen = new L89();
    private final MillerRabinPrimalityTest mrTest = new MillerRabinPrimalityTest();
    private Object MillerRabinPrimalityTest;

    public RandomPrimeGenerator(Generator gen) {
        this.gen = gen;
    }

    public RandomPrimeGenerator(){}

    public BigInteger generatePrime(int byteLength) {
        BigInteger TWO = BigInteger.valueOf(2);

        int[] generate = gen.generate(byteLength);
        byte[] b = new byte[byteLength];

        for (int i = 0; i < generate.length; i++) {
            b[i] = (byte) generate[i];
        }

        BigInteger m = new BigInteger(b);
        while (!mrTest.test(m)) {
            m = m.add(TWO);
        }
        return m;
    }
}
package ua.kpi.lab2;

import ua.kpi.lab1.generators.Generator;
import ua.kpi.lab1.generators.impl.L89;

import java.math.BigInteger;

public class RandomPrimeGenerator {

    private Generator gen = new L89();
    private final MillerRabinPrimalityTest mrTest = new MillerRabinPrimalityTest();

    public RandomPrimeGenerator(Generator gen) {
        this.gen = gen;
    }

    public RandomPrimeGenerator() {

    }

    public BigInteger generatePrime(int bitLength) {
        throw new UnsupportedOperationException();
    }
}

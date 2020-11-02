package ua.kpi.lab2;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class RandomPrimeGeneratorTest {

    RandomPrimeGenerator primeGen = new RandomPrimeGenerator();

    @Test
    public void generatePrime(){
        BigInteger p = primeGen.generatePrime(32);
        assertTrue(p.isProbablePrime(10));
    }
}

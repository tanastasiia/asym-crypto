package ua.kpi.lab2;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

public class RandomPrimeGeneratorTest {

    RandomPrimeGenerator primeGen = new RandomPrimeGenerator();

    @Test
    public void generatePrime(){
        BigInteger p = primeGen.generatePrime(34);
        System.out.println("len: " + p.bitLength() + "\np = " + p.toString(10));
        assertTrue(p.isProbablePrime(20));
    }
}

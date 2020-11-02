package ua.kpi.lab2;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RandomPrimeGeneratorTest {

    RandomPrimeGenerator primeGen = new RandomPrimeGenerator();

    @Test
    public void generatePrime(){
        BigInteger p = primeGen.generatePrime(32);
        System.out.println("len: " + p.bitLength() + "\np = " + p.toString(10));
        assertTrue(p.isProbablePrime(50));
    }
}

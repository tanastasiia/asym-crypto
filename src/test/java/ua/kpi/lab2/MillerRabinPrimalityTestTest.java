package ua.kpi.lab2;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MillerRabinPrimalityTestTest {

    MillerRabinPrimalityTest mrTest = new MillerRabinPrimalityTest();

    @Test
    public void millerRabinRandomFalse() {
        BigInteger n = new BigInteger("234567876543234567654323456765432345672", 10);
        assertFalse(mrTest.test(n));
    }

    @Test
    public void millerRabinRandomFalse2() {
        BigInteger n = (new BigInteger("8683317618811886495518194401279999999", 10))
                .multiply(new BigInteger("4125636888562548868221559797461449", 10));
        assertFalse(mrTest.test(n));
    }

    @Test
    public void millerRabinPrimeTrue1() {
        BigInteger prime = new BigInteger("7919", 10);
        assertTrue(mrTest.test(prime));
    }

    @Test
    public void millerRabinPrimeTrue2() {
        BigInteger prime = new BigInteger("8683317618811886495518194401279999999", 10);
        assertTrue(mrTest.test(prime));
    }

    @Test
    public void millerRabinPrimeTrue3() {
        BigInteger prime = new BigInteger("4125636888562548868221559797461449", 10);
        assertTrue(mrTest.test(prime));
    }
}

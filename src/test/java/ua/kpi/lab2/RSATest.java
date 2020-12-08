package ua.kpi.lab2;


import org.junit.jupiter.api.BeforeEach;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import ua.kpi.util.SignedMsg;
import ua.kpi.util.Util;

import static org.junit.jupiter.api.Assertions.*;

public class RSATest {

    int primeSizeBytes = 32;

    RSA alice = new RSA(primeSizeBytes);
    RSA bob = new RSA(primeSizeBytes);

    BigInteger m;

    public void generateMessage(int size) {
        m = Util.randomBigInteger(BigInteger.ONE, BigInteger.ONE.shiftLeft(size - 1));
    }

    @BeforeEach
    public void init() {

        alice.generateKeyPair();
        bob.generateKeyPair();

        int size = Math.min(alice.getPublicKeyN().bitLength(), bob.getPublicKeyN().bitLength());
        generateMessage(size);

        System.out.println("alice N = " + alice.getPublicKeyN().toString(16).toUpperCase() + " (" + alice.getPublicKeyN().bitLength() + " bits)");
        System.out.println("alice E = " + alice.getPublicKeyE().toString(16).toUpperCase());
        System.out.println("bob N = " + bob.getPublicKeyN().toString(16).toUpperCase() + " (" + alice.getPublicKeyN().bitLength() + " bits)");
        System.out.println("bob E = " + bob.getPublicKeyE().toString(16).toUpperCase());
        System.out.println("M = " + m.toString(16).toUpperCase() + " (" + m.bitLength() + " bits)");

    }


    @Test
    public void testEncryptDecryptEquals() {
        if (alice.getPublicKeyN().compareTo(bob.getPublicKeyN()) > 0) {
            BigInteger encrypted = bob.encrypt(m, alice);
            assertEquals(m, alice.decrypt(encrypted));
        } else {
            BigInteger encrypted = alice.encrypt(m, bob);
            assertEquals(m, bob.decrypt(encrypted));
        }
    }

    @Test
    public void testVerifyTrue() {

        alice.generateKeyPair();
        bob.generateKeyPair();

        assertTrue(bob.verify(new SignedMsg(m, alice.sign(m)), alice));
    }

    @Test
    public void testVerifyFalseMocked() {
        RSA eve = new RSA(primeSizeBytes);

        alice.generateKeyPair();
        bob.generateKeyPair();
        eve.generateKeyPair();

        assertFalse(bob.verify(new SignedMsg(m, eve.sign(m)), alice));
    }

}

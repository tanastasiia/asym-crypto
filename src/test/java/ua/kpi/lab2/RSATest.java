package ua.kpi.lab2;

import org.junit.Test;
import org.mockito.Mockito;


import java.math.BigInteger;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

public class RSATest {

    RandomPrimeGenerator rpGenAlice = Mockito.mock(RandomPrimeGenerator.class);
    RandomPrimeGenerator rpGenBob = Mockito.mock(RandomPrimeGenerator.class);
    RandomPrimeGenerator rpGenEve = Mockito.mock(RandomPrimeGenerator.class);
    RSA server = Mockito.mock(RSA.class);

    BigInteger serverE = new BigInteger("10001", 16);
    BigInteger serverN = new BigInteger("F8157E34070B863424BF30E57A76F649", 16);

    private final BigInteger prime1 = new BigInteger("8683317618811886495518194401279999999", 10);
    private final BigInteger prime2 = new BigInteger("4125636888562548868221559797461449", 10);
    private final BigInteger prime3 = new BigInteger("1298074214633706835075030044377087", 10);
    private final BigInteger prime4 = new BigInteger("263130836933693530167218012159999999", 10);

    @Test
    public void testPrintMocked() {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);

        RSA alice = new RSA(rpGenAlice, 16);
        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        alice.generateKeyPair();
        System.out.println("E = " + alice.getPublicKeyE().toString(16));
        System.out.println("N = " + alice.getPublicKeyN().toString(16));
        System.out.println("M = " + m.toString(16));
        BigInteger encrypted = alice.encrypt(m, alice);
        System.out.println("encrypted: " + encrypted.toString(16));
        System.out.println("decrypted: " + alice.decrypt(encrypted).toString(16));
        System.out.println("sign: " + alice.getSignedMessage(m).toString(16));
    }

    @Test
    public void testEncryptDecryptEqualsMocked() {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(rpGenBob.generatePrime(anyInt())).thenReturn(prime3, prime4);

        RSA alice = new RSA(rpGenAlice, 16);
        RSA bob = new RSA(rpGenBob, 16);
        alice.generateKeyPair();
        bob.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        BigInteger encrypted = alice.encrypt(m, bob);
        assertEquals(m, bob.decrypt(encrypted));
    }

    @Test
    public void testVerifyTrueMocked() {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(rpGenBob.generatePrime(anyInt())).thenReturn(prime3, prime4);

        RSA alice = new RSA(rpGenAlice, 16);
        RSA bob = new RSA(rpGenBob, 16);
        alice.generateKeyPair();
        bob.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        assertTrue(bob.verify(alice.sign(m), alice));
    }

    @Test
    public void testVerifyFalseMocked() {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(rpGenBob.generatePrime(anyInt())).thenReturn(prime3, prime4);
        when(rpGenEve.generatePrime(anyInt())).thenReturn(prime1, prime3);

        RSA alice = new RSA(rpGenAlice, 16);
        RSA bob = new RSA(rpGenBob, 16);
        RSA eve = new RSA(rpGenEve, 16);
        alice.generateKeyPair();
        bob.generateKeyPair();
        eve.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        assertFalse(bob.verify(eve.sign(m), alice));
    }

    @Test
    public void testServerSignVerification() {
        when(server.getPublicKeyN()).thenReturn(serverN);
        when(server.getPublicKeyE()).thenReturn(serverE);
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        RSA alice = new RSA(rpGenAlice, 16);
        alice.generateKeyPair();
        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        BigInteger serverSign = new BigInteger("D359B6A0AF04090EB6D875924DE8268B", 16);
        assertTrue(alice.verify(new RSA.SignedMsg(m, serverSign), server));
    }

    @Test
    public void testSendReceiveMocked() throws RSA.VerificationException {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(rpGenBob.generatePrime(anyInt())).thenReturn(prime3, prime4);

        RSA alice = new RSA(rpGenAlice, 16);
        RSA bob = new RSA(rpGenBob, 16);
        alice.generateKeyPair();
        bob.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        RSA.SignedMsg sent = bob.sendKey(m, alice);

        System.out.println(sent);
        System.out.println(alice.receiveKey(sent, bob));
    }

    @Test(expected = RSA.VerificationException.class)
    public void testSendReceiveVerExcMocked() throws RSA.VerificationException {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(rpGenBob.generatePrime(anyInt())).thenReturn(prime3, prime4);
        when(rpGenEve.generatePrime(anyInt())).thenReturn(prime1, prime3);

        RSA alice = new RSA(rpGenAlice, 16);
        RSA bob = new RSA(rpGenBob, 16);
        RSA eve = new RSA(rpGenEve, 16);
        alice.generateKeyPair();
        bob.generateKeyPair();
        eve.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        RSA.SignedMsg sent = eve.sendKey(m, alice);
        alice.receiveKey(sent, bob);
    }

    @Test
    public void testSendServerMocked() throws RSA.VerificationException {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(server.getPublicKeyN()).thenReturn(serverN);
        when(server.getPublicKeyE()).thenReturn(serverE);

        RSA alice = new RSA(rpGenAlice, 16);
        alice.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        RSA.SignedMsg sm = new RSA.SignedMsg(
                new BigInteger("03A26306F93CD650E19EA02FA539346B837700CF49BA0C5E19DEF6D32F4E", 16),
                new BigInteger("01780C448CEF02AB9FECB1AB5C301D771726A906E0DEF585F38435DA71BD", 16));
        System.out.println(alice.receiveKey(sm, server).toString(16));
    }

    //msg correct but verification fails
    @Test
    public void testReceiveServerMocked() {
        when(rpGenAlice.generatePrime(anyInt())).thenReturn(prime1, prime2);
        when(server.getPublicKeyN()).thenReturn(serverN);
        when(server.getPublicKeyE()).thenReturn(serverE);

        RSA alice = new RSA(rpGenAlice, 16);
        alice.generateKeyPair();

        BigInteger m = new BigInteger("b90a9afcde6536209aafba2edf8", 16);
        System.out.println("M = " + m.toString(16));
        System.out.println("N = " + alice.getPublicKeyN().toString(16));
        System.out.println("E = " + alice.getPublicKeyE().toString(16));
        RSA.SignedMsg sent = alice.sendKey(m, server);
        System.out.println(sent);
    }

}

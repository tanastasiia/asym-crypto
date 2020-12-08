package ua.kpi.lab2;

import ua.kpi.util.SignedMsg;

import java.math.BigInteger;

public class RSA {

    private BigInteger publicKeyN;
    private BigInteger publicKeyE;

    private BigInteger privateKeyD;
    private BigInteger p;
    private BigInteger q;

    private RandomPrimeGenerator randPrimeGen = new RandomPrimeGenerator();
    private int primeLengthBytes = 32;
    private final BigInteger e = BigInteger.valueOf((1L << 16) + 1);

    public RSA() {
    }

    public RSA(int primeLengthBytes) {
        this.primeLengthBytes = primeLengthBytes;
    }

    public RSA(RandomPrimeGenerator randPrimeGen, int primeLengthBytes) {
        this.randPrimeGen = randPrimeGen;
        this.primeLengthBytes = primeLengthBytes;
    }

    public BigInteger getPublicKeyN() {
        return publicKeyN;
    }

    public BigInteger getPublicKeyE() {
        return publicKeyE;
    }

    public void generateKeyPair() {
        p = randPrimeGen.generatePrime(primeLengthBytes);
        q = randPrimeGen.generatePrime(primeLengthBytes);

        BigInteger n = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        publicKeyE = e;
        publicKeyN = n;
        privateKeyD = e.modInverse(phiN);
    }

    public BigInteger encrypt(BigInteger m, RSA receiver) {
        return m.modPow(receiver.getPublicKeyE(), receiver.getPublicKeyN());
    }

    public BigInteger decrypt(BigInteger c) {
        return c.modPow(privateKeyD, publicKeyN);
    }

    public BigInteger sign(BigInteger m) {
        return m.modPow(privateKeyD, publicKeyN);
    }

    public boolean verify(SignedMsg ms, RSA sender) {
        return ms.getM().equals(ms.getS().modPow(sender.getPublicKeyE(), sender.getPublicKeyN()));
    }

    public SignedMsg sendKey(BigInteger k, RSA receiver) {
        BigInteger k1 = encrypt(k, receiver);
        BigInteger s = sign(k);
        BigInteger s1 = encrypt(s, receiver);
        return new SignedMsg(k1, s1);
    }

    public BigInteger receiveKey(SignedMsg sm, RSA sender) throws SignedMsg.VerificationException {
        BigInteger k = decrypt(sm.getM());
        BigInteger s = decrypt(sm.getS());
        if (verify(new SignedMsg(k, s), sender)) {
            return k;
        }
        throw new SignedMsg.VerificationException(k);
    }

}


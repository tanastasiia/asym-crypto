package ua.kpi.lab2;

import java.math.BigInteger;

public class RSA {
    /*
    GenerateKeyPair(), Encrypt(), Decrypt(), Sign(), Verify(), SendKey(), ReceiveKey().
    */
    private BigInteger publicKeyN;
    private BigInteger publicKeyE;

    private BigInteger privateKeyD;
    private BigInteger p;
    private BigInteger q;

    private RandomPrimeGenerator randPrimeGen = new RandomPrimeGenerator();
    private int primeLengthBytes = 32;
    private final BigInteger e = BigInteger.valueOf((1L << 16) + 1);

    public RSA(int primeLengthBytes) {
        this.primeLengthBytes = primeLengthBytes;
    }

    public RSA() {
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

    public static class SignedMsg {
        private final BigInteger m;
        private final BigInteger s;

        public SignedMsg(BigInteger m, BigInteger s) {
            this.m = m;
            this.s = s;
        }

        public BigInteger getM() {
            return m;
        }

        public BigInteger getS() {
            return s;
        }

        @Override
        public String toString() {
            return "M = " + m.toString(16) + "\nS = " + s.toString(16);
        }
    }

    public static class VerificationException extends Exception {
        public VerificationException(BigInteger m) {
            super("message not authenticated: M = " + m.toString(16));
        }
    }

    public void generateKeyPair() {
        p = randPrimeGen.generatePrime(primeLengthBytes);
        q = randPrimeGen.generatePrime(primeLengthBytes);

        if(!p.isProbablePrime(20)){
            throw new RuntimeException("not prime p " + p.toString(16));
        }
        if(!q.isProbablePrime(20)){
            throw new RuntimeException("not prime q " + q.toString(16));
        }

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

    public SignedMsg sign(BigInteger m) {
        return new SignedMsg(m, m.modPow(privateKeyD, publicKeyN));
    }

    public BigInteger getSignedMessage(BigInteger m) {
        return m.modPow(privateKeyD, publicKeyN);
    }

    public boolean verify(SignedMsg ms, RSA sender) {
        return ms.getM().equals(ms.getS().modPow(sender.getPublicKeyE(), sender.getPublicKeyN()));
    }

    public SignedMsg sendKey(BigInteger k, RSA receiver) {
        BigInteger k1 = encrypt(k, receiver);
        BigInteger s = getSignedMessage(k);
        BigInteger s1 = encrypt(s, receiver);
        return new SignedMsg(k1, s1);
    }

    public BigInteger receiveKey(SignedMsg sm, RSA sender) throws VerificationException {
        BigInteger k = decrypt(sm.getM());
        BigInteger s = decrypt(sm.getS());
        if (verify(new SignedMsg(k, s), sender)) {
            return k;
        }
        throw new VerificationException(k);
    }

}


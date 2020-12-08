package ua.kpi.lab3;

import ua.kpi.util.SignedMsg;
import ua.kpi.util.Util;
import ua.kpi.lab2.RSA;
import ua.kpi.lab2.RandomPrimeGenerator;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class Rabin {
    private BigInteger publicKeyN;
    private BigInteger p;
    private BigInteger q;
    private BigInteger publicKeyB;

    private RandomPrimeGenerator randPrimeGen = new RandomPrimeGenerator();
    private int primeLengthBytes = 32;

    private static final BigInteger TWO = BigInteger.valueOf(2);

    public Rabin() {
    }

    public Rabin(int primeLengthBytes) {
        this.primeLengthBytes = primeLengthBytes;
    }

    public Rabin(RandomPrimeGenerator randPrimeGen, int primeLengthBytes) {
        this.randPrimeGen = randPrimeGen;
        this.primeLengthBytes = primeLengthBytes;
    }

    public BigInteger getPublicKeyN() {
        return publicKeyN;
    }

    public BigInteger getPublicKeyB() {
        return publicKeyB;
    }

    public static class CypherText {
        private final BigInteger y;
        private final int c1;
        private final int c2;

        public CypherText(BigInteger y, int c1, int c2) {
            this.y = y;
            this.c1 = c1;
            this.c2 = c2;
        }

        public BigInteger getY() {
            return y;
        }

        public int getC1() {
            return c1;
        }

        public int getC2() {
            return c2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CypherText that = (CypherText) o;
            return c1 == that.c1 &&
                    c2 == that.c2 &&
                    Objects.equals(y, that.y);
        }

        @Override
        public int hashCode() {
            return Objects.hash(y, c1, c2);
        }
    }

    public void generateKeyPair() {

        p = randPrimeGen.generateBlumPrime(primeLengthBytes);
        q = randPrimeGen.generateBlumPrime(primeLengthBytes);

        publicKeyN = p.multiply(q);
        publicKeyB = Util.randomBigInteger(BigInteger.ONE, publicKeyN);
    }

    public BigInteger formatMessage(BigInteger m, BigInteger n) {
        int l = Util.byteLength(n);
        assert(Util.byteLength(m) < l - 10);
        BigInteger r = randPrimeGen.generatePrime(8);
        return r.add(m.shiftLeft(64)).add(BigInteger.valueOf(255).shiftLeft(8 * (l - 2)));
    }

    public BigInteger deFormatMessage(BigInteger x) {
        int l = Util.byteLength(publicKeyN);
        return x.xor(BigInteger.valueOf(255).shiftLeft(8 * (l - 2))).shiftRight(64);
    }

    public CypherText encrypt(BigInteger m, Rabin receiver) {
        BigInteger x = formatMessage(m, receiver.getPublicKeyN());
        BigInteger y = x.multiply(x.add(receiver.getPublicKeyB())).mod(receiver.getPublicKeyN());
        BigInteger xWithB = x.add(receiver.getPublicKeyB().multiply(TWO.modInverse(receiver.getPublicKeyN()))).mod(receiver.getPublicKeyN());

        int c1 = xWithB.testBit(0) ? 1 : 0;
        int c2 = Util.jacobi(xWithB,  receiver.getPublicKeyN()) == 1 ? 1 : 0;

        return new CypherText(y, c1, c2);
    }

    public BigInteger decrypt(CypherText cypherText) {

        BigInteger[] roots = Util
                .compositeBlumSquareRoot(cypherText.getY()
                        .add(publicKeyB.pow(2)
                                .multiply(BigInteger.valueOf(4).modInverse(publicKeyN))), p, q);

        BigInteger root = (roots[0].testBit(0) ? 1 : 0) == cypherText.getC1() && (Util.jacobi(roots[0], publicKeyN) == 1 ? 1 : 0) == cypherText.getC2() ? roots[0]
                : (roots[1].testBit(0) ? 1 : 0) == cypherText.getC1() && (Util.jacobi(roots[1], publicKeyN) == 1 ? 1 : 0) == cypherText.getC2() ? roots[1]
                : (roots[2].testBit(0) ? 1 : 0) == cypherText.getC1() && (Util.jacobi(roots[2], publicKeyN) == 1 ? 1 : 0) == cypherText.getC2() ? roots[2]
                : roots[3];

        return deFormatMessage(root.subtract(publicKeyB.multiply(TWO.modInverse(publicKeyN))).mod(publicKeyN));
    }

    public BigInteger sign(BigInteger m) {
        BigInteger x;
        do {
            x = formatMessage(m, publicKeyN);
        } while (Util.jacobi(x, p) != 1 || Util.jacobi(x, q) != 1);
        return Util.compositeBlumSquareRoot(x, p, q)[0];
    }

    public boolean verify(SignedMsg ms, Rabin sender) {
        return ms.getS().modPow(TWO, sender.getPublicKeyN()).shiftRight(64)
                .equals(formatMessage(ms.getM(), publicKeyN).shiftRight(64));
    }

}

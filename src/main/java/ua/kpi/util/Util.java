package ua.kpi.util;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class Util {

    public final static Random random = new Random();

    public static long random(long min, long mask) {
        while (true) {
            long num = random.nextLong() & mask;
            if (num >= min) {
                return num;
            }
        }
    }

    public static BigInteger randomBigInteger(BigInteger min, BigInteger max) {
        while (true) {
            BigInteger num = new BigInteger(max.bitLength(), random).mod(max);
            if (num.compareTo(min) >= 0) {
                return num;
            }
        }
    }

    private static final String alphabet_ru = " абвгдежзийклмнопрстуфхцчшщъыьэюя";
    private static final String alphabet_eng = " abcdefghijklmnopqrstuvwxyz";

    public static void clean(String inputFile, String outputFile, boolean isRu) throws IOException {
        String str = String.join(" ", Files.readAllLines(Paths.get(inputFile)))
                .toLowerCase()
                .replaceAll("ё", "е")
                .replaceAll("[^" + (isRu ? alphabet_ru : alphabet_eng) + "]", "")
                .replaceAll("[ ]{2,}", " ")
                .trim();
        Files.write(Paths.get(outputFile), str.getBytes());

    }

    public static BigInteger intArrayToBigInteger(int[] arr) {
        byte[] bytes = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            bytes[i] = (byte) arr[i];
        }
        return new BigInteger(bytes).abs();
    }

    private static int jacobi(BigInteger a, BigInteger n, int multiplier) {

        BigInteger f;
        BigInteger one = BigInteger.ONE;

        if (a.signum() == -1) {
            return jacobi(a.negate(), n,
                    multiplier * (n
                            .subtract(one)
                            .shiftRight(1)
                            .testBit(0) ? -1 : 1));
        }
        if (!a.testBit(0)) {
            f = n.pow(2).subtract(one).shiftRight(1);
            return jacobi(a.shiftRight(1), n,
                    multiplier * ((f.testBit(0) || f.testBit(1) || f.testBit(2)) ? -1 : 1));
        }
        if (a.compareTo(one) == 0) return multiplier;
        if (a.compareTo(n) < 0) {
            return jacobi(n, a,
                    multiplier
                            * (n
                            .subtract(one)
                            .shiftRight(1)
                            .multiply(a
                                    .subtract(one)
                                    .shiftRight(1))
                            .testBit(0) ? -1 : 1));
        }
        return jacobi(a.mod(n), n, multiplier);
    }

    public static int jacobi(BigInteger a, BigInteger n) {
        return jacobi(a, n, 1);
    }

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger ONE = BigInteger.ONE;
    private static final BigInteger TEN = BigInteger.TEN;
    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final BigInteger THREE = BigInteger.valueOf(3);
    private static final BigInteger FOUR = BigInteger.valueOf(4);

    public static class Solution {
        private final BigInteger root1;
        private final BigInteger root2;
        private final boolean exists;

        Solution(BigInteger root1, BigInteger root2, boolean exists) {
            this.root1 = root1;
            this.root2 = root2;
            this.exists = exists;
        }

        public BigInteger getRoot1() {
            return root1;
        }

        public BigInteger getRoot2() {
            return root2;
        }

        public boolean isExists() {
            return exists;
        }
    }

    public static BigInteger chineseRemainder(BigInteger[] x, BigInteger[] m) {

        BigInteger M = Arrays.stream(m).reduce(ONE, BigInteger::multiply);

        BigInteger Mi, x0 = ZERO;
        for (int i = 0; i < m.length; i++) {
            Mi = M.divide(m[i]);
            x0 = x0.add(x[i].multiply(Mi).multiply(Mi.modInverse(m[i])));
        }

        return x0.mod(M);
    }

    public static Solution blumSquareRoot(BigInteger x, BigInteger p) {
        BigInteger k = p.subtract(THREE).divide(FOUR);
        if (x.modPow(p.subtract(ONE).divide(TWO), p).equals(ONE)) {
            BigInteger root1 = x.modPow(k.add(ONE), p);
            return new Solution(root1, p.subtract(root1).mod(p), true);
        }
        return new Solution(ZERO, ZERO, false);
    }

    public static BigInteger[] compositeBlumSquareRoot(BigInteger x, BigInteger p, BigInteger q) {

        BigInteger xp = x.mod(p);
        BigInteger xq = x.mod(q);

        Solution yp = blumSquareRoot(xp, p);
        assert(yp.getRoot1().modPow(TWO, p).equals(xp));
        assert(yp.getRoot2().modPow(TWO, p).equals(xp));
        Solution yq = blumSquareRoot(xq, q);
        assert(yq.getRoot1().modPow(TWO, q).equals(xq));
        assert(yq.getRoot2().modPow(TWO, q).equals(xq));

        BigInteger root1 = chineseRemainder(new BigInteger[]{yp.root1, yq.root1}, new BigInteger[]{p, q});
        BigInteger root2 = chineseRemainder(new BigInteger[]{yp.root1, yq.root2}, new BigInteger[]{p, q});
        BigInteger root3 = chineseRemainder(new BigInteger[]{yp.root2, yq.root1}, new BigInteger[]{p, q});
        BigInteger root4 = chineseRemainder(new BigInteger[]{yp.root2, yq.root2}, new BigInteger[]{p, q});

        assert(root1.mod(p).equals(yp.root1.mod(p)));
        assert(root1.mod(q).equals(yq.root1.mod(q)));

        BigInteger n = q.multiply(p);

        assert(x.mod(n).equals(root1.modPow(TWO, n)));
        assert(x.mod(n).equals(root2.modPow(TWO, n)));
        assert(x.mod(n).equals(root3.modPow(TWO, n)));
        assert(x.mod(n).equals(root4.modPow(TWO, n)));

        return new BigInteger[]{root1, root2, root3, root4};

    }

    public static BigInteger[] fastCompositeBlumSquareRoot(BigInteger x, BigInteger p, BigInteger q) {
        BigInteger s1 = x.modPow(p.add(BigInteger.ONE).multiply(FOUR.modInverse(p)), p);
        BigInteger s2 = x.modPow(q.add(BigInteger.ONE).multiply(FOUR.modInverse(q)), q);
    return null;
    }

    public static int byteLength(BigInteger x) {
        return (int) Math.ceil(x.bitLength() / 8.);
    }


}

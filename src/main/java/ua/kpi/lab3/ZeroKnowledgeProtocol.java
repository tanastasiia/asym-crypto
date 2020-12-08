package ua.kpi.lab3;

import ua.kpi.util.Util;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;

public class ZeroKnowledgeProtocol {

    private BigInteger zeroKnowledgeProtocolX;
    private BigInteger zeroKnowledgeProtocolT;
    private final BigInteger TWO = BigInteger.valueOf(2);

    /*
     * @param prover   Bob
     * @param verifier Alice
     */
    public BigInteger zeroKnowledgeProtocolSendY(BigInteger n, int bitLength) {
        zeroKnowledgeProtocolX = new BigInteger(bitLength, new Random());
        return zeroKnowledgeProtocolX.modPow(BigInteger.valueOf(4), n);
    }

    public BigInteger zeroKnowledgeProtocolReceiveYSendZ(BigInteger y, BigInteger n, BigInteger p, BigInteger q) {
        BigInteger[] roots = Util.compositeBlumSquareRoot(y, p, q);
        return Util.jacobi(roots[0], n) == 1 ? roots[0]
                : Util.jacobi(roots[1], n) == 1 ? roots[1]
                : Util.jacobi(roots[2], n) == 1 ? roots[2] : roots[3];
    }

    public boolean zeroKnowledgeProtocolReceiveZVerify(BigInteger z, BigInteger n) {
        return z.equals(zeroKnowledgeProtocolX.modPow(TWO, n));
    }

    public BigInteger zeroKnowledgeProtocolAttackSendT(BigInteger n, int bitLength) {
        zeroKnowledgeProtocolT = new BigInteger(bitLength, new Random());
        return zeroKnowledgeProtocolT.modPow(TWO, n);
    }

    public Optional<BigInteger> zeroKnowledgeProtocolAttackReceiveZVerify(BigInteger z, BigInteger n) {
        if (zeroKnowledgeProtocolT.equals(z) || zeroKnowledgeProtocolT.equals(z.negate())) {
            return Optional.empty();
        }
        BigInteger proversP = n.gcd(zeroKnowledgeProtocolT.add(z));
        System.out.println(" n mod p == 0 : " + n.mod(proversP).equals(BigInteger.ZERO));
        return Optional.of(proversP);
    }

}

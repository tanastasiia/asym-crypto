package ua.kpi.util;

import java.math.BigInteger;

public class SignedMsg {

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


    public static class VerificationException extends Exception {
        public VerificationException(BigInteger m) {
            super("message not authenticated: M = " + m.toString(16));
        }
    }

}

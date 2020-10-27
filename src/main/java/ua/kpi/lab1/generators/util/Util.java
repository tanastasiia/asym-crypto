package ua.kpi.lab1.generators.util;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
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
                .replaceAll("[^" + (isRu? alphabet_ru:alphabet_eng) + "]", "")
                .replaceAll("[ ]{2,}", " ")
                .trim();
        Files.write(Paths.get(outputFile), str.getBytes());

    }
}

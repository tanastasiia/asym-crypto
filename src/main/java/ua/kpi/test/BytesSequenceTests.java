package ua.kpi.test;

import ua.kpi.generators.*;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BytesSequenceTests {

    private int r = 17;

    BiFunction<Double, Double, Double> hiSquaredAlpha = (z, l) -> Math.sqrt(2 * l) * z + l;
    Function<Double, Double> hiSquared1Alpha = (z) -> hiSquaredAlpha.apply(z, 255d);
    Function<Double, Double> hiSquared2Alpha = (z) -> hiSquaredAlpha.apply(z, 255d * 255d);
    Function<Double, Double> hiSquared3Alpha = (z) -> hiSquaredAlpha.apply(z, 255d * (r - 1));

    public double testUniformDistributionHiSquared(int[] bytes) {
        double n = bytes.length / 256d;

        return Arrays.stream(bytes).parallel().boxed()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                .values().parallelStream()
                .map(x -> Math.pow(x - n, 2) / n)
                .reduce(Double::sum).orElse(0d);
    }

    public double testIndependenceHiSquared(int[] bytes) {

        int n = bytes.length / 2;

        int[][] pairsFreq = new int[256][256];
        int[] firstFreq = new int[256];
        int[] secondFreq = new int[256];

        for (int i = 1; i < n; i++) {
            pairsFreq[bytes[2 * i - 1]][bytes[2 * i]]++;
            firstFreq[bytes[2 * i - 1]]++;
            secondFreq[bytes[2 * i]]++;
        }

        double hiSquared = 0;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                if (firstFreq[i] != 0 && secondFreq[j] != 0 && pairsFreq[i][j] != 0) {
                    hiSquared += Math.pow(pairsFreq[i][j], 2) / (firstFreq[i] * secondFreq[j]);
                }
            }
        }
        return n * (hiSquared - 1);
    }

    public double testUniformityHiSquared(int[] bytes) {
        //int r = 1 + (int) (Math.log(bytes.length) / Math.log(2));
        double l = 255 * (r - 1);

        int m2 = bytes.length / r; // # of intervals
        int n = m2 * r; // # bytes to use

        int[][] intervalByteFreq = new int[256][m2];
        int[] bytesFreq = new int[256];

        for (int i = 0; i < n; i++) {
            intervalByteFreq[bytes[i]][i / m2]++;
            bytesFreq[bytes[i]]++;
        }

        double hiSquared = 0;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < r; j++) {
                if (bytesFreq[i] != 0 && intervalByteFreq[i][j] != 0) {
                    hiSquared += Math.pow(intervalByteFreq[i][j], 2) / (m2 * bytesFreq[i]);
                }
            }
        }
        return n * (hiSquared - 1);
    }

    public boolean testUniformDistribution(int[] bytes, double alpha_quantile) {
        double hiSquared = testUniformDistributionHiSquared(bytes);
        double hiSquaredH0 = hiSquared1Alpha.apply(alpha_quantile);
        System.out.println("hiSquared:  " + String.format("%.2f", hiSquared) + "  " + String.format("%.2f", hiSquaredH0));
        return hiSquared <= hiSquaredH0;
    }

    public boolean testIndependence(int[] bytes, double alpha_quantile) {
        double hiSquared = testIndependenceHiSquared(bytes);
        double hiSquaredH0 = hiSquared2Alpha.apply(alpha_quantile);
        System.out.println("hiSquared:  " + String.format("%.2f", hiSquared) + "  " + String.format("%.2f", hiSquaredH0));
        return hiSquared <= hiSquaredH0;
    }

    public boolean testUniformity(int[] bytes, double alpha_quantile) {
        double hiSquared = testUniformityHiSquared(bytes);
        double hiSquaredH0 = hiSquared3Alpha.apply(alpha_quantile);
        System.out.println("hiSquared:  " + String.format("%.2f", hiSquared) + "  " + String.format("%.2f", hiSquaredH0));
        return hiSquared <= hiSquaredH0;
    }
}

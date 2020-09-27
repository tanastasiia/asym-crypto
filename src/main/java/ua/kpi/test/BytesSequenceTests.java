package ua.kpi.test;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BytesSequenceTests {

    private final int r = 42;
    private final BiFunction<Double, Double, Double> chiSquaredAlpha = (z, l) -> Math.sqrt(2 * l) * z + l;

    public Function<Double, Double> chiSquared1Alpha = (z) -> chiSquaredAlpha.apply(z, 255d);
    public Function<Double, Double> chiSquared2Alpha = (z) -> chiSquaredAlpha.apply(z, 255d * 255d);
    public Function<Double, Double> chiSquared3Alpha = (z) -> chiSquaredAlpha.apply(z, 255d * (r - 1));

    public double testFrequencyChiSquared(int[] bytes) {

        double n = bytes.length / 256d;

        return Arrays.stream(bytes).parallel().boxed()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                .values().parallelStream()
                .map(x -> Math.pow(x - n, 2) / n)
                .reduce(Double::sum).orElse(0d);
    }

    public double testSerialChiSquared(int[] bytes) {

        int n = bytes.length / 2;

        int[][] pairsFreq = new int[256][256];
        int[] firstFreq = new int[256];
        int[] secondFreq = new int[256];

        for (int i = 1; i < n; i++) {
            pairsFreq[bytes[2 * i - 1]][bytes[2 * i]]++;
            firstFreq[bytes[2 * i - 1]]++;
            secondFreq[bytes[2 * i]]++;
        }

        double chiSquared = 0;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                if (firstFreq[i] != 0 && secondFreq[j] != 0 && pairsFreq[i][j] != 0) {
                    chiSquared += Math.pow(pairsFreq[i][j], 2) / (firstFreq[i] * secondFreq[j]);
                }
            }
        }
        return n * (chiSquared - 1);
    }

    public double testGapChiSquared(int[] bytes) {

        int numOfIntervals = bytes.length / r;
        int n = numOfIntervals * r;

        int[][] intervalByteFreq = new int[256][numOfIntervals];
        int[] bytesFreq = new int[256];

        for (int i = 0; i < n; i++) {
            intervalByteFreq[bytes[i]][i / numOfIntervals]++;
            bytesFreq[bytes[i]]++;
        }

        double chiSquared = 0;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < r; j++) {
                if (bytesFreq[i] != 0 && intervalByteFreq[i][j] != 0) {
                    chiSquared += Math.pow(intervalByteFreq[i][j], 2) / (numOfIntervals * bytesFreq[i]);
                }
            }
        }
        return n * (chiSquared - 1);
    }

    public boolean testFrequency(int[] bytes, double alpha_quantile) {
        double chiSquared = testFrequencyChiSquared(bytes);
        double chiSquaredH0 = chiSquared1Alpha.apply(alpha_quantile);
        System.out.println("chiSquared:  " + String.format("%.2f", chiSquared) + "  " + String.format("%.2f", chiSquaredH0));
        return chiSquared <= chiSquaredH0;
    }

    public boolean testSerial(int[] bytes, double alpha_quantile) {
        double chiSquared = testSerialChiSquared(bytes);
        double chiSquaredH0 = chiSquared2Alpha.apply(alpha_quantile);
        System.out.println("chiSquared:  " + String.format("%.2f", chiSquared) + "  " + String.format("%.2f", chiSquaredH0));
        return chiSquared <= chiSquaredH0;
    }

    public boolean testGap(int[] bytes, double alpha_quantile) {
        double chiSquared = testGapChiSquared(bytes);
        double chiSquaredH0 = chiSquared3Alpha.apply(alpha_quantile);
        System.out.println("chiSquared:  " + String.format("%.2f", chiSquared) + "  " + String.format("%.2f", chiSquaredH0));
        return chiSquared <= chiSquaredH0;
    }
}

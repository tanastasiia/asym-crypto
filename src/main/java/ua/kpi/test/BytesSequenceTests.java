package ua.kpi.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BytesSequenceTests {

    BiFunction<Double, Double, Double> hiSquaredAlpha = (z, l) -> Math.sqrt(2 * l) * z + l;

    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    public boolean testUniformDistribution(int[] bytes, double alpha_quantile) {

        double n = bytes.length / 256d;
        double l = 255d;

        double hiSquared = Arrays.stream(bytes).parallel().boxed()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                .values().parallelStream()
                .map(x -> Math.pow(x - n, 2) / n)
                .reduce(Double::sum).orElse(0d);

        System.out.println("hiSquared:  " + String.format("%.2f", hiSquared) + "  " + String.format("%.2f", hiSquaredAlpha.apply(alpha_quantile, l)));
        return hiSquared <= hiSquaredAlpha.apply(alpha_quantile, l);
    }

    public boolean testIndependence(int[] bytes, double alpha_quantile) {

        double l = 255 * 255;
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
        hiSquared = n * (hiSquared - 1);

        System.out.println("hiSquared:  " + String.format("%.2f", hiSquared) + "  " + String.format("%.2f", hiSquaredAlpha.apply(alpha_quantile, l)));
        return hiSquared <= hiSquaredAlpha.apply(alpha_quantile, l);
    }

    public boolean testUniformity(int[] bytes) {
        //TODO
        throw new UnsupportedOperationException();
    }

    public void perform() {
        double[] alphas = {0.01, 0.05, 0.1};
        double[] alpha_quantiles = {2.3263478740408416, 1.6448536269514724, 1.2815515655446008};
        //TODO
    }
}

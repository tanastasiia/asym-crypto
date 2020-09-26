package ua.kpi.test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BytesSequenceTests {


    public boolean testUniformDistribution(int[] bytes, double alpha_quantile) {

        double n = bytes.length / 256d;
        Function<Double, Double> hiSquaredAlpha = z -> Math.sqrt(2 * 255) * z + 255;

        double hiSquared = Arrays.stream(bytes).boxed().parallel()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                .values().parallelStream()
                .map(x -> Math.pow(x - n, 2) / n)
                .reduce(Double::sum).orElse(0d);

        System.out.println("hiSquared:  " + Math.ceil(hiSquared) + "  " + Math.round(hiSquaredAlpha.apply(alpha_quantile)));

        return hiSquared <= hiSquaredAlpha.apply(alpha_quantile);
    }

    public boolean testIndependence(int[] bytes) {
        //TODO
        throw new UnsupportedOperationException();
    }

    public boolean testUniformity(int[] bytes) {
        //TODO
        throw new UnsupportedOperationException();
    }

    public void perform() {
        double[] alphas = {0.01, 0.05, 0.1};
        double[] alpha_quantiles = {2.3263478740408416, 1.6448536269514724, 1.2815515655446008};
    }
}

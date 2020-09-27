package ua.kpi;

import org.junit.Test;
import ua.kpi.generators.*;
import ua.kpi.test.BytesSequenceTests;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class BytesSequenceTestsTest {

    private final BytesSequenceTests bytesSequenceTests = new BytesSequenceTests();

    private final List<Generator> gens = Arrays.asList(new JavaRand(), new LehmerLow(), new LehmerHigh(),
            new L20(), new L89(), new Geffe(), new Librarian(), new Wolfram(), new BMBit(), new BMByte(), new BBSBit(), new BBSByte());

    public void performTest(BiFunction<int[], Double, Boolean> test, List<Generator> generators) {
        int bytesLen = 100_000;
        double[] alphas = {0.01, 0.05, 0.1};
        double[] alpha_quantiles = {2.3263478740408416, 1.6448536269514724, 1.2815515655446008};

        for (Generator gen : generators) {
            System.out.println("\n\n" + gen.getName() + ": ");
            int[] bytes = gen.generate(bytesLen);
            for (double alpha_quantile : alpha_quantiles) {
                System.out.println(test.apply(bytes, alpha_quantile));
            }
        }
    }

    public boolean testFrequency(int[] bytes, double alpha_quantile) {
        double chiSquared = bytesSequenceTests.chiSquaredFrequencyTest(bytes);
        double chiSquaredH0 = bytesSequenceTests.chiSquared1Alpha.apply(alpha_quantile);
        System.out.println("chiSquared:  " + String.format("%.2f", chiSquared) + "  " + String.format("%.2f", chiSquaredH0));
        return chiSquared <= chiSquaredH0;
    }

    public boolean testSerial(int[] bytes, double alpha_quantile) {
        double chiSquared = bytesSequenceTests.chiSquaredSerialTest(bytes);
        double chiSquaredH0 = bytesSequenceTests.chiSquared2Alpha.apply(alpha_quantile);
        System.out.println("chiSquared:  " + String.format("%.2f", chiSquared) + "  " + String.format("%.2f", chiSquaredH0));
        return chiSquared <= chiSquaredH0;
    }

    public boolean testGap(int[] bytes, double alpha_quantile) {
        double chiSquared = bytesSequenceTests.chiSquaredGapTest(bytes);
        double chiSquaredH0 = bytesSequenceTests.chiSquared3Alpha.apply(alpha_quantile);
        System.out.println("chiSquared:  " + String.format("%.2f", chiSquared) + "  " + String.format("%.2f", chiSquaredH0));
        return chiSquared <= chiSquaredH0;
    }

    @Test
    public void testFrequencyAllAlphas() {
        System.out.println("testFrequencyAllAlphas");
        performTest(this::testFrequency, gens);
    }

    @Test
    public void testSerialAllAlphas() {
        System.out.println("testSerialAllAlphas");
        performTest(this::testSerial, gens);
    }

    @Test
    public void testGapAllAlphas() {
        System.out.println("testGapAllAlphas");
        performTest(this::testGap, gens);
    }


}

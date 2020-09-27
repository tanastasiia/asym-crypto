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
        int bytesLen = 1_000;
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

    @Test
    public void testUniformDistributionAllAlphas() {
        System.out.println("testUniformDistributionAllAlphas");
        performTest(bytesSequenceTests::testUniformDistribution, gens);
    }

    @Test
    public void testIndependenceAllAlphas() {
        System.out.println("testIndependenceAllAlphas");
        performTest(bytesSequenceTests::testIndependence, gens);
    }

    @Test
    public void testUniformityAllAlphas() {
        System.out.println("testUniformityAllAlphas");
        performTest(bytesSequenceTests::testUniformity, gens);
    }


}

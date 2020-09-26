package ua.kpi;

import org.junit.Test;
import ua.kpi.generators.*;
import ua.kpi.test.BytesSequenceTests;

import java.util.Arrays;
import java.util.List;

public class BytesSequenceTestsTest {

    // double[] alphas = {0.01, 0.05, 0.1};
    //{2.3263478740408416, 1.6448536269514724, 1.2815515655446008};
    private final BytesSequenceTests test = new BytesSequenceTests();

    private final List<Generator> gens = Arrays.asList(new JavaRand(), new LehmerLow(), new LehmerHigh(),
            new L20(), new L89(), new Geffe(), new Librarian(), new Wolfram(), new BMBit(), new BMByte(), new BBSBit(), new BBSByte());

    @Test
    public void testUniformDistribution001() {
        double alpha = 0.01;
        double alpha_quantile = 2.3263478740408416;
        int bytesLen = 300000;
        for (Generator gen : new Generator[] {new LehmerHigh(), new LehmerLow()}) {
            System.out.println(gen.getName() + ": " + test.testUniformDistribution(gen.generate(bytesLen), alpha_quantile));
        }
    }

    @Test
    public void testUniformDistributionAllAlphas() {
        int bytesLen = 40_000;
        double[] alphas = {0.01, 0.05, 0.1};
        double[] alpha_quantiles = {2.3263478740408416, 1.6448536269514724, 1.2815515655446008};

        for (Generator gen : gens) {
            System.out.println("\n\n" + gen.getName() + ": ");
            int[] bytes = gen.generate(bytesLen);
            for (double alpha_quantile : alpha_quantiles) {
                System.out.println(test.testUniformDistribution(bytes, alpha_quantile));
            }
        }
    }
}

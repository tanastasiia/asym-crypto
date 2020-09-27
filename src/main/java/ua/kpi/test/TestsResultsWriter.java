package ua.kpi.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ua.kpi.generators.*;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class TestsResultsWriter {

    private final BytesSequenceTests tests = new BytesSequenceTests();

    private final List<Generator> gens = Arrays.asList(new JavaRand(), new LehmerLow(), new LehmerHigh(),
            new L20(), new L89(), new Geffe(), new Librarian(), new Wolfram(), new BMBit(), new BMByte(), new BBSBit(), new BBSByte());


    private final double[] alpha_quantiles = {2.3263478740408416, 1.6448536269514724, 1.2815515655446008};
    private final double[] alphas = {0.01, 0.05, 0.1};

    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    private String passedString(boolean isPassed) {
        String passed = " (passed)";
        String notPassed = " (not passed)";
        return isPassed ? passed : notPassed;
    }

    public void perform(int bytesLen) throws IOException {

        String[] headers = new String[]{"generator",
                "hi test1 of sequence", "hi test1 a=0.01", "hi test1 a=0.05", "hi test1 a=0.1",
                "hi test2 of sequence", "hi test2 a=0.01", "hi test2 a=0.05", "hi test2 a=0.1",
                "hi test3 of sequence", "hi test3 a=0.01", "hi test3 a=0.05", "hi test3 a=0.1"};

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Future<String[]>> genTestsResults = new ArrayList<>();
        for (Generator gen : gens) {
            System.out.println(gen.getName());
            genTestsResults.add(executor.submit(() -> {
                int[] bytes = gen.generate(bytesLen);
                System.out.println(gen.getName() +": bytes generated");

                double test1Res = tests.testUniformDistributionHiSquared(bytes);
                double test2Res = tests.testIndependenceHiSquared(bytes);
                double test3Res = tests.testUniformityHiSquared(bytes);

                String[] testsResults = new String[alpha_quantiles.length * 3 + 4];
                testsResults[0] = gen.getName();
                testsResults[1] = String.format("%.2f", test1Res);
                testsResults[5] = String.format("%.2f", test2Res);
                testsResults[9] = String.format("%.2f", test3Res);
                System.out.println(gen.getName() +": tests results done");

                for (int i = 0; i < alpha_quantiles.length; i++) {
                    double hi1 = tests.hiSquared1Alpha.apply(alpha_quantiles[i]);
                    double hi2 = tests.hiSquared2Alpha.apply(alpha_quantiles[i]);
                    double hi3 = tests.hiSquared3Alpha.apply(alpha_quantiles[i]);
                    testsResults[i + 2] = String.format("%.2f", hi1) + passedString(test1Res <=hi1);
                    testsResults[alpha_quantiles.length + i + 3] = String.format("%.2f", hi2) + passedString(test2Res <=hi2);
                    testsResults[2 * alpha_quantiles.length + i + 4] = String.format("%.2f", hi3) + passedString(test2Res <=hi2);
                }

                System.out.println(gen.getName() +": TESTED");
                return testsResults;
            }));
        }

        FileWriter out = new FileWriter("files/tests_results.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers))) {
            for (Future<String[]> genRes : genTestsResults) {
                printer.printRecord(genRes.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        out.close();
        System.out.println("File is done");

    }
}

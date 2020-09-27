package ua.kpi;

import ua.kpi.test.TestsResultsWriter;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        long time = System.nanoTime();
        new TestsResultsWriter().perform(100_000);
        System.out.println("DONE in " + (System.nanoTime() - time) / 1000000000.0 + "sec ----------------------------------------------------------------");
        System.exit(0);
    }
}

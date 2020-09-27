package ua.kpi;

import ua.kpi.test.BytesSequenceTests;
import ua.kpi.test.TestsResultsWriter;

import java.time.LocalTime;


public class App {
    public static void main(String[] args) {

        int bytesLen = 200_000;
        int r = 1 + (int) (Math.log(bytesLen) / Math.log(2));
        System.out.println("Started at " + LocalTime.now());
        System.out.println("r = " + r);
        long time = System.nanoTime();

        new TestsResultsWriter().perform(bytesLen);

        System.out.println("DONE in " + (System.nanoTime() - time) / 1000000000.0 + "sec ----------------------------------------------------------------");
        System.exit(0);
    }
}

package ua.kpi;

import ua.kpi.lab1.TestsResultsWriter;

import java.time.LocalTime;


public class App {
    public static void main(String[] args) {

        int bytesLen = 160_000;

        System.out.println("Sequence length: " + bytesLen + " bytes");
        System.out.println("Started at: " + LocalTime.now() + "\n");
        long time = System.nanoTime();
        new TestsResultsWriter().perform(bytesLen);
        System.out.println("\nDONE in " + (System.nanoTime() - time) / 1000000000.0 + "sec ----------------------------------------------------------------");
        System.exit(0);
    }
}

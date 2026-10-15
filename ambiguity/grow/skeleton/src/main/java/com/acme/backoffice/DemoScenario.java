package com.acme.backoffice;

import java.io.PrintStream;

/**
 * A fixed demo of BackOffice: creates a small data set and prints what the features show for it.
 * Its output must be deterministic: no current dates, no random values.
 */
public class DemoScenario {
    public static void run(DataStore store, PrintStream out) {
        out.println("BackOffice demo");
    }

    public static void main(String[] args) {
        run(new DataStore(Database.openInMemory()), System.out);
    }
}

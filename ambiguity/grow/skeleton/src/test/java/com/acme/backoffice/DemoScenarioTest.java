package com.acme.backoffice;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoScenarioTest {
    @Test
    void demoRuns() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DemoScenario.run(new DataStore(Database.openInMemory()), new PrintStream(bytes));
        assertTrue(bytes.toString().startsWith("BackOffice demo"));
    }
}

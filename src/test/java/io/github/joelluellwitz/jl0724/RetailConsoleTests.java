/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * TODO: Document.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/io/github/joelluellwitz/jl0724/internal/data/api/ToolRepoTests.sql")
public class RetailConsoleTests {
    private static final InputStream SYSTEM_IN = System.in;
    private static final PrintStream SYSTEM_OUT = System.out;

    private CommandLineRunner retailConsole;

    @AfterAll
    public static void restoreSystemIn() {
        System.setIn(SYSTEM_IN);
        System.setOut(SYSTEM_OUT);
    }

    // TODO: Document?
    @Autowired
    private RetailConsoleTests(final CommandLineRunner retailConsole) {
        this.retailConsole = retailConsole;
    }

    // TODO: Document?
    // TODO: Note about thread safety.
    @Test
    public void printToolListSucceeds() throws Exception {
        final String expectedOutput = "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: "
                + "________________________________________________________________________________________________\n"
                + "| Tool Code| Tool Type | Brand | Daily Charge| Weekday Charge?| Weekend Charge?| Holiday Charge?|\n"
                + "|===============================================================================================|\n"
                + "| CHNS     | Chainsaw  | Stihl | $1.49       | true           | false          | true           |\n"
                + "| JAKD     | Jackhammer| DeWalt| $2.99       | true           | false          | false          |\n"
                + "| JAKR     | Jackhammer| Ridgid| $2.99       | true           | false          | false          |\n"
                + "| LADW     | Ladder    | Werner| $1.99       | true           | true           | false          |\n"
                + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";

        final ByteArrayOutputStream standardOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(standardOutputStream));

        final PipedOutputStream outputStreamForStandardInput = new PipedOutputStream();
        System.setIn(new PipedInputStream(outputStreamForStandardInput));

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retailConsole.run();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

        outputStreamForStandardInput.write("p\n".getBytes());
        TimeUnit.MILLISECONDS.sleep(1000);
        outputStreamForStandardInput.write("q\n".getBytes());

        assertThat(thread.join(Duration.ofSeconds(10))).isTrue();

        assertThat(standardOutputStream.toString()).isEqualTo(expectedOutput);
    }

    @Test
    public void checkoutSucceeds() throws Exception {
        final String expectedOutput = "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: "
                + "Enter the tool code: "
                + "Enter the checkout date (MM/DD/YY): "
                + "Enter the rental duration in days: "
                + "Enter the discount percentage as an integer (0-100): "
                + "\n"
                + "Rental Agreement:\n"
                + "Tool code: JAKD\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: DeWalt\n"
                + "Rental days: 30\n"
                + "Check out date: 06/30/24\n"
                + "Due date: 07/30/24\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 21\n"
                + "Pre-discount charge: $62.79\n"
                + "Discount percent: 25%\n"
                + "Discount amount: $15.70\n"
                + "Final charge: $47.09\n"
                + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";

        final ByteArrayOutputStream standardOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(standardOutputStream));

        final PipedOutputStream outputStreamForStandardInput = new PipedOutputStream();
        System.setIn(new PipedInputStream(outputStreamForStandardInput));

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retailConsole.run();
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

        outputStreamForStandardInput.write("c\n".getBytes());
        TimeUnit.MILLISECONDS.sleep(1000);
        outputStreamForStandardInput.write("JAKD\n".getBytes());
        TimeUnit.MILLISECONDS.sleep(1000);
        outputStreamForStandardInput.write("06/30/24\n".getBytes());
        TimeUnit.MILLISECONDS.sleep(1000);
        outputStreamForStandardInput.write("30\n".getBytes());
        TimeUnit.MILLISECONDS.sleep(1000);
        outputStreamForStandardInput.write("25\n".getBytes());
        TimeUnit.MILLISECONDS.sleep(1000);
        outputStreamForStandardInput.write("q\n".getBytes());

        assertThat(thread.join(Duration.ofSeconds(10))).isTrue();

        assertThat(standardOutputStream.toString()).isEqualTo(expectedOutput);
    }
}
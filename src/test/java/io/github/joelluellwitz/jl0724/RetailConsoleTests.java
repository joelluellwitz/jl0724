/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.jupiter.api.AfterEach;
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

    @AfterEach
    public void restoreStandardPipes() {
        System.setIn(SYSTEM_IN);
        System.setOut(SYSTEM_OUT);
    }

    // TODO: Document?
    @Autowired
    private RetailConsoleTests(final CommandLineRunner retailConsole) {
        this.retailConsole = retailConsole;
    }

    // TODO: Document?
    @Test
    public void printToolListSucceeds() throws Exception {
        runRetailConsoleTest((outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer) -> {
            final byte[] mainPrompt =
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(mainPrompt.length)).isEqualTo(mainPrompt);
            outputStreamForStandardInput.write("p\n".getBytes());

            final String toolListAndMainPromptString =
                    "________________________________________________________________________________________________\n"
                    + "| Tool Code| Tool Type | Brand | Daily Charge| Weekday Charge?| Weekend Charge?| Holiday Charge?|\n"
                    + "|===============================================================================================|\n"
                    + "| CHNS     | Chainsaw  | Stihl | $1.49       | true           | false          | true           |\n"
                    + "| JAKD     | Jackhammer| DeWalt| $2.99       | true           | false          | false          |\n"
                    + "| JAKR     | Jackhammer| Ridgid| $2.99       | true           | false          | false          |\n"
                    + "| LADW     | Ladder    | Werner| $1.99       | true           | true           | false          |\n"
                    + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";
            final byte[] toolListAndMainPrompt = toolListAndMainPromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(toolListAndMainPrompt.length)).isEqualTo(
                    toolListAndMainPrompt);
            outputStreamForStandardInput.write("p\n".getBytes());

            // The table is printed again so that we get unit test coverage on the saved table data.
            assertThat(inputSreamForStandardOutput.readNBytes(toolListAndMainPrompt.length)).isEqualTo(
                    toolListAndMainPrompt);
            outputStreamForStandardInput.write("q\n".getBytes());
        });
    }

    @Test
    public void checkoutSucceeds() throws Exception {
        runRetailConsoleTest((outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer) -> {
            final byte[] mainPrompt =
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(mainPrompt.length)).isEqualTo(mainPrompt);
            outputStreamForStandardInput.write("c\n".getBytes());

            final byte[] toolCodePrompt = "Enter the tool code: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(toolCodePrompt.length)).isEqualTo(toolCodePrompt);
            outputStreamForStandardInput.write("JAKD\n".getBytes());

            final byte[] checkoutDatePrompt = "Enter the checkout date (MM/DD/YY): ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(checkoutDatePrompt.length)).isEqualTo(checkoutDatePrompt);
            outputStreamForStandardInput.write("06/30/24\n".getBytes());

            final byte[] durationPrompt = "Enter the rental duration in days: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(durationPrompt.length)).isEqualTo(durationPrompt);
            outputStreamForStandardInput.write("30\n".getBytes());

            final byte[] discountPrompt = "Enter the discount percentage as an integer (0-100): ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(discountPrompt.length)).isEqualTo(discountPrompt);
            outputStreamForStandardInput.write("25\n".getBytes());

            final String rentalAgreementAndMainPromptString = "\n"
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
            final byte[] rentalAgreementAndMainPrompt = rentalAgreementAndMainPromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(rentalAgreementAndMainPrompt.length)).isEqualTo(
                    rentalAgreementAndMainPrompt);
            outputStreamForStandardInput.write("q\n".getBytes());
        });
    }

    @Test
    public void checkoutWithUiValidationErrorsSucceeds() throws Exception {
        runRetailConsoleTest((outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer) -> {
            final byte[] mainPrompt =
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(mainPrompt.length)).isEqualTo(mainPrompt);
            outputStreamForStandardInput.write("a\n".getBytes());

            final String errorAndMainPromptString = "Invalid option: a\n"
                    + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";
            final byte[] errorAndMainPrompt = errorAndMainPromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(errorAndMainPrompt.length)).isEqualTo(errorAndMainPrompt);
            outputStreamForStandardInput.write("c\n".getBytes());

            final byte[] toolCodePrompt = "Enter the tool code: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(toolCodePrompt.length)).isEqualTo(toolCodePrompt);
            outputStreamForStandardInput.write("\n".getBytes());

            assertThat(inputSreamForStandardOutput.readNBytes(toolCodePrompt.length)).isEqualTo(toolCodePrompt);
            outputStreamForStandardInput.write("JAKD\n".getBytes());

            final byte[] checkoutDatePrompt = "Enter the checkout date (MM/DD/YY): ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(checkoutDatePrompt.length)).isEqualTo(checkoutDatePrompt);
            outputStreamForStandardInput.write("02/30/24\n".getBytes());

            final String errorAndCheckoutDatePromptString = "Value 02/30/24 is not a valid date.\n"
                    + "Enter the checkout date (MM/DD/YY): ";
            final byte[] errorAndCheckoutDatePrompt = errorAndCheckoutDatePromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(errorAndCheckoutDatePrompt.length)).isEqualTo(
                    errorAndCheckoutDatePrompt);
            outputStreamForStandardInput.write("06/30/24\n".getBytes());

            final byte[] durationPrompt = "Enter the rental duration in days: ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(durationPrompt.length)).isEqualTo(durationPrompt);
            outputStreamForStandardInput.write("a\n".getBytes());

            final String errorAndDurationPromptString = "Value a is not an integer.\n"
                    + "Enter the rental duration in days: ";
            final byte[] errorAndDurationPrompt = errorAndDurationPromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(errorAndDurationPrompt.length)).isEqualTo(
                    errorAndDurationPrompt);
            outputStreamForStandardInput.write("30\n".getBytes());

            final byte[] discountPrompt = "Enter the discount percentage as an integer (0-100): ".getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(discountPrompt.length)).isEqualTo(discountPrompt);
            outputStreamForStandardInput.write("a\n".getBytes());

            final String errorAndDiscountPromptString = "Value a is not an integer.\n"
                    + "Enter the discount percentage as an integer (0-100): ";
            final byte[] errorAndDiscountPrompt = errorAndDiscountPromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(errorAndDiscountPrompt.length)).isEqualTo(
                    errorAndDiscountPrompt);
            outputStreamForStandardInput.write("25\n".getBytes());

            final String rentalAgreementAndMainPromptString = "\n"
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
            final byte[] rentalAgreementAndMainPrompt = rentalAgreementAndMainPromptString.getBytes();
            assertThat(inputSreamForStandardOutput.readNBytes(rentalAgreementAndMainPrompt.length)).isEqualTo(
                    rentalAgreementAndMainPrompt);
            outputStreamForStandardInput.write("q\n".getBytes());
        });
    }

    // TODO: Add test for backend validation error.

    /**
     * Document.
     *
     * @param assertCode
     * @throws IOException
     * @throws InterruptedException
     */
    // TODO: Note about thread safety.
    private void runRetailConsoleTest(final QuadConsumer<PipedOutputStream, PipedInputStream, Thread, Timer> assertCode)
            throws IOException, InterruptedException {
        final PipedOutputStream outputStreamForStandardInput = new PipedOutputStream();
        System.setIn(new PipedInputStream(outputStreamForStandardInput));

        final PipedInputStream inputSreamForStandardOutput = new PipedInputStream();
        System.setOut(new PrintStream(new PipedOutputStream(inputSreamForStandardOutput)));

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

        final Timer timer = new Timer();
        final TimerTask threadTimeoutTask = new TimerTask() {
            @Override
            public void run() {
                thread.interrupt();
            }
        };
        timer.schedule(threadTimeoutTask, 30000);

        assertCode.accept(outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer);

        timer.cancel();

        assertThat(thread.join(Duration.ofSeconds(10))).isTrue();
    }

    /**
     * TODO: Document.
     *
     * @param <T>
     * @param <U>
     * @param <V>
     * @param <W>
     */
    @FunctionalInterface
    public interface QuadConsumer<T, U, V, W> {
        void accept(T t, U u, V v, W w) throws IOException;
    }
}
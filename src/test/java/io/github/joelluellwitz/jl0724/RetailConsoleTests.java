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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
 * Tests {@link io.github.joelluellwitz.jl0724.RetailConsole RetailConsole}.<p>
 *
 * Note: {@link org.mockito.Mockito#mockStatic Mockito#mockStatic} explicitly does not allow mocking of
 *   {@link java.lang.System System} static methods. That makes it impossible to use mocks alone to test the input and
 *   output of the RetailConsole. Instead, I replace {@link java.lang.System#in System#in} and
 *   {@link java.lang.System#out System#out} with thread safe streams and runs the RetailConsole in a separate thread. I
 *   then use the thread safe streams to simulate user input and verify console output. I prefer this method of testing
 *   over adding a console abstraction layer specifically for unit testing. I generally try to avoid altering the 'main'
 *   classes due to unit testing concerns.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/io/github/joelluellwitz/jl0724/internal/data/api/ToolRepoTests.sql")
public class RetailConsoleTests {
    private static final InputStream SYSTEM_IN = System.in;
    private static final PrintStream SYSTEM_OUT = System.out;

    private CommandLineRunner retailConsole;

    /**
     * Restores {@link java.lang.System#in System#in} and {@link java.lang.System#out System#out} to their original
     *   values after each test.
     */
    @AfterEach
    public void restoreStandardPipes() {
        System.setIn(SYSTEM_IN);
        System.setOut(SYSTEM_OUT);
    }

    /**
     * Constructor.
     *
     * @param retailConsole A {@link io.github.joelluellwitz.jl0724.RetailConsole RetailConsole} instance to test.
     */
    @Autowired
    private RetailConsoleTests(final CommandLineRunner retailConsole) {
        this.retailConsole = retailConsole;
    }

    /**
     * Tests printing the tool list. The tool list is printed twice to get unit test code coverage for printing the
     *   saved table data.
     *
     * @throws IOException Thrown if an error occurs with the streams used for input or output.
     * @throws InterruptedException Thrown if thread joining times out.
     */
    @Test
    public void printToolListSucceeds() throws IOException, InterruptedException {
        runRetailConsoleTest((outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer) -> {
            assertStandardOutputEquals(inputSreamForStandardOutput,
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ");
            outputStreamForStandardInput.write("p\n".getBytes());

            final String toolListAndMainPrompt =
                    "________________________________________________________________________________________________\n"
                    + "| Tool Code| Tool Type | Brand | Daily Charge| Weekday Charge?| Weekend Charge?| Holiday Charge?|\n"
                    + "|===============================================================================================|\n"
                    + "| CHNS     | Chainsaw  | Stihl | $1.49       | true           | false          | true           |\n"
                    + "| JAKD     | Jackhammer| DeWalt| $2.99       | true           | false          | false          |\n"
                    + "| JAKR     | Jackhammer| Ridgid| $2.99       | true           | false          | false          |\n"
                    + "| LADW     | Ladder    | Werner| $1.99       | true           | true           | false          |\n"
                    + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";
            assertStandardOutputEquals(inputSreamForStandardOutput, toolListAndMainPrompt);
            outputStreamForStandardInput.write("p\n".getBytes());

            // The table is printed again so that we get unit test coverage on the saved table data.
            assertStandardOutputEquals(inputSreamForStandardOutput, toolListAndMainPrompt);
            outputStreamForStandardInput.write("q\n".getBytes());
        });
    }

    /**
     * Tests checking out a tool.
     *
     * @throws IOException Thrown if an error occurs with the streams used for input or output.
     * @throws InterruptedException Thrown if thread joining times out.
     */
    @Test
    public void checkoutSucceeds() throws IOException, InterruptedException {
        runRetailConsoleTest((outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer) -> {
            assertStandardOutputEquals(inputSreamForStandardOutput,
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ");
            outputStreamForStandardInput.write("c\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput, "Enter the tool code: ");
            outputStreamForStandardInput.write("JAKD\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput, "Enter the checkout date (MM/DD/YY): ");
            outputStreamForStandardInput.write("06/30/24\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput, "Enter the rental duration in days: ");
            outputStreamForStandardInput.write("30\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput,
                    "Enter the discount percentage as an integer (0-100): ");
            outputStreamForStandardInput.write("25\n".getBytes());

            final String rentalAgreementAndMainPrompt = "\n"
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
            assertStandardOutputEquals(inputSreamForStandardOutput, rentalAgreementAndMainPrompt);
            outputStreamForStandardInput.write("q\n".getBytes());
        });
    }

    /**
     * Tests all the validation error messages.
     *
     * @throws IOException Thrown if an error occurs with the streams used for input or output.
     * @throws InterruptedException Thrown if thread joining times out.
     */
    @Test
    public void checkoutWithValidationErrorsSucceeds() throws IOException, InterruptedException {
        runRetailConsoleTest((outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer) -> {
            assertStandardOutputEquals(inputSreamForStandardOutput,
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ");
            outputStreamForStandardInput.write("a\n".getBytes());

            final String errorAndMainPrompt = "Invalid option: a\n"
                    + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";
            assertStandardOutputEquals(inputSreamForStandardOutput, errorAndMainPrompt);
            outputStreamForStandardInput.write("c\n".getBytes());

            final String toolCodePrompt = "Enter the tool code: ";
            assertStandardOutputEquals(inputSreamForStandardOutput, toolCodePrompt);
            outputStreamForStandardInput.write("\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput, toolCodePrompt);
            outputStreamForStandardInput.write("INVD\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput, "Enter the checkout date (MM/DD/YY): ");
            outputStreamForStandardInput.write("02/30/24\n".getBytes());

            final String errorAndCheckoutDatePrompt = "Value 02/30/24 is not a valid date.\n"
                    + "Enter the checkout date (MM/DD/YY): ";
            assertStandardOutputEquals(inputSreamForStandardOutput, errorAndCheckoutDatePrompt);
            outputStreamForStandardInput.write("06/30/24\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput, "Enter the rental duration in days: ");
            outputStreamForStandardInput.write("a\n".getBytes());

            final String errorAndDurationPrompt = "Value a is not an integer.\n"
                    + "Enter the rental duration in days: ";
            assertStandardOutputEquals(inputSreamForStandardOutput, errorAndDurationPrompt);
            outputStreamForStandardInput.write("30\n".getBytes());

            assertStandardOutputEquals(inputSreamForStandardOutput,
                    "Enter the discount percentage as an integer (0-100): ");
            outputStreamForStandardInput.write("a\n".getBytes());

            final String errorAndDiscountPrompt = "Value a is not an integer.\n"
                    + "Enter the discount percentage as an integer (0-100): ";
            assertStandardOutputEquals(inputSreamForStandardOutput, errorAndDiscountPrompt);
            outputStreamForStandardInput.write("25\n".getBytes());

            final String backendValidationErrorAndMainPrompt =
                    "An error occurred during checkout: Unrecognized tool code. You specified: INVD\n"
                    + "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ";
            assertStandardOutputEquals(inputSreamForStandardOutput, backendValidationErrorAndMainPrompt);
            outputStreamForStandardInput.write("q\n".getBytes());
        });
    }

    /**
     * Test the handling of IOExceptions raised from reading from @{link {@link java.lang.System#in System#in}.
     *
     * @throws IOException Thrown if an error occurs with the streams used for input or output.
     * @throws InterruptedException Thrown if thread joining times out.
     */
    @Test
    public void inputIoExceptionFailure() throws IOException, InterruptedException {
        final PipedInputStream standardInputStream = new PipedInputStream(new PipedOutputStream());
        System.setIn(standardInputStream);

        final PipedInputStream inputSreamForStandardOutput = new PipedInputStream();
        System.setOut(new PrintStream(new PipedOutputStream(inputSreamForStandardOutput)));

        // Closing should eventually cause an IOException.
        standardInputStream.close();

        final BlockingQueue<Throwable> exceptionQueue = new ArrayBlockingQueue<Throwable>(1, true);
        final Thread.UncaughtExceptionHandler exceptionHandler = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(final Thread thread, final Throwable e) {
                exceptionQueue.add(e);
            }
        };

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    retailConsole.run();
                } catch (final RuntimeException e) {
                    throw e;
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setUncaughtExceptionHandler(exceptionHandler);
        thread.start();

        final Timer timer = new Timer();
        final TimerTask threadTimeoutTask = new TimerTask() {
            @Override
            public void run() {
                thread.interrupt();
            }
        };

        timer.schedule(threadTimeoutTask, 10000);

        assertStandardOutputEquals(inputSreamForStandardOutput,
                "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ");

        timer.cancel();

        thread.join(Duration.ofSeconds(10));

        final Throwable throwable = exceptionQueue.take();
        assertThat(throwable).isExactlyInstanceOf(RuntimeException.class);
        assertThat(throwable.getMessage()).isEqualTo("Error retrieving user input.");
        assertThat(throwable.getCause()).isExactlyInstanceOf(IOException.class);
        assertThat(throwable.getCause().getMessage()).isEqualTo("Pipe closed");
    }

    /**
     * Replaces {@link java.lang.System#in System#in} and {@link java.lang.System#out System#out} with thread safe
     *   streams and runs the {@link io.github.joelluellwitz.jl0724.RetailConsole RetailConsole} in a separate thread.
     *   The thread safe streams are then used by the callback to mimic user input and verify console output.
     *
     * @param assertCallback Callback that mimics user input and verifies output.
     * @throws IOException Thrown if an error occurs with the streams used for input or output.
     * @throws InterruptedException Thrown if thread joining times out.
     */
    private void runRetailConsoleTest(
            final QuadConsumer<PipedOutputStream, PipedInputStream, Thread, Timer> assertCallback)
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

        assertCallback.accept(outputStreamForStandardInput, inputSreamForStandardOutput, thread, timer);

        timer.cancel();

        assertThat(thread.join(Duration.ofSeconds(10))).isTrue();
    }

    /**
     * Asserts that the expected String equals the bytes read from the input stream.<p>
     *
     * Note: Reading can block if the actual output length is less than the expected output length. To avoid a deadlock,
     *   I setup a {@link java.util.Timer Timer} in the tests that terminates the thread if too much time passes.
     *   Terminating the thread causes the stream to raise an IOException, which breaks the deadlock.
     *
     * @param inputSreamForStandardOutput Input stream providing data intended for display on the console.
     * @param expectedString The expected String.
     * @throws IOException Thrown if an error occurs reading from the stream.
     */
    private void assertStandardOutputEquals(final PipedInputStream inputSreamForStandardOutput,
            final String expectedString) throws IOException {
        assertThat(new String(inputSreamForStandardOutput.readNBytes(
                expectedString.getBytes().length))).isEqualTo(expectedString);
    }

    /**
     * A consumer FunctionalInterface with four parameters.
     *
     * @param <T> Parameter 0.
     * @param <U> Parameter 1.
     * @param <V> Parameter 2.
     * @param <W> Parameter 3.
     */
    @FunctionalInterface
    public interface QuadConsumer<T, U, V, W> {
        /**
         * Executes the callback.
         *
         * @param t Parameter 0.
         * @param u Parameter 1.
         * @param v Parameter 2.
         * @param w Parameter 3.
         * @throws IOException Thrown if an error occurs reading from the stream.
         */
        void accept(T t, U u, V v, W w) throws IOException;
    }
}
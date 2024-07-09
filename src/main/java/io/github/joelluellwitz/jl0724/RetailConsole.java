/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dnl.utils.text.table.TextTable;
import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale;
import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;

/**
 * A console based implementation of the tool rental point of sale user interface.
 */
@SpringBootApplication
public class RetailConsole implements CommandLineRunner {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/uu").withResolverStyle(ResolverStyle.STRICT);

    private static Logger LOGGER = LoggerFactory.getLogger(RetailConsole.class);

    private final RetailPointOfSale retailPointOfSale;

    private String toolList;

    static {
        // Work around in case /tmp is set as 'noexec'. Note the these files are removed at program termination. See
        //   https://github.com/xerial/sqlite-jdbc/issues/1059 for details.
        // This line is in a static block to increase unit test code coverage.
        System.setProperty("org.sqlite.tmpdir", System.getProperty("user.home"));
    }

    /**
     * TODO: Document.
     *
     * @param context
     * @param retailPointOfSale
     */
    // Intentionally package private.
    // TODO: Verify @Autowired is needed.
    RetailConsole(@Autowired final RetailPointOfSale retailPointOfSale) {
        this.retailPointOfSale = retailPointOfSale;
    }

    /**
     * The retail console main loop.
     *
     * @param args The supplied command line parameters, of which none are recognized.
     * @throws IOException Raised in the event that the console cannot be read. Realistically should
     *   never happen.
     */
    public static void main(final String[] args) throws IOException {
        SpringApplication.run(RetailConsole.class, args);
    }

    /**
     * TODO: Document.
     */
    @Override
    public void run(final String... args) throws IOException {
        LOGGER.debug("Staring the Retail Console.");

        String mainInput;
        do {
            mainInput = promptForString("Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ");

            switch(mainInput) {
                case "q":
                    // This is handled by the while loop condition.
                    break;
                case "p":
                    printToolList();
                    break;
                case "c":
                    checkout();
                    break;
                default:
                    System.out.println("Invalid option: %s".formatted(mainInput));
            }
        } while (!mainInput.equals("q"));

        LOGGER.debug("Terminating the Retail Console.");
    }

    /**
     * TODO: Document.
     */
    private void printToolList() {
        if (toolList == null) {
            final String[] columnNames = {"Tool Code", "Tool Type", "Brand", "Daily Charge", "Weekday Charge?",
                    "Weekend Charge?", "Holiday Charge?"};

            final List<Tool> tools = retailPointOfSale.listTools();
            final int toolCount = tools.size();
            final String[][] tableData = new String[toolCount][];
            final Iterator<Tool> toolListIterator = tools.iterator();
            for (int toolIndex = 0; toolIndex < toolCount; toolIndex++) {
                final Tool tool = toolListIterator.next();
                tableData[toolIndex] = new String[] {
                    tool.getCode(), tool.getType(), tool.getBrand(),
                    NumberFormat.getCurrencyInstance(Locale.US).format(tool.getDailyCharge()),
                    Boolean.valueOf(tool.isWeekdayCharge()).toString(), Boolean.valueOf(tool.isWeekendCharge()).toString(),
                    Boolean.valueOf(tool.isHolidayCharge()).toString()
                };
            }

            final OutputStream tableOutputStream = new ByteArrayOutputStream();
            // Unfortunately this table library doesn't print a bottom border.
            new TextTable(columnNames, tableData).printTable(new PrintStream(tableOutputStream), 0);
            toolList = tableOutputStream.toString();
        }
        System.out.print(toolList);
    }

    /**
     * TODO: Document.
     */
    private void checkout() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode(promptForToolCode());
        contractParameters.setCheckoutDate(promptForCheckoutDate());
        contractParameters.setRentalDayCount(promptForInteger("Enter the rental duration in days: "));
        contractParameters.setDiscountPercent(promptForInteger(
                "Enter the discount percentage as an integer (0-100): "));

        try {
            final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

            // Note: rentalAgreement.toString could be (but actually isn't) an expensive operation. Potentially
            //   expensive to generate logging strings should be conditionally executed to avoid wasting execution time.
            //   In this case, toString saves the result of the first generation, so we could just avoid this
            //   conditional, but I wanted to demonstrate that I understand this concept.
            if (LOGGER.isInfoEnabled()) {
                // Note: Ideally we want to include some sort of user identifier in logging statements, but that
                //   obviously isn't available here.
                LOGGER.info("Rental agreement generated: \n{}", rentalAgreement.toString());
            }

            // Note: The requirements do not state that this header should be returned by
            //   'RentalAgreement.printRentalAgreement'. I want to follow the requirements exactly.
            System.out.println("\nRental Agreement:");

            rentalAgreement.printRentalAgreement();
        }
        catch (final IllegalArgumentException e) {
            System.out.println("An error occurred during checkout: %s".formatted(e.getLocalizedMessage()));
        }
    }

    /**
     * TODO:
     *
     * @return
     */
    private String promptForToolCode() {
        String toolCode = "";
        while ("".equals(toolCode)) {
            toolCode = promptForString("Enter the tool code: ");
        }

        return toolCode;
    }

    /**
     * TODO:
     *
     * @return
     */
    private LocalDate promptForCheckoutDate() {
        LocalDate dateValue = null;
        while (dateValue == null) {
            final String input = promptForString("Enter the checkout date (MM/DD/YY): ");
            try {
                dateValue = LocalDate.parse(input, DATE_FORMATTER);
            }
            catch (final Exception e) {
                System.out.println("Value %s is not a valid date.".formatted(input));
            }
        }

        return dateValue;
    }

    /**
     * TODO:
     *
     * @param prompt TOOD:
     * @return
     */
    private int promptForInteger(final String prompt) {
        Integer integerValue = null;
        while (integerValue == null) {
            final String input = promptForString(prompt);
            try {
                integerValue = Integer.valueOf(input);
            }
            catch (final Exception e) {
                System.out.println("Value %s is not an integer.".formatted(input));
            }
        }

        return integerValue.intValue();
    }

    /**
     * TODO:
     *
     * @return
     */
    private String promptForString(final String prompt) {
        System.out.print(prompt);
        final String input;
        try {
            input = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        return input;
    }
}
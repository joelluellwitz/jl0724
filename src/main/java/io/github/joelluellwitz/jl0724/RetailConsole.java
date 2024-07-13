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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dnl.utils.text.table.TextTable;
import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale;
import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;

/**
 * A console based implementation of the tool rental point of sale user interface.<p>
 *
 * Note: I realize this class is explicitly <b>not</b> required by the requirements document. This class represents the
 *   presentation tier in a three tier model. It was written quickly to serve the purpose of proving the business tier
 *   and data tier actually work and don't depend on any resources under src/test to function. Since this class is not
 *   required, it is of prototype level quality. (Some prompts have no option to quit or go back.).
 */
@SpringBootApplication
public class RetailConsole implements CommandLineRunner {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uu")
            .withResolverStyle(ResolverStyle.STRICT);

    private static Logger LOGGER = LoggerFactory.getLogger(RetailConsole.class);

    private final RetailPointOfSale retailPointOfSale;

    private String toolList;

    static {
        // Work around in case tmpdir is set as 'noexec' as it is with RedHat Enterprise Linux. Note that these files
        //   are removed at program termination. See https://github.com/xerial/sqlite-jdbc/issues/1059 for details.
        // This line is in a static block as opposed to 'main' to increase unit test code coverage.
        System.setProperty("org.sqlite.tmpdir", System.getProperty("user.home"));
    }

    /**
     * Constructor.
     *
     * @param retailPointOfSale Provides access to the business logic tier of the Retail Point of Sale application.
     */
    // Intentionally package private.
    RetailConsole(final RetailPointOfSale retailPointOfSale) {
        this.retailPointOfSale = retailPointOfSale;
    }

    /**
     * Entry point of the Retail Point of Sale application.
     *
     * @param args The supplied command line parameters, of which none are recognized.
     */
    public static void main(final String[] args) {
        SpringApplication.run(RetailConsole.class, args);
    }

    /**
     * Runs the retail console main loop.
     *
     * @param args The supplied command line parameters, of which none are recognized.
     */
    @Override
    public void run(final String... args) {
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
     * Prints the list of known tools and tool metadata to the console (STDOUT). The tool information is retrieved from
     *   the business logic tier.
     */
    private void printToolList() {
        if (toolList == null) {
            final String[] columnNames = { "Tool Code", "Tool Type", "Brand", "Daily Charge", "Weekday Charge?",
                    "Weekend Charge?", "Holiday Charge?" };

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
     * Processes a checkout for a tool. Prompts the user for relevant information and submits that information to the
     *   business tier for further validation and processing. Prints the returned rental agreement to the console.
     */
    private void checkout() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode(promptForToolCode());
        contractParameters.setCheckoutDate(promptForCheckoutDate());
        contractParameters.setRentalDayCount(promptForInteger("Enter the rental duration in days: "));
        contractParameters
                .setDiscountPercent(promptForInteger("Enter the discount percentage as an integer (0-100): "));

        try {
            final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

            // Note: RentalAgreement#toString could be (but actually isn't) an expensive operation. Potentially
            //   expensive to generate logging strings should be conditionally executed to avoid wasting execution time.
            //   In this case, toString saves the result of the first generation, so we could just avoid this
            //   conditional, but I wanted to demonstrate that I understand this concept.
            if (LOGGER.isInfoEnabled()) {
                // Note: Ideally we want to include some sort of user identifier in logging statements, but that
                //   obviously isn't available here.
                LOGGER.info("Rental agreement generated: \n{}", rentalAgreement.toString());
            }

            // Note: The requirements do not state that this header should be returned by
            //   'RentalAgreement#printRentalAgreement'. I want to follow the requirements exactly.
            System.out.println("\nRental Agreement:");

            rentalAgreement.printRentalAgreement();
        }
        catch (final IllegalArgumentException e) {
            System.out.println("An error occurred during checkout: %s".formatted(e.getLocalizedMessage()));
        }
    }

    /**
     * Prompts the user for the Tool Code. The user is continuously prompted until a non-empty value is supplied.
     *
     * @return The supplied tool code {@link java.lang.String String}.
     */
    private String promptForToolCode() {
        String toolCode = "";
        while ("".equals(toolCode)) {
            toolCode = promptForString("Enter the tool code: ");
        }

        return toolCode;
    }

    /**
     * Prompts the user for a valid date in the "MM/dd/uu" format. The user is continuously prompted until a valid date
     *   is entered.<p>
     *
     * Note: I really dislike how some validation is in the presentation tier and some validation is in the business
     *   logic tier. When I originally started to code this, I was going to have the date passed as a String to
     *   RetailPointOfSale#checkout and I would do the date validation from within 'checkout'. I later opted to do the
     *   date conversion here because something in the requirements document lead me to believe you expect 'checkout'
     *   to accept a LocalDate object. (Same thinking applies to rentalDayCount and discountPercent integers.)
     *
     * @return The checkout date.
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
     * Prompts the user for an integer until a valid integer is entered.
     *
     * @param prompt The prompt {@link java.lang.String String} to display to the user.
     * @return The supplied integer.
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
     * Prompts the user for a {@link java.lang.String String}.
     *
     * @param prompt The prompt that should be displayed to the user.
     * @return The value entered, even empty Strings.
     */
    private String promptForString(final String prompt) {
        System.out.print(prompt);
        final String input;
        try {
            input = new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (final IOException e) {
            // This block should not generally be reachable as the console should never realistically disappear.
            throw new RuntimeException("Error retrieving user input.", e);
        }

        return input;
    }
}
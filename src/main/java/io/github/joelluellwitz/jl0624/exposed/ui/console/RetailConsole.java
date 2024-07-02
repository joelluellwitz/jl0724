/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.exposed.ui.console;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dnl.utils.text.table.TextTable;
import io.github.joelluellwitz.jl0624.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0624.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0624.exposed.service.api.RetailPointOfSale;
import io.github.joelluellwitz.jl0624.exposed.service.api.Tool;

/**
 * A console based implementation of the tool rental point of sale user interface.
 */
@SpringBootApplication // TODO: (exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = "io.github.joelluellwitz.jl0624")
@EntityScan({"io.github.joelluellwitz.jl0624"}) // TODO: Internal package is referenced from external class.
@EnableJpaRepositories(basePackages = "io.github.joelluellwitz.jl0624") // TODO: Internal package is referenced from external class.
// TODO: Consider moving the command line running under io.github.joelluellwitz.jl0624 and having it call RetailConsole here.
public class RetailConsole implements CommandLineRunner {
    // TODO: private static Logger LOG = LoggerFactory.getLogger(RetailConsole.class);

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yy");

    private final ConfigurableApplicationContext context;
    private final Console console;
    private final RetailPointOfSale retailPointOfSale;

    private String toolList;

    // TODO: Document?
    // Intentionally package private.
    RetailConsole(@Autowired final ConfigurableApplicationContext context,
            @Autowired final RetailPointOfSale retailPointOfSale) {
        this.context = context;
        this.retailPointOfSale = retailPointOfSale;
        console = System.console();
    }

    /**
     * The retail console main loop.
     *
     * @param _args The supplied command line parameters, of which none are recognized.
     * @throws IOException Raised in the event that the console cannot be read. Realistically should
     *   never happen.
     */
    public static void main(final String[] _args) throws IOException {
        // TODO: LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(RetailConsole.class, _args);
        // TODO: LOG.info("APPLICATION FINISHED");
    }

    // TODO: Document.
    @Override
    public void run(final String... _args) throws IOException {
        while (true) {
            final String mainInput = console.readLine(
                    "Type 'p' to print a list of tools, 'c' to checkout, and 'q' to quit: ");

            switch(mainInput) {
                case "q":
                    System.exit(SpringApplication.exit(context));
                case "p":
                    printToolList();
                    break;
                case "c":
                    checkout();
                    break;
                default:
                    console.printf("Invalid option: %s\n", mainInput);
            }
        }
    }

    // TODO: Document.
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
        console.writer().print(toolList);
    }

    // TODO: Document.
    private void checkout() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode(promptForToolCode());
        contractParameters.setCheckoutDate(promptForCheckoutDate());
        contractParameters.setRentalDayCount(promptForInteger("Enter the rental duration in days: "));
        contractParameters.setDiscountPercent(promptForInteger(
                "Enter the discount percentage as an integer (0-100): "));

        // The requirements do not state that this header should be returned by 'RetailPointOfSale.checkout'.
        console.printf("\nRental Agreement:\n");

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);
        rentalAgreement.printRentalAgreement();
    }

    /**
     * TODO:
     *
     * @return
     */
    private String promptForToolCode() {
        String toolCode = "";
        while ("".equals(toolCode)) {
            toolCode = console.readLine("Enter the tool code: ");
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
            final String input = console.readLine("Enter the checkout date (MM/DD/YY): ");
            try {
                dateValue = LocalDate.parse(input, dateFormat);
            }
            catch (final Exception e) {
                console.printf("Value %s is not a date.\n", input);
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
            final String input = console.readLine(prompt);
            try {
                integerValue = Integer.valueOf(input);
            }
            catch (final Exception e) {
                console.printf("Value %s is not an integer.", input);
            }
        }

        return integerValue.intValue();
    }
}
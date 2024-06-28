/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.exposed.ui.console;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import io.github.joelluellwitz.jl0624.exposed.service.api.RetailPointOfSale;

/**
 * A console based implementation of the tool rental point of sale user interface.
 */
@SpringBootApplication
@ComponentScan(basePackages="io.github.joelluellwitz.jl0624")
public class RetailConsole implements CommandLineRunner {
    //private static Logger LOG = LoggerFactory.getLogger(RetailConsole.class);

    private final RetailPointOfSale retailPointOfSale;

    // TODO: Document?
    // Intentionally package private.
    RetailConsole(@Autowired RetailPointOfSale retailPointOfSale) {
        this.retailPointOfSale = retailPointOfSale;
    }

    /**
     * The retail console main loop.
     *
     * @param _args The supplied command line parameters, of which none are recognized.
     * @throws IOException Raised in the event that the console cannot be read. Realistically should
     *   never happen.
     */
    public static void main(final String[] _args) {
        // TODO: LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(RetailConsole.class, _args);
        // TODO: LOG.info("APPLICATION FINISHED");
    }

    // TODO: Document.
    @Override
    public void run(final String... _args) throws IOException {
        char mainInput;

        do {
            mainInput = (char) System.in.read();

            switch(mainInput) {
                case 'q':
                    // Is handled by while condition.
                    break;
                case 'p':
                    // TODO: Format this output:
                    System.out.println(retailPointOfSale.listTools());
                    break;
                case 'c':
                    checkout();
                    break;
            }
        } while (mainInput != 'q');
    }

    // TODO: Document.
    private void checkout() {

    }
}
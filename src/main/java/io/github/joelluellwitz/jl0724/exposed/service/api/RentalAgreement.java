/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.exposed.service.api;

/**
 * Represents a customer rental agreement. Note that the requirements does not require that all the
 *   properties are exposed.
 */
public interface RentalAgreement {
    /**
     * TODO: Document.
     *
     * @return
     */
    String getRentalAgreement();

    /**
     * Prints the rental agreement to the console. This functionality is here because it is
     *   explicitly required. I do not think the code should be organized this way. Ideally a
     *   RentalAgreement should be just a transfer object and some service method should generate
     *   the agreement text. It should then be a third component's responsibility to request that
     *   text and print it to the console. (Separation of UI concerns from business logic concerns.)
     */
    void printRentalAgreement();
}
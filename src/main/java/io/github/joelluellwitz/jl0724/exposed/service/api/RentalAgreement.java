/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.exposed.service.api;

/**
 * Represents a tool rental agreement.<p>
 *
 * Note: The requirements document does not require that all the properties need to be exposed to consumers.
 */
public interface RentalAgreement {
    /**
     * Returns the rental agreement as a {@link java.lang.String String}.<p>
     *
     * Note: This is not required by the requirements document, but it seems like something that should sensibly be
     *   exposed.
     *
     * @return A String representation of the rental agreement.
     */
    String toString();

    /**
     * Prints the rental agreement to the console.<p>
     *
     * Note: This functionality is here because it is explicitly required by the requirements document. That said, I do
     *   not think business tier code should print anything directly to the console. It should be the presentation
     *   tier's responsibility to request the rental agreement text and print it to the console. (I realize when the
     *   requirements document was written, the author probably didn't intend for candidates to demonstrate the 3-tier
     *   model.)
     */
    void printRentalAgreement();
}
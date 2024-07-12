/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.exposed.service.api;

import java.util.List;

/**
 * The main exposed interface for the business tier of the Retail Point of Sale application.
 *
 * Note: This interface, along with other files in the package, represent the exposed API that the presentation tier
 *   is suppose to use to present this application to users. Sometimes, exposed APIs are extracted out into a separate
 *   Java project (a separate jar), but that doesn't work well with this programming demonstration.
 */
public interface RetailPointOfSale {
    /**
     * A {@link java.util.List List} of all tools available for rental sorted by Tool Code.
     *
     * Note: This method is not required by the requirements document. I am including it because it is required to make
     * the Retail Point of Sale UI a usable console.
     *
     * @return A List of {@link io.github.joelluellwitz.jl0724.exposed.service.api.Tool Tools}.
     */
    List<Tool> listTools();

    /**
     * Checks out a {@link io.github.joelluellwitz.jl0724.exposed.service.api.ToolImpl Tool} to a customer using the
     *   provided {@link io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters}.
     *
     * @param contractParameters Represents the customer's choices used in building a rental contract. Note: I used a
     *   transfer object here instead of supplying multiple arguments to the checkout method. This is because positional
     *   arguments become confusing after 1 or 2 arguments, particularly when some parameters share the same data type.
     *   For example, with checkout("CHNS", LocalDate.of(2024, 7, 4), 10, 25), it isn't clear if this is a 10 day
     *   checkout with a 25 percent discount or a 25 day checkout with a 10 percent discount. Using a transfer object
     *   here avoids this ambiguity.
     * @return Represents of the exact terms of the customer's tool rental agreement.
     */
    RentalAgreement checkout(ContractParameters contractParameters);
}

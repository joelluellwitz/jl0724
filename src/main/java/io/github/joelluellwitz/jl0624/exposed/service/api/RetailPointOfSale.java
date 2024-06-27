/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.exposed.service.api;

import java.util.List;

/**
 * Public service interface to be used by the Retail Point of Sale UI. Sometimes, public interfaces
 *   are part of a different project, but that doesn't work well for this programming demonstration.
 */
public interface RetailPointOfSale {
    /**
     * A {@link java.util.List List} of all tools available for rental. This method is not required
     *   to meet the programming demonstration requirements. I'm including it because it is required
     *   by the console based register user interface (also not required).
     *
     * @return A {@link java.util.List List} of
     *   {@link io.github.joelluellwitz.jl0624.exposed.service.api.ToolImpl Tools}.
     */
    List<Tool> listTools();

    /**
     * Checks out a {@link io.github.joelluellwitz.jl0624.exposed.service.api.ToolImpl Tool} to a
     *   customer using the provided
     *   {@link io.github.joelluellwitz.jl0624.exposed.service.api.ContractParameters}.
     *
     * @param contractParameters Represents the customer's choices used in building a rental
     *   contract. An object is used here because positional parameters become confusing after 1 or
     *   2 parameters. TODO: Consider using parameter builder pattern.
     * @return Represents of the exact terms of the customer
     *   {@link io.github.joelluellwitz.jl0624.exposed.service.api.RentalAgreement RentalAgreement}.
     */
    RentalAgreement checkout(ContractParameters contractParameters);
}

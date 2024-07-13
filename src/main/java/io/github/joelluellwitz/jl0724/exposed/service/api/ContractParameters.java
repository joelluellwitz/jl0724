/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.exposed.service.api;

import java.time.LocalDate;

/**
 * Contains the parameters to generate a contract during checkout.
 */
public class ContractParameters {
    // Note: I really don't think transfer objects should have private members. With this pattern, internal state is
    //   hidden in case one wants to add complexity to the class later. However, simple transfer objects, by their
    //   nature, really should never have any implementation details. I find it to be unnecessary abstraction. That
    //   said, having getters and setters for all properties is very much ingrained in Java culture, so I won't try to
    //   buck that trend here.
    private String toolCode;
    private LocalDate checkoutDate;
    private int rentalDayCount;
    private int discountPercent;

    /**
     * @return the toolCode
     */
    public String getToolCode() {
        return toolCode;
    }
    /**
     * Sets the case sensitive tool code.
     *
     * @param toolCode the toolCode to set
     */
    public void setToolCode(final String toolCode) {
        this.toolCode = toolCode;
    }
    /**
     * @return the checkoutDate
     */
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }
    /**
     * @param checkoutDate the checkoutDate to set
     */
    public void setCheckoutDate(final LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    /**
     * @return the rentalDayCount
     */
    public int getRentalDayCount() {
        return rentalDayCount;
    }
    /**
     * @param rentalDayCount the rentalDayCount to set
     */
    public void setRentalDayCount(final int rentalDayCount) {
        this.rentalDayCount = rentalDayCount;
    }
    /**
     * @return the discountPercent
     */
    public int getDiscountPercent() {
        return discountPercent;
    }
    /**
     * @param discountPercent the discountPercent to set
     */
    public void setDiscountPercent(final int discountPercent) {
        this.discountPercent = discountPercent;
    }
}
/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.exposed.service.api;

import java.util.Date;

/**
 * Contains the parameters to generate a contract during checkout.
 */
public class ContractParameters {
    // Note, that I really don't think transfer object should have private members. Internal state
    //   is hidden in case one wants to add complexity to the class later. However, transfer
    //   objects, by their nature, really should never have any implementation details. It is
    //   unnecessary abstraction. That said, having getters and setters for all properties is very
    //   much ingrained in Java culture, so I won't try to buck that trend here.
    private String toolCode;
    private Date checkoutDate;
    private int rentalDayCount;
    private int discountPercent;


    /**
     * @return the toolCode
     */
    public String getToolCode() {
        return toolCode;
    }
    /**
     * @param toolCode the toolCode to set
     */
    public void setToolCode(String toolCode) {
        this.toolCode = toolCode;
    }
	/**
	 * @return the checkoutDate
	 */
	public Date getCheckoutDate() {
		return checkoutDate;
	}
	/**
	 * @param checkoutDate the checkoutDate to set
	 */
	public void setCheckoutDate(Date checkoutDate) {
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
    public void setRentalDayCount(int rentalDayCount) {
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
    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }
}

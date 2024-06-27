/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.math.BigDecimal;

/**
 * TODO: Document.
 */
public class ToolDto {
    private String code;
    private String type;
    private String brand;
    private BigDecimal dailyRentalCharge;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }
    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }
    /**
     * @param brand the brand to set
     */
    public void setBrand(final String brand) {
        this.brand = brand;
    }
    /**
     * @return the dailyRentalCharge
     */
    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }
    /**
     * @param dailyRentalCharge the dailyRentalCharge to set
     */
    public void setDailyRentalCharge(final BigDecimal dailyRentalCharge) {
        this.dailyRentalCharge = dailyRentalCharge;
    }
}

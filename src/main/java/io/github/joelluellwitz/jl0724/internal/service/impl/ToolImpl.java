/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import java.math.BigDecimal;

import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;

/**
 * Exposed representation of a tool that can be rented.
 */
public class ToolImpl implements Tool {
    private int id;
    private String code;
    private String type;
    private String brand;
    private BigDecimal dailyCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;
    private boolean holidayCharge;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(final int id) {
        this.id = id;
    }
    /**
     * @return the code
     */
    @Override
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
    @Override
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
    @Override
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
     * @return the dailyCharge
     */
    @Override
    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }
    /**
     * @param dailyCharge the dailyCharge to set
     */
    public void setDailyCharge(final BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
    }
    /**
     * @return the weekdayCharge
     */
    @Override
    public boolean isWeekdayCharge() {
        return weekdayCharge;
    }
    /**
     * @param weekdayCharge the weekdayCharge to set
     */
    public void setWeekdayCharge(final boolean weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }
    /**
     * @return the weekendCharge
     */
    @Override
    public boolean isWeekendCharge() {
        return weekendCharge;
    }
    /**
     * @param weekendCharge the weekendCharge to set
     */
    public void setWeekendCharge(final boolean weekendCharge) {
        this.weekendCharge = weekendCharge;
    }
    /**
     * @return the holidayCharge
     */
    @Override
    public boolean isHolidayCharge() {
        return holidayCharge;
    }
    /**
     * @param holidayCharge the holidayCharge to set
     */
    public void setHolidayCharge(final boolean holidayCharge) {
        this.holidayCharge = holidayCharge;
    }
}

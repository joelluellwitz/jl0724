/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.exposed.service.api;

import java.math.BigDecimal;

/**
 * TODO: Document.
 */
public interface Tool {
    /**
     * @return the code
     */
    String getCode();
    /**
     * @return the type
     */
    String getType();
    /**
     * @return the brand
     */
    String getBrand();
    /**
     * @return the dailyCharge
     */
    BigDecimal getDailyCharge();
    /**
     * @return the weekdayCharge
     */
    boolean isWeekdayCharge();
    /**
     * @return the weekendCharge
     */
    boolean isWeekendCharge();
    /**
     * @return the holidayCharge
     */
    boolean isHolidayCharge();
}
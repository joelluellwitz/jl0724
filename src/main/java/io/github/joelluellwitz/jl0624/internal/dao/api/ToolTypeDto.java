/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * TODO: Document.
 */
@Entity(name = "ToolType")
@Table(name="tool_type")
public class ToolTypeDto {
    private int id;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String type;
    private BigDecimal dailyCharge;
    private LocalDate weekdayCharge;
    private LocalDate weekendCharge;
    private LocalDate holidayCharge;

    /**
     * @return the id
     */
    public final int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public final void setId(final int id) {
        this.id = id;
    }
    /**
     * @return the createdOn
     */
    public final LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @param createdOn the createdOn to set
     */
    public final void setCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    /**
     * @return the updatedOn
     */
    public final LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    /**
     * @param updatedOn the updatedOn to set
     */
    public final void setUpdatedOn(final LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    /**
     * @return the type
     */
    public final String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public final void setType(final String type) {
        this.type = type;
    }
    /**
     * @return the dailyCharge
     */
    public final BigDecimal getDailyCharge() {
        return dailyCharge;
    }
    /**
     * @param dailyCharge the dailyCharge to set
     */
    public final void setDailyCharge(final BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
    }
    /**
     * @return the weekdayCharge
     */
    public final LocalDate getWeekdayCharge() {
        return weekdayCharge;
    }
    /**
     * @param weekdayCharge the weekdayCharge to set
     */
    public final void setWeekdayCharge(final LocalDate weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }
    /**
     * @return the weekendCharge
     */
    public final LocalDate getWeekendCharge() {
        return weekendCharge;
    }
    /**
     * @param weekendCharge the weekendCharge to set
     */
    public final void setWeekendCharge(final LocalDate weekendCharge) {
        this.weekendCharge = weekendCharge;
    }
    /**
     * @return the holidayCharge
     */
    public final LocalDate getHolidayCharge() {
        return holidayCharge;
    }
    /**
     * @param holidayCharge the holidayCharge to set
     */
    public final void setHolidayCharge(final LocalDate holidayCharge) {
        this.holidayCharge = holidayCharge;
    }
}
/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * TODO: Document.
 */
@Entity(name = "ToolType")
@Table(name = "tool_type")
public class ToolTypeDto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private int id;
    @Version
    @Column(name="version", nullable=false)
    private int version;
    @CreationTimestamp
    @Column(name="created_on", nullable=false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name="updated_on", nullable=false)
    private LocalDateTime updatedOn;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="daily_charge", nullable=false)
    private BigDecimal dailyCharge;
    @Column(name="weekday_charge", nullable=false)
    private boolean weekdayCharge;
    @Column(name="weekend_charge", nullable=false)
    private boolean weekendCharge;
    @Column(name="holiday_charge", nullable=false)
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
    // Intentionally package private.
    void setId(final int id) {
        this.id = id;
    }
    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @param version the version to set
     */
    void setVersion(final int version) {
        this.version = version;
    }
    /**
     * @return the createdOn
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @param createdOn the createdOn to set
     */
    // Intentionally package private.
    void setCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    /**
     * @return the updatedOn
     */
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    /**
     * @param updatedOn the updatedOn to set
     */
    // Intentionally package private.
    void setUpdatedOn(final LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    // Intentionally package private.
    void setName(final String name) {
        this.name = name;
    }
    /**
     * @return the dailyCharge
     */
    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }
    /**
     * @param dailyCharge the dailyCharge to set
     */
    // Intentionally package private.
    void setDailyCharge(final BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
    }
    /**
     * @return the weekdayCharge
     */
    public boolean getWeekdayCharge() {
        return weekdayCharge;
    }
    /**
     * @param weekdayCharge the weekdayCharge to set
     */
    // Intentionally package private.
    void setWeekdayCharge(final boolean weekdayCharge) {
        this.weekdayCharge = weekdayCharge;
    }
    /**
     * @return the weekendCharge
     */
    public boolean getWeekendCharge() {
        return weekendCharge;
    }
    /**
     * @param weekendCharge the weekendCharge to set
     */
    // Intentionally package private.
    void setWeekendCharge(final boolean weekendCharge) {
        this.weekendCharge = weekendCharge;
    }
    /**
     * @return the holidayCharge
     */
    public boolean getHolidayCharge() {
        return holidayCharge;
    }
    /**
     * @param holidayCharge the holidayCharge to set
     */
    // Intentionally package private.
    void setHolidayCharge(final boolean holidayCharge) {
        this.holidayCharge = holidayCharge;
    }
}
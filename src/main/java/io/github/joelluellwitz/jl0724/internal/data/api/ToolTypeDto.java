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
 * The data tier representation of a tool type.
 */
@Entity(name = "ToolType")
@Table(name = "tool_type")
public class ToolTypeDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "daily_charge", nullable = false)
    private BigDecimal dailyCharge;
    @Column(name = "weekday_charge", nullable = false)
    private boolean weekdayCharge;
    @Column(name = "weekend_charge", nullable = false)
    private boolean weekendCharge;
    @Column(name = "holiday_charge", nullable = false)
    private boolean holidayCharge;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @return the createdOn
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @return the updatedOn
     */
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @return the dailyCharge
     */
    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }
    /**
     * @return true if there should be a charge for renting the tool during the weekday. false otherwise.
     */
    public boolean getWeekdayCharge() {
        return weekdayCharge;
    }
    /**
     * @return true if there should be a charge for renting the tool during the weekend. false otherwise.
     */
    public boolean getWeekendCharge() {
        return weekendCharge;
    }
    /**
     * @return true if there should be a charge for renting the tool during a holiday. false otherwise.
     */
    public boolean getHolidayCharge() {
        return holidayCharge;
    }
}
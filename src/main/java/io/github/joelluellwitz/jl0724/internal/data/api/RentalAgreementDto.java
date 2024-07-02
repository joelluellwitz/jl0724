/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * TODO: Document.
 */
@Entity(name="RentalAgreement")
@Table(name="rental_agreement")
public class RentalAgreementDto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private int id;
    @CreationTimestamp
    @Column(name="created_on", nullable=false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name="updated_on", nullable=false)
    private LocalDateTime updatedOn;
    // TODO: Add reference to tool.
    @Column(name="tool_code", nullable=false)
    private String toolCode;
    @Column(name="tool_type", nullable=false)
    private String toolType;
    @Column(name="tool_brand", nullable=false)
    private String toolBrand;
    @Column(name="daily_charge", nullable=false)
    private BigDecimal dailyCharge;
    @Column(name="rental_day_count", nullable=false)
    private int rentalDayCount;
    @Column(name="checkout_date", nullable=false)
    private LocalDate checkoutDate;
    @Column(name="discount_percent", nullable=false)
    private int discountPercent;
    @Column(name="due_date", nullable=false)
    private LocalDate dueDate;
    @Column(name="charge_day_count", nullable=false)
    private Integer chargeDayCount;
    @Column(name="pre_discount_charge", nullable=false)
    private BigDecimal preDiscountCharge;
    @Column(name="discount_amount", nullable=false)
    private BigDecimal discountAmount;
    @Column(name="final_charge", nullable=false)
    private BigDecimal finalCharge;

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
     * @return the createdOn
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(final LocalDateTime createdOn) {
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
    public void setUpdatedOn(final LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    /**
     * @return the toolCode
     */
    public String getToolCode() {
        return toolCode;
    }
    /**
     * @param toolCode the toolCode to set
     */
    public void setToolCode(final String toolCode) {
        this.toolCode = toolCode;
    }
    /**
     * @return the toolType
     */
    public String getToolType() {
        return toolType;
    }
    /**
     * @param toolType the toolType to set
     */
    public void setToolType(final String toolType) {
        this.toolType = toolType;
    }
    /**
     * @return the toolBrand
     */
    public String getToolBrand() {
        return toolBrand;
    }
    /**
     * @param toolBrand the toolBrand to set
     */
    public void setToolBrand(final String toolBrand) {
        this.toolBrand = toolBrand;
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
    public void setDailyCharge(final BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
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
    /**
     * @return the dueDate
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    /**
     * @return the chargeDayCount
     */
    public Integer getChargeDayCount() {
        return chargeDayCount;
    }
    /**
     * @param chargeDayCount the chargeDayCount to set
     */
    public void setChargeDayCount(final Integer chargeDayCount) {
        this.chargeDayCount = chargeDayCount;
    }
    /**
     * @return the preDiscountCharge
     */
    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }
    /**
     * @param preDiscountCharge the preDiscountCharge to set
     */
    public void setPreDiscountCharge(final BigDecimal preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }
    /**
     * @return the discountAmount
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    /**
     * @param discountAmount the discountAmount to set
     */
    public void setDiscountAmount(final BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    /**
     * @return the finalCharge
     */
    public BigDecimal getFinalCharge() {
        return finalCharge;
    }
    /**
     * @param finalCharge the finalCharge to set
     */
    public void setFinalCharge(final BigDecimal finalCharge) {
        this.finalCharge = finalCharge;
    }
}

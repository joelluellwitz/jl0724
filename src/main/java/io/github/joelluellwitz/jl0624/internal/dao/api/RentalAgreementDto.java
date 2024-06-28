/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * TODO: Document.
 */
public class RentalAgreementDto {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private BigDecimal dailyCharge;
    private int rentalDayCount;
    private LocalDate checkoutDate;
    private int discountPercent;
    private LocalDate dueDate;
    private Integer chargeDayCount;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

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

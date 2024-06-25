/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

import org.springframework.util.Assert;

import io.github.joelluellwitz.jl0624.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0624.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0624.exposed.service.api.Tool;

/**
 * TODO: Document
 */
public class RentalAgreementImpl implements RentalAgreement {
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final BigDecimal dailyRentalCharge;
    private final int rentalDayCount;
    private final Date checkoutDate;
    private final int discountPercent;

    private Date dueDate = null;
    private Integer chargeDayCount = null;
    private BigDecimal preDiscountCharge = null;
    private BigDecimal discountAmount = null;
    private BigDecimal finalCharge = null;

    // Intentionally package private.
    RentalAgreementImpl(final ContractParameters contractParameters,
        final Tool tool) {
        Assert.isTrue(contractParameters.getToolCode().equals(tool.getCode()),
                "Tool codes do not match.");

        toolCode = contractParameters.getToolCode();
        toolType = tool.getType();
        toolBrand = tool.getBrand();
        dailyRentalCharge = tool.getDailyRentalCharge();
        rentalDayCount = contractParameters.getRentalDayCount();
        checkoutDate = contractParameters.getCheckoutDate();
        discountPercent = contractParameters.getDiscountPercent();
    }

    /**
     * @return the toolCode
     */
    public String getToolCode() {
        return toolCode;
    }

    /**
     * @return the toolType
     */
    public String getToolType() {
        return toolType;
    }

    /**
     * @return the toolBrand
     */
    public String getToolBrand() {
        return toolBrand;
    }

    /**
     * @return the dailyRentalCharge
     */
    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    /**
     * @return the rentalDayCount
     */
    public int getRentalDayCount() {
        return rentalDayCount;
    }

    /**
     * @return the checkoutDate
     */
    public Date getCheckoutDate() {
        return checkoutDate;
    }

    /**
     * @return the discountPercent
     */
    public int getDiscountPercent() {
        return discountPercent;
    }


    /**
     * TODO: Document.
     *
     * @return the dueDate
     */
    public Date getDueDate() {
        if (dueDate == null) {
            LocalDate.from(getCheckoutDate().toInstant());
            dueDate = new Date(LocalDate.from(getCheckoutDate().toInstant()).plusDays(
                    getRentalDayCount()).getTime());  // TODO: Remove JodaTime
        }

        return dueDate;
    }

    /**
     * TODO: Document.
     *
     * @return the chargeDayCount
     */
    // TODO: Are we using Europing style counting or American style counting?
    // TODO: Consider switching all Dates to LocalDates.
    public Integer getChargeDayCount() {
        if (chargeDayCount == null) {
            final DateTime checkoutDateTime = new DateTime(getCheckoutDate());
            final DateTime dueDateTime = new DateTime(getDueDate());
            final Period rentalPeriod = new Period(checkoutDateTime, dueDateTime);
            final int rentalYearCount = rentalPeriod.getYears();

            final DateTime lastYearIndependenceDay = new DateTime(dueDateTime.getYear(), 7, 4, 0, 0);
            final int independenceDayCount;
            if (new Interval(checkoutDateTime.plusYears(rentalYearCount), dueDateTime).contains(
                    lastYearIndependenceDay)) {
                independenceDayCount = rentalYearCount + 1;
            }
            else {
                independenceDayCount = rentalYearCount;
            }

            final int totalDayCount = Days.daysBetween(checkoutDateTime.toLocalDate(), dueDateTime.toLocalDate()).getDays();

            chargeDayCount = new Interval(checkoutDateTime, dueDateTime);
        }

        return chargeDayCount;
    }

    /**
     * TODO: Document.
     *
     * @return the preDiscountCharge
     */
    public BigDecimal getPreDiscountCharge() {
        if (preDiscountCharge == null) {
            preDiscountCharge = getDailyRentalCharge().multiply(
                    BigDecimal.valueOf(getChargeDayCount()));
        }

        return preDiscountCharge;
    }

    /**
     * TODO: Document.
     *
     * @return the discountAmount
     */
    public BigDecimal getDiscountAmount() {
        if (discountAmount == null) {
            discountAmount = getPreDiscountCharge().multiply(
                    BigDecimal.valueOf(getDiscountPercent()));
        }

        return discountAmount;
    }

    /**
     * TODO: Document.
     *
     * @return the finalCharge
     */
    public BigDecimal getFinalCharge() {
        if (finalCharge == null) {
            finalCharge = preDiscountCharge.subtract(getDiscountAmount());
        }

        return finalCharge;
    }

    // TODO: Document
    @Override
    public void printRentalAgreement() {
        final StringBuilder agreementStringBuilder = new StringBuilder();
        agreementStringBuilder.append("Tool code: ").append(getToolCode()).append('\n');
        agreementStringBuilder.append("Tool type: ").append(getToolType()).append('\n');
        agreementStringBuilder.append("Tool brand: ").append(getToolBrand()).append('\n');
        agreementStringBuilder.append("Rental days: ").append(getRentalDayCount()).append('\n');
        agreementStringBuilder.append("Check out date: ").append(formatDate(getCheckoutDate())).append('\n');
        agreementStringBuilder.append("Due date: ").append(formatDate(getDueDate())).append('\n');
        agreementStringBuilder.append("Daily rental charge: ").append(
                formatCurrency(getDailyRentalCharge())).append('\n');
        agreementStringBuilder.append("Charge days: ").append(getChargeDayCount()).append('\n');
        agreementStringBuilder.append("Pre-discount charge: ").append(
                formatCurrency(getPreDiscountCharge())).append('\n');
        agreementStringBuilder.append("Discount percent: ").append(formatPercentage(getDiscountPercent())).append('\n');
        agreementStringBuilder.append("Discount amount: ").append(formatCurrency(getDiscountAmount())).append('\n');
        agreementStringBuilder.append("Final charge: ").append(formatCurrency(getFinalCharge())).append('\n');

        System.out.println(agreementStringBuilder.toString());  // TODO: Replace with logger?
    }
}
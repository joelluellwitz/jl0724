/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.springframework.util.Assert;

import io.github.joelluellwitz.jl0624.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0624.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0624.exposed.service.api.Tool;
import jakarta.annotation.Nonnull;

/**
 * TODO: Document
 */
public class RentalAgreementImpl implements RentalAgreement {
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("");

    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final BigDecimal dailyCharge;
    private final int rentalDayCount;
    private final LocalDate checkoutDate;
    private final int discountPercent;

    private LocalDate dueDate;
    private Integer chargeDayCount;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    // Intentionally package private.
    // TODO: Verified @Nonnull works with my IDE.
    RentalAgreementImpl(@Nonnull final ContractParameters contractParameters,
        final Tool tool) {
        Assert.isTrue(contractParameters.getToolCode().equals(tool.getCode()),
                "Tool codes do not match.");

        toolCode = contractParameters.getToolCode();
        toolType = tool.getType();
        toolBrand = tool.getBrand();
        dailyCharge = tool.getDailyCharge();
        rentalDayCount = contractParameters.getRentalDayCount();
        checkoutDate = contractParameters.getCheckoutDate();
        discountPercent = contractParameters.getDiscountPercent();
    }

    // TODO: Document
    @Override
    public void printRentalAgreement() {
        final StringBuilder agreementStringBuilder = new StringBuilder();
        agreementStringBuilder.append("Tool code: ").append(getToolCode()).append('\n');
        agreementStringBuilder.append("Tool type: ").append(getToolType()).append('\n');
        agreementStringBuilder.append("Tool brand: ").append(getToolBrand()).append('\n');
        agreementStringBuilder.append("Rental days: ").append(getRentalDayCount()).append('\n');
        agreementStringBuilder.append("Check out date: ");
        dateFormatter.formatTo(getCheckoutDate(), agreementStringBuilder);
        agreementStringBuilder.append('\n');
        agreementStringBuilder.append("Due date: ");
        dateFormatter.formatTo(getDueDate(), agreementStringBuilder);
        agreementStringBuilder.append('\n');
        agreementStringBuilder.append("Daily rental charge: ").append(
                formatCurrency(getDailyCharge())).append('\n');
        agreementStringBuilder.append("Charge days: ").append(getChargeDayCount()).append('\n');
        agreementStringBuilder.append("Pre-discount charge: ").append(
                formatCurrency(getPreDiscountCharge())).append('\n');
        agreementStringBuilder.append("Discount percent: ").append(formatPercentage(getDiscountPercent())).append('\n');
        agreementStringBuilder.append("Discount amount: ").append(formatCurrency(getDiscountAmount())).append('\n');
        agreementStringBuilder.append("Final charge: ").append(formatCurrency(getFinalCharge())).append('\n');

        System.out.println(agreementStringBuilder.toString());  // TODO: Replace with logger?
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
     * @return the dailyCharge
     */
    public BigDecimal getDailyCharge() {
        return dailyCharge;
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
    public LocalDate getCheckoutDate() {
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
    public LocalDate getDueDate() {
        if (dueDate == null) {
            dueDate = getCheckoutDate().plusDays(getRentalDayCount());
        }

        return dueDate;
    }

    /**
     * TODO: Document.
     *
     * For speed, I could remove some of the branches, but the method would harder to read. Usually, readability is more
     *   important.
     *
     * @return the chargeDayCount
     */
    // TODO: Are we using European style counting or American style counting?
    public Integer getChargeDayCount() {
        if (chargeDayCount == null) {
            final Period rentalPeriod = Period.between(getCheckoutDate(), getDueDate());
            final int rentalYearCount = rentalPeriod.getYears();

            final LocalDate lastYearIndependenceDay = LocalDate.of(getDueDate().getYear(), 7, 4);
            final int independenceDayCount;
            if (!getCheckoutDate().withYear(getDueDate().getYear()).isBefore(lastYearIndependenceDay)
                    && getDueDate().isAfter(lastYearIndependenceDay)) {
                independenceDayCount = rentalYearCount + 1;
            }
            else {
                independenceDayCount = rentalYearCount;
            }

            final LocalDate firstSeptemberDay = LocalDate.of(getDueDate().getYear(), 9, 1);
            final int firstSeptemberDayOfWeek = firstSeptemberDay.getDayOfWeek().getValue();
            final LocalDate lastYearLaborDay = firstSeptemberDay.withDayOfMonth((8 - firstSeptemberDayOfWeek) % 7);
            final int laborDayCount;
            if (!getCheckoutDate().withYear(getDueDate().getYear()).isBefore(lastYearLaborDay)
                    && getDueDate().isAfter(lastYearLaborDay)) {
                laborDayCount = rentalYearCount + 1;
            }
            else {
                laborDayCount = rentalYearCount;
            }

            // Week count can never be greater than rentalDayCount, so the long result can be safely casted back to an
            //   int.
            final int fullWeekCount = (int) ChronoUnit.WEEKS.between(getCheckoutDate(), getDueDate());
            final int realStartDayOfWeek = getCheckoutDate().getDayOfWeek().getValue();
            final int effectiveStartDayOfWeek;
            if (realStartDayOfWeek > DayOfWeek.FRIDAY.getValue()) {
                effectiveStartDayOfWeek = DayOfWeek.MONDAY.getValue();
            }
            else {
                effectiveStartDayOfWeek = getCheckoutDate().getDayOfWeek().getValue();
            }
            final int effectiveEndDayOfWeek = Math.max(getDueDate().getDayOfWeek().getValue(), 5);
            final int weekPortionCount = (effectiveEndDayOfWeek - effectiveStartDayOfWeek) % 5;
            final int weekDayCount = fullWeekCount * 5 + weekPortionCount;

            chargeDayCount = weekDayCount - independenceDayCount - laborDayCount;
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
            preDiscountCharge = getDailyCharge().multiply(BigDecimal.valueOf(getChargeDayCount()));
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

    /**
     * TODO: Document.
     *
     * @return
     */
    private String formatCurrency(final BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private String formatPercentage(final int percentage) {
        return NumberFormat.getPercentInstance(Locale.US).format(percentage);
    }
}
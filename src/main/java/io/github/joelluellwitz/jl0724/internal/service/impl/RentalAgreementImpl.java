/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import org.springframework.util.Assert;

import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;

/**
 * TODO: Document
 */
public class RentalAgreementImpl implements RentalAgreement {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uu");

    private final BigDecimal cent = new BigDecimal("0.01");
    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final BigDecimal dailyCharge;
    private final int rentalDayCount;
    private final LocalDate checkoutDate;
    private final int discountPercent;
    private boolean holidayCharge;
    private boolean weekdayCharge;
    private boolean weekendCharge;

    private LocalDate dueDate;
    private Integer chargeDayCount;
    private BigDecimal preDiscountCharge;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;
    private Integer weekdayCount;
    private String rentalAgreement;

    /**
     * TODO: Document.
     *
     * @param contractParameters
     * @param tool
     */
    // Intentionally package private.
    RentalAgreementImpl(final ContractParameters contractParameters, final Tool tool) {
        Assert.isTrue(contractParameters.getToolCode().equals(tool.getCode()), "Tool codes do not match.");

        // Copy all needed values in case 'tool' or 'contractParameters' are changed later.
        toolCode = tool.getCode();
        toolType = tool.getType();
        toolBrand = tool.getBrand();
        dailyCharge = tool.getDailyCharge();
        holidayCharge = tool.isHolidayCharge();
        weekdayCharge = tool.isWeekdayCharge();
        weekendCharge = tool.isWeekendCharge();
        rentalDayCount = contractParameters.getRentalDayCount();
        checkoutDate = contractParameters.getCheckoutDate();
        discountPercent = contractParameters.getDiscountPercent();
    }

    /**
     * TODO: Document
     */
    @Override
    public void printRentalAgreement() {
        System.out.print(toString());
    }

    /**
     * TODO:
     *
     * @return
     */
    @Override
    public String toString() {
        if (rentalAgreement == null) {
            final StringBuilder agreementStringBuilder = new StringBuilder();
            agreementStringBuilder.append("Tool code: ").append(getToolCode()).append('\n');
            agreementStringBuilder.append("Tool type: ").append(getToolType()).append('\n');
            agreementStringBuilder.append("Tool brand: ").append(getToolBrand()).append('\n');
            agreementStringBuilder.append("Rental days: ").append(getRentalDayCount()).append('\n');
            agreementStringBuilder.append("Check out date: ");
            DATE_FORMATTER.formatTo(getCheckoutDate(), agreementStringBuilder);
            agreementStringBuilder.append('\n');
            agreementStringBuilder.append("Due date: ");
            DATE_FORMATTER.formatTo(getDueDate(), agreementStringBuilder);
            agreementStringBuilder.append('\n');
            agreementStringBuilder.append("Daily rental charge: ").append(
                    formatCurrency(getDailyCharge())).append('\n');
            agreementStringBuilder.append("Charge days: ").append(getChargeDayCount()).append('\n');
            agreementStringBuilder.append("Pre-discount charge: ").append(
                    formatCurrency(getPreDiscountCharge())).append('\n');
            agreementStringBuilder.append("Discount percent: ").append(formatPercentage(getDiscountPercent())).append('\n');
            agreementStringBuilder.append("Discount amount: ").append(formatCurrency(getDiscountAmount())).append('\n');
            agreementStringBuilder.append("Final charge: ").append(formatCurrency(getFinalCharge())).append('\n');

            rentalAgreement = agreementStringBuilder.toString();
        }

        return rentalAgreement;
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
     * @return the chargeDayCount
     */
    public int getChargeDayCount() {
        if (chargeDayCount == null) {
            chargeDayCount = (weekendCharge ? getWeekendChargeDayCount() : 0)
                    + (weekdayCharge ? getWeekdayChargeDayCount() : 0);
        }

        return chargeDayCount.intValue();
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
            final BigDecimal discount = BigDecimal.valueOf(getDiscountPercent()).multiply(cent);
            final BigDecimal unroundedDiscountAmount = getPreDiscountCharge().multiply(discount);
            discountAmount = unroundedDiscountAmount.divide(cent, 0, RoundingMode.HALF_UP).multiply(cent);
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
            finalCharge = getPreDiscountCharge().subtract(getDiscountAmount());
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
    private String formatPercentage(final int discounPercent) {
        final BigDecimal discount = BigDecimal.valueOf(getDiscountPercent()).multiply(cent);
        return NumberFormat.getPercentInstance(Locale.US).format(discount);
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private int getWeekdayChargeDayCount() {
        final int weekdayChargeDayCount;
        if (holidayCharge) {
            weekdayChargeDayCount = getWeekdayCount();
        }
        else {
            weekdayChargeDayCount = getWeekdayCount() - getIndependenceDayCount() - getLaborDayCount();
        }

        return weekdayChargeDayCount;
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private int getWeekendChargeDayCount() {
        return rentalDayCount - getWeekdayCount();
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private int getWeekdayCount() {
        if (weekdayCount == null) {
            // These next two lines remove the weekends efficiently.
            final LocalDate effectiveCheckoutDate = getCheckoutDate().plusDays(
                    Math.max(0, ((12 - getCheckoutDate().plusDays(1).getDayOfWeek().getValue()) % 7) - 4) + 1);
            final LocalDate effectiveDueDate = getDueDate().plusDays(
                    1 - Math.max(0, getDueDate().getDayOfWeek().getValue() - 5));

            // Week count can never be greater than rentalDayCount, so the long result can be safely casted back to an
            //   int.
            final int weekCount = (int) ChronoUnit.WEEKS.between(effectiveCheckoutDate, effectiveDueDate);

            final int effectiveCheckoutDayOfWeek = effectiveCheckoutDate.getDayOfWeek().getValue();
            final int effectiveDueDayOfWeek = effectiveDueDate.getDayOfWeek().getValue();

            // Handles the case where the effective week starts on Monday and ends on Friday.
            final int additionalWeek = effectiveDueDayOfWeek - effectiveCheckoutDayOfWeek == 5 ? 1 : 0;

            final int weekPortionCount = Math.floorMod(effectiveDueDayOfWeek - effectiveCheckoutDayOfWeek, 5);
            weekdayCount = (weekCount + additionalWeek) * 5 + weekPortionCount;
        }

        return weekdayCount.intValue();
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private int getIndependenceDayCount() {
        final LocalDate firstYearIndependenceDay =
                findNearestWeekday(LocalDate.of(getCheckoutDate().plusDays(1).getYear(), 7, 4));
        final LocalDate lastYearIndependenceDay = findNearestWeekday(LocalDate.of(getDueDate().getYear(), 7, 4));

        return getHolidayCount(firstYearIndependenceDay, lastYearIndependenceDay);
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private int getLaborDayCount() {
        final LocalDate firstYearFirstSeptemberDay = LocalDate.of(getCheckoutDate().plusDays(1).getYear(), 9, 1);
        final int firstYearFirstSeptemberDayOfWeek = firstYearFirstSeptemberDay.getDayOfWeek().getValue();
        final LocalDate firstYearLaborDay =
                firstYearFirstSeptemberDay.withDayOfMonth((8 - firstYearFirstSeptemberDayOfWeek) % 7 + 1);

        final LocalDate lastYearFirstSeptemberDay = LocalDate.of(getDueDate().getYear(), 9, 1);
        final int lastYearFirstSeptemberDayOfWeek = lastYearFirstSeptemberDay.getDayOfWeek().getValue();
        final LocalDate lastYearLaborDay =
                lastYearFirstSeptemberDay.withDayOfMonth((8 - lastYearFirstSeptemberDayOfWeek) % 7 + 1);

        return getHolidayCount(firstYearLaborDay, lastYearLaborDay);
    }

    /**
     * TODO: Document.
     *
     * @param firstYearHolidayDate
     * @param lastYearHolidayDate
     * @return
     */
    private int getHolidayCount(final LocalDate firstYearHolidayDate, final LocalDate lastYearHolidayDate) {
        final int firstYear;
        if (getCheckoutDate().isBefore(firstYearHolidayDate)
                && firstYearHolidayDate.isBefore(getDueDate().plusDays(1))) {
            firstYear = getCheckoutDate().plusDays(1).getYear() - 1;
        }
        else {
            firstYear = getCheckoutDate().plusDays(1).getYear();
        }

        final int lastYear;
        if (getCheckoutDate().isBefore(lastYearHolidayDate)
                && lastYearHolidayDate.isBefore(getDueDate().plusDays(1))) {
            lastYear = getDueDate().getYear();
        }
        else {
            lastYear = getDueDate().getYear() - 1;
        }

        return Math.max(0, lastYear - firstYear);
    }

    /**
     * TODO: Document.
     *
     * @return
     */
    private LocalDate findNearestWeekday(final LocalDate date) {
        final LocalDate correctedDate;
        if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            correctedDate = date.minusDays(1);
        }
        else if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            correctedDate = date.plusDays(1);
        }
        else {
            correctedDate = date;
        }

        return correctedDate;
    }
}
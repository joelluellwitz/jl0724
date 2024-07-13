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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;

/**
 * Business logic tier representation of a rental agreement. This class is immutable.<p>
 *
 * For the purposes of this class, the "chargeable rental period" is defined as the period of time between the
 *   checkoutDate (exclusive) to the dueDate (inclusive).<p>
 *
 * Note: The requirements state that charge days are calculated "...from day after checkout through and including
 *   due date...". This means that the first day of the rental period is never a chargeable day. This calculation
 *   method seems very unidiomatic to me (I would expect the last day to not be chargeable) and I want to call this
 *   requirement out to the reviewer. This requirement is the reason for most of the "plusDays(1)" found throughout
 *   the class.<p>
 *
 * Note: I realize that, as written, this class cannot be constructed from an existing
 *   {@link io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto}. Of course, for the purposes of this
 *   demo application, there is no reason to construct a RentalAgreementImpl from a RentalAgreementDto. As this class is
 *   written, I can demonstrate immutable classes and saving computed results for later use. In my opinion, the benefits
 *   of demonstrating these concepts outweighs any disadvantages.
 */
public class RentalAgreementImpl implements RentalAgreement {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/uu");

    private static Logger LOGGER = LoggerFactory.getLogger(RentalAgreementImpl.class);

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
     * Constructs an immutable RentalAgreementImpl.
     *
     * @param contractParameters Contains the contract parameters as specified by the user.
     * @param tool The business logic tier representation of a tool.
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
     * {@inheritDoc}
     */
    @Override
    public void printRentalAgreement() {
        System.out.print(toString());
    }

    /**
     * {@inheritDoc}
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
            agreementStringBuilder.append("Daily rental charge: ").append(formatCurrency(getDailyCharge()))
                    .append('\n');
            agreementStringBuilder.append("Charge days: ").append(getChargeDayCount()).append('\n');
            agreementStringBuilder.append("Pre-discount charge: ").append(formatCurrency(getPreDiscountCharge()))
                    .append('\n');
            agreementStringBuilder.append("Discount percent: ").append(formatPercentage(getDiscountPercent()))
                    .append('\n');
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
     * Calculates and returns the tool due date.
     *
     * @return The due date.
     */
    public LocalDate getDueDate() {
        if (dueDate == null) {
            dueDate = getCheckoutDate().plusDays(getRentalDayCount());
            LOGGER.debug("Calculated due date: {}", dueDate.toString());
        }

        return dueDate;
    }

    /**
     * Calculates and returns the number of chargeable days during the rental period. The returned value excludes
     *   weekdays, weekends, and holidays if the tool being rented is set to not charge for weekdays, weekends, and
     *   holidays (respectively).
     *
     * @return The number of days that apply to the rental charge.
     */
    public int getChargeDayCount() {
        if (chargeDayCount == null) {
            chargeDayCount = (weekendCharge ? getWeekendChargeDayCount() : 0)
                    + (weekdayCharge ? getWeekdayChargeDayCount() : 0);
            LOGGER.debug("Calculated charge day count: {}", chargeDayCount);
        }

        return chargeDayCount.intValue();
    }

    /**
     * Calculates and returns the total rental charge before the discount is applied.
     *
     * @return The total rental charge before the discount is applied.
     */
    public BigDecimal getPreDiscountCharge() {
        if (preDiscountCharge == null) {
            preDiscountCharge = getDailyCharge().multiply(BigDecimal.valueOf(getChargeDayCount()));
            LOGGER.debug("Calculated pre-discount charge: {}", preDiscountCharge);
        }

        return preDiscountCharge;
    }

    /**
     * Calculates and returns the discount used to find the final rental charge.
     *
     * @return The discount amount.
     */
    public BigDecimal getDiscountAmount() {
        if (discountAmount == null) {
            final BigDecimal discount = BigDecimal.valueOf(getDiscountPercent()).multiply(cent);
            final BigDecimal unroundedDiscountAmount = getPreDiscountCharge().multiply(discount);
            discountAmount = unroundedDiscountAmount.divide(cent, 0, RoundingMode.HALF_UP).multiply(cent);
            LOGGER.debug("Calculated discount amount: {}", discountAmount);
        }

        return discountAmount;
    }

    /**
     * Calculates and returns the final rental charge after the discount is applied.
     *
     * @return The final rental charge.
     */
    public BigDecimal getFinalCharge() {
        if (finalCharge == null) {
            finalCharge = getPreDiscountCharge().subtract(getDiscountAmount());
            LOGGER.debug("Calculated final charge amount: {}", finalCharge);
        }

        return finalCharge;
    }

    /**
     * Formats a currency amount assuming a United States of America locale.
     *
     * @return The formatted currency amount.
     */
    private String formatCurrency(final BigDecimal amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    /**
     * Formats a percentage assuming a United States of America locale.
     *
     * @return The formatted percentage.
     */
    private String formatPercentage(final int discounPercent) {
        final BigDecimal discount = BigDecimal.valueOf(getDiscountPercent()).multiply(cent);
        return NumberFormat.getPercentInstance(Locale.US).format(discount);
    }

    /**
     * Calculates and returns the number of days that occur on weekdays during the chargeable rental period. The
     *   returned value excludes holidays if the tool being rented is set to not charge for holidays.
     *
     * @return The number of chargeable weekdays.
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
     * Calculates and returns the number of weekend days during the chargeable rental period. It is assumed that no
     *   holidays are celebrated on a weekend.
     *
     * @return The number of chargeable weekend days.
     */
    private int getWeekendChargeDayCount() {
        return rentalDayCount - getWeekdayCount();
    }

    /**
     * Calculates and returns the total number of weekdays during the chargeable rental period. Holidays are not
     *   considered by this method.
     *
     * @return The number of weekdays during the chargeable rental period.
     */
    private int getWeekdayCount() {
        if (weekdayCount == null) {
            // These next two lines remove the weekends efficiently. It leverages the fact that DayOfWeek enums are
            //   backed by ordered integers. This ordering is documented in official Java documentation.
            final LocalDate effectiveCheckoutDate = getCheckoutDate().plusDays(
                    Math.max(0, ((12 - getCheckoutDate().plusDays(1).getDayOfWeek().getValue()) % 7) - 4) + 1);
            final LocalDate effectiveDueDate = getDueDate()
                    .plusDays(1 - Math.max(0, getDueDate().getDayOfWeek().getValue() - 5));

            // Week count can never be greater than rentalDayCount, so the 'long' result can be safely casted back to an
            //   'int'.
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
     * Calculates and returns the number of Independence Days during the chargeable rental period.
     *
     * @return The number of Independence Days during the chargeable rental period.
     */
    private int getIndependenceDayCount() {
        final LocalDate firstYearIndependenceDay = findNearestWeekday(
                LocalDate.of(getCheckoutDate().plusDays(1).getYear(), 7, 4));
        final LocalDate lastYearIndependenceDay = findNearestWeekday(LocalDate.of(getDueDate().getYear(), 7, 4));

        return getHolidayCount(firstYearIndependenceDay, lastYearIndependenceDay);
    }

    /**
     * Calculates and returns the number of Labor Days during the chargeable rental period.
     *
     * @return The number of Labor Days during the rental period.
     */
    private int getLaborDayCount() {
        final LocalDate firstYearFirstSeptemberDay = LocalDate.of(getCheckoutDate().plusDays(1).getYear(), 9, 1);
        final int firstYearFirstSeptemberDayOfWeek = firstYearFirstSeptemberDay.getDayOfWeek().getValue();
        final LocalDate firstYearLaborDay = firstYearFirstSeptemberDay
                .withDayOfMonth((8 - firstYearFirstSeptemberDayOfWeek) % 7 + 1);

        final LocalDate lastYearFirstSeptemberDay = LocalDate.of(getDueDate().getYear(), 9, 1);
        final int lastYearFirstSeptemberDayOfWeek = lastYearFirstSeptemberDay.getDayOfWeek().getValue();
        final LocalDate lastYearLaborDay = lastYearFirstSeptemberDay
                .withDayOfMonth((8 - lastYearFirstSeptemberDayOfWeek) % 7 + 1);

        return getHolidayCount(firstYearLaborDay, lastYearLaborDay);
    }

    /**
     * Helper method to determine the number of specific holidays during the chargeable rental period. It is assumed
     *   that there is one specific holiday per year.
     *
     * @param firstYearHolidayDate The date of the specific holiday in the first year of the chargeable rental period.
     *   This date might fall before the chargeable rental period.
     * @param lastYearHolidayDate The date of the specific holiday in the last year of the chargeable rental period.
     *   This date might fall after the chargeable rental period.
     * @return The number of specific holidays during the chargeable rental period.
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
        if (getCheckoutDate().isBefore(lastYearHolidayDate) && lastYearHolidayDate.isBefore(getDueDate().plusDays(1))) {
            lastYear = getDueDate().getYear();
        }
        else {
            lastYear = getDueDate().getYear() - 1;
        }

        return Math.max(0, lastYear - firstYear);
    }

    /**
     * Calculates and returns the weekday date that is closest to the weekend date. Used to determine the observed
     *   holiday date for some holidays.
     *
     * @return The weekday date that is closet to the weekend date.
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
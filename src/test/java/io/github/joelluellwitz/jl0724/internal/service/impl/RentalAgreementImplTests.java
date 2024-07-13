/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;

/**
 * Tests {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl}.<p>
 *
 * Note: There is admittedly a very large amount of unit tests here, but date math is confusing and complex,
 *   particularly when rental durations span multiple years. I added enough tests to make myself certain I implemented
 *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl#getChargeDayCount
 *   getChargeDayCount} correctly. Good thing too, because I caught many errors in my initial implementation.
 */
public class RentalAgreementImplTests {

    /**
     * Verifies the due date is calculated correctly when the rental duration is 1 day.
     */
    @Test
    public void getDueDateCalculates1DayLater() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 1));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final LocalDate dueDate = new RentalAgreementImpl(contractParameters, tool).getDueDate();

        assertThat(dueDate).isEqualTo(LocalDate.of(2024, 7, 2));
    }

    /**
     * Verifies the due date is calculated correctly when the rental duration is 1000 days.
     */
    @Test
    public void getDueDateCalculates1000DaysLater() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 1));
        contractParameters.setRentalDayCount(1000);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final LocalDate dueDate = new RentalAgreementImpl(contractParameters, tool).getDueDate();

        assertThat(dueDate).isEqualTo(LocalDate.of(2027, 3, 28));
    }

    /**
     * Verifies the due date calculation is saved to avoid recalculation.
     */
    @Test
    public void getDueDateSavesResult() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final LocalDate dueDate0 = rentalAgreement.getDueDate();
        final LocalDate dueDate1 = rentalAgreement.getDueDate();

        assertThat(dueDate0).isSameAs(dueDate1);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day is Friday, skipping the weekend, and ending
     *   on Wednesday (inclusive). This is a weekend edge case test.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingThursday6DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 27));
        contractParameters.setRentalDayCount(6);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(4);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.WEDNESDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day would be Saturday except weekends are not
     *   chargeable, so the chargeable days are Monday through Thursday (inclusive). (Holidays are chargeable.) This is
     *   a weekend edge case test.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingFriday6DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 28));
        contractParameters.setRentalDayCount(6);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(4);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day would be Sunday except weekends are not
     *   chargeable, so the chargeable days are Monday through Thursday (inclusive). (Holidays are chargeable.) This is
     *   a weekend edge case test.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingSaturday5DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 29));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(4);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
    }

    /**
     * Counts the number of chargeable days, where the chargeable days are Monday through Thursday (inclusive).
     *   (Holidays are chargeable.) This is a weekend edge case test.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingSunday4DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(4);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(4);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
    }

    /**
     * Counts the number of chargeable days, where the chargeable days are Tuesday through Friday (inclusive).
     *   (Holidays are chargeable.) This is a weekend edge case test.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingMonday4DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 1));
        contractParameters.setRentalDayCount(4);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(4);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day is Wednesday, skipping the weekend, and ending
     *   on Monday (inclusive). (Holidays are chargeable.) This is a weekend edge case test.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingTuesday6DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 2));
        contractParameters.setRentalDayCount(6);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(4);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day is Friday, skipping the weekend, and ending
     *   on Thursday (inclusive). (Holidays are chargeable.) This test is <i>edge case</i> testing for the special case
     *   where the first chargeable day is a Monday and the last chargeable day is a Friday.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingThursday7DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 27));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(5);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day would be Saturday except weekends are not
     *   chargeable, so the chargeable days are Monday through Friday (inclusive). (Holidays are chargeable.) This test
     *   is edge case testing for the special case where the first chargeable day is a Monday and the last chargeable
     *   day is a Friday.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingFriday7DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 28));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(5);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day would be Saturday and the last chargeable
     *   day would be Sunday, except weekends are not chargeable, so the chargeable days are Monday through Friday
     *   (inclusive). (Holidays are chargeable.) This test is mostly testing that weekends are truncated correctly.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingFriday9DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 28));
        contractParameters.setRentalDayCount(9);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(5);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day would be Sunday and the last chargeable day
     *   would be Saturday, except weekends are not chargeable, so the chargeable days are Monday through Friday
     *   (inclusive). (Holidays are chargeable.) This test is edge case testing for the special case where the first
     *   chargeable day is a Monday and the last chargeable day is a Friday.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingSaturday7DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 29));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(5);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.SATURDAY);
    }

    /**
     * Counts the number of chargeable days, where the chargeable days are Monday through Friday (inclusive). (Holidays
     *   are chargeable.) This test is edge case testing for the special case where the first chargeable day is a Monday
     *   and the last chargeable day is a Friday.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingSunday5DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(5);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }

    /**
     * Counts the number of chargeable days, where the first chargeable day is Tuesday, skipping the weekend, and ending
     *   on Monday (inclusive). (Holidays are chargeable.) This test is <i>edge case</i> testing for the special case
     *   where the first chargeable day is a Monday and the last chargeable day is a Friday.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingMonday7DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 1));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(5);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    /**
     * Counts the number of chargeable days over a period of multiple weeks, where the first chargeable day would be
     *   Saturday and the last chargeable day would be Sunday, except weekends are not chargeable, so the actual
     *   chargeable period begins on a Monday and ends on a Friday. (Holidays are chargeable.) This test is verifying
     *   weekend truncation and the special case where the chargeable period starts on a Monday and ends on a Friday
     *   both still work correctly with a multi-week duration.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingFriday23DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 28));
        contractParameters.setRentalDayCount(23);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(15);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.SUNDAY);
    }

    /**
     * Counts the number of chargeable days over a period of multiple weeks, where the first chargeable day would be
     *   Sunday and the last chargeable day would be Saturday, except weekends are not chargeable, so the actual
     *   chargeable period begins on a Monday and ends on a Friday. (Holidays are chargeable.) This test is verifying
     *   weekend truncation and the special case where the chargeable period starts on a Monday and ends on a Friday
     *   both still work correctly with a multi-week duration.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingSaturday21DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 29));
        contractParameters.setRentalDayCount(21);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(15);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.SATURDAY);
    }

    /**
     * Counts the number of chargeable days over a period of multiple weeks, where the first chargeable day is a Monday
     *   and the last chargeable is a Friday. (Holidays are chargeable.) This test is verifying that the special case
     *   where the chargeable period starts on a Monday and ends on a Friday still works correctly with a multi-week
     *   duration.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingSunday19DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(19);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(15);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }

    /**
     * Counts the number of chargeable days over a period of multiple weeks, where the first chargeable day is a Friday
     *   and the last chargeable is a Monday. (Holidays are chargeable.) This test is verifying that multi-week
     *   chargeable rental periods are still counted correctly even when the special case where the chargeable period
     *   starts on a Monday and ends on a Friday does not apply.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingThursday25DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 27));
        contractParameters.setRentalDayCount(25);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(17);
    }

    /**
     * Verifies that weekday counting is accurate over a multi-year rental period.
     */
    @Test
    public void getChargeDayCountCountingWeekdaysStartingWednesday1000DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 26));
        contractParameters.setRentalDayCount(1000);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(714);
    }

    /**
     * Verifies that no holidays are excluded from the chargeable day count over a large rental period that starts and
     *   ends within the same year. This test is <i>edge case</i> testing that multi-year holiday counting logic is
     *   correct.
     */
    @Test
    public void getChargeDayCountNoHolidaysSameYear() {
        final int dayCount = 90;

        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 10, 1));
        contractParameters.setRentalDayCount(dayCount);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(dayCount);
        assertThat(rentalAgreement.getDueDate().getYear()).isEqualTo(rentalAgreement.getCheckoutDate().getYear());
    }

    /**
     * Verifies that no holidays are excluded from the chargeable day count over a large rental period that starts in
     *   one year and ends in the next year. This test is <i>edge case</i> testing that multi-year holiday counting
     *   logic is correct.
     */
    @Test
    public void getChargeDayCountNoHolidaysTwoYears() {
        final int dayCount = 272;

        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 10, 1));
        contractParameters.setRentalDayCount(dayCount);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(dayCount);
        assertThat(rentalAgreement.getDueDate().getYear()).isEqualTo(2025);
    }

    /**
     * Independence Day is not charged with a single day rental where Independence Day falls on a weekday. This test
     *   verifies that the identification of Independence Day is exactly right.
     */
    @Test
    public void getChargeDayCountSkippingIndependenceDayOnWeekday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(0);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
    }

    /**
     * Verifies Independence Day is not charged when Independence Day is moved to a Monday.
     */
    @Test
    public void getChargeDayCountSkippingIndependenceDayMovedToMonday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2021, 7, 4));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(0);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
    }

    /**
     * Verifies Independence Day is not charged when Independence Day is moved to a Friday.
     */
    @Test
    public void getChargeDayCountSkippingIndependenceDayMovedToFriday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2020, 7, 2));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(0);
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
    }

    /**
     * Verifies the second Independence Day is calculated correctly on a weekday when two Independence Days fall within
     *   the chargeable rental period.
     */
    @Test
    public void getChargeDayCountSkippingSecondIndependenceDayOnWeekday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 3));
        contractParameters.setRentalDayCount(366);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2025, 7, 4));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        // Holiday count includes 1 Labor Day.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(363);
    }

    /**
     * Verifies the second Independence Day is calculated correctly when two Independence Days fall within
     *   the chargeable rental period and the second Independence Day is moved to a Monday.
     */
    @Test
    public void getChargeDayCountSkippingSecondIndependenceDayMovedToMonday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2020, 7, 2));
        contractParameters.setRentalDayCount(368);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2021, 7, 5));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        // Holiday count includes 1 Labor Day.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(365);
    }

    /**
     * Verifies the second Independence Day is calculated correctly when two Independence Days fall within
     *   the chargeable rental period and the second Independence Day is moved to a Friday.
     */
    @Test
    public void getChargeDayCountSkippingSecondIndependenceDayMovedToFriday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2019, 7, 3));
        contractParameters.setRentalDayCount(366); // Remember, 2020 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2020, 7, 3));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
        // Holiday count includes 1 Labor Day.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(363);
    }

    /**
     * Verifies three Independence Days are not charged when the chargeable rental period includes three Independence
     *   Days.
     */
    @Test
    public void getChargeDayCountSkippingThreeInpedenceDays() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2028, 7, 3));
        contractParameters.setRentalDayCount(731);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2030, 7, 4));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);
        // Holiday count includes 2 Labor Days.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(726);
    }

    /**
     * Verifies Labor Day is not charged with a single day rental where Labor Day falls on the first day of September.
     */
    @Test
    public void getChargeDayCountSkippingLaborDayOnSept1() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2025, 8, 31));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(0);
    }

    /**
     * Verifies Labor Day is not charged with a single day rental where Labor Day falls on the second day of September.
     */
    @Test
    public void getChargeDayCountSkippingLaborDayOnSept2() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 9, 1));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(0);
    }

    /**
     * Verifies Labor Day is not charged with a single day rental where Labor Day falls on the seventh day of September.
     */
    @Test
    public void getChargeDayCountSkippingLaborDayOnSept7() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2020, 9, 6));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(0);
    }

    /**
     * Verifies two Labor Days are not charged when two Labor Days fall within the chargeable rental period and the
     *   second Labor Day falls on the first day of September.
     */
    @Test
    public void getChargeDayCountSkippingSecondLaborDayOnSept1() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 9, 1));
        contractParameters.setRentalDayCount(365);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2025, 9, 1));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        // Holiday count includes 1 Independence Day.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(362);
    }

    /**
     * Verifies two Labor Days are not charged when two Labor Days fall within the chargeable rental period and the
     *   second Labor Day falls on the second day of September.
     */
    @Test
    public void getChargeDayCountSkippingSecondLaborDayOnSept2() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2023, 9, 3));
        contractParameters.setRentalDayCount(365); // Remember, 2024 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2024, 9, 2));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        // Holiday count includes 1 Independence Day.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(362);
    }

    /**
     * Verifies two Labor Days are not charged when two Labor Days fall within the chargeable rental period and the
     *   second Labor Day falls on the seventh day of September.
     */
    @Test
    public void getChargeDayCountSkippingSecondLaborDayOnSept7() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2019, 9, 1));
        contractParameters.setRentalDayCount(372); // Remember, 2020 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2020, 9, 7));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        // Holiday count includes 1 Independence Day.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(369);
    }

    /**
     * Verifies four Labor Days are not charged when the chargeable rental period includes four Labor Days.
     */
    @Test
    public void getChargeDayCountSkippingFourLaborDays() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2023, 9, 3));
        contractParameters.setRentalDayCount(1100); // Remember, 2024 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2026, 9, 7));
        assertThat(rentalAgreement.getDueDate().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        // Holiday count includes 3 Independence Days.
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(1093);
    }

    /**
     * Tests holidays are not chargeable days when the chargeable rental period starts on the first day of one year and
     *   ends on the last day of the next year.<p>
     *
     * Note: This test broke my original implementation of {@link
     *   io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl#getHolidayCount getHolidayCount}, so I
     *   thought it was a good idea to keep this test.
     */
    @Test
    public void getChargeDayCountSkippingHolidaysWithDatesAtYearEnds() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2023, 12, 31));
        contractParameters.setRentalDayCount(731); // Remember, 2024 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        assertThat(rentalAgreement.getDueDate()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(rentalAgreement.getChargeDayCount()).isEqualTo(727);
    }

    /**
     * Verifies weekends are charged correctly over a multi-year rental period.<p>
     *
     * Note: This test case makes the assumption that the algorithm used to calculate chargeable weekend days is simply
     *   the rental duration minus the number of weekdays in that period (not including the checkout day). Generally, it
     *   is a good idea for unit tests to not rely on knowledge of the underlying implementation, but not making this
     *   assumption would require me to create an additional 16 tests. I think the maintainability benefits of relying
     *   on implementation details outweighs the benefits provided by these additional tests.
     */
    @Test
    public void getChargeDayCountCountingWeekendDaysStartingWednesday1000DayDuration() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 26));
        contractParameters.setRentalDayCount(1000);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(false);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(true);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(286);
    }

    /**
     * Verifies the correct calculation of a pre-discount charge.
     */
    @Test
    public void getPreDiscountChargeCalculatesCorrectResult() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal preDiscountCharge = new RentalAgreementImpl(contractParameters, tool).getPreDiscountCharge();

        assertThat(preDiscountCharge).isEqualTo(new BigDecimal("5.96"));
    }

    /**
     * Verifies the correct calculation of a pre-discount charge when the chargeable days are 0. (Edge case testing.)
     */
    @Test
    public void getPreDiscountChargeCalculatesCorrectResultWithZeroChargeDays() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(false);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal preDiscountCharge = new RentalAgreementImpl(contractParameters, tool).getPreDiscountCharge();

        assertThat(preDiscountCharge).isEqualTo(new BigDecimal("0.00"));
    }

    /**
     * Verifies the correct calculation of a pre-discount charge when the daily charge is 0. (Edge case testing.)
     */
    @Test
    public void getPreDiscountChargeCalculatesCorrectResultWithZeroDailyCharge() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("0.00"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(true);

        final BigDecimal preDiscountCharge = new RentalAgreementImpl(contractParameters, tool).getPreDiscountCharge();

        assertThat(preDiscountCharge).isEqualTo(new BigDecimal("0.00"));
    }

    /**
     * Verifies the Pre-discount amount calculation is saved to avoid recalculation.
     */
    @Test
    public void getPreDiscountChargeSavesResult() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final BigDecimal preDiscountCharge0 = rentalAgreement.getPreDiscountCharge();
        final BigDecimal preDiscountCharge1 = rentalAgreement.getPreDiscountCharge();

        assertThat(preDiscountCharge0).isSameAs(preDiscountCharge1);
    }

    /**
     * Verifies the discount amount is calculated correctly when the discount percent is 0. (Edge case testing.)
     */
    @Test
    public void getDiscountAmountCalculatesNoDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(0);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal discountAmount = new RentalAgreementImpl(contractParameters, tool).getDiscountAmount();

        assertThat(discountAmount).isEqualTo("0.00");
    }

    /**
     * Verifies the discount amount is calculated correctly when the discount percent is 1.
     */
    @Test
    public void getDiscountAmountCalculates1PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(1);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal discountAmount = new RentalAgreementImpl(contractParameters, tool).getDiscountAmount();

        assertThat(discountAmount).isEqualTo("0.06");
    }

    /**
     * Verifies the discount amount is calculated correctly when the discount percent is 99.
     */
    @Test
    public void getDiscountAmountCalculates99PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(99);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal discountAmount = new RentalAgreementImpl(contractParameters, tool).getDiscountAmount();

        assertThat(discountAmount).isEqualTo("5.90");
    }

    /**
     * Verifies the discount amount is calculated correctly when the discount percent is 100. (Edge case testing.)
     */
    @Test
    public void getDiscountAmountCalculates100PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(100);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal discountAmount = new RentalAgreementImpl(contractParameters, tool).getDiscountAmount();

        assertThat(discountAmount).isEqualTo("5.96");
    }

    /**
     * Verifies the discount amount fractional cent rounding is correct.
     */
    @Test
    public void getDiscountAmountRoundsUpHalfCents() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 1));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(50);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.05"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal discountAmount = new RentalAgreementImpl(contractParameters, tool).getDiscountAmount();

        assertThat(discountAmount).isEqualTo("0.53");
    }

    /**
     * Verifies the discount amount calculation is saved to avoid recalculation.
     */
    @Test
    public void getDiscountAmountSavesResult() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final BigDecimal discountAmount0 = rentalAgreement.getDiscountAmount();
        final BigDecimal discountAmount1 = rentalAgreement.getDiscountAmount();

        assertThat(discountAmount0).isSameAs(discountAmount1);
    }

    /**
     * Verifies the correct calculation of the final charge when there is no discount. (Edge case testing.)
     */
    @Test
    public void getFinalChargeWithNoDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(0);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal finalCharge = new RentalAgreementImpl(contractParameters, tool).getFinalCharge();

        assertThat(finalCharge).isEqualTo("5.96");
    }

    /**
     * Verifies the correct calculation of the final charge when there is a 1% discount.
     */
    @Test
    public void getFinalChargeWith1PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(1);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal finalCharge = new RentalAgreementImpl(contractParameters, tool).getFinalCharge();

        assertThat(finalCharge).isEqualTo("5.90");
    }

    /**
     * Verifies the correct calculation of the final charge when there is a 99% discount.
     */
    @Test
    public void getFinalChargeWith99PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(99);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal finalCharge = new RentalAgreementImpl(contractParameters, tool).getFinalCharge();

        assertThat(finalCharge).isEqualTo("0.06");
    }

    /**
     * Verifies the correct calculation of the final charge when there is a 100% discount. (Edge case testing.)
     */
    @Test
    public void getFinalChargeWith100PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 6, 30));
        contractParameters.setRentalDayCount(7);
        contractParameters.setDiscountPercent(100);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal finalCharge = new RentalAgreementImpl(contractParameters, tool).getFinalCharge();

        assertThat(finalCharge).isEqualTo("0.00");
    }

    /**
     * Verifies the final charge correctly reflects the discount amount rounding requirement.
     */
    @Test
    public void getFinalChargeWithRoundsDownHalfCents() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 7, 1));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(50);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.05"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(false);

        final BigDecimal finalCharge = new RentalAgreementImpl(contractParameters, tool).getFinalCharge();

        assertThat(finalCharge).isEqualTo("0.52");
    }

    /**
     * Verifies the final charge calculation is saved to avoid recalculation.
     */
    @Test
    public void getFinalChargeSavesResult() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final BigDecimal finalCharge0 = rentalAgreement.getFinalCharge();
        final BigDecimal finalCharge1 = rentalAgreement.getFinalCharge();

        assertThat(finalCharge0).isSameAs(finalCharge1);
    }

    /**
     * Verifies accurate rental agreement text is generated.
     */
    @Test
    public void toStringSucceeds() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final String expectedRentalAgreement =
                "Tool code: CHNS\n"
                + "Tool type: Chainsaw\n"
                + "Tool brand: Stihl\n"
                + "Rental days: 5\n"
                + "Check out date: 07/02/15\n"
                + "Due date: 07/07/15\n"
                + "Daily rental charge: $1.49\n"
                + "Charge days: 3\n"
                + "Pre-discount charge: $4.47\n"
                + "Discount percent: 25%\n"
                + "Discount amount: $1.12\n"
                + "Final charge: $3.35\n";

        final String rentalAgreementString = new RentalAgreementImpl(contractParameters, tool).toString();

        assertThat(rentalAgreementString).isEqualTo(expectedRentalAgreement);
    }

    /**
     * Verifies the rental agreement String generation is saved to avoid recalculation.
     */
    @Test
    public void toStringSavesResult() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final String rentalAgreementString0 = rentalAgreement.toString();
        final String rentalAgreementString1 = rentalAgreement.toString();

        assertThat(rentalAgreementString0).isSameAs(rentalAgreementString1);
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl#printRentalAgreement
     *   RentalAgreementImpl#printRentalAgreement} prints accurate rental agreement text to
     *   {@link java.lang.System#out System#out}.
     */
    @Test
    public void printRentalAgreementSucceeds() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(false);
        tool.setHolidayCharge(true);

        final String expectedRentalAgreement =
                "Tool code: CHNS\n"
                + "Tool type: Chainsaw\n"
                + "Tool brand: Stihl\n"
                + "Rental days: 5\n"
                + "Check out date: 07/02/15\n"
                + "Due date: 07/07/15\n"
                + "Daily rental charge: $1.49\n"
                + "Charge days: 3\n"
                + "Pre-discount charge: $4.47\n"
                + "Discount percent: 25%\n"
                + "Discount amount: $1.12\n"
                + "Final charge: $3.35\n";

        final PrintStream originalSystemOut = System.out;
        try {
            final ByteArrayOutputStream standardOutputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(standardOutputStream));

            new RentalAgreementImpl(contractParameters, tool).printRentalAgreement();

            assertThat(new String(standardOutputStream.toByteArray())).isEqualTo(expectedRentalAgreement);
        }
        finally {
            System.setOut(originalSystemOut);
        }
    }
}
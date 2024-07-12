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
 * Tests {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl}.
 */
public class RentalAgreementImplTests {

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(4);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(4);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(4);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(4);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(4);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(4);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(5);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(5);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(5);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(5);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(5);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(5);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(15);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(15);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(15);
    }

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

    @Test
    public void getChargeDayCountNoHolidaysSameYear() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 10, 1));
        contractParameters.setRentalDayCount(90);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(90);
    }

    @Test
    public void getChargeDayCountNoHolidaysTwoYears() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2024, 10, 1));
        contractParameters.setRentalDayCount(272);
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(272);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(0);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(0);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(0);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2025, 7, 4));
        assertThat(LocalDate.of(2025, 7, 4).getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);

        // Holiday count includes 1 Labor Day.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(363);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2021, 7, 5));
        assertThat(LocalDate.of(2021, 7, 5).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        // Holiday count includes 1 Labor Day.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(365);
    }

    @Test
    public void getChargeDayCountSkippingSecondIndependenceDayMovedToFriday() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2019, 7, 3));
        contractParameters.setRentalDayCount(366);  // Remember, 2020 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2020, 7, 3));
        assertThat(LocalDate.of(2020, 7, 3).getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);

        // Holiday count includes 1 Labor Day.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(363);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2030, 7, 4));
        assertThat(LocalDate.of(2030, 7, 4).getDayOfWeek()).isEqualTo(DayOfWeek.THURSDAY);

        // Holiday count includes 2 Labor Days.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(726);
    }

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

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2025, 9, 1));
        assertThat(LocalDate.of(2025, 9, 1).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        // Holiday count includes 1 Independence Day.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(362);
    }

    @Test
    public void getChargeDayCountSkippingSecondLaborDayOnSept2() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2023, 9, 3));
        contractParameters.setRentalDayCount(365);  // Remember, 2024 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2024, 9, 2));
        assertThat(LocalDate.of(2024, 9, 2).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        // Holiday count includes 1 Independence Day.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(362);
    }

    @Test
    public void getChargeDayCountSkippingSecondLaborDayOnSept7() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2019, 9, 1));
        contractParameters.setRentalDayCount(372);  // Remember, 2020 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2020, 9, 7));
        assertThat(LocalDate.of(2020, 9, 7).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        // Holiday count includes 1 Independence Day.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(369);
    }

    @Test
    public void getChargeDayCountSkippingFourLaborDays() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2023, 9, 3));
        contractParameters.setRentalDayCount(1100);  // Remember, 2024 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(LocalDate.of(2026, 9, 7));
        assertThat(LocalDate.of(2026, 9, 7).getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        // Holiday count includes 3 Independence Days.
        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(1093);
    }

    @Test
    public void getChargeDayCountSkippingHolidaysWithDatesAtYearEnds() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2023, 12, 31));
        contractParameters.setRentalDayCount(731);  // Remember, 2024 is a leap year.
        contractParameters.setDiscountPercent(25);

        final ToolImpl tool = new ToolImpl();
        tool.setCode("CHNS");
        tool.setType("Chainsaw");
        tool.setBrand("Stihl");
        tool.setDailyCharge(new BigDecimal("1.49"));
        tool.setWeekdayCharge(true);
        tool.setWeekendCharge(true);
        tool.setHolidayCharge(false);

        assertThat(new RentalAgreementImpl(contractParameters, tool).getDueDate()).isEqualTo(
                LocalDate.of(2025, 12, 31));

        assertThat(new RentalAgreementImpl(contractParameters, tool).getChargeDayCount()).isEqualTo(727);
    }

    // TODO: Note how this test assumes an specific implementation of getWeekendChargeDayCount.
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

            new RentalAgreementImpl(contractParameters, tool).printRentalAgreement();;

            assertThat(new String(standardOutputStream.toByteArray())).isEqualTo(expectedRentalAgreement);
        }
        finally {
            System.setOut(originalSystemOut);
        }
    }
}
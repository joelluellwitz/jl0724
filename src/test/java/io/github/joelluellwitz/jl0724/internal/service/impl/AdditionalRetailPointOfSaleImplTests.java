/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Assertions.within;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.joelluellwitz.jl0724.TestConfiguration;
import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale;
import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;
import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto;
import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementRepo;
import io.github.joelluellwitz.jl0724.internal.data.api.ToolDto;

/**
 * Additional tests for {@link io.github.joelluellwitz.jl0724.internal.service.impl.RetailPointOfSaleImpl} as I found
 *   the required tests (as specified in the requirements document) insufficient.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/io/github/joelluellwitz/jl0724/internal/data/api/ToolRepoTests.sql")
public class AdditionalRetailPointOfSaleImplTests {
    private final RentalAgreementRepo retailAgreementRepo;
    private final RetailPointOfSale retailPointOfSale;

    /**
     * Constructor.
     *
     * @param retailAgreementRepo Used to verify a
     *   {@link io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto RentalAgreementDto} is saved to the
     *   database during checkout.
     * @param retailPointOfSale A
     *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.RetailPointOfSaleImpl RetailPointOfSaleImpl}
     *   instance to test.
     */
    @Autowired
    public AdditionalRetailPointOfSaleImplTests(final RentalAgreementRepo retailAgreementRepo,
            final RetailPointOfSale retailPointOfSale) {
        this.retailAgreementRepo = retailAgreementRepo;
        this.retailPointOfSale = retailPointOfSale;
    }

    /**
     * Verifies the entire tool list contains the correct properties and is sorted by the Tool Code.
     */
    @Test
    public void listToolsSucceeds() {
        final List<Tool> tools = retailPointOfSale.listTools();

        assertThat(tools)
                .extracting("code", "type", "brand", "dailyCharge", "weekdayCharge", "weekendCharge", "holidayCharge")
                .contains(tuple("CHNS", "Chainsaw", "Stihl", new BigDecimal("1.49"), true, false, true),
                        tuple("JAKD", "Jackhammer", "DeWalt", new BigDecimal("2.99"), true, false, false),
                        tuple("JAKR", "Jackhammer", "Ridgid", new BigDecimal("2.99"), true, false, false),
                        tuple("LADW", "Ladder", "Werner", new BigDecimal("1.99"), true, true, false));
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} throws an
     *   {@link java.lang.IllegalArgumentException IllegalArgumentException} when the rental duration is 0.
     */
    @Test
    public void checkoutFailsWithRentalDayCountOfZero() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(0);
        contractParameters.setDiscountPercent(0);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The number of rental days must be greater than 1. You specified: 0");

    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} succeeds
     *   when the rental duration is 1.
     */
    @Test
    public void checkoutSucceedsWithRentalDayCountOfOne() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(0);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        final String expectedRentalAgreement =
                "Tool code: JAKR\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: Ridgid\n"
                + "Rental days: 1\n"
                + "Check out date: 09/03/15\n"
                + "Due date: 09/04/15\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 1\n"
                + "Pre-discount charge: $2.99\n"
                + "Discount percent: 0%\n"
                + "Discount amount: $0.00\n"
                + "Final charge: $2.99\n";

        assertThat(rentalAgreement.toString()).isEqualTo(expectedRentalAgreement);
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} throws an
     *   {@link java.lang.IllegalArgumentException IllegalArgumentException} when the discount percentage is -1.
     */
    @Test
    public void checkoutFailsWithNegativeDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(-1);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Discount percentage must be between 0 and 100 (inclusive). You specified: -1");
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} succeeds
     *   when the discount percentage is 0.<p>
     *
     * Note: I realize this is also tested in {@link
     *   io.github.joelluellwitz.jl0724.internal.service.impl.RequiredRetailPointOfSaleImplTests
     *   RequiredRetailPointOfSaleImplTests}.
     */
    @Test
    public void checkoutSucceedsWithZeroPercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(0);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        final String expectedRentalAgreement =
                "Tool code: JAKR\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: Ridgid\n"
                + "Rental days: 1\n"
                + "Check out date: 09/03/15\n"
                + "Due date: 09/04/15\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 1\n"
                + "Pre-discount charge: $2.99\n"
                + "Discount percent: 0%\n"
                + "Discount amount: $0.00\n"
                + "Final charge: $2.99\n";

        assertThat(rentalAgreement.toString()).isEqualTo(expectedRentalAgreement);
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} succeeds
     *   when the discount percentage is 100.
     */
    @Test
    public void checkoutSucceedsWith100PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(100);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        final String expectedRentalAgreement =
                "Tool code: JAKR\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: Ridgid\n"
                + "Rental days: 1\n"
                + "Check out date: 09/03/15\n"
                + "Due date: 09/04/15\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 1\n"
                + "Pre-discount charge: $2.99\n"
                + "Discount percent: 100%\n"
                + "Discount amount: $2.99\n"
                + "Final charge: $0.00\n";

        assertThat(rentalAgreement.toString()).isEqualTo(expectedRentalAgreement);
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} throws an
     *   {@link java.lang.IllegalArgumentException IllegalArgumentException} when the discount percentage is 101.<p>
     *
     * Note: I realize this is also tested in {@link
     *   io.github.joelluellwitz.jl0724.internal.service.impl.RequiredRetailPointOfSaleImplTests
     *   RequiredRetailPointOfSaleImplTests}.
     */
    @Test
    public void checkoutFailsWith101PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(101);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Discount percentage must be between 0 and 100 (inclusive). You specified: 101");
    }

    /**
     * Verifies {@link io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale#checkout checkout} throws an
     *   {@link java.lang.IllegalArgumentException IllegalArgumentException} when an invalid Tool Code is supplied.
     */
    @Test
    public void checkoutFailsWithInvalidToolCode() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("INVD");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(0);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unrecognized tool code. You specified: INVD");
    }

    /**
     * Verifies the {@link io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement RentalAgreement} is
     *   successfully saved to the database.
     */
    @Test
    public void checkoutSucceedsWithSavedRentalAgreement() {
        final String toolCode = "JAKR"; // Note: Ensures I don't misspell JAKR anywhere in the test.

        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode(toolCode);
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(100);

        retailPointOfSale.checkout(contractParameters);

        final List<RentalAgreementDto> rentalAgreementDtos = retailAgreementRepo.findAll();

        assertThat(rentalAgreementDtos.size()).isEqualTo(1);

        final RentalAgreementDto rentalAgreementDto = rentalAgreementDtos.getFirst();
        assertThat(rentalAgreementDto.getId()).isEqualTo(1);
        assertThat(rentalAgreementDto.getVersion()).isEqualTo(0);
        assertThat(rentalAgreementDto.getCreatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(rentalAgreementDto.getUpdatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(rentalAgreementDto.getToolCode()).isEqualTo(toolCode);
        assertThat(rentalAgreementDto.getToolType()).isEqualTo("Jackhammer");
        assertThat(rentalAgreementDto.getToolBrand()).isEqualTo("Ridgid");
        assertThat(rentalAgreementDto.getRentalDayCount()).isEqualTo(1);
        assertThat(rentalAgreementDto.getCheckoutDate()).isEqualTo(LocalDate.of(2015, 9, 3));
        assertThat(rentalAgreementDto.getDueDate()).isEqualTo(LocalDate.of(2015, 9, 4));
        assertThat(rentalAgreementDto.getDailyCharge()).isEqualTo(new BigDecimal("2.99"));
        assertThat(rentalAgreementDto.getChargeDayCount()).isEqualTo(1);
        assertThat(rentalAgreementDto.getPreDiscountCharge()).isEqualTo(new BigDecimal("2.99"));
        assertThat(rentalAgreementDto.getDiscountPercent()).isEqualTo(100);
        assertThat(rentalAgreementDto.getDiscountAmount()).isEqualTo(new BigDecimal("2.99"));
        assertThat(rentalAgreementDto.getFinalCharge()).isEqualTo(new BigDecimal("0.00"));

        final ToolDto toolDto = rentalAgreementDto.getTool();
        assertThat(toolDto.getCode()).isEqualTo(toolCode);
    }
}
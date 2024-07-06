/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.time.LocalDate;
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

/**
 * TODO: Document.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("RequiredRetailPointOfSaleImplTests.sql")
public class AdditionalRetailPointOfSaleImplTests {
    private final RetailPointOfSale retailPointOfSale;

    /**
     * TODO: Document?
     *
     * @param retailPointOfSale
     */
    @Autowired
    public AdditionalRetailPointOfSaleImplTests(final RetailPointOfSale retailPointOfSale) {
        this.retailPointOfSale = retailPointOfSale;
    }

    @Test
    public void listToolsSucceeds() {
        final List<Tool> tools = retailPointOfSale.listTools();

        assertThat(tools).extracting(
                "code", "type", "brand", "dailyCharge", "weekdayCharge", "weekendCharge", "holidayCharge"
                ).contains(tuple("CHNS", "Chainsaw", "Stihl", new BigDecimal("1.49"), true, false, true),
                        tuple("JAKD", "Jackhammer", "DeWalt", new BigDecimal("2.99"), true, false, false),
                        tuple("JAKR", "Jackhammer", "Ridgid", new BigDecimal("2.99"), true, false, false),
                        tuple("LADW", "Ladder", "Werner" , new BigDecimal("1.99"),  true, true, false));
    }

    @Test
    public void checkoutFailsWithRentalDayCountOfZero() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(0);
        contractParameters.setDiscountPercent(0);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                "The number of rental days must be greater than 1. You specified: 0");

    }

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

        // TODO: Look into changing the call to printRentalAgreement.
        assertThat(rentalAgreement.getRentalAgreement()).isEqualTo(expectedRentalAgreement);
    }

    @Test
    public void checkoutFailsWithNegativeDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(-1);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                "Discount percentage must be between 0 and 100 (inclusive). You specified: -1");
    }

    // Note: I realize this is also tested in RequiredRetailPointOfSaleImplTests.
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

        assertThat(rentalAgreement.getRentalAgreement()).isEqualTo(expectedRentalAgreement);
    }

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

        assertThat(rentalAgreement.getRentalAgreement()).isEqualTo(expectedRentalAgreement);
    }

    // Note: I realize this is also tested in RequiredRetailPointOfSaleImplTests.
    @Test
    public void checkoutFailsWith101PercentDiscount() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(101);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                "Discount percentage must be between 0 and 100 (inclusive). You specified: 101");
    }

    @Test
    public void checkoutFailsWithInvalidToolCode() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("INVD");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(1);
        contractParameters.setDiscountPercent(0);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                "Unrecognized tool code. You specified: INVD");
    }
}
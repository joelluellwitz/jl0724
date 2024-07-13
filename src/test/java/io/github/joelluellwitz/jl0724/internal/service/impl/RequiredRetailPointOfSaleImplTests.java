/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

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

/**
 * Required tests for {@link io.github.joelluellwitz.jl0724.internal.service.impl.RetailPointOfSaleImpl} as specified
 *   in the requirements document.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql("/io/github/joelluellwitz/jl0724/internal/data/api/ToolRepoTests.sql")
public class RequiredRetailPointOfSaleImplTests {
    private final RetailPointOfSale retailPointOfSale;

    /**
     * Constructor.
     *
     * @param retailPointOfSale A
     *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.RetailPointOfSaleImpl RetailPointOfSaleImpl}
     *   instance to test.
     */
    @Autowired
    public RequiredRetailPointOfSaleImplTests(final RetailPointOfSale retailPointOfSale) {
        this.retailPointOfSale = retailPointOfSale;
    }

    /**
     * The first test case as specified in the requirements document.
     */
    @Test
    public void checkoutTest1() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(101);

        assertThatThrownBy(() -> {
            retailPointOfSale.checkout(contractParameters);
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(
                "Discount percentage must be between 0 and 100 (inclusive). You specified: 101");
    }

    /**
     * The second test case as specified in the requirements document.
     */
    @Test
    public void checkoutTest2() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("LADW");
        contractParameters.setCheckoutDate(LocalDate.of(2020, 7, 2));
        contractParameters.setRentalDayCount(3);
        contractParameters.setDiscountPercent(10);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        // Note: I don't test each property of RentalAgreementImpl because each property is tested through
        //   RentalAgreement#printRentalAgreement. There are pros and cons of not testing each property. The pros being
        //   there could be reduced unit test maintenance if implementation details change later. The con being that I
        //   am making an assumption about how RentalAgreement#printRentalAgreement is implemented.

        // Note: I typically don't use helper methods to setup expected test data. The reason for this is that I want
        //   the expected value to be easy to read and completely unobfuscated. This way I can be more certain the test
        //   is correct. Additionally, I avoid the temptation to copy the string building code form the class being
        //   tested.
        final String expectedRentalAgreement =
                "Tool code: LADW\n"
                + "Tool type: Ladder\n"
                + "Tool brand: Werner\n"
                + "Rental days: 3\n"
                + "Check out date: 07/02/20\n"
                + "Due date: 07/05/20\n"
                + "Daily rental charge: $1.99\n"
                + "Charge days: 2\n"
                + "Pre-discount charge: $3.98\n"
                + "Discount percent: 10%\n"
                + "Discount amount: $0.40\n"
                + "Final charge: $3.58\n";

        assertEqualsPrintedRentalAgreement(expectedRentalAgreement, rentalAgreement);
    }

    /**
     * The third test case as specified in the requirements document.
     */
    @Test
    public void checkoutTest3() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("CHNS");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(5);
        contractParameters.setDiscountPercent(25);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

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

        assertEqualsPrintedRentalAgreement(expectedRentalAgreement, rentalAgreement);
    }

    /**
     * The forth test case as specified in the requirements document.
     */
    @Test
    public void checkoutTest4() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKD");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 9, 3));
        contractParameters.setRentalDayCount(6);
        contractParameters.setDiscountPercent(0);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        final String expectedRentalAgreement =
                "Tool code: JAKD\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: DeWalt\n"
                + "Rental days: 6\n"
                + "Check out date: 09/03/15\n"
                + "Due date: 09/09/15\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 3\n"
                + "Pre-discount charge: $8.97\n"
                + "Discount percent: 0%\n"
                + "Discount amount: $0.00\n"
                + "Final charge: $8.97\n";

        assertEqualsPrintedRentalAgreement(expectedRentalAgreement, rentalAgreement);
    }

    /**
     * The fifth test case as specified in the requirements document.
     */
    @Test
    public void checkoutTest5() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2015, 7, 2));
        contractParameters.setRentalDayCount(9);
        contractParameters.setDiscountPercent(0);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        final String expectedRentalAgreement =
                "Tool code: JAKR\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: Ridgid\n"
                + "Rental days: 9\n"
                + "Check out date: 07/02/15\n"
                + "Due date: 07/11/15\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 5\n"
                + "Pre-discount charge: $14.95\n"
                + "Discount percent: 0%\n"
                + "Discount amount: $0.00\n"
                + "Final charge: $14.95\n";

        assertEqualsPrintedRentalAgreement(expectedRentalAgreement, rentalAgreement);
    }

    /**
     * The sixth test case as specified in the requirements document.
     */
    @Test
    public void checkoutTest6() {
        final ContractParameters contractParameters = new ContractParameters();
        contractParameters.setToolCode("JAKR");
        contractParameters.setCheckoutDate(LocalDate.of(2020, 7, 2));
        contractParameters.setRentalDayCount(4);
        contractParameters.setDiscountPercent(50);

        final RentalAgreement rentalAgreement = retailPointOfSale.checkout(contractParameters);

        final String expectedRentalAgreement =
                "Tool code: JAKR\n"
                + "Tool type: Jackhammer\n"
                + "Tool brand: Ridgid\n"
                + "Rental days: 4\n"
                + "Check out date: 07/02/20\n"
                + "Due date: 07/06/20\n"
                + "Daily rental charge: $2.99\n"
                + "Charge days: 1\n"
                + "Pre-discount charge: $2.99\n"
                + "Discount percent: 50%\n"
                + "Discount amount: $1.50\n"
                + "Final charge: $1.49\n";

        assertEqualsPrintedRentalAgreement(expectedRentalAgreement, rentalAgreement);
    }

    /**
     * Verifies the code printed to {@link java.lang.System#out System#out} matches the expected String.
     *
     * @param rentalAgreement The rental agreement being tested.
     * @param expectedString The expected String.
     */
    private void assertEqualsPrintedRentalAgreement(final String expectedString,
            final RentalAgreement rentalAgreement) {
        final PrintStream originalSystemOut = System.out;
        final ByteArrayOutputStream standardOutputStream = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(standardOutputStream));

            rentalAgreement.printRentalAgreement();
        }
        finally {
            System.setOut(originalSystemOut);
        }

        assertThat(new String(standardOutputStream.toByteArray())).isEqualTo(expectedString);
    }
}
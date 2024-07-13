/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.github.joelluellwitz.jl0724.TestConfiguration;

/**
 * Tests {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolRepo ToolRepo} and related
 *   {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDto} and
 *   {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolTypeDto ToolTypeDto}.<p>
 *
 * Note: There is no RentalAgreementRepoTests as that interface and related DTO already has full unit test code
 *   coverage.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@Sql
public class ToolRepoTests {
    private final ToolRepo toolRepo;

    /**
     * Constructor.
     *
     * @param toolRepo A {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolRepo ToolRepo} instance to test.
     */
    @Autowired
    public ToolRepoTests(final ToolRepo toolRepo) {
        this.toolRepo = toolRepo;
    }

    /**
     * Verifies tool information is correctly pulled from the database.
     */
    @Test
    public void getToolByCodeReturnsValidTool() {
        final String toolCode = "JAKR"; // Note: Ensures I don't misspell JAKR anywhere in the test.

        final Optional<ToolDto> toolDtoOptional = toolRepo.getToolByCode(toolCode);

        assertThat(toolDtoOptional.isPresent()).isTrue();

        final ToolDto toolDto = toolDtoOptional.get();
        assertThat(toolDto.getId()).isEqualTo(4);
        assertThat(toolDto.getVersion()).isEqualTo(0);
        assertThat(toolDto.getCreatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(toolDto.getUpdatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(toolDto.getCode()).isEqualTo(toolCode);
        assertThat(toolDto.getBrand()).isEqualTo("Ridgid");

        final ToolTypeDto toolTypeDto = toolDto.getToolType();
        assertThat(toolTypeDto.getId()).isEqualTo(3);
        assertThat(toolTypeDto.getVersion()).isEqualTo(0);
        assertThat(toolTypeDto.getCreatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(toolTypeDto.getUpdatedOn()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(toolTypeDto.getName()).isEqualTo("Jackhammer");
        assertThat(toolTypeDto.getDailyCharge()).isEqualTo(new BigDecimal("2.99"));
        assertThat(toolTypeDto.getWeekdayCharge()).isTrue();
        assertThat(toolTypeDto.getWeekendCharge()).isFalse();
        assertThat(toolTypeDto.getHolidayCharge()).isFalse();
    }
}
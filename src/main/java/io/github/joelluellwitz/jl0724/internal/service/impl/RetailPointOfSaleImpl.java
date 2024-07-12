/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.joelluellwitz.jl0724.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0724.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0724.exposed.service.api.RetailPointOfSale;
import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;
import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto;
import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementRepo;
import io.github.joelluellwitz.jl0724.internal.data.api.ToolDto;
import io.github.joelluellwitz.jl0724.internal.data.api.ToolRepo;

/**
 * The main business tier implementation of the Retail Point of Sale application.
 */
@Service
@Transactional
public class RetailPointOfSaleImpl implements RetailPointOfSale {
    private static Logger LOGGER = LoggerFactory.getLogger(RetailPointOfSaleImpl.class);

    private final RentalAgreementMapper rentalAgreementMapper;
    private final RentalAgreementRepo rentalAgreementRepo;
    private final ToolMapper toolMapper;
    private final ToolRepo toolRepo;

    /**
     * Constructor.
     *
     * @param rentalAgreementMapper Mapper to convert a business tier
     *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl RentalAgreementImpl} to a data
     *   tier {@link io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto RentalAgreementDto}.
     * @param rentalAgreementRepo JPA Repository for RentalAgreementDto.
     * @param toolMapper Mapper to convert data tier
     *   {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDtos} to business tier
     *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.ToolImpl ToolImpls}.
     * @param toolRepo JPA Repository for ToolDto.
     */
    // Intentionally package private.
    RetailPointOfSaleImpl(final RentalAgreementMapper rentalAgreementMapper,
            final RentalAgreementRepo rentalAgreementRepo, final ToolMapper toolMapper, final ToolRepo toolRepo) {
        this.rentalAgreementMapper = rentalAgreementMapper;
        this.rentalAgreementRepo = rentalAgreementRepo;
        this.toolMapper = toolMapper;
        this.toolRepo = toolRepo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tool> listTools() {
       LOGGER.debug("Retrieving the tool list.");
       return toolMapper.toolDtosToTools(toolRepo.listToolsSortedByToolCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RentalAgreement checkout(final ContractParameters contractParameters) {
        LOGGER.debug("Starting checkout.");

        final int discountPercent = contractParameters.getDiscountPercent();
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(String.format(
                    "Discount percentage must be between 0 and 100 (inclusive). You specified: %d", discountPercent));
        }

        final int rentalDayCount = contractParameters.getRentalDayCount();
        if (rentalDayCount < 1) {
            throw new IllegalArgumentException(String.format(
                    "The number of rental days must be greater than 1. You specified: %d", rentalDayCount));
        }

        // Note: The fact that the requirements document does not say anything about Tool Code validation leads me to
        //   believe the requirements author intended for Tool Code to be represented as an {@link java.lang.Enum Enum}.
        //   In this demo, I opted to back Tool Code with a database entry which I think is a reasonable justification
        //   for not having a ToolCode Enum.
        final Optional<ToolDto> toolOptional = toolRepo.getToolByCode(contractParameters.getToolCode());
        if (toolOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "Unrecognized tool code. You specified: %s", contractParameters.getToolCode()));
        }
        final Tool tool = toolMapper.toolDtoToTool(toolOptional.get());

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final RentalAgreementDto rentalAgreementDto =
                rentalAgreementMapper.rentalAgreementToRentalAgreementDto(rentalAgreement);

        rentalAgreementDto.setTool(toolOptional.get());
        rentalAgreementRepo.saveAndFlush(rentalAgreementDto);

        LOGGER.debug("Finishing checkout.");
        return rentalAgreement;
    }
}
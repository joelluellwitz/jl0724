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
 * TODO: Document.
 */
@Service
@Transactional
public class RetailPointOfSaleImpl implements RetailPointOfSale {
    private static Logger LOGGER = LoggerFactory.getLogger(RetailPointOfSaleImpl.class);

    private final RentalAgreementRepo rentalAgreementRepo;
    private final RentalAgreementMapper rentalAgreementMapper;
    private final ToolRepo toolRepo;
    private final ToolMapper toolMapper;

    /**
     * TODO: Document.
     *
     * @param rentalAgreementRepo
     * @param toolRepo
     */
    // Intentionally package private.
    RetailPointOfSaleImpl(final RentalAgreementRepo rentalAgreementRepo, final ToolRepo toolRepo,
            final ToolMapper toolMapper, final RentalAgreementMapper rentalAgreementMapper) {
        this.rentalAgreementRepo = rentalAgreementRepo;
        this.toolRepo = toolRepo;
        this.toolMapper = toolMapper;
        this.rentalAgreementMapper = rentalAgreementMapper;
    }

    // For documentation, see interface definition.
    @Override
    public List<Tool> listTools() {
       LOGGER.debug("Retrieving the tool list.");
       return toolMapper.toolDtosToTools(toolRepo.listToolsSortedByToolCode());
    }

    // For documentation, see interface definition.
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
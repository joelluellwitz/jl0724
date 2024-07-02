/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
public class RetailPointOfSaleImpl implements RetailPointOfSale {
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
       return toolMapper.toolDtosToTools(toolRepo.listToolsSortedByToolCode());
    }

    // For documentation, see interface definition.
    @Override
    public RentalAgreement checkout(final ContractParameters contractParameters) {
        final int discountPercent = contractParameters.getDiscountPercent();
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException(String.format(
                    "Discount percentage must be between 0 and 100 (inclusive). You specified: %i", discountPercent));
        }

        final int rentalDayCount = contractParameters.getRentalDayCount();
        if (rentalDayCount < 1) {
            throw new IllegalArgumentException(String.format(
                    "The number of rental days must be greater than 1. You specified: %i", rentalDayCount));
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

        rentalAgreementRepo.saveAndFlush(rentalAgreementDto);

        return rentalAgreement;
    }
}

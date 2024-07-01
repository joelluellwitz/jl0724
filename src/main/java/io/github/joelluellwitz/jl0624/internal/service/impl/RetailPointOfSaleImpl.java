/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import io.github.joelluellwitz.jl0624.exposed.service.api.ContractParameters;
import io.github.joelluellwitz.jl0624.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0624.exposed.service.api.RetailPointOfSale;
import io.github.joelluellwitz.jl0624.exposed.service.api.Tool;
import io.github.joelluellwitz.jl0624.internal.dao.api.RentalAgreementDao;
import io.github.joelluellwitz.jl0624.internal.dao.api.RentalAgreementDto;
import io.github.joelluellwitz.jl0624.internal.dao.api.ToolDao;
import io.github.joelluellwitz.jl0624.internal.dao.api.ToolDto;

/**
 * TODO: Document.
 */
@Service
public class RetailPointOfSaleImpl implements RetailPointOfSale {
    private final RentalAgreementDao rentalAgreementDao;
    private final RentalAgreementMapper rentalAgreementMapper;
    private final ToolDao toolDao;
    private final ToolMapper toolMapper;

    /**
     * TODO: Document.
     *
     * @param rentalAgreementDao
     * @param toolDao
     */
    // Intentionally package private.
    RetailPointOfSaleImpl(final RentalAgreementDao rentalAgreementDao, final ToolDao toolDao,
            final ToolMapper toolMapper, final RentalAgreementMapper rentalAgreementMapper) {
        this.rentalAgreementDao = rentalAgreementDao;
        this.toolDao = toolDao;
        this.toolMapper = toolMapper;
        this.rentalAgreementMapper = rentalAgreementMapper;
    }

    // For documentation, see interface definition.
    @Override
    public List<Tool> listTools() {
       return toolMapper.toolDtosToTools(toolDao.listToolsSortedByToolCode());
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

        final Optional<ToolDto> toolOptional = toolDao.getToolByCode(contractParameters.getToolCode());
        if (toolOptional.isEmpty()) {
            throw new IllegalArgumentException(String.format(
                    "Unrecognized tool code. You specified: %s", contractParameters.getToolCode()));
        }
        final Tool tool = toolMapper.toolDtoToTool(toolOptional.get());

        final RentalAgreementImpl rentalAgreement = new RentalAgreementImpl(contractParameters, tool);
        final RentalAgreementDto rentalAgreementDto =
                rentalAgreementMapper.rentalAgreementToRentalAgreementDto(rentalAgreement);

        rentalAgreementDao.saveAndFlush(rentalAgreementDto);

        return rentalAgreement;
    }
}

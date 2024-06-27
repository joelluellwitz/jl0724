/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.service.impl;

import org.mapstruct.factory.Mappers;

import io.github.joelluellwitz.jl0624.exposed.service.api.RentalAgreement;
import io.github.joelluellwitz.jl0624.internal.dao.api.RentalAgreementDto;

/**
 * TODO: Document.
 */
public interface RentalAgreementMapper {
    final RentalAgreementMapper INSTANCE = Mappers.getMapper(RentalAgreementMapper.class);

    RentalAgreementDto rentalAgreementToRentalAgreementDto(RentalAgreement tool);
}

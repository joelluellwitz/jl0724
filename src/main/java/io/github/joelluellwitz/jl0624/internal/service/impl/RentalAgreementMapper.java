/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.service.impl;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.github.joelluellwitz.jl0624.internal.dao.api.RentalAgreementDto;

/**
 * TODO: Document.
 */
@Mapper(componentModel = "spring")
public interface RentalAgreementMapper {
    RentalAgreementMapper INSTANCE = Mappers.getMapper(RentalAgreementMapper.class);

    RentalAgreementDto rentalAgreementToRentalAgreementDto(RentalAgreementImpl tool);
}

/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto;

/**
 * TODO: Document.
 */
@Mapper(componentModel = "spring")
public interface RentalAgreementMapper {
    RentalAgreementMapper INSTANCE = Mappers.getMapper(RentalAgreementMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    RentalAgreementDto rentalAgreementToRentalAgreementDto(RentalAgreementImpl tool);
}
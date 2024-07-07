/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto;

/**
 * TODO: Document.
 */
@Mapper(componentModel = "spring")
public interface RentalAgreementMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "tool", ignore = true)
    RentalAgreementDto rentalAgreementToRentalAgreementDto(RentalAgreementImpl tool);
}
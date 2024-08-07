/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto;

/**
 * Mapper to convert a business tier {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl
 *   RentalAgreementImpl} to a data tier {@link io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto
 *   RentalAgreementDto}.
 */
@Mapper(componentModel = "spring")
public interface RentalAgreementMapper {

    /**
     * Converts a business tier {@link io.github.joelluellwitz.jl0724.internal.service.impl.RentalAgreementImpl
     *   RentalAgreementImpl} to a data tier {@link io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto
     *   RentalAgreementDto}.
     *
     * @param tool A service tier representation of the rental agreement.
     * @return A data tier representation of the rental agreement.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "tool", ignore = true)
    RentalAgreementDto rentalAgreementToRentalAgreementDto(RentalAgreementImpl tool);
}
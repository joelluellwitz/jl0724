/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TODO: Document.
 */
@Repository
public interface RentalAgreementRepo extends JpaRepository<RentalAgreementDto, Integer> {

}
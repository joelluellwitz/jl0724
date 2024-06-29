/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.data.repository.Insert;

/**
 * TODO: Document.
 */
@Repository
public interface RentalAgreementDao extends JpaRepository<RentalAgreementDto, Integer> {
    /**
     * TODO: Document.
     *
     * @param rentalAgreement
     */
    void persist(RentalAgreementDto rentalAgreement);
}
/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.sqlite;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import io.github.joelluellwitz.jl0624.internal.dao.api.RentalAgreementDao;
import io.github.joelluellwitz.jl0624.internal.dao.api.RentalAgreementDto;
import jakarta.persistence.EntityManager;

/**
 * TODO: Document.
 */
@Repository
public class SqliteRentalAgreementDao implements RentalAgreementDao {

    private final EntityManager entityManager;

    // set up constructor injection
    //@Autowired
    public SqliteRentalAgreementDao(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // See documentation in interface definition.
    //@Override
    public void persist(final RentalAgreementDto rentalAgreement) {
        final Session currentSession = entityManager.unwrap(Session.class);

        currentSession.persist(rentalAgreement);
    }
}
/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link io.github.joelluellwitz.jl0724.internal.data.api.RentalAgreementDto RentalAgreementDto}.
 *
 * Note: Concerning the package name "io.github.joelluellwitz.jl0724.internal.data.api", 'api' is at the end because
 *   it identifies the classes and interfaces to be used by the business logic tier. There could just as well have been
 *   an "io.github.joelluellwitz.jl0724.internal.data.impl" package if I needed concrete implementations of the data API
 *   interfaces. Fortunately, I was able to leverage Spring annotations and dynamic code generation to avoid writing my
 *   own implementations.
 */
@Repository
public interface RentalAgreementRepo extends JpaRepository<RentalAgreementDto, Integer> {

}
/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDto}.
 */
@Repository
public interface ToolRepo extends org.springframework.data.repository.Repository<ToolDto, Integer> {
    /**
     * Returns the {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDto} matching the supplied
     *   Tool Code.
     *
     * @param toolCode A Tool Code.
     * @return The matching tool.
     */
    @Query("SELECT tool FROM Tool tool INNER JOIN FETCH tool.toolType toolType WHERE tool.code = :toolCode")
    Optional<ToolDto> getToolByCode(@Param(value = "toolCode") String toolCode);

    /**
     * Returns all tools sorted by Tool Code.
     *
     * @return A List of data tier tools.
     */
    @Query("SELECT tool FROM Tool tool INNER JOIN FETCH tool.toolType toolType ORDER BY tool.code")
    List<ToolDto> listToolsSortedByToolCode();
}
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
 * TODO: Document.
 */
@Repository
public interface ToolRepo extends org.springframework.data.repository.Repository<ToolDto, Integer> {
    /**
     * TODO:
     *
     * @param toolCode
     * @return
     */
    @Query("SELECT tool FROM Tool tool INNER JOIN FETCH tool.toolType toolType WHERE tool.code = :toolCode")
    Optional<ToolDto> getToolByCode(@Param(value = "toolCode") String toolCode);

    /**
     * TODO:
     *
     * @return
     */
    @Query("SELECT tool FROM Tool tool INNER JOIN FETCH tool.toolType toolType ORDER BY tool.code")
    List<ToolDto> listToolsSortedByToolCode();
}
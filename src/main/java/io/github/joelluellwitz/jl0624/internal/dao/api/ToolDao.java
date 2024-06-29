/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * TODO: Document.
 */
@Repository
public interface ToolDao {
    /**
     * TODO:
     *
     * @param toolCode
     * @return
     */
    // TODO: Remove: @HQL("SELECT tool FROM Tool tool INNER JOIN FETCH ToolType toolType WHERE tool.code = :toolCode")
    ToolDto getToolByCode(String toolCode);

    /**
     * TODO:
     *
     * @return
     */
    // TODO: Remove: @HQL("SELECT tool FROM Tool tool INNER JOIN FETCH ToolType toolType ORDER BY tool.code")
    List<ToolDto> listToolsSortedByToolCode();
}
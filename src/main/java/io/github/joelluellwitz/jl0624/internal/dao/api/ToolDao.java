/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.util.List;

import org.hibernate.annotations.processing.HQL;

/**
 * TODO: Document.
 */
public interface ToolDao {
    /**
     * TODO:
     *
     * @param toolCode
     * @return
     */
    @HQL("SELECT tool FROM Tool tool INNER JOIN FETCH ToolType toolType WHERE tool.code = :toolCode")
    ToolDto getToolByCode(String toolCode);

    /**
     * TODO:
     *
     * @return
     */
    @HQL("SELECT tool FROM Tool tool INNER JOIN FETCH ToolType toolType ORDER BY tool.code")
    List<ToolDto> listToolsSortedByToolCode();
}
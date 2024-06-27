/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.util.List;

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
    ToolDto getToolByCode(String toolCode);

    /**
     * TODO:
     *
     * @return
     */
    List<ToolDto> listToolsSortedByToolCode();
}
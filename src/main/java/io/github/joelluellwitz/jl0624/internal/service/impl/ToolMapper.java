/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.service.impl;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.github.joelluellwitz.jl0624.exposed.service.api.Tool;
import io.github.joelluellwitz.jl0624.internal.dao.api.ToolDto;

/**
 * TODO: Document.
 */
@Mapper
public interface ToolMapper {

    final ToolMapper INSTANCE = Mappers.getMapper(ToolMapper.class);

    ToolImpl toolDtoToTool(ToolDto tool);
    List<Tool> toolDtoToTool(List<ToolDto> tool);
    ToolDto toolToToolDto(Tool tool);
    List<Tool> toolToToolDto(List<Tool> tool);
}
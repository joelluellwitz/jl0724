/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.service.impl;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.joelluellwitz.jl0624.exposed.service.api.Tool;
import io.github.joelluellwitz.jl0624.internal.dao.api.ToolDto;

/**
 * TODO: Document.
 */
@Mapper(componentModel = "spring")
public interface ToolMapper {

    ToolMapper INSTANCE = Mappers.getMapper(ToolMapper.class);

    @Mapping(source = "toolType.name", target = "type")
    @Mapping(source = "toolType.dailyCharge", target = "dailyCharge")
    @Mapping(source = "toolType.weekdayCharge", target = "weekdayCharge")
    @Mapping(source = "toolType.weekendCharge", target = "weekendCharge")
    @Mapping(source = "toolType.holidayCharge", target = "holidayCharge")
    ToolImpl toolDtoToTool(ToolDto tool);
    List<Tool> toolDtosToTools(List<ToolDto> tool);
}
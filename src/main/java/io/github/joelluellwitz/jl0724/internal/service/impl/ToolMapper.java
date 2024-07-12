/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.service.impl;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.joelluellwitz.jl0724.exposed.service.api.Tool;
import io.github.joelluellwitz.jl0724.internal.data.api.ToolDto;

/**
 * Mapper to convert data tier {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDtos} to business
 *   tier {@link io.github.joelluellwitz.jl0724.internal.service.impl.ToolImpl ToolImpls}.
 *
 * Note: I feel it is a good idea to have different transfer objects for the data tier and the business tier. The reason
 *   being is that some properties are not relevant for all tiers. Generally you don't want data tier concerns affecting
 *   business tier classes and vice versa.
 */
@Mapper(componentModel = "spring")
public interface ToolMapper {

    /**
     * Converts a data tier {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDto} to a business tier
     *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.ToolImpl ToolImpl}.
     *
     * @param toolDto A data tier representation of a tool.
     * @return A business logic tier representation of a tool.
     */
    @Mapping(source = "toolType.name", target = "type")
    @Mapping(source = "toolType.dailyCharge", target = "dailyCharge")
    @Mapping(source = "toolType.weekdayCharge", target = "weekdayCharge")
    @Mapping(source = "toolType.weekendCharge", target = "weekendCharge")
    @Mapping(source = "toolType.holidayCharge", target = "holidayCharge")
    ToolImpl toolDtoToTool(ToolDto toolDto);

    /**
     * Converts data tier {@link io.github.joelluellwitz.jl0724.internal.data.api.ToolDto ToolDtos} to business tier
     *   {@link io.github.joelluellwitz.jl0724.internal.service.impl.ToolImpl ToolImpls}.
     *
     * @param toolDtos A List of data tier representations of a tool.
     * @return A List of business logic tier representations of a tool.
     */
    List<Tool> toolDtosToTools(List<ToolDto> toolDtos);
}
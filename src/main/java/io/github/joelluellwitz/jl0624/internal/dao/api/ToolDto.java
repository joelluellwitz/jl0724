/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * TODO: Document.
 */
@Entity(name="Tool")
@Table(name="tool")
public class ToolDto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private int id;
    @Column(name="created_on", nullable=false)
    private LocalDateTime createdOn;
    @Column(name="updated_on", nullable=false)
    private LocalDateTime updatedOn;
    private String code;
    private ToolTypeDto toolType;
    private String brand;

    /**
     * @return the id
     */
    public final int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public final void setId(final int id) {
        this.id = id;
    }
    /**
     * @return the createdOn
     */
    public final LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @param createdOn the createdOn to set
     */
    public final void setCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    /**
     * @return the updatedOn
     */
    public final LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    /**
     * @param updatedOn the updatedOn to set
     */
    public final void setUpdatedOn(final LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    /**
     * @return the code
     */
    public final String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    public final void setCode(final String code) {
        this.code = code;
    }
    /**
     * @return the toolType
     */
    public final ToolTypeDto getToolType() {
        return toolType;
    }
    /**
     * @param toolType the toolType to set
     */
    public final void setToolType(final ToolTypeDto toolType) {
        this.toolType = toolType;
    }
    /**
     * @return the brand
     */
    public final String getBrand() {
        return brand;
    }
    /**
     * @param brand the brand to set
     */
    public final void setBrand(final String brand) {
        this.brand = brand;
    }
}

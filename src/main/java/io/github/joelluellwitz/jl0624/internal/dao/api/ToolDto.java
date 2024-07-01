/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0624.internal.dao.api;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
    @CreationTimestamp
    @Column(name="created_on", nullable=false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name="updated_on", nullable=false)
    private LocalDateTime updatedOn;
    @Column(name="code", nullable=false)
    private String code;
    @OneToOne // TODO: (cascade = CascadeType.ALL)
    @JoinColumn(name = "tool_type_id", referencedColumnName = "id")
    private ToolTypeDto toolType;
    @Column(name="brand", nullable=false)
    private String brand;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    // Intentionally package private.
    void setId(final int id) {
        this.id = id;
    }
    /**
     * @return the createdOn
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @param createdOn the createdOn to set
     */
    // Intentionally package private.
    void setCreatedOn(final LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
    /**
     * @return the updatedOn
     */
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    /**
     * @param updatedOn the updatedOn to set
     */
    // Intentionally package private.
    void setUpdatedOn(final LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code the code to set
     */
    // Intentionally package private.
    void setCode(final String code) {
        this.code = code;
    }
    /**
     * @return the toolType
     */
    public ToolTypeDto getToolType() {
        return toolType;
    }
    /**
     * @param toolType the toolType to set
     */
    // Intentionally package private.
    void setToolType(final ToolTypeDto toolType) {
        this.toolType = toolType;
    }
    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }
    /**
     * @param brand the brand to set
     */
    // Intentionally package private.
    void setBrand(final String brand) {
        this.brand = brand;
    }
}
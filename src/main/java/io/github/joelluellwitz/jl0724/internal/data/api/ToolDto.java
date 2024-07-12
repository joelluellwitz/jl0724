/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * The data tier representation of a tool.
 */
@Entity(name="Tool")
@Table(name="tool")
public class ToolDto {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false, unique=true)
    private int id;
    @Version
    @Column(name="version", nullable=false)
    private int version;
    @CreationTimestamp
    @Column(name="created_on", nullable=false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name="updated_on", nullable=false)
    private LocalDateTime updatedOn;
    @Column(name="code", nullable=false)
    private String code;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
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
     * @return the version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @return the createdOn
     */
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }
    /**
     * @return the updatedOn
     */
    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    /**
     * @return the toolType
     */
    public ToolTypeDto getToolType() {
        return toolType;
    }
    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }
}
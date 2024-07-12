/**
 * Copyright (C) Joel Luellwitz 2024
 */
package io.github.joelluellwitz.jl0724.internal.data.api;

import java.math.BigDecimal;
import java.time.LocalDate;
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
 * The data tier representation of the tool rental agreement.
 *
 * Note: There is intentionally both a reference to a ToolDto and properties of information copied from ToolDto and
 *   ToolTypeDto. The reference to ToolDto allows the rental business to maintain the association to the tool record
 *   used to generate the rental agreement. Maintaining this association might be useful later for some business process
 *   like fulfillment or auditing. The copying of relevant properties allows the business to change the properties of
 *   the tools (such as the tool price) without changing existing contracts.
 *
 * Note: This table does not exist in the initial SQLite database. It is created by Hibernate. This has the unfortunate
 *   side effect that the table columns are unordered. I could obviously work around this by creating the table first,
 *   but I wanted to demonstrate Hibernate table creation.
 */
@Entity(name = "RentalAgreement")
@Table(name = "rental_agreement")
public class RentalAgreementDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;
    @Version
    @Column(name = "version", nullable = false)
    private int version;
    @CreationTimestamp
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private LocalDateTime updatedOn;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "tool_id", referencedColumnName = "id")
    private ToolDto tool;
    @Column(name = "tool_code", nullable = false)
    private String toolCode;
    @Column(name = "tool_type", nullable = false)
    private String toolType;
    @Column(name = "tool_brand", nullable = false)
    private String toolBrand;
    @Column(name = "daily_charge", nullable = false)
    private BigDecimal dailyCharge;
    @Column(name = "rental_day_count", nullable = false)
    private int rentalDayCount;
    @Column(name = "checkout_date", nullable = false)
    private LocalDate checkoutDate;
    @Column(name = "discount_percent", nullable = false)
    private int discountPercent;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Column(name = "charge_day_count", nullable = false)
    private Integer chargeDayCount;
    @Column(name = "pre_discount_charge", nullable = false)
    private BigDecimal preDiscountCharge;
    @Column(name = "discount_amount", nullable = false)
    private BigDecimal discountAmount;
    @Column(name = "final_charge", nullable = false)
    private BigDecimal finalCharge;

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
     * @return The tool record used to generate the rental agreement.
     */
    public ToolDto getTool() {
        return tool;
    }
    /**
     * @param tool The tool record used to generate the rental agreement.
     */
    public void setTool(ToolDto tool) {
        this.tool = tool;
    }
    /**
     * @return The Tool Code. For accurate contract record keeping, if the Tool Code changes in Tool table, this field
     *   should remain unchanged.
     */
    public String getToolCode() {
        return toolCode;
    }
    /**
     * @param toolCode The Tool Code to set. For accurate contract record keeping, if the Tool Code changes in the Tool
     *   table, this field should remain unchanged.
     */
    public void setToolCode(final String toolCode) {
        this.toolCode = toolCode;
    }
    /**
     * @return The Tool Type. For accurate contract record keeping, if the Tool Type changes in Tool Type table, this
     *   field should remain unchanged.
     */
    public String getToolType() {
        return toolType;
    }
    /**
     * @param toolType The Tool Type to set. For accurate contract record keeping, if the Tool Type changes in the Tool
     *   Type table, this field should remain unchanged.
     */
    public void setToolType(final String toolType) {
        this.toolType = toolType;
    }
    /**
     * @return The Tool Brand. For accurate contract record keeping, if the Tool Brand changes in Tool table, this field
     *   should remain unchanged.
     */
    public String getToolBrand() {
        return toolBrand;
    }
    /**
     * @param toolBrand The Tool Brand to set. For accurate contract record keeping, if the Tool Brand changes in the
     *   Tool table, this field should remain unchanged.
     */
    public void setToolBrand(final String toolBrand) {
        this.toolBrand = toolBrand;
    }
    /**
     * @return The daily rental charge. For accurate contract record keeping, if the daily rental charge changes in the
     *   Tool Type table, this field should remain unchanged.
     */
    public BigDecimal getDailyCharge() {
        return dailyCharge;
    }
    /**
     * @param dailyCharge The daily rental charge to set. For accurate contract record keeping, if the daily rental
     *   charge changes in the Tool Type table, this field should remain unchanged.
     */
    public void setDailyCharge(final BigDecimal dailyCharge) {
        this.dailyCharge = dailyCharge;
    }
    /**
     * @return the rentalDayCount
     */
    public int getRentalDayCount() {
        return rentalDayCount;
    }
    /**
     * @param rentalDayCount the rentalDayCount to set
     */
    public void setRentalDayCount(final int rentalDayCount) {
        this.rentalDayCount = rentalDayCount;
    }
    /**
     * @return the checkoutDate
     */
    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }
    /**
     * @param checkoutDate the checkoutDate to set
     */
    public void setCheckoutDate(final LocalDate checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    /**
     * @return the discountPercent
     */
    public int getDiscountPercent() {
        return discountPercent;
    }
    /**
     * @param discountPercent the discountPercent to set
     */
    public void setDiscountPercent(final int discountPercent) {
        this.discountPercent = discountPercent;
    }
    /**
     * @return the dueDate
     */
    public LocalDate getDueDate() {
        return dueDate;
    }
    /**
     * @param dueDate the dueDate to set
     */
    public void setDueDate(final LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    /**
     * @return the chargeDayCount
     */
    public Integer getChargeDayCount() {
        return chargeDayCount;
    }
    /**
     * @param chargeDayCount the chargeDayCount to set
     */
    public void setChargeDayCount(final Integer chargeDayCount) {
        this.chargeDayCount = chargeDayCount;
    }
    /**
     * @return the preDiscountCharge
     */
    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }
    /**
     * @param preDiscountCharge the preDiscountCharge to set
     */
    public void setPreDiscountCharge(final BigDecimal preDiscountCharge) {
        this.preDiscountCharge = preDiscountCharge;
    }
    /**
     * @return the discountAmount
     */
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    /**
     * @param discountAmount the discountAmount to set
     */
    public void setDiscountAmount(final BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    /**
     * @return the finalCharge
     */
    public BigDecimal getFinalCharge() {
        return finalCharge;
    }
    /**
     * @param finalCharge the finalCharge to set
     */
    public void setFinalCharge(final BigDecimal finalCharge) {
        this.finalCharge = finalCharge;
    }
}
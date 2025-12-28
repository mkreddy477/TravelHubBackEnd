package com.example.travelhub.flightbooking.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class for PAYMENT_DETAILS table
 */
@Table("PAYMENT_DETAILS")
public class PaymentDetails {

    @Id
    @Column("PAYMENT_DETAIL_ID")
    private Long paymentDetailId;

    @Column("BOOKING_DETAIL_ID")
    private Long bookingDetailId;

    @Column("PAYMENT_METHOD_ID")
    private Integer paymentMethodId;

    @Column("PAYMENT_STATUS_ID")
    private Integer paymentStatusId;

    @Column("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column("BASE_AMOUNT")
    private BigDecimal baseAmount;

    @Column("ADDITIONAL_AMOUNT")
    private BigDecimal additionalAmount;

    @Column("DISCOUNT_AMOUNT")
    private BigDecimal discountAmount;

    @Column("TAX_AMOUNT")
    private BigDecimal taxAmount;

    @Column("ROUNDED_AMOUNT")
    private BigDecimal roundedAmount;

    @Column("REFUND_AMOUNT")
    private BigDecimal refundAmount;

    @Column("CURRENCY_CODE")
    private String currencyCode;

    @Column("GATEWAYTXNID")
    private String gatewayTxnId;

    @Column("GATEWAYAUTHCODE")
    private String gatewayAuthCode;

    @Column("INITIATED_ON")
    private LocalDateTime initiatedOn;

    @Column("COMPLETED_ON")
    private LocalDateTime completedOn;

    @Column("IS_ACTIVE")
    private Boolean isActive;

    @Column("CREATED_BY")
    private Long createdBy;

    @Column("CREATED_ON")
    private LocalDateTime createdOn;

    @Column("UPDATED_BY")
    private Long updatedBy;

    @Column("UPDATED_ON")
    private LocalDateTime updatedOn;

    @Column("DELETED_BY")
    private Long deletedBy;

    @Column("DELETED_ON")
    private LocalDateTime deletedOn;

    public PaymentDetails() {
    }

    // Getters and Setters
    public Long getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(Long paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    public Long getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(Long bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Integer getPaymentStatusId() {
        return paymentStatusId;
    }

    public void setPaymentStatusId(Integer paymentStatusId) {
        this.paymentStatusId = paymentStatusId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public BigDecimal getAdditionalAmount() {
        return additionalAmount;
    }

    public void setAdditionalAmount(BigDecimal additionalAmount) {
        this.additionalAmount = additionalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getRoundedAmount() {
        return roundedAmount;
    }

    public void setRoundedAmount(BigDecimal roundedAmount) {
        this.roundedAmount = roundedAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getGatewayTxnId() {
        return gatewayTxnId;
    }

    public void setGatewayTxnId(String gatewayTxnId) {
        this.gatewayTxnId = gatewayTxnId;
    }

    public String getGatewayAuthCode() {
        return gatewayAuthCode;
    }

    public void setGatewayAuthCode(String gatewayAuthCode) {
        this.gatewayAuthCode = gatewayAuthCode;
    }

    public LocalDateTime getInitiatedOn() {
        return initiatedOn;
    }

    public void setInitiatedOn(LocalDateTime initiatedOn) {
        this.initiatedOn = initiatedOn;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(LocalDateTime deletedOn) {
        this.deletedOn = deletedOn;
    }
}

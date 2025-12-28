package com.example.travelhub.flightbooking.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class for BOOKING_DETAILS table
 */
@Table("BOOKING_DETAILS")
public class BookingDetails {

    @Id
    @Column("BOOKING_DETAIL_ID")
    private Long bookingDetailId;

    @Column("BOOKING_REFERENCE")
    private String bookingReference;

    @Column("USER_ID")
    private Long userId;

    @Column("BOOKING_TYPE_ID")
    private Integer bookingTypeId;

    @Column("FROM_AIRPORT_ID")
    private Long fromAirportId;

    @Column("TO_AIRPORT_ID")
    private Long toAirportId;

    @Column("TRIP_TYPE_ID")
    private Integer tripTypeId;

    @Column("NO_OF_MEAL")
    private Integer noOfMeal;

    @Column("BOOKING_STATUS_ID")
    private Integer bookingStatusId;

    @Column("BOOKING_DATE")
    private LocalDateTime bookingDate;

    @Column("TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column("REFUND_AMOUNT")
    private BigDecimal refundAmount;

    @Column("CURRENCY_CODE")
    private String currencyCode;

    @Column("NOTES")
    private String notes;

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

    public BookingDetails() {
    }

    // Getters and Setters
    public Long getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(Long bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public String getBookingReference() {
        return bookingReference;
    }

    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getBookingTypeId() {
        return bookingTypeId;
    }

    public void setBookingTypeId(Integer bookingTypeId) {
        this.bookingTypeId = bookingTypeId;
    }

    public Long getFromAirportId() {
        return fromAirportId;
    }

    public void setFromAirportId(Long fromAirportId) {
        this.fromAirportId = fromAirportId;
    }

    public Long getToAirportId() {
        return toAirportId;
    }

    public void setToAirportId(Long toAirportId) {
        this.toAirportId = toAirportId;
    }

    public Integer getTripTypeId() {
        return tripTypeId;
    }

    public void setTripTypeId(Integer tripTypeId) {
        this.tripTypeId = tripTypeId;
    }

    public Integer getNoOfMeal() {
        return noOfMeal;
    }

    public void setNoOfMeal(Integer noOfMeal) {
        this.noOfMeal = noOfMeal;
    }

    public Integer getBookingStatusId() {
        return bookingStatusId;
    }

    public void setBookingStatusId(Integer bookingStatusId) {
        this.bookingStatusId = bookingStatusId;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

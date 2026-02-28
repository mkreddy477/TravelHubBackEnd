package com.example.travelhub.flightbooking.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.travelhub.flightbooking.dao.FlightBookingSqlQueries;
import com.example.travelhub.flightbooking.dao.IFlightBookingDao;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.TravellerInfo;
import com.example.travelhub.user.dao.IUserDao;

@Repository
public class FlightBookingDaoImpl implements IFlightBookingDao {
	

    private static final Logger log = LoggerFactory.getLogger(FlightBookingDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;
    private final IUserDao userDao;

    public FlightBookingDaoImpl(JdbcTemplate jdbcTemplate, IUserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public void saveBooking(BookingRequest bookingRequest, BigDecimal totalAmount) {
        if (bookingRequest.getBookingDetailId() == null) {
            log.warn("Skipping DB persistence because bookingDetailId is null. bookingId={}", bookingRequest.getBookingId());
            return;
        }
        if (bookingRequest.getPaymentMethodId() == null) {
            Integer resolvedMethodId = resolveActivePaymentMethodId();
            bookingRequest.setPaymentMethodId(resolvedMethodId);
            log.info("paymentMethodId not provided. Resolved paymentMethodId={} from CMN_MA_PAYMENT_METHOD (IS_ACTIVE=1)", resolvedMethodId);
        }
        if (bookingRequest.getPaymentStatusId() == null) {
            Integer resolvedStatusId = resolveActivePaymentStatusId();
            bookingRequest.setPaymentStatusId(resolvedStatusId);
            log.info("paymentStatusId not provided. Resolved paymentStatusId={} from CMN_MA_PAYMENT_STATUS (IS_ACTIVE=1)", resolvedStatusId);
        }
        if (bookingRequest.getPaymentMethodId() == null) {
            throw new IllegalArgumentException("paymentMethodId is required to persist payment");
        }
        if (bookingRequest.getPaymentStatusId() == null) {
            throw new IllegalArgumentException("paymentStatusId is required to persist payment");
        }

        Long createdByUserId = null;
        if (bookingRequest.getCreatedBy() != null && !bookingRequest.getCreatedBy().isBlank()) {
            createdByUserId = userDao.findByUsername(bookingRequest.getCreatedBy())
                    .map(u -> u.getUserDetailId())
                    .orElse(null);
        }

        LocalDateTime now = LocalDateTime.now();
        Timestamp nowTs = Timestamp.valueOf(now);

        log.info("Persisting booking to DB. bookingDetailId={}, paymentMethodId={}, paymentStatusId={}, totalAmount={}, createdBy={} (resolvedUserId={})",
                bookingRequest.getBookingDetailId(),
                bookingRequest.getPaymentMethodId(),
                bookingRequest.getPaymentStatusId(),
                totalAmount,
                bookingRequest.getCreatedBy(),
                createdByUserId);

        String email = null;
        String mobile = null;
        if (bookingRequest.getDeliveryInfo() != null) {
            if (bookingRequest.getDeliveryInfo().getEmails() != null && !bookingRequest.getDeliveryInfo().getEmails().isEmpty()) {
                email = bookingRequest.getDeliveryInfo().getEmails().get(0);
            }
            if (bookingRequest.getDeliveryInfo().getContacts() != null && !bookingRequest.getDeliveryInfo().getContacts().isEmpty()) {
                mobile = bookingRequest.getDeliveryInfo().getContacts().get(0);
            }
        }

        try {
            int passengerRows = 0;
            if (bookingRequest.getTravellerInfo() != null) {
                for (TravellerInfo traveller : bookingRequest.getTravellerInfo()) {
                    String gender = deriveGender(traveller.getTi());
                    passengerRows += jdbcTemplate.update(
                            FlightBookingSqlQueries.INSERT_PASSENGER,
                            bookingRequest.getBookingDetailId(),
                            traveller.getfN(),
                            traveller.getlN(),
                            gender,
                            traveller.getPt(),
                            null,
                            mobile,
                            email,
                            null,
                            createdByUserId,
                            nowTs);
                }
            }

            BigDecimal baseAmount = null;
            BigDecimal taxAmount = null;
            BigDecimal finalTotalAmount = totalAmount;
            if (bookingRequest.getFareDetails() != null) {
                log.info("fareDetails received: {}", bookingRequest.getFareDetails());
                if (bookingRequest.getFareDetails().getBaseFare() != null) {
                    baseAmount = BigDecimal.valueOf(bookingRequest.getFareDetails().getBaseFare());
                    log.info("Using baseFare from fareDetails: {}", baseAmount);
                } else {
                    log.warn("fareDetails.baseFare is null");
                }
                if (bookingRequest.getFareDetails().getTaxes() != null) {
                    taxAmount = BigDecimal.valueOf(bookingRequest.getFareDetails().getTaxes());
                    log.info("Using taxes from fareDetails: {}", taxAmount);
                } else {
                    log.warn("fareDetails.taxes is null");
                }
                if (bookingRequest.getFareDetails().getTotal() != null) {
                    finalTotalAmount = BigDecimal.valueOf(bookingRequest.getFareDetails().getTotal());
                    log.info("Using total from fareDetails: {}", finalTotalAmount);
                } else {
                    log.warn("fareDetails.total is null; using paymentInfos total: {}", finalTotalAmount);
                }
            } else {
                log.warn("fareDetails is null in bookingRequest");
                // Calculate taxAmount as fallback: tax = total - baseAmount
                if (baseAmount != null && finalTotalAmount != null) {
                    taxAmount = finalTotalAmount.subtract(baseAmount);
                } 
            }
            if (baseAmount == null) {
                baseAmount = bookingRequest.getBaseAmount() != null ? bookingRequest.getBaseAmount() : finalTotalAmount;
                log.info("baseAmount fallback to request.baseAmount or total: {}", baseAmount);
                // Recalculate taxAmount if we have new baseAmount
                if (taxAmount == null && baseAmount != null && finalTotalAmount != null) {
                    taxAmount = finalTotalAmount.subtract(baseAmount);
                    log.info("Recalculated taxAmount after baseAmount fallback: {}", taxAmount);
                }
            }

            int paymentRows = jdbcTemplate.update(
                    FlightBookingSqlQueries.INSERT_PAYMENT_DETAILS,
                    bookingRequest.getBookingDetailId(),
                    bookingRequest.getPaymentMethodId(),
                    bookingRequest.getPaymentStatusId(),
                    finalTotalAmount,
                    baseAmount,
                    taxAmount,
                    nowTs,  
                    nowTs, 
                    createdByUserId,
                    nowTs); 

            log.info("DB persistence complete. passengerRowsInserted={}, paymentRowsInserted={}, bookingDetailId={}",
                    passengerRows, paymentRows, bookingRequest.getBookingDetailId());
        } catch (DataAccessException e) {
            log.error("DB persistence failed for bookingDetailId={}: {}", bookingRequest.getBookingDetailId(), e.getMessage(), e);
            throw e;
        }
    }

    private String deriveGender(String title) {
        if (title == null) {
            return "UNKNOWN";
        }
        String t = title.trim().toLowerCase();
        if (t.equals("mr") || t.equals("master")) {
            return "MALE";
        }
        if (t.equals("mrs") || t.equals("ms") || t.equals("miss")) {
            return "FEMALE";
        }
        return "UNKNOWN";
    }

    private Integer resolveActivePaymentMethodId() {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT TOP 1 PAYMENT_METHOD_ID FROM CMN_MA_PAYMENT_METHOD WHERE IS_ACTIVE = 1 ORDER BY PAYMENT_METHOD_ID",
                    Integer.class);
        } catch (DataAccessException e) {
            log.error("Failed to resolve active paymentMethodId from CMN_MA_PAYMENT_METHOD: {}", e.getMessage(), e);
            return null;
        }
    }

    private Integer resolveActivePaymentStatusId() {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT TOP 1 PAYMENT_STATUS_ID FROM CMN_MA_PAYMENT_STATUS WHERE IS_ACTIVE = 1 ORDER BY PAYMENT_STATUS_ID",
                    Integer.class);
        } catch (DataAccessException e) {
            log.error("Failed to resolve active paymentStatusId from CMN_MA_PAYMENT_STATUS: {}", e.getMessage(), e);
            return null;
        }
    }
}

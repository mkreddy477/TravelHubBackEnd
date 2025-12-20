package com.example.travelhub.flightbooking.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.travelhub.flightbooking.models.bookingmodels.ConfirmBookRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.ContactInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.DeliveryInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.PaymentInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.TravellerInfo;

/**
 * Validator for ConfirmBookRequest to ensure data quality before sending to TripJack API.
 * Validates required fields, formats, and data constraints.
 */
@Component
public class ConfirmBookRequestValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{2,50}$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");

    /**
     * Validate the confirm book request and return a list of validation errors.
     * Returns an empty list if validation passes.
     */
    public List<String> validate(ConfirmBookRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate bookingId
        if (request.getBookingId() == null || request.getBookingId().trim().isEmpty()) {
            errors.add("bookingId is required");
        }

        // Validate paymentInfos
        errors.addAll(validatePaymentInfos(request.getPaymentInfos()));

        // If this is an instant book request (has traveller info), validate all fields
        if (request.isInstantBookRequest()) {
            // Validate deliveryInfo
            errors.addAll(validateDeliveryInfo(request.getDeliveryInfo()));

            // Validate contactInfo
            errors.addAll(validateContactInfo(request.getContactInfo()));

            // Validate travellerInfo
            errors.addAll(validateTravellerInfos(request.getTravellerInfo()));
        }

        return errors;
    }

    private List<String> validatePaymentInfos(List<PaymentInfo> paymentInfos) {
        List<String> errors = new ArrayList<>();

        if (paymentInfos == null || paymentInfos.isEmpty()) {
            errors.add("paymentInfos is required and must have at least one payment");
            return errors;
        }

        for (int i = 0; i < paymentInfos.size(); i++) {
            PaymentInfo payment = paymentInfos.get(i);
            if (payment.getAmount() == null) {
                errors.add("paymentInfos[" + i + "].amount is required");
            } else if (payment.getAmount().doubleValue() <= 0) {
                errors.add("paymentInfos[" + i + "].amount must be greater than 0");
            }
        }

        return errors;
    }

    private List<String> validateDeliveryInfo(DeliveryInfo deliveryInfo) {
        List<String> errors = new ArrayList<>();

        if (deliveryInfo == null) {
            errors.add("deliveryInfo is required for instant booking");
            return errors;
        }

        // Validate emails
        if (deliveryInfo.getEmails() == null || deliveryInfo.getEmails().isEmpty()) {
            errors.add("deliveryInfo.emails is required");
        } else {
            for (int i = 0; i < deliveryInfo.getEmails().size(); i++) {
                String email = deliveryInfo.getEmails().get(i);
                if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                    errors.add("deliveryInfo.emails[" + i + "] is not a valid email format");
                }
            }
        }

        // Validate contacts
        if (deliveryInfo.getContacts() == null || deliveryInfo.getContacts().isEmpty()) {
            errors.add("deliveryInfo.contacts is required");
        }

        return errors;
    }

    private List<String> validateContactInfo(ContactInfo contactInfo) {
        List<String> errors = new ArrayList<>();

        if (contactInfo == null) {
            errors.add("contactInfo is required for instant booking");
            return errors;
        }

        // Validate emails
        if (contactInfo.getEmails() == null || contactInfo.getEmails().isEmpty()) {
            errors.add("contactInfo.emails is required");
        }

        // Validate contacts
        if (contactInfo.getContacts() == null || contactInfo.getContacts().isEmpty()) {
            errors.add("contactInfo.contacts is required");
        }

        // Validate emergency contact name
        if (contactInfo.getEcn() == null || contactInfo.getEcn().trim().isEmpty()) {
            errors.add("contactInfo.ecn (emergency contact name) is required");
        }

        return errors;
    }

    private List<String> validateTravellerInfos(List<TravellerInfo> travellerInfos) {
        List<String> errors = new ArrayList<>();

        if (travellerInfos == null || travellerInfos.isEmpty()) {
            errors.add("travellerInfo is required for instant booking");
            return errors;
        }

        for (int i = 0; i < travellerInfos.size(); i++) {
            TravellerInfo traveller = travellerInfos.get(i);
            String prefix = "travellerInfo[" + i + "]";

            // Validate title
            if (traveller.getTi() == null || traveller.getTi().trim().isEmpty()) {
                errors.add(prefix + ".ti (title) is required");
            }

            // Validate pax type
            if (traveller.getPt() == null || traveller.getPt().trim().isEmpty()) {
                errors.add(prefix + ".pt (pax type) is required");
            } else if (!traveller.getPt().matches("^(ADULT|CHILD|INFANT)$")) {
                errors.add(prefix + ".pt must be ADULT, CHILD, or INFANT (uppercase)");
            }

            // Validate first name
            if (traveller.getfN() == null || traveller.getfN().trim().isEmpty()) {
                errors.add(prefix + ".fN (first name) is required");
            } else if (traveller.getfN().length() < 2) {
                errors.add(prefix + ".fN (first name) must be at least 2 characters");
            }

            // Validate last name
            if (traveller.getlN() == null || traveller.getlN().trim().isEmpty()) {
                errors.add(prefix + ".lN (last name) is required");
            } else if (traveller.getlN().length() < 2) {
                errors.add(prefix + ".lN (last name) must be at least 2 characters");
            }

            // Validate date of birth
            if (traveller.getDob() == null || traveller.getDob().trim().isEmpty()) {
                errors.add(prefix + ".dob (date of birth) is required");
            } else if (!DATE_PATTERN.matcher(traveller.getDob()).matches()) {
                errors.add(prefix + ".dob must be in YYYY-MM-DD format");
            }

            // Validate passport fields if any passport info is provided
            if (hasPassportInfo(traveller)) {
                errors.addAll(validatePassportInfo(traveller, prefix));
            }
        }

        return errors;
    }

    private boolean hasPassportInfo(TravellerInfo traveller) {
        return (traveller.getpNum() != null && !traveller.getpNum().trim().isEmpty()) ||
               (traveller.getpNat() != null && !traveller.getpNat().trim().isEmpty()) ||
               traveller.geteD() != null;
    }

    private List<String> validatePassportInfo(TravellerInfo traveller, String prefix) {
        List<String> errors = new ArrayList<>();

        // If any passport field is provided, validate all passport fields
        if (traveller.getpNum() == null || traveller.getpNum().trim().isEmpty()) {
            errors.add(prefix + ".pNum (passport number) is required when providing passport info");
        } else if (!PASSPORT_PATTERN.matcher(traveller.getpNum().toUpperCase()).matches()) {
            errors.add(prefix + ".pNum (passport number) must be 6-12 alphanumeric characters");
        }

        if (traveller.getpNat() == null || traveller.getpNat().trim().isEmpty()) {
            errors.add(prefix + ".pNat (passport nationality) is required when providing passport info");
        } else if (!COUNTRY_CODE_PATTERN.matcher(traveller.getpNat().toUpperCase()).matches()) {
            errors.add(prefix + ".pNat (passport nationality) must be a 2-letter IATA country code");
        }

        if (traveller.geteD() == null) {
            errors.add(prefix + ".eD (passport expiry date) is required when providing passport info");
        } else {
            if (traveller.geteD().getMonth() == null || traveller.geteD().getMonth() < 1 || traveller.geteD().getMonth() > 12) {
                errors.add(prefix + ".eD.month must be between 1 and 12");
            }
            if (traveller.geteD().getYear() == null || traveller.geteD().getYear() < 2024) {
                errors.add(prefix + ".eD.year must be 2024 or later");
            }
        }

        return errors;
    }
}

package com.example.travelhub.flightbooking.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.ContactInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.DeliveryInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.PaymentInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.TravellerInfo;

/**
 * Validator for BookingRequest to ensure data quality before sending to TripJack API.
 * Validates required fields, formats, and data constraints.
 */
@Component
public class BookingRequestValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z\\s]{2,50}$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");
    private static final Pattern COUNTRY_CODE_PATTERN = Pattern.compile("^[A-Z]{2}$");
    private static final Pattern BOOKING_ID_PATTERN = Pattern.compile("^TJ[A-Z0-9]{10,20}$");
    
    // Common test/fake names that TripJack will reject
    private static final String[] INVALID_TEST_NAMES = {
        "abc", "xyz", "test", "demo", "sample", "dummy", "fake", "asdf", "qwerty",
        "aaa", "bbb", "ccc", "xxx", "yyy", "zzz", "name", "first", "last",
        "firstname", "lastname", "user", "guest", "traveller", "passenger"
    };

    /**
     * Validate the booking request and return a list of validation errors.
     * Returns an empty list if validation passes.
     */
    public List<String> validate(BookingRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate bookingId
        if (request.getBookingId() == null || request.getBookingId().trim().isEmpty()) {
            errors.add("bookingId is required");
        } else if (!BOOKING_ID_PATTERN.matcher(request.getBookingId().trim()).matches()) {
            errors.add("bookingId format is invalid. Must start with 'TJ' followed by 10-20 alphanumeric characters");
        }

        // Validate paymentInfos
        errors.addAll(validatePaymentInfos(request.getPaymentInfos()));

        // Validate deliveryInfo
        errors.addAll(validateDeliveryInfo(request.getDeliveryInfo()));

        // Validate contactInfo
        errors.addAll(validateContactInfo(request.getContactInfo()));

        // Validate travellerInfo
        errors.addAll(validateTravellerInfos(request.getTravellerInfo()));

        return errors;
    }

    /**
     * Validate the booking request for HOLD booking (no payment required).
     * Returns an empty list if validation passes.
     */
    public List<String> validateForHold(BookingRequest request) {
        List<String> errors = new ArrayList<>();

        // Validate bookingId
        if (request.getBookingId() == null || request.getBookingId().trim().isEmpty()) {
            errors.add("bookingId is required");
        } else if (!BOOKING_ID_PATTERN.matcher(request.getBookingId().trim()).matches()) {
            errors.add("bookingId format is invalid. Must start with 'TJ' followed by 10-20 alphanumeric characters");
        }

        // Skip paymentInfos validation for hold booking

        // Validate deliveryInfo
        errors.addAll(validateDeliveryInfo(request.getDeliveryInfo()));

        // Validate contactInfo
        errors.addAll(validateContactInfo(request.getContactInfo()));

        // Validate travellerInfo
        errors.addAll(validateTravellerInfos(request.getTravellerInfo()));

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
            errors.add("deliveryInfo is required");
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
        } else {
            for (int i = 0; i < deliveryInfo.getContacts().size(); i++) {
                String contact = deliveryInfo.getContacts().get(i);
                if (contact == null || !PHONE_PATTERN.matcher(contact.replace("+", "").replace("-", "")).matches()) {
                    errors.add("deliveryInfo.contacts[" + i + "] is not a valid phone format (use +CountryCodeNumber, e.g., +919876543210)");
                }
            }
        }

        return errors;
    }

    private List<String> validateContactInfo(ContactInfo contactInfo) {
        List<String> errors = new ArrayList<>();

        if (contactInfo == null) {
            errors.add("contactInfo is required");
            return errors;
        }

        // Validate emails
        if (contactInfo.getEmails() == null || contactInfo.getEmails().isEmpty()) {
            errors.add("contactInfo.emails is required");
        } else {
            for (int i = 0; i < contactInfo.getEmails().size(); i++) {
                String email = contactInfo.getEmails().get(i);
                if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
                    errors.add("contactInfo.emails[" + i + "] is not a valid email format");
                }
            }
        }

        // Validate contacts
        if (contactInfo.getContacts() == null || contactInfo.getContacts().isEmpty()) {
            errors.add("contactInfo.contacts is required");
        }

        // Validate emergency contact name
        if (contactInfo.getEcn() == null || contactInfo.getEcn().trim().isEmpty()) {
            errors.add("contactInfo.ecn (emergency contact name) is required");
        } else if (contactInfo.getEcn().length() < 2 || contactInfo.getEcn().length() > 50) {
            errors.add("contactInfo.ecn must be between 2 and 50 characters");
        }

        return errors;
    }

    private List<String> validateTravellerInfos(List<TravellerInfo> travellerInfos) {
        List<String> errors = new ArrayList<>();

        if (travellerInfos == null || travellerInfos.isEmpty()) {
            errors.add("travellerInfo is required and must have at least one traveller");
            return errors;
        }

        for (int i = 0; i < travellerInfos.size(); i++) {
            TravellerInfo traveller = travellerInfos.get(i);
            String prefix = "travellerInfo[" + i + "]";

            // Validate title
            if (traveller.getTi() == null || traveller.getTi().trim().isEmpty()) {
                errors.add(prefix + ".ti (title) is required (Mr, Mrs, Ms, Master)");
            } else if (!isValidTitle(traveller.getTi(), traveller.getPt())) {
                errors.add(prefix + ".ti (title) is invalid for pax type. Adult: Mr/Mrs/Ms, Child/Infant: Ms/Master");
            }

            // Validate pax type
            if (traveller.getPt() == null || traveller.getPt().trim().isEmpty()) {
                errors.add(prefix + ".pt (pax type) is required (ADULT, CHILD, INFANT)");
            } else if (!traveller.getPt().matches("^(ADULT|CHILD|INFANT)$")) {
                errors.add(prefix + ".pt must be ADULT, CHILD, or INFANT (uppercase)");
            }

            // Validate first name
            if (traveller.getfN() == null || traveller.getfN().trim().isEmpty()) {
                errors.add(prefix + ".fN (first name) is required");
            } else if (traveller.getfN().length() < 2) {
                errors.add(prefix + ".fN (first name) must be at least 2 characters");
            } else if (!NAME_PATTERN.matcher(traveller.getfN()).matches()) {
                errors.add(prefix + ".fN (first name) must contain only letters and spaces (2-50 characters)");
            } else if (isInvalidTestName(traveller.getfN())) {
                errors.add(prefix + ".fN (first name) '" + traveller.getfN() + "' appears to be test/fake data. Please use a real name");
            }

            // Validate last name
            if (traveller.getlN() == null || traveller.getlN().trim().isEmpty()) {
                errors.add(prefix + ".lN (last name) is required");
            } else if (traveller.getlN().length() < 2) {
                errors.add(prefix + ".lN (last name) must be at least 2 characters");
            } else if (!NAME_PATTERN.matcher(traveller.getlN()).matches()) {
                errors.add(prefix + ".lN (last name) must contain only letters and spaces (2-50 characters)");
            } else if (isInvalidTestName(traveller.getlN())) {
                errors.add(prefix + ".lN (last name) '" + traveller.getlN() + "' appears to be test/fake data. Please use a real name");
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

        // If any passport field is provided, all passport fields should be provided
        if (traveller.getpNum() == null || traveller.getpNum().trim().isEmpty()) {
            errors.add(prefix + ".pNum (passport number) is required when providing passport info");
        } else if (!PASSPORT_PATTERN.matcher(traveller.getpNum().toUpperCase()).matches()) {
            errors.add(prefix + ".pNum (passport number) must be 6-12 alphanumeric characters");
        }

        if (traveller.getpNat() == null || traveller.getpNat().trim().isEmpty()) {
            errors.add(prefix + ".pNat (passport nationality) is required when providing passport info");
        } else if (!COUNTRY_CODE_PATTERN.matcher(traveller.getpNat().toUpperCase()).matches()) {
            errors.add(prefix + ".pNat (passport nationality) must be a 2-letter IATA country code (e.g., IN, US, GB)");
        }

        if (traveller.geteD() == null) {
            errors.add(prefix + ".eD (passport expiry date) is required when providing passport info");
        } else {
            if (traveller.geteD().getMonth() == null || traveller.geteD().getMonth() < 1 || traveller.geteD().getMonth() > 12) {
                errors.add(prefix + ".eD.month must be between 1 and 12");
            }
            if (traveller.geteD().getYear() == null || traveller.geteD().getYear() < 2024) {
                errors.add(prefix + ".eD.year must be 2024 or later (passport must not be expired)");
            }
        }

        return errors;
    }

    private boolean isValidTitle(String title, String paxType) {
        if (title == null || paxType == null) {
            return false;
        }

        String normalizedTitle = title.trim();
        String normalizedPaxType = paxType.trim().toUpperCase();

        switch (normalizedPaxType) {
            case "ADULT":
                return normalizedTitle.matches("(?i)^(Mr|Mrs|Ms|Miss)$");
            case "CHILD":
            case "INFANT":
                return normalizedTitle.matches("(?i)^(Ms|Master|Miss|Mstr)$");
            default:
                return false;
        }
    }

    /**
     * Check if the given name is a common test/fake name that TripJack will reject.
     */
    private boolean isInvalidTestName(String name) {
        if (name == null) {
            return false;
        }
        String lowerName = name.trim().toLowerCase();
        for (String invalidName : INVALID_TEST_NAMES) {
            if (lowerName.equals(invalidName)) {
                return true;
            }
        }
        // Also check for repeated characters like "aaa", "bbb", etc.
        if (lowerName.length() >= 2 && lowerName.chars().distinct().count() == 1) {
            return true;
        }
        return false;
    }
}

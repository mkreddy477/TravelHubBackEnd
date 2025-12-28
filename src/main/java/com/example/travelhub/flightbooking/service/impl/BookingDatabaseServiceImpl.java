package com.example.travelhub.flightbooking.service.impl;

import com.example.travelhub.flightbooking.entity.BookingDetails;
import com.example.travelhub.flightbooking.entity.PaymentDetails;
import com.example.travelhub.flightbooking.entity.UserDetails;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingRequest;
import com.example.travelhub.flightbooking.models.bookingmodels.BookingResponse;
import com.example.travelhub.flightbooking.models.bookingmodels.PaymentInfo;
import com.example.travelhub.flightbooking.models.bookingmodels.TravellerInfo;
import com.example.travelhub.flightbooking.repository.BookingDetailsRepository;
import com.example.travelhub.flightbooking.repository.PaymentDetailsRepository;
import com.example.travelhub.flightbooking.repository.UserDetailsRepository;
import com.example.travelhub.flightbooking.service.BookingDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of BookingDatabaseService for persisting booking data using R2DBC
 */
@Service
public class BookingDatabaseServiceImpl implements BookingDatabaseService {

    private static final Logger log = LoggerFactory.getLogger(BookingDatabaseServiceImpl.class);

    private final UserDetailsRepository userDetailsRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final PaymentDetailsRepository paymentDetailsRepository;

    public BookingDatabaseServiceImpl(
            UserDetailsRepository userDetailsRepository,
            BookingDetailsRepository bookingDetailsRepository,
            PaymentDetailsRepository paymentDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.paymentDetailsRepository = paymentDetailsRepository;
    }

    @Override
    @Transactional
    public Mono<BookingDetails> saveBookingData(BookingRequest bookingRequest, BookingResponse bookingResponse) {
        log.info("Saving booking data for bookingId: {}", bookingResponse.getBookingId());

        return findOrCreateUser(bookingRequest)
                .flatMap(user -> {
                    log.info("User found/created with ID: {}", user.getUserDetailId());
                    return saveBookingDetails(bookingRequest, bookingResponse, user.getUserDetailId());
                })
                .flatMap(booking -> {
                    log.info("Booking saved with ID: {}", booking.getBookingDetailId());
                    return savePaymentDetails(bookingRequest, booking.getBookingDetailId())
                            .thenReturn(booking);
                })
                .doOnSuccess(booking -> 
                    log.info("Successfully saved all booking data. BookingDetailId: {}", booking.getBookingDetailId())
                )
                .doOnError(error -> 
                    log.error("Error saving booking data: {}", error.getMessage(), error)
                );
    }

    @Override
    public Mono<UserDetails> findOrCreateUser(BookingRequest bookingRequest) {
        String email = extractPrimaryEmail(bookingRequest);
        String phone = extractPrimaryPhone(bookingRequest);
        
        if (email == null || email.isEmpty()) {
            email = "guest_" + UUID.randomUUID().toString().substring(0, 8) + "@travelhub.com";
        }
        
        final String finalEmail = email;
        final String finalPhone = phone;

        return userDetailsRepository.findByPrimaryEmail(finalEmail)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Creating new user with email: {}", finalEmail);
                    UserDetails newUser = createUserFromRequest(bookingRequest, finalEmail, finalPhone);
                    return userDetailsRepository.save(newUser);
                }))
                .doOnSuccess(user -> 
                    log.info("User resolved: {} (ID: {})", user.getPrimaryEmail(), user.getUserDetailId())
                );
    }

    private UserDetails createUserFromRequest(BookingRequest bookingRequest, String email, String phone) {
        UserDetails user = new UserDetails();
        
        // Set default role ID (assuming 1 is a default customer role)
        user.setRoleId(1L);
        
        // Generate unique username from email
        String username = email.split("@")[0] + "_" + System.currentTimeMillis();
        user.setUsername(username);
        
        // Extract name from first traveller if available
        if (bookingRequest.getTravellerInfo() != null && !bookingRequest.getTravellerInfo().isEmpty()) {
            TravellerInfo firstTraveller = bookingRequest.getTravellerInfo().get(0);
            user.setTitle(firstTraveller.getTi());
            user.setFirstName(firstTraveller.getfN());
            user.setLastName(firstTraveller.getlN());
        } else {
            user.setFirstName("Guest");
            user.setLastName("User");
        }
        
        user.setPrimaryEmail(email);
        user.setPrimaryPhone(phone != null ? phone : "N/A");
        user.setIsActive(true);
        user.setCreatedOn(LocalDateTime.now());
        
        return user;
    }

    private Mono<BookingDetails> saveBookingDetails(BookingRequest bookingRequest, 
                                                     BookingResponse bookingResponse, 
                                                     Long userId) {
        BookingDetails booking = new BookingDetails();
        
        // Use TripJack bookingId as reference
        booking.setBookingReference(bookingResponse.getBookingId());
        booking.setUserId(userId);
        
        // Default booking type ID for flight (assuming 1 = Flight)
        booking.setBookingTypeId(1);
        
        // Extract FROM_AIRPORT_ID and TO_AIRPORT_ID from searchQuery
        Long[] airportIds = extractAirportIds(bookingResponse);
        booking.setFromAirportId(airportIds[0]);
        booking.setToAirportId(airportIds[1]);
        
        // Default trip type ID (assuming 1 = One Way)
        booking.setTripTypeId(extractTripTypeId(bookingResponse));
        
        // Extract NO_OF_MEAL from traveller info
        booking.setNoOfMeal(countTotalMeals(bookingRequest));
        
        // Map booking status from response
        booking.setBookingStatusId(mapBookingStatus(bookingResponse));
        
        booking.setBookingDate(LocalDateTime.now());
        
        // Calculate total amount from payment info
        BigDecimal totalAmount = calculateTotalAmount(bookingRequest);
        booking.setTotalAmount(totalAmount);
        booking.setRefundAmount(BigDecimal.ZERO);
        
        booking.setCurrencyCode("INR");
        booking.setIsActive(true);
        booking.setCreatedOn(LocalDateTime.now());
        
        return bookingDetailsRepository.save(booking);
    }

    private Mono<PaymentDetails> savePaymentDetails(BookingRequest bookingRequest, Long bookingDetailId) {
        PaymentDetails payment = new PaymentDetails();
        
        payment.setBookingDetailId(bookingDetailId);
        
        // Default payment method ID (assuming 1 = Online)
        payment.setPaymentMethodId(1);
        
        // Default payment status ID (assuming 1 = Completed)
        payment.setPaymentStatusId(1);
        
        BigDecimal totalAmount = calculateTotalAmount(bookingRequest);
        payment.setTotalAmount(totalAmount);
        payment.setBaseAmount(totalAmount);
        payment.setRefundAmount(BigDecimal.ZERO);
        
        payment.setCurrencyCode("INR");
        payment.setInitiatedOn(LocalDateTime.now());
        payment.setCompletedOn(LocalDateTime.now());
        payment.setIsActive(true);
        payment.setCreatedOn(LocalDateTime.now());
        
        return paymentDetailsRepository.save(payment);
    }

    private String extractPrimaryEmail(BookingRequest bookingRequest) {
        if (bookingRequest.getContactInfo() != null 
                && bookingRequest.getContactInfo().getEmails() != null 
                && !bookingRequest.getContactInfo().getEmails().isEmpty()) {
            return bookingRequest.getContactInfo().getEmails().get(0);
        }
        return null;
    }

    private String extractPrimaryPhone(BookingRequest bookingRequest) {
        if (bookingRequest.getContactInfo() != null 
                && bookingRequest.getContactInfo().getContacts() != null 
                && !bookingRequest.getContactInfo().getContacts().isEmpty()) {
            return bookingRequest.getContactInfo().getContacts().get(0);
        }
        return null;
    }

    private BigDecimal calculateTotalAmount(BookingRequest bookingRequest) {
        List<PaymentInfo> paymentInfos = bookingRequest.getPaymentInfos();
        if (paymentInfos != null && !paymentInfos.isEmpty()) {
            return paymentInfos.stream()
                    .filter(p -> p.getAmount() != null)
                    .map(p -> new BigDecimal(p.getAmount().toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    private Integer mapBookingStatus(BookingResponse bookingResponse) {
        if (bookingResponse.getStatus() != null) {
            String statusStr = bookingResponse.getStatus().getSuccess() != null 
                    && bookingResponse.getStatus().getSuccess() ? "SUCCESS" : "PENDING";
            
            switch (statusStr) {
                case "SUCCESS":
                    return 1; // Confirmed
                case "PENDING":
                    return 2; // Pending
                case "FAILED":
                    return 3; // Failed
                default:
                    return 2; // Default to Pending
            }
        }
        return 2; // Default to Pending
    }

    /**
     * Extract FROM_AIRPORT_ID and TO_AIRPORT_ID from BookingResponse searchQuery.
     * The searchQuery contains routeInfos with fromCityOrAirport and toCityOrAirport.
     * Returns array [fromAirportId, toAirportId] - uses airport code hash as ID if no numeric ID available.
     */
    private Long[] extractAirportIds(BookingResponse bookingResponse) {
        Long fromAirportId = null;
        Long toAirportId = null;
        
        try {
            JsonNode searchQuery = bookingResponse.getSearchQuery();
            if (searchQuery != null && searchQuery.has("routeInfos")) {
                JsonNode routeInfos = searchQuery.get("routeInfos");
                if (routeInfos.isArray() && routeInfos.size() > 0) {
                    JsonNode firstRoute = routeInfos.get(0);
                    
                    // Extract from airport
                    if (firstRoute.has("fromCityOrAirport")) {
                        JsonNode fromAirport = firstRoute.get("fromCityOrAirport");
                        fromAirportId = extractAirportIdFromNode(fromAirport);
                    }
                    
                    // Extract to airport
                    if (firstRoute.has("toCityOrAirport")) {
                        JsonNode toAirport = firstRoute.get("toCityOrAirport");
                        toAirportId = extractAirportIdFromNode(toAirport);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting airport IDs from searchQuery: {}", e.getMessage());
        }
        
        return new Long[]{fromAirportId, toAirportId};
    }

    /**
     * Extract airport ID from a JSON node. Uses code hash if no numeric ID available.
     */
    private Long extractAirportIdFromNode(JsonNode airportNode) {
        if (airportNode == null) {
            return null;
        }
        
        // Try to get numeric ID if present
        if (airportNode.has("id") && airportNode.get("id").isNumber()) {
            return airportNode.get("id").asLong();
        }
        
        // Use airport code hash as fallback ID
        if (airportNode.has("code")) {
            String code = airportNode.get("code").asText();
            if (code != null && !code.isEmpty()) {
                // Generate a consistent numeric ID from airport code
                return (long) Math.abs(code.hashCode());
            }
        }
        
        return null;
    }

    /**
     * Extract trip type ID from BookingResponse searchQuery.
     * Returns: 1 = One Way, 2 = Round Trip, 3 = Multi City
     */
    private Integer extractTripTypeId(BookingResponse bookingResponse) {
        try {
            JsonNode searchQuery = bookingResponse.getSearchQuery();
            if (searchQuery != null && searchQuery.has("routeInfos")) {
                JsonNode routeInfos = searchQuery.get("routeInfos");
                if (routeInfos.isArray()) {
                    int routeCount = routeInfos.size();
                    if (routeCount == 1) {
                        return 1; // One Way
                    } else if (routeCount == 2) {
                        return 2; // Round Trip
                    } else if (routeCount > 2) {
                        return 3; // Multi City
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting trip type from searchQuery: {}", e.getMessage());
        }
        return 1; // Default to One Way
    }

    /**
     * Count total number of meals from all travellers in the booking request.
     */
    private Integer countTotalMeals(BookingRequest bookingRequest) {
        int totalMeals = 0;
        
        try {
            List<TravellerInfo> travellers = bookingRequest.getTravellerInfo();
            if (travellers != null) {
                for (TravellerInfo traveller : travellers) {
                    if (traveller.getSsrMealInfos() != null) {
                        totalMeals += traveller.getSsrMealInfos().size();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error counting meals from traveller info: {}", e.getMessage());
        }
        
        return totalMeals > 0 ? totalMeals : null;
    }
}

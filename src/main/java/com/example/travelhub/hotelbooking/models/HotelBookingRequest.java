package com.example.travelhub.hotelbooking.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelBookingRequest {
    
    @JsonProperty("bookingId")
    @NotBlank(message = "Booking ID is required")
    private String bookingId;
    
    @JsonProperty("roomTravellerInfo")
    @NotEmpty(message = "Room traveller info is required")
    @Valid
    private List<RoomTravellerInfo> roomTravellerInfo;
    
    @JsonProperty("deliveryInfo")
    @NotNull(message = "Delivery info is required")
    @Valid
    private DeliveryInfo deliveryInfo;
    
    @JsonProperty("paymentInfos")
    @NotEmpty(message = "Payment info is required")
    @Valid
    private List<PaymentInfo> paymentInfos;
    
    @JsonProperty("type")
    @NotBlank(message = "Type is required")
    private String type = "HOTEL";
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoomTravellerInfo {
        
        @JsonProperty("travellerInfo")
        @NotEmpty(message = "Traveller info is required for each room")
        @Valid
        private List<TravellerInfo> travellerInfo;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TravellerInfo {
        
        @JsonProperty("ti")
        @NotBlank(message = "Title is required")
        private String ti; // Mr, Master, Mrs, Ms, Miss
        
        @JsonProperty("pt")
        @NotBlank(message = "Passenger type is required")
        private String pt; // ADULT, CHILD
        
        @JsonProperty("fN")
        @NotBlank(message = "First name is required")
        private String fN; // First name (lead pax name should be unique)
        
        @JsonProperty("lN")
        @NotBlank(message = "Last name is required")
        private String lN; // Last name
        
        @JsonProperty("pan")
        private String pan; // PAN (Permanent Account Number)
        
        @JsonProperty("pNum")
        private String pNum; // Passport Number
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeliveryInfo {
        
        @JsonProperty("emails")
        @NotEmpty(message = "At least one email is required")
        private List<@Email(message = "Invalid email format") String> emails;
        
        @JsonProperty("contacts")
        @NotEmpty(message = "At least one contact number is required")
        private List<String> contacts;
        
        @JsonProperty("code")
        @NotEmpty(message = "Country dialing codes are required")
        private List<String> code;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentInfo {
        
        @JsonProperty("amount")
        @NotNull(message = "Payment amount is required")
        private Double amount;
    }
}
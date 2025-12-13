package com.example.travelhub.flightbooking.service;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for Tripjack API response - matches actual API structure
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TripjackResponse {
    
    private Status status;
    private SearchQuery searchQuery;
    private SearchResult searchResult;
    
    public TripjackResponse() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public SearchQuery getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Status {
        private boolean success;
        private Integer httpStatus;
        
        public boolean isSuccess() {
            return success;
        }
        
        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        public Integer getHttpStatus() {
            return httpStatus;
        }
        
        public void setHttpStatus(Integer httpStatus) {
            this.httpStatus = httpStatus;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchQuery {
        private String cabinClass;
        private Map<String, String> paxInfo;
        private List<Map<String, Object>> routeInfos;
        
        public String getCabinClass() {
            return cabinClass;
        }
        
        public void setCabinClass(String cabinClass) {
            this.cabinClass = cabinClass;
        }
        
        public Map<String, String> getPaxInfo() {
            return paxInfo;
        }
        
        public void setPaxInfo(Map<String, String> paxInfo) {
            this.paxInfo = paxInfo;
        }
        
        public List<Map<String, Object>> getRouteInfos() {
            return routeInfos;
        }
        
        public void setRouteInfos(List<Map<String, Object>> routeInfos) {
            this.routeInfos = routeInfos;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResult {
        // tripInfos is a Map where keys are "ONWARD", "RETURN", etc.
        @JsonProperty("tripInfos")
        private Map<String, List<TripInfo>> tripInfos;
        
        public Map<String, List<TripInfo>> getTripInfos() {
            return tripInfos;
        }
        
        public void setTripInfos(Map<String, List<TripInfo>> tripInfos) {
            this.tripInfos = tripInfos;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TripInfo {
        @JsonProperty("sI")
        private List<SegmentInfo> segmentInfos;
        
        private List<PriceInfo> totalPriceList;
        
        private String airFlowType;
        private String ipm;
        private Boolean issf;
        
        public List<SegmentInfo> getSegmentInfos() {
            return segmentInfos;
        }
        
        public void setSegmentInfos(List<SegmentInfo> segmentInfos) {
            this.segmentInfos = segmentInfos;
        }
        
        public List<PriceInfo> getTotalPriceList() {
            return totalPriceList;
        }
        
        public void setTotalPriceList(List<PriceInfo> totalPriceList) {
            this.totalPriceList = totalPriceList;
        }
        
        public String getAirFlowType() {
            return airFlowType;
        }
        
        public void setAirFlowType(String airFlowType) {
            this.airFlowType = airFlowType;
        }
        
        public String getIpm() {
            return ipm;
        }
        
        public void setIpm(String ipm) {
            this.ipm = ipm;
        }
        
        public Boolean getIssf() {
            return issf;
        }
        
        public void setIssf(Boolean issf) {
            this.issf = issf;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SegmentInfo {
        private String id;
        
        @JsonProperty("da")
        private AirportInfo departureAirport;
        
        @JsonProperty("aa")
        private AirportInfo arrivalAirport;
        
        @JsonProperty("dt")
        private String departureTime;
        
        @JsonProperty("at")
        private String arrivalTime;
        
        private Integer duration;
        private Integer stops;
        
        @JsonProperty("cT")
        private Integer connectionTime;
        
        @JsonProperty("sN")
        private Integer segmentNumber;
        
        private Boolean iand;
        private Boolean isRs;
        
        @JsonProperty("so")
        private List<Object> stopOvers;
        
        @JsonProperty("fD")
        private FlightDetails flightDetails;
        
        public String getId() {
            return id;
        }
        
        public void setId(String id) {
            this.id = id;
        }
        
        public AirportInfo getDepartureAirport() {
            return departureAirport;
        }
        
        public void setDepartureAirport(AirportInfo departureAirport) {
            this.departureAirport = departureAirport;
        }
        
        public AirportInfo getArrivalAirport() {
            return arrivalAirport;
        }
        
        public void setArrivalAirport(AirportInfo arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
        }
        
        public String getDepartureTime() {
            return departureTime;
        }
        
        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }
        
        public String getArrivalTime() {
            return arrivalTime;
        }
        
        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }
        
        public Integer getDuration() {
            return duration;
        }
        
        public void setDuration(Integer duration) {
            this.duration = duration;
        }
        
        public Integer getStops() {
            return stops;
        }
        
        public void setStops(Integer stops) {
            this.stops = stops;
        }
        
        public Integer getConnectionTime() {
            return connectionTime;
        }
        
        public void setConnectionTime(Integer connectionTime) {
            this.connectionTime = connectionTime;
        }
        
        public Integer getSegmentNumber() {
            return segmentNumber;
        }
        
        public void setSegmentNumber(Integer segmentNumber) {
            this.segmentNumber = segmentNumber;
        }
        
        public Boolean getIand() {
            return iand;
        }
        
        public void setIand(Boolean iand) {
            this.iand = iand;
        }
        
        public Boolean getIsRs() {
            return isRs;
        }
        
        public void setIsRs(Boolean isRs) {
            this.isRs = isRs;
        }
        
        public List<Object> getStopOvers() {
            return stopOvers;
        }
        
        public void setStopOvers(List<Object> stopOvers) {
            this.stopOvers = stopOvers;
        }
        
        public FlightDetails getFlightDetails() {
            return flightDetails;
        }
        
        public void setFlightDetails(FlightDetails flightDetails) {
            this.flightDetails = flightDetails;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirportInfo {
        private String code;
        private String name;
        private String city;
        private String cityCode;
        private String country;
        private String countryCode;
        private String terminal;
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getCity() {
            return city;
        }
        
        public void setCity(String city) {
            this.city = city;
        }
        
        public String getCityCode() {
            return cityCode;
        }
        
        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }
        
        public String getCountry() {
            return country;
        }
        
        public void setCountry(String country) {
            this.country = country;
        }
        
        public String getCountryCode() {
            return countryCode;
        }
        
        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }
        
        public String getTerminal() {
            return terminal;
        }
        
        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FlightDetails {
        @JsonProperty("aI")
        private AirlineInfo airlineInfo;
        
        @JsonProperty("fN")
        private String flightNumber;
        
        @JsonProperty("eT")
        private String equipmentType;
        
        public AirlineInfo getAirlineInfo() {
            return airlineInfo;
        }
        
        public void setAirlineInfo(AirlineInfo airlineInfo) {
            this.airlineInfo = airlineInfo;
        }
        
        public String getFlightNumber() {
            return flightNumber;
        }
        
        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }
        
        public String getEquipmentType() {
            return equipmentType;
        }
        
        public void setEquipmentType(String equipmentType) {
            this.equipmentType = equipmentType;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirlineInfo {
        private String code;
        private String name;
        private Boolean isLcc;
        
        public String getCode() {
            return code;
        }
        
        public void setCode(String code) {
            this.code = code;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public Boolean getIsLcc() {
            return isLcc;
        }
        
        public void setIsLcc(Boolean isLcc) {
            this.isLcc = isLcc;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PriceInfo {
        private Map<String, Object> fd; // Fare details
        private String fareIdentifier;
        private String id;
        private List<Object> msri;
        private List<Object> messages;
        private Map<String, Object> tai; // Additional info with baggage per segment
        private Boolean icca;

        public Map<String, Object> getFd() {
            return fd;
        }

        public void setFd(Map<String, Object> fd) {
            this.fd = fd;
        }

        public String getFareIdentifier() {
            return fareIdentifier;
        }

        public void setFareIdentifier(String fareIdentifier) {
            this.fareIdentifier = fareIdentifier;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Object> getMsri() {
            return msri;
        }

        public void setMsri(List<Object> msri) {
            this.msri = msri;
        }

        public List<Object> getMessages() {
            return messages;
        }

        public void setMessages(List<Object> messages) {
            this.messages = messages;
        }

        public Map<String, Object> getTai() {
            return tai;
        }

        public void setTai(Map<String, Object> tai) {
            this.tai = tai;
        }

        public Boolean getIcca() {
            return icca;
        }

        public void setIcca(Boolean icca) {
            this.icca = icca;
        }

        // Helper method to extract total price
        public Double getTotalPrice() {
            if (fd != null && fd.containsKey("ADULT")) {
                Map<String, Object> adult = (Map<String, Object>) fd.get("ADULT");
                if (adult != null && adult.containsKey("fC")) {
                    Map<String, Object> fareComponents = (Map<String, Object>) adult.get("fC");
                    if (fareComponents != null && fareComponents.containsKey("TF")) {
                        return ((Number) fareComponents.get("TF")).doubleValue();
                    }
                }
            }
            return null;
        }
    }
}

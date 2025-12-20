package com.example.travelhub.flightbooking.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class FlightOptionDto {

    private String priceId;
    private String direction;
    private String from;
    private String to;
    private String departure;
    private String arrival;
    private int totalDurationMinutes;
    private int stops;
    private String marketingAirlineCode;
    private String marketingAirlineName;
    private String flightNumber;
    private String cabinClass;
    private String bookingClass;
    private BigDecimal totalFare;
    private String currency;
    private Integer seatsRemaining;
    private String baggageCheckIn;
    private String baggageCabin;
    private boolean refundable;

    private List<SegmentDto> segments;

    public static class SegmentDto {
        private String from;
        private String to;
        private String departure;
        private String arrival;
        private Integer duration;
        private String airlineCode;
        private String airlineName;
        private String flightNumber;

        public SegmentDto() {
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getDeparture() {
            return departure;
        }

        public void setDeparture(String departure) {
            this.departure = departure;
        }

        public String getArrival() {
            return arrival;
        }

        public void setArrival(String arrival) {
            this.arrival = arrival;
        }

        public Integer getDuration() {
            return duration;
        }

        public void setDuration(Integer duration) {
            this.duration = duration;
        }

        public String getAirlineCode() {
            return airlineCode;
        }

        public void setAirlineCode(String airlineCode) {
            this.airlineCode = airlineCode;
        }

        public String getAirlineName() {
            return airlineName;
        }

        public void setAirlineName(String airlineName) {
            this.airlineName = airlineName;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public void setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
        }
    }

    public FlightOptionDto(String priceId, String direction, String from, String to, String departure, String arrival,
			int totalDurationMinutes, int stops, String marketingAirlineCode, String marketingAirlineName,
			String flightNumber, String cabinClass, String bookingClass, BigDecimal totalFare, String currency,
			Integer seatsRemaining, String baggageCheckIn, String baggageCabin, boolean refundable) {
		super();
		this.priceId = priceId;
		this.direction = direction;
		this.from = from;
		this.to = to;
		this.departure = departure;
		this.arrival = arrival;
		this.totalDurationMinutes = totalDurationMinutes;
		this.stops = stops;
		this.marketingAirlineCode = marketingAirlineCode;
		this.marketingAirlineName = marketingAirlineName;
		this.flightNumber = flightNumber;
		this.cabinClass = cabinClass;
		this.bookingClass = bookingClass;
		this.totalFare = totalFare;
		this.currency = currency;
		this.seatsRemaining = seatsRemaining;
		this.baggageCheckIn = baggageCheckIn;
		this.baggageCabin = baggageCabin;
		this.refundable = refundable;
	}

	public FlightOptionDto() {
    }

	public String getPriceId() {
		return priceId;
	}

	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public int getTotalDurationMinutes() {
		return totalDurationMinutes;
	}

	public void setTotalDurationMinutes(int totalDurationMinutes) {
		this.totalDurationMinutes = totalDurationMinutes;
	}

	public int getStops() {
		return stops;
	}

	public void setStops(int stops) {
		this.stops = stops;
	}

	public String getMarketingAirlineCode() {
		return marketingAirlineCode;
	}

	public void setMarketingAirlineCode(String marketingAirlineCode) {
		this.marketingAirlineCode = marketingAirlineCode;
	}

	public String getMarketingAirlineName() {
		return marketingAirlineName;
	}

	public void setMarketingAirlineName(String marketingAirlineName) {
		this.marketingAirlineName = marketingAirlineName;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getCabinClass() {
		return cabinClass;
	}

	public void setCabinClass(String cabinClass) {
		this.cabinClass = cabinClass;
	}

	public String getBookingClass() {
		return bookingClass;
	}

	public void setBookingClass(String bookingClass) {
		this.bookingClass = bookingClass;
	}

	public BigDecimal getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(BigDecimal totalFare) {
		this.totalFare = totalFare;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getSeatsRemaining() {
		return seatsRemaining;
	}

	public void setSeatsRemaining(Integer seatsRemaining) {
		this.seatsRemaining = seatsRemaining;
	}

	public String getBaggageCheckIn() {
		return baggageCheckIn;
	}

	public void setBaggageCheckIn(String baggageCheckIn) {
		this.baggageCheckIn = baggageCheckIn;
	}

	public String getBaggageCabin() {
		return baggageCabin;
	}

	public void setBaggageCabin(String baggageCabin) {
		this.baggageCabin = baggageCabin;
	}

	public boolean isRefundable() {
		return refundable;
	}

	public void setRefundable(boolean refundable) {
		this.refundable = refundable;
	}

	public List<SegmentDto> getSegments() {
		return segments;
	}

	public void setSegments(List<SegmentDto> segments) {
		this.segments = segments;
	}

	@Override
	public String toString() {
		return "FlightOptionDto [priceId=" + priceId + ", direction=" + direction + ", from=" + from + ", to=" + to
				+ ", departure=" + departure + ", arrival=" + arrival + ", totalDurationMinutes=" + totalDurationMinutes
				+ ", stops=" + stops + ", marketingAirlineCode=" + marketingAirlineCode + ", marketingAirlineName="
				+ marketingAirlineName + ", flightNumber=" + flightNumber + ", cabinClass=" + cabinClass
				+ ", bookingClass=" + bookingClass + ", totalFare=" + totalFare + ", currency=" + currency
				+ ", seatsRemaining=" + seatsRemaining + ", baggageCheckIn=" + baggageCheckIn + ", baggageCabin="
				+ baggageCabin + ", refundable=" + refundable + "]";
	}

}

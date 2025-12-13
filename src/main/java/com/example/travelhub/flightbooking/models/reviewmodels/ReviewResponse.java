package com.example.travelhub.flightbooking.models.reviewmodels;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.travelhub.flightbooking.service.TripjackResponse.SearchQuery;
import com.example.travelhub.flightbooking.service.TripjackResponse.Status;
import com.example.travelhub.flightbooking.service.TripjackResponse.TripInfo;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResponse {
	private List<TripInfo> tripInfos;
    private SearchQuery searchQuery;
    private String bookingId;
    private JsonNode totalPriceInfo;
    private Status status;
    private Conditions conditions;

	public ReviewResponse() {
		super();
	}

	public List<TripInfo> getTripInfos() {
		return tripInfos;
	}

	public void setTripInfos(List<TripInfo> tripInfos) {
		this.tripInfos = tripInfos;
	}

	public SearchQuery getSearchQuery() {
		return searchQuery;
	}

	public void setSearchQuery(SearchQuery searchQuery) {
		this.searchQuery = searchQuery;
	}

	public String getBookingId() {
		return bookingId;
	}

	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}

	public JsonNode getTotalPriceInfo() {
		return totalPriceInfo;
	}

	public void setTotalPriceInfo(JsonNode totalPriceInfo) {
		this.totalPriceInfo = totalPriceInfo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Conditions getConditions() {
		return conditions;
	}

	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}
}

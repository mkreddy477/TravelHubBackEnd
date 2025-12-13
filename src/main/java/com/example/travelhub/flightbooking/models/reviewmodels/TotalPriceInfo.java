package com.example.travelhub.flightbooking.models.reviewmodels;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.travelhub.flightbooking.models.FareDetail;
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class TotalPriceInfo {
	@JsonProperty("totalFareDetail")
	private FareDetail totalFareDetail;

	public TotalPriceInfo() {
		super();
	}

	public FareDetail getTotalFareDetail() {
		return totalFareDetail;
	}

	public void setTotalFareDetail(FareDetail totalFareDetail) {
		this.totalFareDetail = totalFareDetail;
	}

}

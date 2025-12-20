package com.example.travelhub.flightbooking.models.reviewmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DateOfBirth {
	
	 private Boolean adobr;
	    private Boolean cdobr;
	    private Boolean idobr;
		
	public Boolean getAdobr() {
		return adobr;
	}

	public void setAdobr(Boolean adobr) {
		this.adobr = adobr;
	}

	public Boolean getCdobr() {
		return cdobr;
	}

	public void setCdobr(Boolean cdobr) {
		this.cdobr = cdobr;
	}

	public Boolean getIdobr() {
		return idobr;
	}

	public void setIdobr(Boolean idobr) {
		this.idobr = idobr;
	}

}

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
public class DuplicateCheck {
	
	 private Boolean ida;
	    private Boolean idm;
	    private Boolean iqpe;

	public Boolean getIda() {
		return ida;
	}

	public void setIda(Boolean ida) {
		this.ida = ida;
	}

	public Boolean getIdm() {
		return idm;
	}

	public void setIdm(Boolean idm) {
		this.idm = idm;
	}

	public Boolean getIqpe() {
		return iqpe;
	}

	public void setIqpe(Boolean iqpe) {
		this.iqpe = iqpe;
	}

}

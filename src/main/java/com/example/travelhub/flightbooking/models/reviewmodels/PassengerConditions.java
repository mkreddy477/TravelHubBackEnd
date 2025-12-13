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
public class PassengerConditions {
	
	private Boolean pped;
    private Boolean pid;
    private Boolean pm;
    private Boolean dobe;

	public PassengerConditions() {
		super();
	}

	public Boolean getPped() {
		return pped;
	}

	public void setPped(Boolean pped) {
		this.pped = pped;
	}

	public Boolean getPid() {
		return pid;
	}

	public void setPid(Boolean pid) {
		this.pid = pid;
	}

	public Boolean getPm() {
		return pm;
	}

	public void setPm(Boolean pm) {
		this.pm = pm;
	}

	public Boolean getDobe() {
		return dobe;
	}

	public void setDobe(Boolean dobe) {
		this.dobe = dobe;
	}

}

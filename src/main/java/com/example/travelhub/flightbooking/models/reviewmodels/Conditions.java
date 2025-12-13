package com.example.travelhub.flightbooking.models.reviewmodels;

import java.util.List;

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
public class Conditions {
	
	  private PassengerConditions pcs;
	    private List<String> ffas;
	    private Boolean isa;
	    private DateOfBirth dob;
	    private Boolean iecr;
	    private DuplicateCheck dc;
	    private NameLimits anlm;
	    private Boolean ipa;
	
	public Conditions() {
		super();
	}

	public PassengerConditions getPcs() {
		return pcs;
	}

	public void setPcs(PassengerConditions pcs) {
		this.pcs = pcs;
	}

	public List<String> getFfas() {
		return ffas;
	}

	public void setFfas(List<String> ffas) {
		this.ffas = ffas;
	}

	public Boolean getIsa() {
		return isa;
	}

	public void setIsa(Boolean isa) {
		this.isa = isa;
	}

	public DateOfBirth getDob() {
		return dob;
	}

	public void setDob(DateOfBirth dob) {
		this.dob = dob;
	}

	public Boolean getIecr() {
		return iecr;
	}

	public void setIecr(Boolean iecr) {
		this.iecr = iecr;
	}

	public DuplicateCheck getDc() {
		return dc;
	}

	public void setDc(DuplicateCheck dc) {
		this.dc = dc;
	}

	public NameLimits getAnlm() {
		return anlm;
	}

	public void setAnlm(NameLimits anlm) {
		this.anlm = anlm;
	}

	public Boolean getIpa() {
		return ipa;
	}

	public void setIpa(Boolean ipa) {
		this.ipa = ipa;
	}
	  

}

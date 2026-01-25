package com.example.travelhub.flightbooking.models.reviewmodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NameLimits {

	
	 @JsonProperty("fN")
	    private Integer firstName;
	    
	    @JsonProperty("lN")
	    private Integer lastName;
	    
	    @JsonProperty("n")
	    private Integer name;

	    @JsonProperty("finml")
	    private Integer finml;

}

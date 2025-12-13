package com.example.travelhub.flightbooking.models.reviewmodels;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NameLimits {

	
	 @JsonProperty("fN")
	    private Integer firstName;
	    
	    @JsonProperty("lN")
	    private Integer lastName;
	    
	    @JsonProperty("n")
	    private Integer name;

}

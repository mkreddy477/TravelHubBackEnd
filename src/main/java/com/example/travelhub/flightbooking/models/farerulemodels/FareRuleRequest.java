package com.example.travelhub.flightbooking.models.farerulemodels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FareRuleRequest {

    private String id;
    private String flowType;

    public FareRuleRequest() {
        super();
    }

    public FareRuleRequest(String id, String flowType) {
        super();
        this.id = id;
        this.flowType = flowType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    @Override
    public String toString() {
        return "FareRuleRequest{id='" + id + "', flowType='" + flowType + "'}";
    }
}

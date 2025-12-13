package com.example.travelhub.flightbooking.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TripjackSearchQuery {

    private String cabinClass;
    private PaxInfo paxInfo;
    private List<RouteInfo> routeInfos;
    private SearchModifiers searchModifiers;          // null for now
    private List<PreferredAirline> preferredAirline; // null for now

    public TripjackSearchQuery() {
    }

    public TripjackSearchQuery(String cabinClass,
                               PaxInfo paxInfo,
                               List<RouteInfo> routeInfos,
                               SearchModifiers searchModifiers,
                               List<PreferredAirline> preferredAirline) {
        this.cabinClass = cabinClass;
        this.paxInfo = paxInfo;
        this.routeInfos = routeInfos;
        this.searchModifiers = searchModifiers;
        this.preferredAirline = preferredAirline;
    }

    public String getCabinClass() { return cabinClass; }
    public void setCabinClass(String cabinClass) { this.cabinClass = cabinClass; }

    public PaxInfo getPaxInfo() { return paxInfo; }
    public void setPaxInfo(PaxInfo paxInfo) { this.paxInfo = paxInfo; }

    public List<RouteInfo> getRouteInfos() { return routeInfos; }
    public void setRouteInfos(List<RouteInfo> routeInfos) { this.routeInfos = routeInfos; }

    public SearchModifiers getSearchModifiers() { return searchModifiers; }
    public void setSearchModifiers(SearchModifiers searchModifiers) { this.searchModifiers = searchModifiers; }

    public List<PreferredAirline> getPreferredAirline() { return preferredAirline; }
    public void setPreferredAirline(List<PreferredAirline> preferredAirline) { this.preferredAirline = preferredAirline; }
}

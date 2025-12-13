package com.example.travelhub.flightbooking.models;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Component
public class TripjackMapper {

    public TripjackSearchRequest toTripjackRequest(FlightSearchRequest uiReq) {

        // cabin class
        String cabinClass = uiReq.getTravelClass() == null
                ? "ECONOMY"
                : uiReq.getTravelClass().toUpperCase(Locale.ROOT);

        // passengers -> PaxInfo
        Passengers p = uiReq.getPassengers();
        PaxInfo paxInfo = new PaxInfo(
                p == null ? "1" : String.valueOf(p.getAdults()),
                p == null ? "0" : String.valueOf(p.getChildren()),
                p == null ? "0" : String.valueOf(p.getInfants())
        );

        // routes
        List<RouteInfo> routeInfos = buildRouteInfos(uiReq);

        // Build searchModifiers and preferredAirline from advancedOptions
        SearchModifiers searchModifiers = buildSearchModifiers(uiReq.getAdvancedOptions());
        List<PreferredAirline> preferredAirlines = buildPreferredAirlines(uiReq.getAdvancedOptions());

        TripjackSearchQuery searchQuery = new TripjackSearchQuery(
                cabinClass,
                paxInfo,
                routeInfos,
                searchModifiers,
                preferredAirlines
        );

        return new TripjackSearchRequest(searchQuery);
    }

    private SearchModifiers buildSearchModifiers(AdvancedOptions advancedOptions) {
        if (advancedOptions == null) {
            return null;
        }

        // If directOnly is true, set isDirectFlight=true and isConnectingFlight=false
        // Otherwise, allow both direct and connecting flights
        boolean isDirectFlight = advancedOptions.isDirectOnly();
        boolean isConnectingFlight = !advancedOptions.isDirectOnly();

        // pft (Preferred Flight Type) - can be set based on requirements
        // Common values: "REGULAR", "CORPORATE", etc. - set to null if not needed
        String pft = null;

        return new SearchModifiers(isDirectFlight, isConnectingFlight, pft);
    }

    private List<PreferredAirline> buildPreferredAirlines(AdvancedOptions advancedOptions) {
        if (advancedOptions == null || advancedOptions.getPreferredAirline() == null 
                || advancedOptions.getPreferredAirline().trim().isEmpty()) {
            return null;
        }

        List<PreferredAirline> airlines = new ArrayList<>();
        
        // Split by comma if multiple airlines are provided (e.g., "AA,UA,DL")
        String[] airlineCodes = advancedOptions.getPreferredAirline().split(",");
        for (String code : airlineCodes) {
            String trimmedCode = code.trim();
            if (!trimmedCode.isEmpty()) {
                airlines.add(new PreferredAirline(trimmedCode));
            }
        }

        return airlines.isEmpty() ? null : airlines;
    }

    private List<RouteInfo> buildRouteInfos(FlightSearchRequest uiReq) {
        List<RouteInfo> routeInfos = new ArrayList<RouteInfo>();

        String tripType = uiReq.getTripType();

        if ("one-way".equalsIgnoreCase(tripType)) {

            routeInfos.add(new RouteInfo(
                    new CityOrAirport(uiReq.getOrigin()),
                    new CityOrAirport(uiReq.getDestination()),
                    uiReq.getDepartureDate().toString()
            ));

        } else if ("round-trip".equalsIgnoreCase(tripType)) {

            // onward
            routeInfos.add(new RouteInfo(
                    new CityOrAirport(uiReq.getOrigin()),
                    new CityOrAirport(uiReq.getDestination()),
                    uiReq.getDepartureDate().toString()
            ));
            // return
            routeInfos.add(new RouteInfo(
                    new CityOrAirport(uiReq.getDestination()),
                    new CityOrAirport(uiReq.getOrigin()),
                    uiReq.getReturnDate().toString()
            ));

        } else if ("multi-city".equalsIgnoreCase(tripType)) {

            if (uiReq.getMultiCityLegs() != null) {
                // 🔴 use top-level MultiCityLeg, not FlightSearchRequest.MultiCityLeg
                for (MultiCityLeg leg : uiReq.getMultiCityLegs()) {
                    routeInfos.add(new RouteInfo(
                            new CityOrAirport(leg.getOrigin()),
                            new CityOrAirport(leg.getDestination()),
                            leg.getTravelDate().toString()
                    ));
                }
            }

        } else {
            throw new IllegalArgumentException("Unsupported tripType: " + tripType);
        }

        // 🔹 Tripjack rule: travelDate must be in ascending order
        Collections.sort(routeInfos, new Comparator<RouteInfo>() {
            @Override
            public int compare(RouteInfo r1, RouteInfo r2) {
                LocalDate d1 = LocalDate.parse(r1.getTravelDate());
                LocalDate d2 = LocalDate.parse(r2.getTravelDate());
                return d1.compareTo(d2);
            }
        });

        return routeInfos;
    }
}

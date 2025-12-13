package com.example.travelhub.flightbooking.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripjackResponseMapper {

    public static FlightSearchResultGroup toUiResult(
            FlightSearchRequest uiReq,
            TripjackSearchResponse resp
    ) {
        if (resp == null || resp.getSearchResult() == null || resp.getSearchResult().getTripInfos() == null) {
            return new FlightSearchResultGroup(
                    Collections.<FlightOptionDto>emptyList(),
                    Collections.<FlightOptionDto>emptyList(),
                    Collections.<FlightOptionDto>emptyList()
            );
        }

        TripInfos tripInfos = resp.getSearchResult().getTripInfos();

        List<FlightOptionDto> onward  = mapDirection("ONWARD", tripInfos.getOnward());
        List<FlightOptionDto> returns = mapDirection("RETURN", tripInfos.getReturnTrips());
        List<FlightOptionDto> combos  = mapDirection("COMBO",  tripInfos.getCombo());

        return new FlightSearchResultGroup(onward, returns, combos);
    }

    private static List<FlightOptionDto> mapDirection(
            String direction,
            List<TripInfo> tripInfos
    ) {
        if (tripInfos == null) {
            return Collections.emptyList();
        }

        List<FlightOptionDto> result = new ArrayList<FlightOptionDto>();

        for (TripInfo trip : tripInfos) {

            if (trip.getTotalPriceList() == null || trip.getTotalPriceList().isEmpty()) {
                continue;
            }

            TotalPriceList price = trip.getTotalPriceList().get(0);

            if (trip.getSegmentInfos() == null || trip.getSegmentInfos().isEmpty()) {
                continue;
            }

            SegmentInfo first = trip.getSegmentInfos().get(0);
            SegmentInfo last  = trip.getSegmentInfos().get(trip.getSegmentInfos().size() - 1);

            FareDetail fareDetail = price.getFd();
            FareComponent adultFare = (fareDetail != null) ? fareDetail.getADULT() : null;

            BigDecimal totalFare = BigDecimal.ZERO;
            if (adultFare != null && adultFare.getfC() != null && adultFare.getfC().get("TF") != null) {
                totalFare = adultFare.getfC().get("TF");
            }

            Integer seatsRemaining = (adultFare != null) ? adultFare.getsR() : null;
            BaggageInfo bi = (adultFare != null) ? adultFare.getbI() : null;

            int totalDurationMinutes = 0;
            for (SegmentInfo s : trip.getSegmentInfos()) {
                Integer d = s.getDuration();
                if (d != null) {
                    totalDurationMinutes += d.intValue();
                }
            }

            int stops = (first.getStops() == null) ? 0 : first.getStops().intValue();

            String currency = "INR";

            boolean refundable = false;
            if (adultFare != null && adultFare.getrT() != null) {
                refundable = adultFare.getrT().intValue() != 0;
            }

            FlightOptionDto dto = new FlightOptionDto(
                    price.getId(),
                    direction,
                    first.getDepartureAirport().getCode(),
                    last.getArrivalAirport().getCode(),
                    first.getDt(),
                    last.getAt(),
                    totalDurationMinutes,
                    stops,
                    first.getFlightDesignator().getAirlineInfo().getCode(),
                    first.getFlightDesignator().getAirlineInfo().getName(),
                    first.getFlightDesignator().getFlightNumber(),
                    adultFare != null ? adultFare.getCc() : null,
                    adultFare != null ? adultFare.getcB() : null,
                    totalFare,
                    currency,
                    seatsRemaining,
                    bi != null ? bi.getCheckInBaggage() : null,
                    bi != null ? bi.getCabinBaggage() : null,
                    refundable
            );

            result.add(dto);
        }

        return result;
    }
}

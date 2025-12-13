package com.example.travelhub.flightbooking.models;

import java.util.List;

public class TripInfo {

    private java.util.List<SegmentInfo> segmentInfos;
    private java.util.List<TotalPriceList> totalPriceList;

    public TripInfo() {
    }

    public java.util.List<SegmentInfo> getSegmentInfos() {
        return segmentInfos;
    }

    public void setSegmentInfos(java.util.List<SegmentInfo> segmentInfos) {
        this.segmentInfos = segmentInfos;
    }

    public java.util.List<TotalPriceList> getTotalPriceList() {
        return totalPriceList;
    }

    public void setTotalPriceList(java.util.List<TotalPriceList> totalPriceList) {
        this.totalPriceList = totalPriceList;
    }
}

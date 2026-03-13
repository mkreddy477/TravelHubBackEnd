package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class SearchQuery {
    
    private String checkinDate;
    private String checkoutDate;
    private List<RoomInfo> roomInfo;
    private SearchCriteria searchCriteria;
    private SearchPreferences searchPreferences;
    
    public SearchQuery() {
    }
    
    public String getCheckinDate() {
        return checkinDate;
    }
    
    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }
    
    public String getCheckoutDate() {
        return checkoutDate;
    }
    
    public void setCheckoutDate(String checkoutDate) {
        this.checkoutDate = checkoutDate;
    }
    
    public List<RoomInfo> getRoomInfo() {
        return roomInfo;
    }
    
    public void setRoomInfo(List<RoomInfo> roomInfo) {
        this.roomInfo = roomInfo;
    }
    
    public SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }
    
    public void setSearchCriteria(SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
    
    public SearchPreferences getSearchPreferences() {
        return searchPreferences;
    }
    
    public void setSearchPreferences(SearchPreferences searchPreferences) {
        this.searchPreferences = searchPreferences;
    }
}
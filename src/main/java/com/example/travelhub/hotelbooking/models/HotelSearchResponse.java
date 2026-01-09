package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class HotelSearchResponse {
    
    private SearchResult searchResult;
    
    public HotelSearchResponse() {
    }
    
    public SearchResult getSearchResult() {
        return searchResult;
    }
    
    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
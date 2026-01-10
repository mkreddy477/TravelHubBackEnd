package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
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
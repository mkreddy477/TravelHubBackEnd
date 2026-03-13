package com.example.travelhub.hotelbooking.models;

import java.util.List;

public class HotelSearchRequest {
    
    private SearchQuery searchQuery;
    private boolean sync;
    
    public HotelSearchRequest() {
    }
    
    public SearchQuery getSearchQuery() {
        return searchQuery;
    }
    
    public void setSearchQuery(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }
    
    public boolean isSync() {
        return sync;
    }
    
    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
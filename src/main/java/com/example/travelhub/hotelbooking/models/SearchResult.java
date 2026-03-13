package com.example.travelhub.hotelbooking.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    
    private List<HotelInfo> his;
    private int size;
    
    public SearchResult() {
    }
    
    public List<HotelInfo> getHis() {
        return his;
    }
    
    public void setHis(List<HotelInfo> his) {
        this.his = his;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
}
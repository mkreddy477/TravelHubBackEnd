package com.example.travelhub.locationservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelData {
    
    @JsonProperty("hotelOpInfos")
    private List<HotelInfo> hotelOpInfos;
    
    public List<HotelInfo> getHotelOpInfos() {
        return hotelOpInfos;
    }
    
    public void setHotelOpInfos(List<HotelInfo> hotelOpInfos) {
        this.hotelOpInfos = hotelOpInfos;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HotelInfo {
        private String name;
        private String hotelId;
        private Integer rating;
        private Address address;
        private Geolocation geolocation;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getHotelId() {
            return hotelId;
        }
        
        public void setHotelId(String hotelId) {
            this.hotelId = hotelId;
        }
        
        public Integer getRating() {
            return rating;
        }
        
        public void setRating(Integer rating) {
            this.rating = rating;
        }
        
        public Address getAddress() {
            return address;
        }
        
        public void setAddress(Address address) {
            this.address = address;
        }
        
        public Geolocation getGeolocation() {
            return geolocation;
        }
        
        public void setGeolocation(Geolocation geolocation) {
            this.geolocation = geolocation;
        }
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Address {
            private City city;
            private State state;
            private Country country;
            
            public City getCity() {
                return city;
            }
            
            public void setCity(City city) {
                this.city = city;
            }
            
            public State getState() {
                return state;
            }
            
            public void setState(State state) {
                this.state = state;
            }
            
            public Country getCountry() {
                return country;
            }
            
            public void setCountry(Country country) {
                this.country = country;
            }
            
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class City {
                private String name;
                
                public String getName() {
                    return name;
                }
                
                public void setName(String name) {
                    this.name = name;
                }
            }
            
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class State {
                private String name;
                
                public String getName() {
                    return name;
                }
                
                public void setName(String name) {
                    this.name = name;
                }
            }
            
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Country {
                private String code;
                private String name;
                
                public String getCode() {
                    return code;
                }
                
                public void setCode(String code) {
                    this.code = code;
                }
                
                public String getName() {
                    return name;
                }
                
                public void setName(String name) {
                    this.name = name;
                }
            }
        }
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Geolocation {
            private String lt;
            private String ln;
            
            public String getLt() {
                return lt;
            }
            
            public void setLt(String lt) {
                this.lt = lt;
            }
            
            public String getLn() {
                return ln;
            }
            
            public void setLn(String ln) {
                this.ln = ln;
            }
        }
    }
}
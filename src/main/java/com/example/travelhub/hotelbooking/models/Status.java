package com.example.travelhub.hotelbooking.models;

public class Status {

    private boolean success;
    private int httpStatus;
    
    // getters & setters
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}  
}
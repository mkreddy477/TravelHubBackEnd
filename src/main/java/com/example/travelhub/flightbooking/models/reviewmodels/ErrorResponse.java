package com.example.travelhub.flightbooking.models.reviewmodels;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
	
	 private LocalDateTime timestamp;
	    private Integer status;
	    private String error;
	    private String message;
	    private List<String> details;
		public LocalDateTime getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public List<String> getDetails() {
			return details;
		}
		public void setDetails(List<String> details) {
			this.details = details;
		}
		@Override
		public String toString() {
			return "ErrorResponse [timestamp=" + timestamp + ", status=" + status + ", error=" + error + ", message="
					+ message + ", details=" + details + "]";
		}	
	    

}

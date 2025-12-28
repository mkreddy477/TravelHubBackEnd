package com.example.travelhub.flightbooking.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private boolean success;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	@Override
	public String toString() {
		return "RegisterResponse [userId=" + userId + ", username=" + username + ", email=" + email + ", firstName="
				+ firstName + ", lastName=" + lastName + ", message=" + message + ", success=" + success + "]";
	}

    public static RegisterResponseBuilder builder() {
        return new RegisterResponseBuilder();
    }

    public static class RegisterResponseBuilder {
        private Long userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String message;
        private boolean success;

        public RegisterResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public RegisterResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public RegisterResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RegisterResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RegisterResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public RegisterResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public RegisterResponse build() {
            RegisterResponse response = new RegisterResponse();
            response.setUserId(this.userId);
            response.setUsername(this.username);
            response.setEmail(this.email);
            response.setFirstName(this.firstName);
            response.setLastName(this.lastName);
            response.setMessage(this.message);
            response.setSuccess(this.success);
            return response;
        }
    }
}

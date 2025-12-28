package com.example.travelhub.flightbooking.models;

public class UserResponse {

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }

    public static class UserResponseBuilder {
        private Long userId;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String phone;
        private String message;
        private boolean success;

        public UserResponseBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public UserResponseBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserResponseBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public UserResponseBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public UserResponse build() {
            UserResponse response = new UserResponse();
            response.setUserId(this.userId);
            response.setUsername(this.username);
            response.setEmail(this.email);
            response.setFirstName(this.firstName);
            response.setLastName(this.lastName);
            response.setPhone(this.phone);
            response.setMessage(this.message);
            response.setSuccess(this.success);
            return response;
        }
    }
}

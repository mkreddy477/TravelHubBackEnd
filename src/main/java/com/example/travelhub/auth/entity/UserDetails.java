package com.example.travelhub.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("USER_DETAILS")
public class UserDetails {

    @Id
    @Column("USER_DETAIL_ID")
    private Long userDetailId;

    @Column("ROLE_ID")
    private Long roleId;

    @Column("USERNAME")
    private String username;

    @Column("TITLE")
    private String title;

    @Column("FIRST_NAME")
    private String firstName;

    @Column("MIDDLE_NAME")
    private String middleName;

    @Column("LAST_NAME")
    private String lastName;

    @Column("PRIMARY_EMAIL")
    private String primaryEmail;

    @Column("ALTERNATE_EMAIL")
    private String alternateEmail;

    @Column("PRIMARY_PHONE")
    private String primaryPhone;

    @Column("ALTERNATE_PHONE")
    private String alternatePhone;

    @Column("IS_ACTIVE")
    private Boolean isActive;

    @Column("CREATED_BY")
    private Long createdBy;

    @Column("CREATED_ON")
    private LocalDateTime createdOn;

    @Column("UPDATED_BY")
    private Long updatedBy;

    @Column("UPDATED_ON")
    private LocalDateTime updatedOn;

    @Column("DELETED_BY")
    private Long deletedBy;

    @Column("DELETED_ON")
    private LocalDateTime deletedOn;

    public Long getUserDetailId() {
        return userDetailId;
    }

    public void setUserDetailId(Long userDetailId) {
        this.userDetailId = userDetailId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(Long deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(LocalDateTime deletedOn) {
        this.deletedOn = deletedOn;
    }

    public static UserDetailsBuilder builder() {
        return new UserDetailsBuilder();
    }

    public static class UserDetailsBuilder {
        private Long roleId;
        private String username;
        private String firstName;
        private String lastName;
        private String primaryEmail;
        private String primaryPhone;
        private Boolean isActive;
        private LocalDateTime createdOn;

        public UserDetailsBuilder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserDetailsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDetailsBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserDetailsBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserDetailsBuilder primaryEmail(String primaryEmail) {
            this.primaryEmail = primaryEmail;
            return this;
        }

        public UserDetailsBuilder primaryPhone(String primaryPhone) {
            this.primaryPhone = primaryPhone;
            return this;
        }

        public UserDetailsBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public UserDetailsBuilder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public UserDetails build() {
            UserDetails userDetails = new UserDetails();
            userDetails.setRoleId(this.roleId);
            userDetails.setUsername(this.username);
            userDetails.setFirstName(this.firstName);
            userDetails.setLastName(this.lastName);
            userDetails.setPrimaryEmail(this.primaryEmail);
            userDetails.setPrimaryPhone(this.primaryPhone);
            userDetails.setIsActive(this.isActive);
            userDetails.setCreatedOn(this.createdOn);
            return userDetails;
        }
    }
}

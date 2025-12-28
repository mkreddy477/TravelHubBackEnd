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
@Table("USER_LOGIN_DETAILS")
public class UserLoginDetails {

    @Id
    @Column("USER_LOGIN_DETAIL_ID")
    private Long userLoginDetailId;

    @Column("USER_DETAIL_ID")
    private Long userDetailId;

    @Column("PASSWORD")
    private String password;

    @Column("PASSWORDSALT")
    private String passwordSalt;

    @Column("PASSWORDQUESTION")
    private String passwordQuestion;

    @Column("PASSWORDANSWER")
    private String passwordAnswer;

    @Column("ISLOCKEDOUT")
    private Boolean isLockedOut;

    @Column("LASTLOGINDATE")
    private LocalDateTime lastLoginDate;

    @Column("LASTPASSWORDCHANGEDDATE")
    private LocalDateTime lastPasswordChangedDate;

    @Column("LASTLOCKOUTDATE")
    private LocalDateTime lastLockoutDate;

    @Column("FAILEDPASSWORDATTEMPTCOUNT")
    private Integer failedPasswordAttemptCount;

    @Column("FAILEDPASSWORDANSWERATTEMPTCOUNT")
    private Integer failedPasswordAnswerAttemptCount;

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

	public Long getUserLoginDetailId() {
		return userLoginDetailId;
	}

	public void setUserLoginDetailId(Long userLoginDetailId) {
		this.userLoginDetailId = userLoginDetailId;
	}

	public Long getUserDetailId() {
		return userDetailId;
	}

	public void setUserDetailId(Long userDetailId) {
		this.userDetailId = userDetailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordQuestion() {
		return passwordQuestion;
	}

	public void setPasswordQuestion(String passwordQuestion) {
		this.passwordQuestion = passwordQuestion;
	}

	public String getPasswordAnswer() {
		return passwordAnswer;
	}

	public void setPasswordAnswer(String passwordAnswer) {
		this.passwordAnswer = passwordAnswer;
	}

	public Boolean getIsLockedOut() {
		return isLockedOut;
	}

	public void setIsLockedOut(Boolean isLockedOut) {
		this.isLockedOut = isLockedOut;
	}

	public LocalDateTime getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(LocalDateTime lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public LocalDateTime getLastPasswordChangedDate() {
		return lastPasswordChangedDate;
	}

	public void setLastPasswordChangedDate(LocalDateTime lastPasswordChangedDate) {
		this.lastPasswordChangedDate = lastPasswordChangedDate;
	}

	public LocalDateTime getLastLockoutDate() {
		return lastLockoutDate;
	}

	public void setLastLockoutDate(LocalDateTime lastLockoutDate) {
		this.lastLockoutDate = lastLockoutDate;
	}

	public Integer getFailedPasswordAttemptCount() {
		return failedPasswordAttemptCount;
	}

	public void setFailedPasswordAttemptCount(Integer failedPasswordAttemptCount) {
		this.failedPasswordAttemptCount = failedPasswordAttemptCount;
	}

	public Integer getFailedPasswordAnswerAttemptCount() {
		return failedPasswordAnswerAttemptCount;
	}

	public void setFailedPasswordAnswerAttemptCount(Integer failedPasswordAnswerAttemptCount) {
		this.failedPasswordAnswerAttemptCount = failedPasswordAnswerAttemptCount;
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

	@Override
	public String toString() {
		return "UserLoginDetails [userLoginDetailId=" + userLoginDetailId + ", userDetailId=" + userDetailId
				+ ", password=" + password + ", passwordSalt=" + passwordSalt + ", passwordQuestion=" + passwordQuestion
				+ ", passwordAnswer=" + passwordAnswer + ", isLockedOut=" + isLockedOut + ", lastLoginDate="
				+ lastLoginDate + ", lastPasswordChangedDate=" + lastPasswordChangedDate + ", lastLockoutDate="
				+ lastLockoutDate + ", failedPasswordAttemptCount=" + failedPasswordAttemptCount
				+ ", failedPasswordAnswerAttemptCount=" + failedPasswordAnswerAttemptCount + ", createdBy=" + createdBy
				+ ", createdOn=" + createdOn + ", updatedBy=" + updatedBy + ", updatedOn=" + updatedOn + ", deletedBy="
				+ deletedBy + ", deletedOn=" + deletedOn + "]";
	}

    public static UserLoginDetailsBuilder builder() {
        return new UserLoginDetailsBuilder();
    }

    public static class UserLoginDetailsBuilder {
        private Long userDetailId;
        private String password;
        private String passwordSalt;
        private Boolean isLockedOut;
        private Integer failedPasswordAttemptCount;
        private Integer failedPasswordAnswerAttemptCount;
        private LocalDateTime createdOn;

        public UserLoginDetailsBuilder userDetailId(Long userDetailId) {
            this.userDetailId = userDetailId;
            return this;
        }

        public UserLoginDetailsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserLoginDetailsBuilder passwordSalt(String passwordSalt) {
            this.passwordSalt = passwordSalt;
            return this;
        }

        public UserLoginDetailsBuilder isLockedOut(Boolean isLockedOut) {
            this.isLockedOut = isLockedOut;
            return this;
        }

        public UserLoginDetailsBuilder failedPasswordAttemptCount(Integer failedPasswordAttemptCount) {
            this.failedPasswordAttemptCount = failedPasswordAttemptCount;
            return this;
        }

        public UserLoginDetailsBuilder failedPasswordAnswerAttemptCount(Integer failedPasswordAnswerAttemptCount) {
            this.failedPasswordAnswerAttemptCount = failedPasswordAnswerAttemptCount;
            return this;
        }

        public UserLoginDetailsBuilder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public UserLoginDetails build() {
            UserLoginDetails loginDetails = new UserLoginDetails();
            loginDetails.setUserDetailId(this.userDetailId);
            loginDetails.setPassword(this.password);
            loginDetails.setPasswordSalt(this.passwordSalt);
            loginDetails.setIsLockedOut(this.isLockedOut);
            loginDetails.setFailedPasswordAttemptCount(this.failedPasswordAttemptCount);
            loginDetails.setFailedPasswordAnswerAttemptCount(this.failedPasswordAnswerAttemptCount);
            loginDetails.setCreatedOn(this.createdOn);
            return loginDetails;
        }
    }
}

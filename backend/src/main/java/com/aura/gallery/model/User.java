package com.aura.gallery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // BCrypt hashed

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // OTP Fields
    @Column(length = 6)
    private String otp;

    @Column
    private LocalDateTime otpCreatedAt;

    @Column
    private LocalDateTime otpExpiresAt;

    @Column(nullable = false)
    private Boolean isOtpVerified = false;

    public enum Role {
        ARTIST, COLLECTOR
    }

    // Constructors
    public User() {}

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(Long id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpCreatedAt() {
        return otpCreatedAt;
    }

    public void setOtpCreatedAt(LocalDateTime otpCreatedAt) {
        this.otpCreatedAt = otpCreatedAt;
    }

    public LocalDateTime getOtpExpiresAt() {
        return otpExpiresAt;
    }

    public void setOtpExpiresAt(LocalDateTime otpExpiresAt) {
        this.otpExpiresAt = otpExpiresAt;
    }

    public Boolean getIsOtpVerified() {
        return isOtpVerified;
    }

    public void setIsOtpVerified(Boolean isOtpVerified) {
        this.isOtpVerified = isOtpVerified;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Builder
    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private Role role;
        private String otp;
        private LocalDateTime otpCreatedAt;
        private LocalDateTime otpExpiresAt;
        private Boolean isOtpVerified = false;

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder otp(String otp) {
            this.otp = otp;
            return this;
        }

        public UserBuilder otpCreatedAt(LocalDateTime otpCreatedAt) {
            this.otpCreatedAt = otpCreatedAt;
            return this;
        }

        public UserBuilder otpExpiresAt(LocalDateTime otpExpiresAt) {
            this.otpExpiresAt = otpExpiresAt;
            return this;
        }

        public UserBuilder isOtpVerified(Boolean isOtpVerified) {
            this.isOtpVerified = isOtpVerified;
            return this;
        }

        public User build() {
            User user = new User(id, name, email, password, role);
            user.setOtp(otp);
            user.setOtpCreatedAt(otpCreatedAt);
            user.setOtpExpiresAt(otpExpiresAt);
            user.setIsOtpVerified(isOtpVerified);
            return user;
        }
    }
}

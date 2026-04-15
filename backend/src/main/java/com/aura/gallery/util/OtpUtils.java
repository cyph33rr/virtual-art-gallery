package com.aura.gallery.util;

import java.util.Random;
import java.time.LocalDateTime;

public class OtpUtils {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALIDITY_MINUTES = 10; // OTP valid for 10 minutes

    /**
     * Generate a random 6-digit OTP
     */
    public static String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    /**
     * Get OTP expiration time (10 minutes from now)
     */
    public static LocalDateTime getOtpExpirationTime() {
        return LocalDateTime.now().plusMinutes(OTP_VALIDITY_MINUTES);
    }

    /**
     * Check if OTP is expired
     */
    public static boolean isOtpExpired(LocalDateTime otpExpiresAt) {
        if (otpExpiresAt == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(otpExpiresAt);
    }

    /**
     * Validate OTP
     */
    public static boolean isValidOtp(String otp) {
        return otp != null && otp.matches("\\d{6}");
    }
}

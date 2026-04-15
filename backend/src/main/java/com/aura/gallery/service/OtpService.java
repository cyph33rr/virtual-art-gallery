package com.aura.gallery.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OtpService {
    
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

    /**
     * Send OTP via email (for production, integrate with actual email service)
     * For now, we'll just log it to console
     */
    public void sendOtpEmail(String email, String otp) {
        // In production, you would integrate with a service like JavaMailSender
        // or SendGrid/AWS SES
        logger.info("═══════════════════════════════════════════");
        logger.info("OTP SENT TO: {}", email);
        logger.info("OTP CODE: {}", otp);
        logger.info("VALID FOR 10 MINUTES");
        logger.info("═══════════════════════════════════════════");
        // TODO: Implement actual email sending
    }

    /**
     * Resend OTP (in case user didn't receive it)
     */
    public void resendOtpEmail(String email, String otp) {
        logger.warn("═══════════════════════════════════════════");
        logger.warn("OTP RESENT TO: {}", email);
        logger.warn("NEW OTP CODE: {}", otp);
        logger.warn("VALID FOR 10 MINUTES");
        logger.warn("═══════════════════════════════════════════");
    }
}

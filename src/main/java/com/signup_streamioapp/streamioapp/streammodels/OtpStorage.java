package com.signup_streamioapp.streamioapp.streammodels;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class OtpStorage {
    private final Map<String, String> otpMap = new ConcurrentHashMap<>();

    public void storeOtp(String email, String otp) {
        otpMap.put(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {
        return otp.equals(otpMap.get(email));
    }

    public void removeOtp(String email) {
        otpMap.remove(email);
    }
}


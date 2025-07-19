package com.signup_streamioapp.streamioapp;


import java.util.Base64;

public class Base64Decoder {
    public static void main(String[] args) {
        // Example of a URL-safe Base64 string
        String urlSafeBase64 = "your-url-safe-base64-string";

        // Replace URL-safe characters
        String standardBase64 = urlSafeBase64.replace('-', '+').replace('_', '/');

        // Decode
        byte[] decodedBytes = Base64.getDecoder().decode(standardBase64);
        String decodedString = new String(decodedBytes);

        System.out.println(decodedString);
    }
}

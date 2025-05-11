package com.example.ecom.security;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SecretGenerator {

    public static String generateSecret(int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(keySize);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate secret key", e);
        }
    }

    public static void main(String[] args) {
        String secretKey = generateSecret(256); // Generates a 256-bit key, suitable for HS256
        System.out.println("Generated Secret Key: " + secretKey);
    }
}
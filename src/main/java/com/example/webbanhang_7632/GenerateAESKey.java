package com.example.webbanhang_7632;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class GenerateAESKey {
    public static void main(String[] args) {
        try {
            // Tạo khóa AES 128-bit
            SecretKey secretKey = generateAESKey();

            // Chuyển khóa sang dạng chuỗi Base64
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

            // In ra khóa dưới dạng chuỗi Base64
            System.out.println("Generated AES Key (Base64): " + encodedKey);
        } catch (Exception e) {
            System.out.println("Error generating AES key: " + e.getMessage());
        }
    }

    // Hàm tạo khóa AES 128-bit
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // Khóa 128-bit
        return keyGen.generateKey();
    }
}

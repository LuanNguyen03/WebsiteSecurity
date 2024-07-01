package com.example.webbanhang_7632.crypted;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESExample {
    // Khóa mặc định
    private static final String DEFAULT_KEY = "0uZZiyILaIA88HxZHOjzIQeh"; // Độ dài 128 bit (16 bytes)

    public static void main(String[] args) throws Exception {
        String plaintext = "Ma hoa AES";

        // Mã hóa
        String encryptedText = encrypt(plaintext);
        System.out.println("Encrypted Text: " + encryptedText);

        // Giải mã
        String decryptedText = decrypt(encryptedText);
        System.out.println("Decrypted Text: " + decryptedText);
    }

    // Hàm mã hóa
    public static String encrypt(String data) throws Exception {
        String key = DEFAULT_KEY;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Hàm giải mã
    public static String decrypt(String encryptedData) throws Exception {
        String key = DEFAULT_KEY;
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }
}

package com.example.webbanhang_7632.service;

import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class CryptoService {

    public String hashSHA256(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }

    public String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }

    public boolean verifySignature(String data, String signature, PublicKey publicKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initVerify(publicKey);
        sign.update(data.getBytes());
        byte[] signBytes = Base64.getDecoder().decode(signature);
        return sign.verify(signBytes);
    }

    public KeyPair generateECKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
        keyGen.initialize(ecSpec);
        return keyGen.generateKeyPair();
    }

    public String signDataWithEC(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signBytes);
    }

    public boolean verifySignatureWithEC(String data, String signature, PublicKey publicKey) throws Exception {
        Signature sign = Signature.getInstance("SHA256withECDSA");
        sign.initVerify(publicKey);
        sign.update(data.getBytes());
        byte[] signBytes = Base64.getDecoder().decode(signature);
        return sign.verify(signBytes);
    }
}

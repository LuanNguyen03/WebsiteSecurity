package com.example.webbanhang_7632.controller;

import com.example.webbanhang_7632.entity.User;
import com.example.webbanhang_7632.service.CryptoService;
import com.example.webbanhang_7632.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/login")
    public String login() {
        return "/users/login";
    }
    @GetMapping("/register")
    public String register(@NotNull Model model) {
        model.addAttribute("user", new User());
        return "/users/register";
    }
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            return "/users/register";
        }
        userService.save(user);
        userService.setDefaultRole(user.getUsername());
        return "redirect:/login";
    }
    @Autowired
    private CryptoService cryptoService;

    @GetMapping("/hash")
    public String hashData(@RequestParam String data) throws Exception {
        return cryptoService.hashSHA256(data);
    }

    @GetMapping("/generate-rsa")
    public String generateRSAKeyPair() throws Exception {
        KeyPair keyPair = cryptoService.generateRSAKeyPair();
        return "Private Key: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
                "\nPublic Key: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    @PostMapping("/sign")
    public String signData(@RequestParam String data, @RequestParam String privateKeyStr) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        return cryptoService.signData(data, privateKey);
    }

    @PostMapping("/verify")
    public boolean verifySignature(@RequestParam String data, @RequestParam String signature, @RequestParam String publicKeyStr) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        return cryptoService.verifySignature(data, signature, publicKey);
    }

    @GetMapping("/generate-ec")
    public String generateECKeyPair() throws Exception {
        KeyPair keyPair = cryptoService.generateECKeyPair();
        return "Private Key: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()) +
                "\nPublic Key: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
    }

    @PostMapping("/sign-ec")
    public String signDataWithEC(@RequestParam String data, @RequestParam String privateKeyStr) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyStr);
        PrivateKey privateKey = KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        return cryptoService.signDataWithEC(data, privateKey);
    }

    @PostMapping("/verify-ec")
    public boolean verifySignatureWithEC(@RequestParam String data, @RequestParam String signature, @RequestParam String publicKeyStr) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        PublicKey publicKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        return cryptoService.verifySignatureWithEC(data, signature, publicKey);
    }
}

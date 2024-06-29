package com.example.webbanhang_7632.controller;

import com.example.webbanhang_7632.entity.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String show(Model model) {
        return "index";
    }
}

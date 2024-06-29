package com.example.webbanhang_7632.controller;

import com.example.webbanhang_7632.entity.Product;
import com.example.webbanhang_7632.service.CategoryService;
import com.example.webbanhang_7632.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/products")

public class ProductController {

    @Value("${upload.path}")
    private String uploadPath;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService
    // Display a list of all products
    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/products/products-list";
    }
    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
        return "/products/add-product";
    }
    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result,@RequestParam("file") MultipartFile file, Model model) {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        // Xử lý tải lên file
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + File.separator + file.getOriginalFilename());
                Files.write(path, bytes);
                product.setImage("/" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to upload '" + file.getOriginalFilename() + "'");
                return "/products/add-product";
            }
        }
        productService.addProduct(product);
        return "redirect:/products";
    }
    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
        return "/products/update-product";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product,
                                BindingResult result,@RequestParam("file") MultipartFile file, Model model) {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "/products/update-product";
        }
        // Xử lý tải lên file
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + File.separator + file.getOriginalFilename());
                Files.write(path, bytes);
                product.setImage("/" + file.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to upload '" + file.getOriginalFilename() + "'");
                return "/products/update-product";
            }
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }
    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}

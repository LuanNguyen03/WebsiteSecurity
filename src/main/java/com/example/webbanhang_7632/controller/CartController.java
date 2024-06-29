package com.example.webbanhang_7632.controller;

import com.example.webbanhang_7632.model.CartItem;
import com.example.webbanhang_7632.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @GetMapping
    public String showCart(Model model) {
        float sum=0;
        model.addAttribute("cartItems", cartService.getCartItems());
        List<CartItem> cartItems =cartService.getCartItems();
        for (CartItem item : cartItems){
            sum+= item.getQuantity()*item.getProduct().getPrice();
        }
        model.addAttribute("sum", sum);
        return "/cart/cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int
            quantity) {
        List<CartItem> cartItemList= cartService.getCartItems();
        for(CartItem item : cartItemList){
            if(item.getProduct().getId()==productId){
                item.setQuantity(item.getQuantity()+quantity);
                return "redirect:/cart";
            }
        }
        cartService.addToCart(productId, quantity);
        return "redirect:/cart";
    }
    @PostMapping("/update")
    public String updateToCart(@RequestParam Long productId, @RequestParam int
            quantity) {
        List<CartItem> cartItemList= cartService.getCartItems();
        for(CartItem item : cartItemList){
            if(item.getProduct().getId()==productId){
                item.setQuantity(quantity);
                return "redirect:/cart";
            }
        }
        return "redirect:/cart";
    }
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clearCart();
        return "redirect:/cart";
    }
}

package com.example.webbanhang_7632.controller;
import com.example.webbanhang_7632.crypted.AESExample;
import com.example.webbanhang_7632.entity.Order;
import com.example.webbanhang_7632.model.CartItem;
import com.example.webbanhang_7632.service.CartService;
import com.example.webbanhang_7632.service.OrderService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CartService cartService;
    @GetMapping("/checkout")
    public String checkout() {
        return "/cart/checkout";
    }
    @PostMapping("/submit")
    public String submitOrder(String customerName,String customerPhone,String customerAddress, String customerNote ) throws Exception {
        List<CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            return "redirect:/cart"; // Redirect if cart is empty
        }
        orderService.createOrder(customerName,customerPhone,customerAddress,customerNote, cartItems);
        return "redirect:/order/confirmation";
    }
    @GetMapping("/confirmation")
    public String orderConfirmation(Model model) {
        model.addAttribute("message", "Your order has been successfully placed.");
        return "cart/order-confirmation";
    }
    @GetMapping("/list")
    public String orderList(Model model) throws Exception {
        List<Order> orderList = orderService.getAllOrders();
        for (Order order : orderList) {
            order.setCustomerName(AESExample.decrypt(order.getCustomerName()));
            order.setCustomerAddress(AESExample.decrypt(order.getCustomerAddress()));
            order.setCustomerNote(AESExample.decrypt(order.getCustomerNote()));
            order.setCustomerPhone(AESExample.decrypt(order.getCustomerPhone()));
        }
        model.addAttribute("orders", orderList);
        return "orders/order-list";
    }
}

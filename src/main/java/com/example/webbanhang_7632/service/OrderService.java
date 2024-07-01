package com.example.webbanhang_7632.service;

import com.example.webbanhang_7632.crypted.AESExample;
import com.example.webbanhang_7632.entity.Order;
import com.example.webbanhang_7632.entity.OrderDetail;
import com.example.webbanhang_7632.entity.Product;
import com.example.webbanhang_7632.model.CartItem;
import com.example.webbanhang_7632.repository.OrderDetailRepository;
import com.example.webbanhang_7632.repository.OrderRepository;
import com.example.webbanhang_7632.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    private final ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private CartService cartService; // Assuming you have a CartService
    @Transactional
    public Order createOrder(String customerName,String customerPhone,String customerAddress,String customerNote, List<CartItem> cartItems) throws Exception {
        Order order = new Order();
        order.setCustomerName(AESExample.encrypt(customerName));
        order.setCustomerPhone(AESExample.encrypt(customerPhone));
        order.setCustomerAddress(AESExample.encrypt(customerAddress));
        order.setCustomerNote(AESExample.encrypt(customerNote));
        order = orderRepository.save(order);
        for (CartItem item : cartItems) {
            Product product=item.getProduct();
            product.setQuantity(product.getQuantity()-item.getQuantity());
            productRepository.save(product);
            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(item.getProduct());
            detail.setQuantity(item.getQuantity());
            orderDetailRepository.save(detail);
        }
        // Optionally clear the cart after order placement
        cartService.clearCart();
        return order;
    }
}

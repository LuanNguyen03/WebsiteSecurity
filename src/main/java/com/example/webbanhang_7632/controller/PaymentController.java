package com.example.webbanhang_7632.controller;

import com.example.webbanhang_7632.Payment.VNPayConfig;

import com.example.webbanhang_7632.model.CartItem;
import com.example.webbanhang_7632.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.webbanhang_7632.Payment.VNPayConfig.hmacSHA512;

@Controller
@RequestMapping("/cart/payment")
public class PaymentController {
    @Autowired
    private CartService cartService; // Đảm bảo bạn đã inject CartService
    @GetMapping("/vnpay")
    public Object CreatePayment(HttpServletRequest req, HttpServletResponse resp /*,@RequestBody  */) throws IOException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";
        long sum = 0;
        List<CartItem> cartItems = cartService.getCartItems();
        for (CartItem item : cartItems) {
            sum += item.getQuantity() * item.getProduct().getPrice();
        }

        Map<String, String> params = new HashMap<>();
      //  long amount = Integer.parseInt(sum*100);
        long amount = sum * 100L;
        String bankCode = params.get(" ");

        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = params.get("language");
        if (locate != null && !locate.isEmpty()) {
            vnp_Params.put("vnp_Locale", locate);
        } else {
            vnp_Params.put("vnp_Locale", "vn");
        }
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        return "redirect:"+paymentUrl;

    }
    @GetMapping("/vnpay-return")
    public String vnpayReturn(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
        // Lấy các tham số trả về từ VNPay
        String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = request.getParameter("vnp_TxnRef");
        String vnp_Amount = request.getParameter("vnp_Amount");
        String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
        String vnp_BankCode = request.getParameter("vnp_BankCode");
        String vnp_BankTranNo = request.getParameter("vnp_BankTranNo");
        String vnp_CardType = request.getParameter("vnp_CardType");
        String vnp_PayDate = request.getParameter("vnp_PayDate");
        String vnp_OrderInfo = request.getParameter("vnp_OrderInfo");
        String vnp_TransStatus = request.getParameter("vnp_TransStatus");

        // Kiểm tra kết quả thanh toán từ VNPay
        if ("00".equals(vnp_ResponseCode)) {
            // Thanh toán thành công
            model.addAttribute("message", "Payment successful!");
            cartService.clearCart();
            return "/cart/order-confirmation"; // Chuyển hướng đến trang xác nhận đơn hàng
        } else {
            // Thanh toán thất bại hoặc hủy bỏ
            model.addAttribute("message", "Payment failed or canceled!");
            return "/cart/payment-failed"; // Chuyển hướng đến trang thông báo thanh toán thất bại
        }
    }
}

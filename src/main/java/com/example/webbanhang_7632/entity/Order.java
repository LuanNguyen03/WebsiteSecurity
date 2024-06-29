package com.example.webbanhang_7632.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private String customerPhone;
    private String customerAddress;
    private String customerNote;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetails;
}

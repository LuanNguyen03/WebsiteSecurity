package com.example.webbanhang_7632.entity;
import com.example.webbanhang_7632.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên là bắt buộc")
    @Size(min = 3, max = 50, message = "Tên phải có độ dài từ 3 đến 50 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Tên chỉ được chứa các ký tự chữ và số")
    private String name;
    @PositiveOrZero(message = "Giá phải là số dương hoặc bằng 0")
    private double price;
    @PositiveOrZero(message = "Số lượng là số dương hoặc bằng 0")
    private double quantity;
    private String description;
    private String image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

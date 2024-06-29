package com.example.webbanhang_7632.repository;

import com.example.webbanhang_7632.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { }

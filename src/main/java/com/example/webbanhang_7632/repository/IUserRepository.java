package com.example.webbanhang_7632.repository;

import com.example.webbanhang_7632.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

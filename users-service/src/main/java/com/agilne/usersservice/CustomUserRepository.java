package com.agilne.usersservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {
    
    boolean existsByRole(String role);
    CustomUser findByRole(String role);
    CustomUser findByEmail(String email);
    boolean existsByEmailAndRole(String email, String role);
    boolean existsByEmail(String email);
}


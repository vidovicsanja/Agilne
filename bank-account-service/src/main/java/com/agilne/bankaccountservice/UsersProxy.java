package com.agilne.bankaccountservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface UsersProxy {
    
    @GetMapping("/users-service/users/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email);
}
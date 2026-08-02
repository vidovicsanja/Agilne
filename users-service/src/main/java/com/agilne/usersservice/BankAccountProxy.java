package com.agilne.usersservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
    
    @DeleteMapping("/bank-account/{id}")
	public ResponseEntity<?> deleteBankAccount(@PathVariable("id") Long id);
}

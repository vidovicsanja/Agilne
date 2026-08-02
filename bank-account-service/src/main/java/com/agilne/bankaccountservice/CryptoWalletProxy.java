package com.agilne.bankaccountservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
    
    @DeleteMapping("/crypto-wallet/{email}")
    public ResponseEntity<?> deleteWallet(@PathVariable("email") String email);
}

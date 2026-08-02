package cryptoWallet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {
    
    @GetMapping("/bank-account/{email}")
	public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email);
}

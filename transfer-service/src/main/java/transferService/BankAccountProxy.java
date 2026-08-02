package transferService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import transferService.dto.TransferServiceDto;

@FeignClient(name = "bank-account")
public interface BankAccountProxy {

    @GetMapping("/bank-account/{email}")
	public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email);

    @PostMapping("/bank-account/conversion")
    public ResponseEntity<?> conversion(@RequestBody  TransferServiceDto requestDto);
}

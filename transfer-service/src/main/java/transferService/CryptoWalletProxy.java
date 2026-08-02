package transferService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import transferService.dto.TransferServiceDto;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
    
    @PostMapping("/crypto-wallet/conversion")
    public ResponseEntity<?> conversion(@RequestBody TransferServiceDto requestDto);

    @GetMapping("/crypto-wallet/{email}")
	public Boolean getByEmail(@PathVariable("email") String email);
}

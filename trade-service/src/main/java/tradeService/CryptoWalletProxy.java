package tradeService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import tradeService.dto.WalletAccountDto;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
    
    @PostMapping("/crypto-wallet/conversion")
    public ResponseEntity<?> conversion(@RequestBody WalletAccountDto requestDto);
}

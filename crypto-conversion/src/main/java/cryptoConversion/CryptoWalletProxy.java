package cryptoConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import cryptoConversion.dto.CryptoWalletDto;
import cryptoConversion.dto.CryptoWalletResponseDto;

@FeignClient(name = "crypto-wallet")
public interface CryptoWalletProxy {
    
    @PostMapping("/crypto-wallet/conversion")
    public ResponseEntity<CryptoWalletResponseDto> walletConversion(CryptoWalletDto walletDto);
}

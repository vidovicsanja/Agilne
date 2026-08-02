package cryptoConversion;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "crypto-exchange")
public interface CryptoExchangeProxy {
    
    @GetMapping("/crypto-exchange/from/{from}/to/{to}") 
	public ResponseEntity<CryptoConversion> getExchange(@PathVariable String from, @PathVariable String to);
}

package tradeService;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import tradeService.dto.WalletAccountDto;

@FeignClient(name = "currency-exchange")
public interface CurrencyExchangeProxy {
    
    @GetMapping("/currency-exchange/from/{from}/to/{to}") 
	public ResponseEntity<WalletAccountDto> getExchange(@PathVariable String from, @PathVariable String to);
}

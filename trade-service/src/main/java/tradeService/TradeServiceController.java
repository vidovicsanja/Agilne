package tradeService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import tradeService.dto.TradeServiceDto;
import tradeService.dto.WalletAccountDto;

@RestController
public class TradeServiceController {
    
    @Autowired //dependency injection
    private TradeServiceRepository repo;

    @Autowired
    private BankAccountProxy bankAccountProxy;

    @Autowired
    private CryptoWalletProxy cryptoWalletProxy;

    @Autowired
    private CurrencyExchangeProxy currencyExchangeProxy;
    
    //localhost:8600/trade-service?from=BTC&to=EUR&quantity=0.5 - request example
    @GetMapping("/trade-service")
    @RateLimiter(name = "default")
    public ResponseEntity<?> getExchange(@RequestParam String from, @RequestParam String to, @RequestParam(defaultValue = "10") double quantity, 
        @RequestHeader("Authorization") String authorization) {

        TradeServiceDto request = new TradeServiceDto("", from, "", to, BigDecimal.valueOf(0), BigDecimal.valueOf(quantity));

        request.setFromActual(request.getFrom());        
        request.setToActual(request.getTo());
        request.setQuantityActual(request.getQuantity());

        TradeService kurs;
        ResponseEntity<WalletAccountDto> response;

        try {
            if (request.getFrom().toUpperCase().equals("GBP") 
                || request.getFrom().toUpperCase().equals("CHF") 
                || request.getFrom().toUpperCase().equals("RSD")) {
                
                // convert to eur and then to crypto

                // send request to currency-exchange microservice
                response = currencyExchangeProxy.getExchange(request.getFrom().toUpperCase(), "EUR"); 
                    
                request.setFrom(response.getBody().getTo()); 
                request.setQuantity(request.getQuantityActual().multiply(response.getBody().getToValue()));

            } else if (request.getTo().toUpperCase().equals("GBP") 
                || request.getTo().toUpperCase().equals("CHF") 
                || request.getTo().toUpperCase().equals("RSD")) {

                kurs = repo.findByFromAndToIgnoreCase(request.getFrom().toUpperCase(), "EUR"); 

                // convert to eur and then to fiat

                // send request to currency-exchange microservice
                response = currencyExchangeProxy.getExchange("EUR", request.getTo());
                    
                request.setTo(response.getBody().getFrom());
                request.setQuantity(request.getQuantityActual().multiply(response.getBody().getToValue()));
            }
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }

        kurs = repo.findByFromAndToIgnoreCase(request.getFrom().toUpperCase(), request.getTo().toUpperCase()); //find this in database, based on from and to values

        if (kurs != null) {

            String email = getEmail(authorization);

            // crypto to fiat
            if (request.getFrom().toLowerCase().equals("btc") || request.getFrom().toLowerCase().equals("eth")
                || request.getFrom().toLowerCase().equals("bnb") || request.getFrom().toLowerCase().equals("ada")) {

                return ResponseEntity.status(200).body(cryptoToFiat(request, email, kurs).getBody());             
            }
            // fiat to crypto
            else if (request.getFrom().toLowerCase().equals("eur") || request.getFrom().toLowerCase().equals("usd"))
            {
                return ResponseEntity.status(200).body(fiatToCrypto(request, email, kurs).getBody());
            }
        } 
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Requested exchange could not be found");
    }

    private ResponseEntity<?> fiatToCrypto(TradeServiceDto request, String email, TradeService kurs)
    {
        try {
            // call bank account service, check if there is enough money and update bank account
            WalletAccountDto requestDto = new WalletAccountDto(email, request.getFromActual(), request.getTo(), request.getQuantityActual(), 
                kurs.getToValue().multiply(request.getQuantity()));

            // call crypto wallet service, check if there is enough money and update wallet  
            bankAccountProxy.conversion(requestDto);
                                
            ResponseEntity<?> responseWallet = cryptoWalletProxy.conversion(requestDto);
    
            return responseWallet;
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }
    }

    private ResponseEntity<?> cryptoToFiat(TradeServiceDto request, String email, TradeService kurs)
    {
        try {
            WalletAccountDto requestDto = new WalletAccountDto(email, request.getFrom(), request.getToActual(), request.getQuantityActual(), 
                kurs.getToValue().multiply(request.getQuantity()));

            // call crypto wallet service, check if there is enough money and update wallet  
            cryptoWalletProxy.conversion(requestDto);
                                
            ResponseEntity<?> responseAccount = bankAccountProxy.conversion(requestDto);  

            return responseAccount;
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }
    }

    private String getEmail(String authorization) {
        // Extract the username and password from the Authorization header
        String base64Credentials = authorization.substring("Basic".length()).trim();
        byte[] decoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(decoded, StandardCharsets.UTF_8);
        String[] emailPassword = credentials.split(":", 2);
        String email = emailPassword[0];
        return email;
	}

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> rateLimiterExceptionHandler(RequestNotPermitted ex) {
        return ResponseEntity.status(503).body("Trade service can only serve up to 2 requests every 45 seconds");
    }
}

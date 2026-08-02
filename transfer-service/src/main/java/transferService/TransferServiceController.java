package transferService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import transferService.dto.TransferServiceDto;

@RestController
public class TransferServiceController {
    
    @Autowired
    private BankAccountProxy bankAccountProxy;

    @Autowired
    private CryptoWalletProxy cryptoWalletProxy;
    
    //localhost:8700/transfer-service?currency=EUR&to=abcd@gmail.com&quantity=50 - request example
	@GetMapping("/transfer-service") //query params
    @RateLimiter(name = "default")
    public ResponseEntity<?> transfer
        (@RequestParam String currency, @RequestParam String to, @RequestParam(defaultValue = "10") double quantity, 
        @RequestHeader("Authorization") String authorization) {

		String email = getEmail(authorization);
        TransferServiceDto requestDto = new TransferServiceDto(email, to, currency, quantity + quantity * 0.01, quantity);

        try {
            Boolean toEmailExists = cryptoWalletProxy.getByEmail(email);

            if (currency.equals("EUR") || currency.equals("USD") || currency.equals("GBP") 
                || currency.equals("CHF") || currency.equals("RSD")) {

                if (!toEmailExists)
                    return ResponseEntity.status(404).body("Bank account with email " + to + " doesn't exist");

                // subtract from the account
                bankAccountProxy.conversion(requestDto);

                requestDto.setEmail(to);
                requestDto.setTo(currency);
                requestDto.setToValue(quantity);
                requestDto.setFrom("");
                // add to the account
                bankAccountProxy.conversion(requestDto);

            } else if (currency.equals("BTC") || currency.equals("ETH") 
                || currency.equals("ADA") || currency.equals("BNB")) {

                if (!toEmailExists)
                    return ResponseEntity.status(404).body("Crypto wallet with email " + to + " doesn't exist");

                // subtract from the account
                cryptoWalletProxy.conversion(requestDto);  

                requestDto.setEmail(to);
                requestDto.setTo(currency);
                requestDto.setToValue(quantity);
                requestDto.setFrom("");
                // add to the account
                cryptoWalletProxy.conversion(requestDto);  

            } else {
                return ResponseEntity.status(400).body("Currency " + currency + " doesn't exist");
            }
        } catch (FeignException e) {
            return ResponseEntity.status(e.status()).body(e.getMessage());
        }

        return ResponseEntity.status(200).body("The transfer was successfully completed from the " + email + " account to the " + to + " account, with an amount of " + quantity + " and currency denoted as " + currency);
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

    // handles error if there is missing parameter
    @ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
	    String parameter = ex.getParameterName();
	    return ResponseEntity.status(ex.getStatusCode()).body("Value [" + ex.getParameterType() + "] of parameter [" + parameter + "] has been ommited");
	}

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> rateLimiterExceptionHandler(RequestNotPermitted ex) {
        return ResponseEntity.status(503).body("Transfer service can only serve up to 2 requests every 45 seconds");
    }
}

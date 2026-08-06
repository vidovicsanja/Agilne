package currencyConversion;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import currencyConversion.exception.CurrencyExchangeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import currencyConversion.dto.BankAccountDto;
import feign.FeignException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CurrencyConversionController {

    @Autowired
	private CurrencyExchangeService currencyExchangeService;

    @Autowired
    private BankAccountProxy bankAccountProxy;

	@GetMapping("/currency-conversion") 
    @RateLimiter(name = "default")
    public ResponseEntity<?> getConversionParams
        (@RequestParam String from, @RequestParam String to, @RequestParam(defaultValue = "10") double quantity, 
        @RequestHeader("Authorization") String authorization) {

		String email = getEmail(authorization);

            CurrencyConversion response = currencyExchangeService.getExchange(from, to);

            BankAccountDto accountDto = new BankAccountDto(email, from, to, BigDecimal.valueOf(quantity),
                response.getToValue().multiply(BigDecimal.valueOf(quantity)));

            try {
                ResponseEntity<?> responseBank = bankAccountProxy.conversion(accountDto);
                return ResponseEntity.status(HttpStatus.OK).body(responseBank.getBody());
            } catch (FeignException feignException) {

                int status = feignException.status();

                return ResponseEntity.status(status).body(feignException.contentUTF8());
            }
	}

    private String getEmail(String authorization) {
		String base64Credentials = authorization.substring("Basic".length()).trim();
		byte[] decoded = Base64.getDecoder().decode(base64Credentials);
		String credentials = new String(decoded, StandardCharsets.UTF_8);
		String[] emailPassword = credentials.split(":", 2);
		String email = emailPassword[0];
		return email;
	}

    @ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
	    String parameter = ex.getParameterName();
	    return ResponseEntity.status(ex.getStatusCode()).body("Value [" + ex.getParameterType() + "] of parameter [" + parameter + "] has been ommited");
	}

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<String> rateLimiterExceptionHandler(RequestNotPermitted ex) {
        return ResponseEntity.status(503).body("Currency conversion service can only serve up to 2 requests every 45 seconds");
    }

}

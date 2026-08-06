package currencyConversion.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CurrencyExchangeServiceException.class)
    public ResponseEntity<String> handleCurrencyExchangeException(
            CurrencyExchangeServiceException ex) {

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getMessage());
    }
}

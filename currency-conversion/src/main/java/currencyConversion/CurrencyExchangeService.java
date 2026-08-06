package currencyConversion;

import currencyConversion.exception.CurrencyExchangeServiceException;
import feign.FeignException;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService {

    private final CurrencyExchangeProxy proxy;

    public CurrencyExchangeService(CurrencyExchangeProxy proxy) {
        this.proxy = proxy;
    }

    @Retry(name = "currencyExchangeRetry", fallbackMethod = "fallback")
    public CurrencyConversion getExchange(String from, String to) {

        ResponseEntity<CurrencyConversion> response =
                proxy.getExchange(from, to);

        return response.getBody();
    }

    private CurrencyConversion fallback(String from, String to, Throwable t) throws CurrencyExchangeServiceException {
        if (t instanceof FeignException feignException) {

            int status = feignException.status();

            if (status >= 400 && status < 500) {
                throw new CurrencyExchangeServiceException(((FeignException) t).contentUTF8(), status);
            }

            if (status >= 500) {
                throw new CurrencyExchangeServiceException("Unexpected currency exchange service error", status);
            }
        }

        throw new CurrencyExchangeServiceException("Currency exchange service is unavailable", 503);
    }
}

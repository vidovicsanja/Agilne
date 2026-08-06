package currencyConversion.exception;

public class CurrencyExchangeServiceException extends Exception {

    private int statusCode;

    public CurrencyExchangeServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}

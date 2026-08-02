package currencyConversion.dto;

import java.math.BigDecimal;

public class BankAccountDto {

    private String email;
	private String from;
	private String to;
	private BigDecimal fromValue;
	private BigDecimal toValue;

    public BankAccountDto() {
		
	}

    public BankAccountDto(String email, String from, String to, BigDecimal fromValue, BigDecimal toValue) {
        this.email = email;
        this.from = from;
        this.to = to;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public BigDecimal getFromValue() {
        return fromValue;
    }
    public void setFromValue(BigDecimal fromValue) {
        this.fromValue = fromValue;
    }
    public BigDecimal getToValue() {
        return toValue;
    }
    public void setToValue(BigDecimal toValue) {
        this.toValue = toValue;
    }

}

package com.agilne.currencyExchange;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class CurrencyExchange {
    
    @Id
	private long id;

	@Column(name = "currency_from") //attribute name in database
	private String from;

	@Column(name = "currency_to")
	private String to;

	private BigDecimal toValue;

	@Transient
	private String environment;

    public CurrencyExchange() {

    }

    public CurrencyExchange(long id, String from, String to, BigDecimal toValue, String environment) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.toValue = toValue;
        this.environment = environment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BigDecimal getToValue() {
        return toValue;
    }

    public void setToValue(BigDecimal toValue) {
        this.toValue = toValue;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    
}

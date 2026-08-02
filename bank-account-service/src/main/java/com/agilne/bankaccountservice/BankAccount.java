package com.agilne.bankaccountservice;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BankAccount {
    
    @Id
	private long id;

	private BigDecimal eur;
	private BigDecimal usd;
	private BigDecimal rsd;
	private BigDecimal gbp;
	private BigDecimal chf;

	private String email;

    public BankAccount() {
    }

    public BankAccount(long id, BigDecimal eur, BigDecimal usd, BigDecimal rsd, BigDecimal gbp, BigDecimal chf,
            String email) {
        this.id = id;
        this.eur = eur;
        this.usd = usd;
        this.rsd = rsd;
        this.gbp = gbp;
        this.chf = chf;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getEur() {
        return eur;
    }

    public void setEur(BigDecimal eur) {
        this.eur = eur;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public BigDecimal getRsd() {
        return rsd;
    }

    public void setRsd(BigDecimal rsd) {
        this.rsd = rsd;
    }

    public BigDecimal getGbp() {
        return gbp;
    }

    public void setGbp(BigDecimal gbp) {
        this.gbp = gbp;
    }

    public BigDecimal getChf() {
        return chf;
    }

    public void setChf(BigDecimal chf) {
        this.chf = chf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

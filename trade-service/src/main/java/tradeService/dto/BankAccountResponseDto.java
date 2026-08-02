package tradeService.dto;

import java.math.BigDecimal;

public class BankAccountResponseDto {

    private String email;
	private BigDecimal eur;
	private BigDecimal usd;
	private BigDecimal rsd;
	private BigDecimal gbp;
	private BigDecimal chf;
	private String message;

	public BankAccountResponseDto() {
		
	}

	public BankAccountResponseDto(String email, BigDecimal eur, BigDecimal usd, BigDecimal rsd, BigDecimal gbp,
			BigDecimal chf, String message) {
		this.email = email;
		this.eur = eur;
		this.usd = usd;
		this.rsd = rsd;
		this.gbp = gbp;
		this.chf = chf;
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
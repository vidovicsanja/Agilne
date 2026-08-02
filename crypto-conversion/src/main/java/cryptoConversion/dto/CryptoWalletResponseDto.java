package cryptoConversion.dto;

import java.math.BigDecimal;

public class CryptoWalletResponseDto {

    private String email;
	private BigDecimal btc;
	private BigDecimal eth;
	private BigDecimal bnb;
	private BigDecimal ada;
	private String message;

	public CryptoWalletResponseDto() {
		
	}

	public CryptoWalletResponseDto(String email, BigDecimal btc, BigDecimal eth, BigDecimal bnb, BigDecimal ada,
			String message) {
		this.email = email;
		this.btc = btc;
		this.eth = eth;
		this.bnb = bnb;
		this.ada = ada;
		this.message = message;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getBtc() {
		return btc;
	}

	public void setBtc(BigDecimal btc) {
		this.btc = btc;
	}

	public BigDecimal getEth() {
		return eth;
	}

	public void setEth(BigDecimal eth) {
		this.eth = eth;
	}

	public BigDecimal getBnb() {
		return bnb;
	}

	public void setBnb(BigDecimal bnb) {
		this.bnb = bnb;
	}

	public BigDecimal getAda() {
		return ada;
	}

	public void setAda(BigDecimal ada) {
		this.ada = ada;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
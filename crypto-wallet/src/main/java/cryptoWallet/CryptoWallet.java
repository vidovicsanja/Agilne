package cryptoWallet;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CryptoWallet {

    @Id
	private long id;

	private BigDecimal btc;
	private BigDecimal eth;
	private BigDecimal bnb;
	private BigDecimal ada;

	private String email;

    public CryptoWallet() {
    }

    public CryptoWallet(long id, BigDecimal btc, BigDecimal eth, BigDecimal bnb, BigDecimal ada, String email) {
        this.id = id;
        this.btc = btc;
        this.eth = eth;
        this.bnb = bnb;
        this.ada = ada;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}

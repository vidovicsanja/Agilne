package tradeService;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TradeService {

    @Id
	private long id;

	@Column(name = "currency_from") //attribute name in database
	private String from;

	@Column(name = "currency_to")
	private String to;

	private BigDecimal toValue;
  
    public TradeService() {
    }

    public TradeService(long id, String from, String to, BigDecimal toValue) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.toValue = toValue;
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
    
}

package tradeService.dto;

import java.math.BigDecimal;

public class TradeServiceDto {
    
	private String fromActual;		
    private String from;	
    private String toActual;    
    private String to;
    private BigDecimal quantityActual;
    private BigDecimal quantity;

    public TradeServiceDto() {
    }

    public TradeServiceDto(String fromActual, String from, String toActual, String to, BigDecimal quantityActual,
            BigDecimal quantity) {
        this.fromActual = fromActual;
        this.from = from;
        this.toActual = toActual;
        this.to = to;
        this.quantityActual = quantityActual;
        this.quantity = quantity;
    }

    public String getFromActual() {
        return fromActual;
    }

    public void setFromActual(String fromActual) {
        this.fromActual = fromActual;
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

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getToActual() {
        return toActual;
    }

    public void setToActual(String toActual) {
        this.toActual = toActual;
    }

    public BigDecimal getQuantityActual() {
        return quantityActual;
    }

    public void setQuantityActual(BigDecimal quantityActual) {
        this.quantityActual = quantityActual;
    }
    
}

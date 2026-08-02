package transferService.dto;

public class TransferServiceDto {
    
    private String email;
    private String to; // to which user 
    private String from; // currency
    private double fromValue;    
    private double toValue;

    public TransferServiceDto() {

    }

    public TransferServiceDto(String email, String to, String from, double fromValue, double toValue) {
        this.email = email;
        this.to = to;
        this.from = from;
        this.fromValue = fromValue;        
        this.toValue = toValue;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public double getFromValue() {
        return fromValue;
    }

    public void setFromValue(double fromValue) {
        this.fromValue = fromValue;
    }

    public double getToValue() {
        return toValue;
    }

    public void setToValue(double toValue) {
        this.toValue = toValue;
    }
    
}

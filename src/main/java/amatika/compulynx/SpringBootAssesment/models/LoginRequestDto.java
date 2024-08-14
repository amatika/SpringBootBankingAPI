package amatika.compulynx.SpringBootAssesment.models;

public class LoginRequestDto 
{
    private String customerId;
    private String pin;
    
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

    
}

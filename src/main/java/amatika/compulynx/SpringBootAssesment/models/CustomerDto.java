package amatika.compulynx.SpringBootAssesment.models;

public class CustomerDto 
{
    private String name;
    private String email;
    private String customerId;
    
	public String getName() 
	{
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String custId) 
	{
		this.customerId = custId;
	}
    
	
    
}

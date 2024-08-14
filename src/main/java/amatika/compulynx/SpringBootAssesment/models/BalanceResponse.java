package amatika.compulynx.SpringBootAssesment.models;

public class BalanceResponse 
{

	Double balance=0.0;

	public BalanceResponse(Double balance) 
	{
		super();
		this.balance = balance;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
}

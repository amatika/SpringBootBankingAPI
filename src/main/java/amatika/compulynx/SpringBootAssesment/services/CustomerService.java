package amatika.compulynx.SpringBootAssesment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import amatika.compulynx.SpringBootAssesment.models.Account;
import amatika.compulynx.SpringBootAssesment.models.Customer;
import amatika.compulynx.SpringBootAssesment.models.CustomerDto;
import amatika.compulynx.SpringBootAssesment.models.Transaction;
import amatika.compulynx.SpringBootAssesment.repositories.AccountRepository;
import amatika.compulynx.SpringBootAssesment.repositories.CustomerRepository;
import amatika.compulynx.SpringBootAssesment.repositories.TransactionRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class CustomerService 
{
		
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;

    //implementation that saves the customer information
    public Customer registerCustomer(CustomerDto customerDto) 
    {
    	  	

        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setPin(generateRandomPin());

        Account account = new Account();
        account.setAccountNumber(generateRandomAccountNumber());
        account.setBalance(0.0);
        account.setCustomer(customer);

        customer.setAccount(account);

        customerRepository.save(customer);
        return customer;
    }
    //method for generating the pin 
    private String generateRandomPin()
    {
        return String.format("%04d", new Random().nextInt(10000));
    }
    
    //method for generating account number
    public static String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder(10);
        
        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10);  // Generates a random digit between 0 and 9
            accountNumber.append(digit);
        }
        
        return accountNumber.toString();
    }
    
    //method that gets all the customer data
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    //method that would retrieve the balance of the customer
    public Double getCustomerBalance(String customerId) {
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer account not found"));

        return account.getBalance();
    }
    
    //method that would get the mini statement for the customer
    public List<Transaction> getMiniStatement(String customerId) 
    {
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Customer account not found"));

        return transactionRepository.findTop10ByAccountOrderByTimestampDesc(account);
    }
    
    //method that deposits cash
    public Transaction deposit(String customerId, double amount) 
    {
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setDescription("Cash deposit");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return transaction;
    }
    
    //method for making withdrawals
    public Transaction withdraw(String customerId, double amount) 
    {
        Account account = accountRepository.findByCustomerCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) 
        {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setDescription("Cash withdrawal");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(account);

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return transaction;
    }

    //method that facilitates funds transfer
    public Transaction transfer(String fromCustomerId, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByCustomerCustomerId(fromCustomerId)
                .orElseThrow(() -> new RuntimeException("From account not found"));

        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setTransactionId(generateTransactionId());
        transaction.setType("TRANSFER");
        transaction.setAmount(amount);
        transaction.setDescription("Funds transfer");
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setAccount(fromAccount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(transaction);

        return transaction;
    }

    //method for generating the transaction id
    public static String generateTransactionId() 
    {
        // Get current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();
        
        // Generate a random number between 1000 and 9999
        int randomNum = new Random().nextInt(9000) + 1000;
        
        // Combine timestamp and random number to form the transaction ID
        return "TXN" + timestamp + randomNum;
    }
    
    
}

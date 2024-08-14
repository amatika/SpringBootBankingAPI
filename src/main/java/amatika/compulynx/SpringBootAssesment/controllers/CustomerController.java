package amatika.compulynx.SpringBootAssesment.controllers;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import amatika.compulynx.SpringBootAssesment.models.AuthResponse;
import amatika.compulynx.SpringBootAssesment.models.BalanceResponse;
import amatika.compulynx.SpringBootAssesment.models.Customer;
import amatika.compulynx.SpringBootAssesment.models.CustomerDto;
import amatika.compulynx.SpringBootAssesment.models.JwtResponse;
import amatika.compulynx.SpringBootAssesment.models.LoginRequestDto;
import amatika.compulynx.SpringBootAssesment.models.Transaction;
import amatika.compulynx.SpringBootAssesment.repositories.CustomerRepository;
import amatika.compulynx.SpringBootAssesment.services.CustomerDetailsService;
import amatika.compulynx.SpringBootAssesment.services.CustomerService;
import amatika.compulynx.SpringBootAssesment.services.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;



@RestController
@RequestMapping("/api/customers")
public class CustomerController 
{
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private  TokenService tokenService;
    
    @Autowired
    CustomerRepository customerRepository;
   
    //the login endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGenerateToken(@RequestBody LoginRequestDto loginRequest) 
    {
        Customer customer = customerRepository.findByCustomerId(loginRequest.getCustomerId());
        if (customer == null) {
            System.out.println("Customer not found: " + loginRequest.getCustomerId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Customer not found");
        }

        UserDetails userDetails;
		try 
			{
			 userDetails = customerDetailsService.loadUserByUsername(loginRequest.getCustomerId());
			} 
		catch (UsernameNotFoundException e)
			{
			 return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
			}
		
		String token = tokenService.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthResponse(token));
    }   

    //endpoint that returns all the customers
    @GetMapping("/all")
    public List<Customer> getAllCustomers()
    {
        return customerService.getAllCustomers();
    }
    
    //endpoint for registering the customer
    @PostMapping("/register")
    public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerDto customerDto) 
    {
        return ResponseEntity.ok(customerService.registerCustomer(customerDto));
    }

    //endpoint that gets the account balance of the customer
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam String customerId) 
    {
        return ResponseEntity.ok(new BalanceResponse(customerService.getCustomerBalance(customerId)));
    }

   //endpoint for generating the ministatement
    @GetMapping("/mini-statement")
    public ResponseEntity<List<Map<String, Object>>> getMiniStatement(@RequestParam String customerId) {
        List<Transaction> transactions = customerService.getMiniStatement(customerId);

        // Convert transactions to a list of maps for easier access in Angular
        List<Map<String, Object>> transactionList = transactions.stream().map(transaction -> {
            Map<String, Object> transactionMap = new HashMap<>();
            transactionMap.put("id", transaction.getId());
            transactionMap.put("transactionId", transaction.getTransactionId());
            transactionMap.put("amount", transaction.getAmount());
            transactionMap.put("date", transaction.getTimestamp());
            transactionMap.put("type", transaction.getType());
            transactionMap.put("description", transaction.getDescription());
            transactionMap.put("balance", transaction.getAccount().getBalance());
            // Add other properties as needed
            return transactionMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(transactionList);
    }

    //endpoint for depositing cash
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam String customerId, @RequestParam double amount) 
    {
        return ResponseEntity.ok(new BalanceResponse(customerService.deposit(customerId, amount).getAccount().getBalance()));
    }

    //endpoint for performing withdrawals
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam String customerId, @RequestParam double amount) {
        return ResponseEntity.ok(new BalanceResponse(customerService.withdraw(customerId, amount).getAccount().getBalance()));
    }

    //endpoint for making cash transfer
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestParam String fromCustomerId,
                                                @RequestParam String toAccountNumber,
                                                @RequestParam double amount) {
        return ResponseEntity.ok(new BalanceResponse(customerService.transfer(fromCustomerId, toAccountNumber, amount).getAccount().getBalance()));
    }

    
   
}

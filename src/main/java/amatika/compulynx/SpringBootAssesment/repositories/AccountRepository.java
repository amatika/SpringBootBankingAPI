package amatika.compulynx.SpringBootAssesment.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import amatika.compulynx.SpringBootAssesment.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long> 
{
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByCustomerCustomerId(String customerId);
    
}
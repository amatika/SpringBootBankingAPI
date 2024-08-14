package amatika.compulynx.SpringBootAssesment.repositories;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import amatika.compulynx.SpringBootAssesment.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>
{
    Customer findByCustomerId(String customerId);
    
}








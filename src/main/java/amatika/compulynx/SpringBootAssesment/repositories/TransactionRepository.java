package amatika.compulynx.SpringBootAssesment.repositories;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import amatika.compulynx.SpringBootAssesment.models.Account;
import amatika.compulynx.SpringBootAssesment.models.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> 
{
    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findTop10ByAccountOrderByTimestampDesc(Account account);
}
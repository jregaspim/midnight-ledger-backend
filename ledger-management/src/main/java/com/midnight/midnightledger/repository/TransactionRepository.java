package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<List<Transaction>> findByTransactionType(TransactionType transactionType);

    @Query("SELECT t FROM Transaction t WHERE YEAR(t.transactionDate) = :year")
    List<Transaction> findByYear(@Param("year") int year);

    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :transactionType AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month")
    List<Transaction> findByTransactionTypeYearAndMonth(@Param("transactionType") TransactionType transactionType, @Param("year") int year, @Param("month") int month);
}

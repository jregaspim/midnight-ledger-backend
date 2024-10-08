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

    Optional<List<Transaction>> findByTransactionTypeAndAccountId(TransactionType transactionType, Long accountId);

    @Query("SELECT t FROM Transaction t WHERE YEAR(t.transactionDate) = :year AND t.accountId = :accountId")
    List<Transaction> findByYearAndAccountId(@Param("year") int year, @Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.transactionType = :transactionType AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month AND t.accountId = :accountId")
    List<Transaction> findByTransactionTypeYearAndMonthAndAccountId(
            @Param("transactionType") TransactionType transactionType,
            @Param("year") int year,
            @Param("month") int month,
            @Param("accountId") Long accountId
    );

    List<Transaction> findAllByAccountId(Long accountId);
    Optional<List<Transaction>> findByCategoryAndAccountId(String category, Long accountId);
}

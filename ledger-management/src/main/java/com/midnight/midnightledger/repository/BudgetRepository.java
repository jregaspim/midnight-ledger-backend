package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    Optional<Budget> findByCategoryAndAccountId(String category, Long accountId);

    Optional<List<Budget>> findByAccountId(Long accountId);
}

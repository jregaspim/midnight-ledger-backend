package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {

    Optional<List<FinancialGoal>> findByAccountId(Long accountId);
}

package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {
}

package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DebtRepository extends JpaRepository<Debt, Long> {

    Optional<List<Debt>> findByAccountId(Long accountId);
}

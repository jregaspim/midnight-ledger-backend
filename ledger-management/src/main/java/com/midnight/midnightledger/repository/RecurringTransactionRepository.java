package com.midnight.midnightledger.repository;


import com.midnight.midnightledger.model.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {

    @Query("SELECT rt FROM RecurringTransaction rt WHERE rt.active = true AND rt.nextExecutionDate = :today")
    List<RecurringTransaction> findAllByActiveTrueAndNextExecutionDateEquals(@Param("today") LocalDate today);
}

package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.SavingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavingProgressRepository extends JpaRepository<SavingProgress, Long> {

}

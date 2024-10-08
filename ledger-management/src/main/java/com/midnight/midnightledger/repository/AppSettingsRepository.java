package com.midnight.midnightledger.repository;

import com.midnight.midnightledger.model.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppSettingsRepository extends JpaRepository<AppSettings, Long> {

    Optional<AppSettings> findByAccountId(Long accountId);
}

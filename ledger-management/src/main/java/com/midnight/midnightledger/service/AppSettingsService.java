package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.AppSettings;
import com.midnight.midnightledger.repository.AppSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppSettingsService {

    private final AppSettingsRepository appSettingsRepository;

    public void saveAppSettings(AppSettings appSettings) {
        appSettingsRepository.findByAccountId(appSettings.getAccountId()).ifPresentOrElse(
                oldSettings -> {
                    oldSettings.setCurrency(appSettings.getCurrency());
                    oldSettings.setNotificationPreferences(appSettings.isNotificationPreferences());
                    appSettingsRepository.save(oldSettings);
                    log.info("Updated settings for account ID: {}", appSettings.getAccountId());
                },
                () -> {
                    log.warn("No existing settings found for account ID: {}", appSettings.getAccountId());
                }
        );
    }

    public AppSettings getAppSettings(Long accountId) {
        return appSettingsRepository.findByAccountId(accountId)
                .orElseGet(() -> {
                    AppSettings newSettings = new AppSettings(accountId, "₱", false);
                    appSettingsRepository.save(newSettings);
                    log.info("Created new settings for account ID: {}", accountId);
                    return newSettings;
                });
    }
}

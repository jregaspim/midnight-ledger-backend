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
        Optional<AppSettings> oldSettings = appSettingsRepository.findByAccountId(appSettings.getAccountId());
        if(oldSettings.isPresent()) {
            oldSettings.get().setCurrency(appSettings.getCurrency());
            oldSettings.get().setNotificationPreferences(appSettings.getNotificationPreferences());
            appSettingsRepository.save(oldSettings.get());
        }
    }

    public AppSettings getAppSettings(Long accountId) {

        Optional<AppSettings> appSettings = appSettingsRepository.findByAccountId(accountId);

        if(appSettings.isEmpty()) {
            AppSettings newSettings = AppSettings.builder()
                    .accountId(accountId)
                    .currency("₱")
                    .notificationPreferences(false)
                    .build();

            appSettingsRepository.save(newSettings);

            return newSettings;
        }

        return appSettings.get();
    }

}

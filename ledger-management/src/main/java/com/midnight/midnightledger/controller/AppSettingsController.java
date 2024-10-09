package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.AppSettings;
import com.midnight.midnightledger.service.AppSettingsService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/app-settings")
@RequiredArgsConstructor
@Slf4j
public class AppSettingsController {

    private final AppSettingsService appSettingsService;

    @GetMapping
    public ResponseEntity<AppSettings> getAppSettings() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Fetching settings for user: {}", user.getUsername());
                    AppSettings appSettings = appSettingsService.getAppSettings(user.getId());
                    return ResponseEntity.ok(appSettings);
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping
    public ResponseEntity<Void> saveAppSettings(@RequestBody AppSettings appSettings) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    appSettings.setAccountId(user.getId());
                    appSettingsService.saveAppSettings(appSettings);
                    log.info("Settings saved for user: {}", user.getUsername());
                    return ResponseEntity.status(201).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(401).<Void>build());
    }
}

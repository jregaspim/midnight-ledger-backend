package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.AppSettings;
import com.midnight.midnightledger.model.User;
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
        User currentUser = SecurityUtils.getCurrentUser();

        log.info(String.valueOf(currentUser));
        if (currentUser != null) {
            return ResponseEntity.ok(appSettingsService.getAppSettings(currentUser.getId()));
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<Void> saveAppSettings(@RequestBody AppSettings appSettings) {
        User currentUser = SecurityUtils.getCurrentUser();

        if (currentUser != null) {
            appSettings.setAccountId(currentUser.getId());
            appSettingsService.saveAppSettings(appSettings);
            return ResponseEntity.status(201).build();
        }

        return ResponseEntity.status(401).build();
    }
}

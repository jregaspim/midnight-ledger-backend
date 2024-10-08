package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @GetMapping("/current-user")
    public ResponseEntity<User> getCurrentUser() {
        User currentUser = SecurityUtils.getCurrentUser();

        log.info("Current user: {}", currentUser);
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        }

        return ResponseEntity.status(401).build();
    }
}

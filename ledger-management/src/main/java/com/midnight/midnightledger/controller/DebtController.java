package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.model.Debt;
import com.midnight.midnightledger.service.DebtService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/debts")
@Slf4j
public class DebtController {

    private final DebtService debtService;

    @GetMapping
    public ResponseEntity<List<Debt>> getAllDebts() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Fetching debts for user: {}", user.getUsername());
                    List<Debt> debts = debtService.getAllDebts(user.getId());
                    return ResponseEntity.ok(debts);
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping
    public ResponseEntity<Void> createDebt(@RequestBody Debt debt) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    debt.setAccountId(user.getId());
                    debtService.saveDebt(debt);
                    log.info("Debt created for user: {}", user.getUsername());
                    return ResponseEntity.status(201).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(401).<Void>build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Long id) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Deleting debt with ID: {} for user: {}", id, user.getUsername());
                    debtService.deleteDebt(id);
                    return ResponseEntity.status(200).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(401).<Void>build());
    }
}

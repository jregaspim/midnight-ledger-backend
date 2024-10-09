package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.service.BudgetService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budget")
@RequiredArgsConstructor
@Slf4j
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<Budget>> getAllBudget() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Fetching budgets for user: {}", user.getUsername());
                    List<Budget> budgets = budgetService.getAllBudgets(user.getId());
                    return ResponseEntity.ok(budgets);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<List<Budget>>build());
    }

    @PostMapping
    public ResponseEntity<Void> saveBudget(@RequestBody Budget budget) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    budget.setAccountId(user.getId());
                    budgetService.saveBudget(budget);
                    log.info("Budget saved for user: {}", user.getUsername());
                    return ResponseEntity.status(HttpStatus.CREATED).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Deleting budget with ID: {} for user: {}", id, user.getUsername());
                    boolean deleted = budgetService.deleteBudget(id);
                    if (deleted) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build();
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).<Void>build();
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build());
    }
}

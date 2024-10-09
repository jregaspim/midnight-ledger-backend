package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.FinancialGoal;
import com.midnight.midnightledger.model.dto.request.UpdateFinancialGoalRequest;
import com.midnight.midnightledger.service.FinancialGoalService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/financial-goal")
@RequiredArgsConstructor
@Slf4j
public class FinancialGoalController {

    private final FinancialGoalService financialGoalService;

    @GetMapping
    public ResponseEntity<List<FinancialGoal>> getAllFinancialGoals() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Fetching financial goals for user: {}", user.getUsername());
                    List<FinancialGoal> goals = financialGoalService.getAllFinancialGoals(user.getId());
                    return ResponseEntity.ok(goals);
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @GetMapping("/savings-progress")
    public ResponseEntity<Map<String, Map<String, BigDecimal>>> getAllFinancialGoalTotals() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Fetching savings progress for user: {}", user.getUsername());
                    Map<String, Map<String, BigDecimal>> savingsProgress = financialGoalService.getAllFinancialGoalTotals(user.getId());
                    return ResponseEntity.ok(savingsProgress);
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping
    public ResponseEntity<Void> saveFinancialGoal(@RequestBody FinancialGoal financialGoal) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Saving financial goal for user: {}", user.getUsername());
                    financialGoalService.saveFinancialGoal(financialGoal, user.getId());
                    return ResponseEntity.status(201).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(401).<Void>build());
    }

    @PatchMapping("/{id}/updateCurrentAmount")
    public ResponseEntity<String> updateGoalCurrentAmount(@PathVariable Long id, @RequestBody UpdateFinancialGoalRequest updateRequest) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Updating current amount for goal ID: {} for user: {}", id, user.getUsername());
                    boolean updated = financialGoalService.updateCurrentAmount(id, updateRequest.getCurrentAmount(), user.getId());
                    if (updated) {
                        return ResponseEntity.ok("Goal updated successfully.");
                    } else {
                        return ResponseEntity.status(404).body("Goal not found.");
                    }
                })
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialGoal(@PathVariable Long id) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    log.info("Deleting financial goal with ID: {} for user: {}", id, user.getUsername());
                    financialGoalService.deleteFinancialGoal(id);
                    return ResponseEntity.status(200).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(401).<Void>build());
    }
}

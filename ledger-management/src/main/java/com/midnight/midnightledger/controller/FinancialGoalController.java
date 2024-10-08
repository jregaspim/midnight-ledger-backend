package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.FinancialGoal;
import com.midnight.midnightledger.model.SavingProgress;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.model.dto.request.UpdateFinancialGoalRequest;
import com.midnight.midnightledger.model.dto.response.SavingProgressResponse;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.service.FinancialGoalService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/financial-goal")
@RequiredArgsConstructor
public class FinancialGoalController {

    private final FinancialGoalService financialGoalService;

    @GetMapping
    public ResponseEntity<List<FinancialGoal>> getAllFinancialGoal() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(financialGoalService.getAllFinancialGoal(currentUser.getId()));
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/savings-progress")
    public ResponseEntity<Map<String, Map<String, BigDecimal>>> getAllFinancialGoalTotal() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(financialGoalService.getAllFinancialGoalTotal(currentUser.getId()));
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<Void> saveFinancialGoal(@RequestBody FinancialGoal financialGoal){
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            financialGoalService.saveFinancialGoal(financialGoal, currentUser.getId());
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(401).build();
    }

    @PatchMapping("/{id}/updateCurrentAmount")
    public ResponseEntity<String> updateGoalCurrentAmount(@PathVariable Long id, @RequestBody UpdateFinancialGoalRequest updateRequest) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            boolean updated = financialGoalService.updateCurrentAmount(id, updateRequest.getCurrentAmount(), currentUser.getId());
            if (updated) {
                return ResponseEntity.ok("Goal updated successfully.");
            } else {
                return ResponseEntity.status(404).body("Goal not found.");
            }
        }

        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialGoal(@PathVariable Long id) {
        if(SecurityUtils.getCurrentUser() != null) {
            financialGoalService.deleteFinancialGoal(id);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(401).build();
    }

}

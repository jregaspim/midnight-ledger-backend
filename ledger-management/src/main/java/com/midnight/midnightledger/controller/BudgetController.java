package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.service.BudgetService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        User currentUser = SecurityUtils.getCurrentUser();

        if (currentUser != null) {
            return ResponseEntity.ok(budgetService.getAllBudget(currentUser.getId()));
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<Void> saveBudget(@RequestBody Budget budget){
        User currentUser = SecurityUtils.getCurrentUser();

        if (currentUser != null) {
            budget.setAccountId(currentUser.getId());
            budgetService.saveBudget(budget);
            return ResponseEntity.status(201).build();
        }

        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteTransaction(id);
        return ResponseEntity.status(200).build();
    }

}

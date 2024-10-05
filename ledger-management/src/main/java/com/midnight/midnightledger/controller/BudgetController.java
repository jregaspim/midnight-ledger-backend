package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/budget")
@CrossOrigin(origins = "http://localhost:4200")
public class BudgetController {

    @Autowired
    BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<Budget>> getAllTransaction() {
        return ResponseEntity.ok(budgetService.getAllBudget());
    }

    @PostMapping
    public void saveTransaction(@RequestBody Budget budget){
        budget.setAccountId(1001L); //temp set accountID
        System.out.println(budget);
        budgetService.saveBudget(budget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        budgetService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}

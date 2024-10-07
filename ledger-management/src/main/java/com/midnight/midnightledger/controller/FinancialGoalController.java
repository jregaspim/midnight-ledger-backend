package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.FinancialGoal;
import com.midnight.midnightledger.model.SavingProgress;
import com.midnight.midnightledger.model.dto.request.UpdateFinancialGoalRequest;
import com.midnight.midnightledger.model.dto.response.SavingProgressResponse;
import com.midnight.midnightledger.service.FinancialGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/financial-goal")
@CrossOrigin(origins = "http://localhost:4200")
public class FinancialGoalController {

    @Autowired
    FinancialGoalService financialGoalService;

    @GetMapping
    public ResponseEntity<List<FinancialGoal>> getAllFinancialGoal() {
        return ResponseEntity.ok(financialGoalService.getAllFinancialGoal());
    }

    @GetMapping("/savings-progress")
    public ResponseEntity<Map<String, Map<String, BigDecimal>>> getAllFinancialGoalTotal() {
        return ResponseEntity.ok(financialGoalService.getAllFinancialGoalTotal());
    }

    @PostMapping
    public void saveFinancialGoal(@RequestBody FinancialGoal financialGoal){
        financialGoal.setAccountId(1001L); //temp set accountID
        System.out.println(financialGoal);
        financialGoalService.saveFinancialGoal(financialGoal);
    }

    @PatchMapping("/{id}/updateCurrentAmount")
    public ResponseEntity<String> updateGoalCurrentAmount(@PathVariable Long id, @RequestBody UpdateFinancialGoalRequest updateRequest) {
        boolean updated = financialGoalService.updateCurrentAmount(id, updateRequest.getCurrentAmount());
        if (updated) {
            return ResponseEntity.ok("Goal updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Goal not found.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialGoal(@PathVariable Long id) {
        financialGoalService.deleteFinancialGoal(id);
        return ResponseEntity.noContent().build();
    }

}

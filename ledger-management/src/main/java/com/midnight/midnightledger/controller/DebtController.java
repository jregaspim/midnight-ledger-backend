package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Debt;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.service.DebtService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/debts")
public class DebtController {

    private final DebtService debtService;

    @GetMapping
    public ResponseEntity<List<Debt>> getAllDebts() {
        User currentUser = SecurityUtils.getCurrentUser();

        if (currentUser != null) {
            return new ResponseEntity<>(debtService.getAllDebts(currentUser.getId()), HttpStatus.OK);
        }

        return ResponseEntity.status(401).build();
    }

    @PostMapping
    public ResponseEntity<Void> createDebt(@RequestBody Debt debt) {
        User currentUser = SecurityUtils.getCurrentUser();

        if(currentUser != null) {
            debt.setAccountId(currentUser.getId());
            debtService.saveDebt(debt);
            return ResponseEntity.status(201).build();
        }

        return ResponseEntity.status(401).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Long id) {
        User currentUser = SecurityUtils.getCurrentUser();

        if (currentUser != null) {
            debtService.deleteDebt(id);
            return ResponseEntity.status(200).build();
        }

        return ResponseEntity.status(401).build();
    }
}

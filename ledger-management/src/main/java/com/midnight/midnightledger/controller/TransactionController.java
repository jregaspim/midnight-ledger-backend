package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.model.dto.request.TransactionRequest;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.service.TransactionService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{transactionType}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByType(@PathVariable String transactionType) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            try {
                TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());
                return ResponseEntity.ok(transactionService.getAllTransactionByTransactionType(type, currentUser.getId()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build(); // handle invalid transaction type
            }
        }
        return ResponseEntity.status(401).build(); // Unauthorized if no user
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(transactionService.getAllTransaction(currentUser.getId()));
        }
        return ResponseEntity.status(401).build(); // Unauthorized if no user
    }

    @PostMapping
    public ResponseEntity<Void> saveTransaction(@RequestBody TransactionRequest transaction) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            transactionService.saveTransaction(transaction, currentUser.getId());
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Map<String, BigDecimal[]>> getTransactionsByYear(@PathVariable int year) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            Map<String, BigDecimal[]> monthlyIncomeExpensesData = transactionService.getTransactionsByYear(year, currentUser.getId());
            return ResponseEntity.ok(monthlyIncomeExpensesData);
        }
        return ResponseEntity.status(401).build(); // Unauthorized if no user
    }

    @GetMapping("/{transactionType}/by-date")
    public ResponseEntity<Map<String, BigDecimal>> getTransactionsForYearAndMonth(
            @PathVariable String transactionType,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            Map<String, BigDecimal> monthlyTransaction = transactionService.getTransactionsForYearAndMonth(transactionType, year, month, currentUser.getId());
            return ResponseEntity.ok(monthlyTransaction);
        }
        return ResponseEntity.status(401).build(); // Unauthorized if no user
    }

    @GetMapping("/top/{transactionType}")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPerCategories(@PathVariable String transactionType) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            return ResponseEntity.ok(transactionService.getTotalPerCategories(transactionType, currentUser.getId()));
        }
        return ResponseEntity.status(401).build(); // Unauthorized if no user
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            transactionService.deleteTransaction(id, currentUser.getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(401).build(); // Unauthorized if no user
    }
}

package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.dto.request.TransactionRequest;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{transactionType}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByType(@PathVariable String transactionType) {
        try {
            TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());
            return ResponseEntity.ok(transactionService.getAllTransactionByTransactionType(type));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // handle invalid transaction type
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransaction());
    }

    @PostMapping
    public ResponseEntity<Void> saveTransaction(@RequestBody TransactionRequest transaction) {
        transactionService.saveTransaction(transaction);
        return ResponseEntity.status(201).build(); // return 201 Created
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Map<String, BigDecimal[]>> getTransactionsByYear(@PathVariable int year) {
        Map<String, BigDecimal[]> monthlyIncomeExpensesData = transactionService.getTransactionsByYear(year);
        return ResponseEntity.ok(monthlyIncomeExpensesData);
    }

    @GetMapping("/{transactionType}/by-date")
    public ResponseEntity<Map<String, BigDecimal>> getTransactionsForYearAndMonth(
            @PathVariable String transactionType,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {

        Map<String, BigDecimal> monthlyTransaction = transactionService.getTransactionsForYearAndMonth(transactionType, year, month);
        return ResponseEntity.ok(monthlyTransaction);
    }

    @GetMapping("/top/{transactionType}")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPerCategories(@PathVariable String transactionType) {
        return ResponseEntity.ok(transactionService.getTotalPerCategories(transactionType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

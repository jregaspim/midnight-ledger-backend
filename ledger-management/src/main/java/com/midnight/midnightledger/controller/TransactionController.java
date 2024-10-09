package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.model.dto.request.TransactionRequest;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.service.TransactionService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{transactionType}")
    public ResponseEntity<List<Transaction>> getAllTransactionsByType(@PathVariable String transactionType) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    try {
                        TransactionType type = TransactionType.valueOf(transactionType.toUpperCase());
                        List<Transaction> transactions = transactionService.getAllTransactionByTransactionType(type, user.getId());
                        return ResponseEntity.<List<Transaction>>ok(transactions);
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.badRequest().<List<Transaction>>build(); // Handle invalid transaction type
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<List<Transaction>>build());
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    List<Transaction> transactions = transactionService.getAllTransaction(user.getId());
                    return ResponseEntity.ok(transactions);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping
    public ResponseEntity<Void> saveTransaction(@RequestBody TransactionRequest transaction) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    transactionService.saveTransaction(transaction, user.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build());
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<Map<String, BigDecimal[]>> getTransactionsByYear(@PathVariable int year) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    Map<String, BigDecimal[]> monthlyData = transactionService.getTransactionsByYear(year, user.getId());
                    return ResponseEntity.ok(monthlyData);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/{transactionType}/by-date")
    public ResponseEntity<Map<String, BigDecimal>> getTransactionsForYearAndMonth(
            @PathVariable String transactionType,
            @RequestParam("year") int year,
            @RequestParam("month") int month) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    Map<String, BigDecimal> monthlyTransaction = transactionService.getTransactionsForYearAndMonth(transactionType, year, month, user.getId());
                    return ResponseEntity.ok(monthlyTransaction);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/top/{transactionType}")
    public ResponseEntity<Map<String, BigDecimal>> getTotalPerCategories(@PathVariable String transactionType) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    Map<String, BigDecimal> totalCategories = transactionService.getTotalPerCategories(transactionType, user.getId());
                    return ResponseEntity.ok(totalCategories);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    boolean deleted = transactionService.deleteTransaction(id, user.getId());
                    return deleted
                            ? ResponseEntity.noContent().<Void>build()
                            : ResponseEntity.status(HttpStatus.NOT_FOUND).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build());
    }
}

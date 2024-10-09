package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.RecurringTransaction;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.service.RecurringTransactionService;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/recurring-transactions")
@RequiredArgsConstructor
public class RecurringTransactionController {

    private final RecurringTransactionService recurringTransactionService;

    @PostMapping
    public ResponseEntity<Void> addRecurringTransaction(@RequestBody RecurringTransaction transaction) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    recurringTransactionService.saveRecurringTransaction(transaction, user.getId());
                    return ResponseEntity.status(HttpStatus.CREATED).<Void>build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build());
    }

    @GetMapping
    public ResponseEntity<List<RecurringTransaction>> getAllTransactions() {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    List<RecurringTransaction> transactions = recurringTransactionService.getAllRecurringTransactions(user.getId());
                    return ResponseEntity.ok(transactions);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransaction> updateTransaction(@RequestBody RecurringTransaction transaction) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    Optional<RecurringTransaction> updatedTransaction = recurringTransactionService.update(transaction);
                    return updatedTransaction
                            .map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        return SecurityUtils.getCurrentUser()
                .map(user -> {
                    boolean deleted = recurringTransactionService.delete(id);
                    if (deleted) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).<Void>build();
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).<Void>build();
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).<Void>build());
    }
}

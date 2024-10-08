package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.RecurringTransaction;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.service.RecurringTransactionService;
import com.midnight.midnightledger.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/recurring-transactions")
public class RecurringTransactionController {

    @Autowired
    private RecurringTransactionService recurringTransactionService;

    @PostMapping
    public void addRecurringTransaction(@RequestBody RecurringTransaction transaction) {
        Long accountId = SecurityUtils.getCurrentUser().getId();
        recurringTransactionService.saveRecurrentTransaction(transaction, accountId);
    }

    @GetMapping
    public List<RecurringTransaction> getAllTransactions() {
        Long accountId = SecurityUtils.getCurrentUser().getId();
        return recurringTransactionService.getALlRecurringTransaction(accountId);
    }

    @PutMapping("/{id}")
    public RecurringTransaction updateTransaction(@PathVariable Long id, @RequestBody RecurringTransaction transaction) {
        return recurringTransactionService.update(id, transaction);
    }

    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        recurringTransactionService.delete(id);
    }
}
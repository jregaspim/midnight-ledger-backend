package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.RecurringTransaction;
import com.midnight.midnightledger.service.RecurringTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/recurring-transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class RecurringTransactionController {

    @Autowired
    private RecurringTransactionService recurringTransactionService;

    @PostMapping
    public void addRecurringTransaction(@RequestBody RecurringTransaction transaction) {
        recurringTransactionService.saveRecurrentTransaction(transaction);
    }

    @GetMapping
    public List<RecurringTransaction> getAllTransactions() {
        return recurringTransactionService.getALlRecurringTransaction();
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
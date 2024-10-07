package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.RecurringTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LedgerManagementScheduler {

    @Autowired
    private RecurringTransactionService recurringTransactionService;

    @Autowired
    private BudgetService budgetService;

    @Scheduled(cron = "0 0 4 * * ?")
    public void executeRecurringTransactions() {
        System.out.println("Recurring Transaction Schedule run....");
        List<RecurringTransaction> transactions = recurringTransactionService.getTransactionsDueForExecution();

        for (RecurringTransaction transaction : transactions) {
            System.out.println("Transaction Name: "  + transaction.getTransactionName() + " | Transaction Recurrence Type: " +  transaction.getRecurrenceType());
            recurringTransactionService.processTransaction(transaction);
        }
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void executeBudgetValueReset() {
        System.out.println("Budget Service Amount Used Reset Schedule run....");
        budgetService.resetBudgetValue();
    }
}
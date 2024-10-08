package com.midnight.midnightledger.service;

import com.midnight.midnightledger.exception.TransactionNotFoundException;
import com.midnight.midnightledger.model.RecurringTransaction;
import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.User;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.repository.RecurringTransactionRepository;
import com.midnight.midnightledger.repository.TransactionRepository;
import com.midnight.midnightledger.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;

    private final TransactionRepository transactionRepository;


    public List<RecurringTransaction> getALlRecurringTransaction(Long accountId) {
        return recurringTransactionRepository.findAllByAccountId(accountId);
    }

    public List<RecurringTransaction> getTransactionsDueForExecution() {
        LocalDate now = LocalDate.now();
        return recurringTransactionRepository.findAllByActiveTrueAndNextExecutionDateEquals(now);
    }

    public void updateNextExecutionDate(RecurringTransaction transaction) {
        LocalDate nextExecutionDate = transaction.getNextExecutionDate();

        switch (transaction.getRecurrenceType()) {
            case DAILY:
                nextExecutionDate = nextExecutionDate.plusDays(1);
                break;
            case WEEKLY:
                nextExecutionDate = nextExecutionDate.plusWeeks(1);
                break;
            case MONTHLY:
                nextExecutionDate = nextExecutionDate.plusMonths(1);
                break;
        }

        transaction.setNextExecutionDate(nextExecutionDate);
        recurringTransactionRepository.save(transaction);
    }

    public void processTransaction(RecurringTransaction recurringTransaction) {
        System.out.println("Processing transaction: " + recurringTransaction.getTransactionName());

        Transaction transaction = Transaction.builder()
                .accountId(recurringTransaction.getAccountId())
                .amount(recurringTransaction.getAmount())
                .transactionType(recurringTransaction.getTransactionType())
                .category(recurringTransaction.getCategory())
                .transactionDate(recurringTransaction.getTransactionDate())
                .description(recurringTransaction.getDescription() + " (Recurring)")
                .build();

        transactionRepository.save(transaction);

        updateNextExecutionDate(recurringTransaction);
    }


    public void saveRecurrentTransaction(RecurringTransaction transaction, Long accountId) {

        transaction.setAccountId(accountId);
        transaction.setTransactionType(TransactionType.EXPENSES);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setStartDate(LocalDate.now());
        transaction.setActive(true);
        switch (transaction.getRecurrenceType()) {
            case DAILY:
                transaction.setNextExecutionDate(LocalDate.now().plusDays(1));
                break;
            case WEEKLY:
                transaction.setNextExecutionDate(LocalDate.now().plusWeeks(1));
                break;
            case MONTHLY:
                transaction.setNextExecutionDate(LocalDate.now().plusMonths(1));
                break;
        }

        recurringTransactionRepository.save(transaction);
    }

    public RecurringTransaction update(Long id, RecurringTransaction transaction) {
        RecurringTransaction existingTransaction = recurringTransactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        existingTransaction.setTransactionName(transaction.getTransactionName());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setRecurrenceType(transaction.getRecurrenceType());
        existingTransaction.setNextExecutionDate(transaction.getNextExecutionDate());

        return recurringTransactionRepository.save(existingTransaction);
    }

    public void delete(Long id) {
        recurringTransactionRepository.deleteById(id);
    }
}

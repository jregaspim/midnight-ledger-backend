package com.midnight.midnightledger.service;

import com.midnight.midnightledger.exception.TransactionNotFoundException;
import com.midnight.midnightledger.model.RecurringTransaction;
import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.enums.RecurrenceType;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.repository.RecurringTransactionRepository;
import com.midnight.midnightledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecurringTransactionService {

    private final RecurringTransactionRepository recurringTransactionRepository;
    private final TransactionRepository transactionRepository;

    public List<RecurringTransaction> getAllRecurringTransactions(Long accountId) {
        return recurringTransactionRepository.findAllByAccountId(accountId);
    }

    public List<RecurringTransaction> getTransactionsDueForExecution() {
        LocalDate now = LocalDate.now();
        return recurringTransactionRepository.findAllByActiveTrueAndNextExecutionDateEquals(now);
    }

    public void updateNextExecutionDate(RecurringTransaction transaction) {
        transaction.setNextExecutionDate(calculateNextExecutionDate(transaction.getNextExecutionDate(), transaction.getRecurrenceType()));
        recurringTransactionRepository.save(transaction);
    }

    private LocalDate calculateNextExecutionDate(LocalDate currentDate, RecurrenceType recurrenceType) {
        switch (recurrenceType) {
            case DAILY:
                return currentDate.plusDays(1);
            case WEEKLY:
                return currentDate.plusWeeks(1);
            case MONTHLY:
                return currentDate.plusMonths(1);
            default:
                throw new IllegalArgumentException("Unsupported recurrence type: " + recurrenceType);
        }
    }

    public void processTransaction(RecurringTransaction recurringTransaction) {
        Transaction transaction = createTransactionFromRecurring(recurringTransaction);
        transactionRepository.save(transaction);
        updateNextExecutionDate(recurringTransaction);
    }

    private Transaction createTransactionFromRecurring(RecurringTransaction recurringTransaction) {
        return Transaction.builder()
                .accountId(recurringTransaction.getAccountId())
                .amount(recurringTransaction.getAmount())
                .transactionType(recurringTransaction.getTransactionType())
                .category(recurringTransaction.getCategory())
                .transactionDate(recurringTransaction.getTransactionDate())
                .description(recurringTransaction.getDescription() + " (Recurring)")
                .build();
    }

    public void saveRecurringTransaction(RecurringTransaction transaction, Long accountId) {
        transaction.setAccountId(accountId);
        transaction.setTransactionType(TransactionType.EXPENSES);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setStartDate(LocalDate.now());
        transaction.setActive(true);
        transaction.setNextExecutionDate(calculateNextExecutionDate(LocalDate.now(), transaction.getRecurrenceType()));

        recurringTransactionRepository.save(transaction);
    }

    public Optional<RecurringTransaction> update(RecurringTransaction transaction) {
        RecurringTransaction existingTransaction = recurringTransactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new TransactionNotFoundException(transaction.getId()));

        existingTransaction.setTransactionName(transaction.getTransactionName());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setRecurrenceType(transaction.getRecurrenceType());
        existingTransaction.setNextExecutionDate(transaction.getNextExecutionDate());

        return Optional.of(recurringTransactionRepository.save(existingTransaction));
    }

    public boolean delete(Long id) {
        if (recurringTransactionRepository.existsById(id)) {
            recurringTransactionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

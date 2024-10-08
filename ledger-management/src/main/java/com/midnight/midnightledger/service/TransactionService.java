package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.dto.request.TransactionRequest;
import com.midnight.midnightledger.model.enums.ExpenseCategory;
import com.midnight.midnightledger.model.enums.IncomeCategory;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.repository.BudgetRepository;
import com.midnight.midnightledger.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Transactional
    public void saveTransaction(TransactionRequest transactionRequest, Long accountId) {
        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.getAmount())
                .category(transactionRequest.getCategory())
                .transactionDate(transactionRequest.getTransactionDate())
                .description(transactionRequest.getDescription())
                .transactionType(transactionRequest.getTransactionType())
                .accountId(accountId) // Associate transaction with the user
                .build();

        transactionRepository.save(transaction);

        Optional<Budget> budgetOptional = budgetRepository.findByCategoryAndAccountId(transactionRequest.getCategory(), accountId);

        if (budgetOptional.isPresent()) {
            Budget budget = budgetOptional.get();
            BigDecimal amountUsed = budget.getAmountUsed() != null ? budget.getAmountUsed() : BigDecimal.ZERO;
            budget.setAmountUsed(amountUsed.add(transactionRequest.getAmount()));
            budgetRepository.save(budget);
        }
    }

    public List<Transaction> getAllTransactionByTransactionType(TransactionType transactionType, Long accountId) {
        return transactionRepository.findByTransactionTypeAndAccountId(transactionType, accountId).orElseThrow();
    }

    public List<Transaction> getAllTransaction(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public Map<String, BigDecimal[]> getTransactionsByYear(int year, Long accountId) {
        BigDecimal[] monthlyIncome = new BigDecimal[12];
        BigDecimal[] monthlyExpenses = new BigDecimal[12];
        BigDecimal[] monthlySavings = new BigDecimal[12];

        Arrays.fill(monthlyIncome, BigDecimal.ZERO);
        Arrays.fill(monthlyExpenses, BigDecimal.ZERO);
        Arrays.fill(monthlySavings, BigDecimal.ZERO);

        List<Transaction> transactions = transactionRepository.findByYearAndAccountId(year, accountId);

        transactions.forEach(transaction -> {
            int monthIndex = transaction.getTransactionDate().getMonthValue() - 1; // 0-based index

            if (transaction.getTransactionType() == TransactionType.INCOME) {
                monthlyIncome[monthIndex] = monthlyIncome[monthIndex].add(transaction.getAmount());
            } else if (transaction.getTransactionType() == TransactionType.EXPENSES) {
                monthlyExpenses[monthIndex] = monthlyExpenses[monthIndex].add(transaction.getAmount());
            } else if (transaction.getTransactionType() == TransactionType.SAVINGS) {
                monthlySavings[monthIndex] = monthlySavings[monthIndex].add(transaction.getAmount());
            }
        });

        return Map.of(
                "INCOME", monthlyIncome,
                "EXPENSES", monthlyExpenses,
                "SAVINGS", monthlySavings
        );
    }

    public Map<String, BigDecimal> getTransactionsForYearAndMonth(String transactionType, int year, int month, Long accountId) {
        TransactionType type = getTransactionType(transactionType);
        List<Transaction> transactions = transactionRepository.findByTransactionTypeYearAndMonthAndAccountId(type, year, month, accountId);

        if (type == TransactionType.EXPENSES) {
            return updateMonthlyDataValue(ExpenseCategory.getExpenseMap(), transactions);
        } else if (type == TransactionType.INCOME) {
            return updateMonthlyDataValue(IncomeCategory.getIncomeMap(), transactions);
        }

        return Map.of(); // return empty map instead of null
    }

    public Map<String, BigDecimal> getTotalPerCategories(String transactionType, Long accountId) {
        List<Transaction> transactions = transactionRepository.findByTransactionTypeAndAccountId(getTransactionType(transactionType), accountId).orElseThrow();

        Map<String, BigDecimal> categoryTotals = ExpenseCategory.getExpenseMap();

        for (Transaction transaction : transactions) {
            categoryTotals.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add);
        }

        return categoryTotals;
    }

    private Map<String, BigDecimal> updateMonthlyDataValue(Map<String, BigDecimal> monthlyMap, List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            monthlyMap.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add);
        }
        return monthlyMap;
    }


    @Transactional
    public void deleteTransaction(Long id, Long accountId) {

        Optional<Transaction> transaction = transactionRepository.findById(id);

        if(transaction.isPresent()) {
            transactionRepository.deleteById(id);
            Optional<Budget> budgetOptional = budgetRepository.findByCategoryAndAccountId(transaction.get().getCategory(), accountId);

            if (budgetOptional.isPresent()) {
                Budget budget = budgetOptional.get();
                BigDecimal amountUsed = budget.getAmountUsed() != null ? budget.getAmountUsed() : BigDecimal.ZERO;
                budget.setAmountUsed(amountUsed.subtract(transaction.get().getAmount()));
                budgetRepository.save(budget);
            }
        }
    }

    private TransactionType getTransactionType(String transactionType) {
        try {
            return TransactionType.valueOf(transactionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
    }
}

package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.dto.request.TransactionRequest;
import com.midnight.midnightledger.model.enums.ExpenseCategory;
import com.midnight.midnightledger.model.enums.IncomeCategory;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void saveTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = Transaction.builder()
                .amount(transactionRequest.getAmount())
                .category(transactionRequest.getCategory())
                .transactionDate(transactionRequest.getTransactionDate())
                .description(transactionRequest.getDescription())
                .transactionType(transactionRequest.getTransactionType())
                .accountId(1001L)
                .build();

        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactionByTransactionType(TransactionType transactionType) {
        return transactionRepository.findByTransactionType(transactionType).orElseThrow();
    }

    public List<Transaction> getAllTransaction() {
        return transactionRepository.findAll();
    }

    public Map<String, BigDecimal[]> getTransactionsByYear(int year) {
        BigDecimal[] monthlyIncome = new BigDecimal[12];
        BigDecimal[] monthlyExpenses = new BigDecimal[12];

        Arrays.fill(monthlyIncome, BigDecimal.ZERO);
        Arrays.fill(monthlyExpenses, BigDecimal.ZERO);

        List<Transaction> transactions = transactionRepository.findByYear(year);

        transactions.forEach(transaction -> {
            int monthIndex = transaction.getTransactionDate().getMonthValue() - 1; // 0-based index

            if (transaction.getTransactionType() == TransactionType.INCOME) {
                monthlyIncome[monthIndex] = monthlyIncome[monthIndex].add(transaction.getAmount());
            } else if (transaction.getTransactionType() == TransactionType.EXPENSES) {
                monthlyExpenses[monthIndex] = monthlyExpenses[monthIndex].add(transaction.getAmount());
            }
        });

        return Map.of(
                "INCOME", monthlyIncome,
                "EXPENSES", monthlyExpenses
        );
    }

    public Map<String, BigDecimal> getTransactionsForYearAndMonth(String transactionType, int year, int month) {
        TransactionType type = getTransactionType(transactionType);
        List<Transaction> transactions = transactionRepository.findByTransactionTypeYearAndMonth(type, year, month);

        if (type == TransactionType.EXPENSES) {
            return updateMonthlyDataValue(ExpenseCategory.getExpenseMap(), transactions);
        } else if (type == TransactionType.INCOME) {
            return updateMonthlyDataValue(IncomeCategory.getIncomeMap(), transactions);
        }

        return Map.of(); // return empty map instead of null
    }

    public Map<String, BigDecimal> getTotalPerCategories(String transactionType) {
        List<Transaction> transactions = transactionRepository.findByTransactionType(getTransactionType(transactionType)).orElseThrow();

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

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    private TransactionType getTransactionType(String transactionType) {
        try {
            return TransactionType.valueOf(transactionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
    }
}

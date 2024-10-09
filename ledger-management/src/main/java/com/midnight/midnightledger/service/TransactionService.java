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

    private static final int MONTHS_IN_YEAR = 12;

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
        updateBudget(transactionRequest.getCategory(), transactionRequest.getAmount(), accountId, true);
    }

    public List<Transaction> getAllTransactionByTransactionType(TransactionType transactionType, Long accountId) {
        return transactionRepository.findByTransactionTypeAndAccountId(transactionType, accountId)
                .orElseThrow(() -> new NoSuchElementException("No transactions found for the given type."));
    }

    public List<Transaction> getAllTransaction(Long accountId) {
        return transactionRepository.findAllByAccountId(accountId);
    }

    public Map<String, BigDecimal[]> getTransactionsByYear(int year, Long accountId) {
        BigDecimal[] monthlyIncome = new BigDecimal[MONTHS_IN_YEAR];
        BigDecimal[] monthlyExpenses = new BigDecimal[MONTHS_IN_YEAR];
        BigDecimal[] monthlySavings = new BigDecimal[MONTHS_IN_YEAR];

        Arrays.fill(monthlyIncome, BigDecimal.ZERO);
        Arrays.fill(monthlyExpenses, BigDecimal.ZERO);
        Arrays.fill(monthlySavings, BigDecimal.ZERO);

        List<Transaction> transactions = transactionRepository.findByYearAndAccountId(year, accountId);
        transactions.forEach(transaction -> {
            int monthIndex = transaction.getTransactionDate().getMonthValue() - 1; // 0-based index

            switch (transaction.getTransactionType()) {
                case INCOME -> monthlyIncome[monthIndex] = monthlyIncome[monthIndex].add(transaction.getAmount());
                case EXPENSES -> monthlyExpenses[monthIndex] = monthlyExpenses[monthIndex].add(transaction.getAmount());
                case SAVINGS -> monthlySavings[monthIndex] = monthlySavings[monthIndex].add(transaction.getAmount());
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

        return type == TransactionType.EXPENSES ?
                updateMonthlyDataValue(ExpenseCategory.getExpenseMap(), transactions) :
                type == TransactionType.INCOME ?
                        updateMonthlyDataValue(IncomeCategory.getIncomeMap(), transactions) :
                        Collections.emptyMap(); // return empty map instead of null
    }

    public Map<String, BigDecimal> getTotalPerCategories(String transactionType, Long accountId) {
        List<Transaction> transactions = transactionRepository.findByTransactionTypeAndAccountId(getTransactionType(transactionType), accountId)
                .orElseThrow(() -> new NoSuchElementException("No transactions found for the given type."));

        Map<String, BigDecimal> categoryTotals = ExpenseCategory.getExpenseMap();

        transactions.forEach(transaction ->
                categoryTotals.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add)
        );

        return categoryTotals;
    }

    private Map<String, BigDecimal> updateMonthlyDataValue(Map<String, BigDecimal> monthlyMap, List<Transaction> transactions) {
        transactions.forEach(transaction ->
                monthlyMap.merge(transaction.getCategory(), transaction.getAmount(), BigDecimal::add)
        );
        return monthlyMap;
    }

    @Transactional
    public Boolean deleteTransaction(Long id, Long accountId) {
        Optional<Transaction> transaction = transactionRepository.findById(id);

        if (transaction.isPresent()) {
            transactionRepository.deleteById(id);
            updateBudget(transaction.get().getCategory(), transaction.get().getAmount(), accountId, false);
            return true;
        }

        return false;
    }

    private void updateBudget(String category, BigDecimal amount, Long accountId, boolean isAddition) {
        budgetRepository.findByCategoryAndAccountId(category, accountId).ifPresent(budget -> {
            BigDecimal amountUsed = budget.getAmountUsed() != null ? budget.getAmountUsed() : BigDecimal.ZERO;
            budget.setAmountUsed(isAddition ? amountUsed.add(amount) : amountUsed.subtract(amount));
            budgetRepository.save(budget);
        });
    }

    private TransactionType getTransactionType(String transactionType) {
        try {
            return TransactionType.valueOf(transactionType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionType);
        }
    }
}

package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.dto.request.TransactionRequest;
import com.midnight.midnightledger.model.dto.response.MonthlyTransactionResponse;
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

    @Autowired
    TransactionRepository transactionRepository;

    public void saveTransaction(TransactionRequest transactionRequest){

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

    public List<Transaction> getAllTransactionByTransactionType(TransactionType transactionType){

        return transactionRepository.findByTransactionType(transactionType).get();
    }

    public List<Transaction> getAllTransaction(){
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

        Map<String, BigDecimal[]> monthlyIncomeExpensesData = new HashMap<>();
        monthlyIncomeExpensesData.put("INCOME", monthlyIncome);
        monthlyIncomeExpensesData.put("EXPENSES", monthlyExpenses);

        return monthlyIncomeExpensesData;
    }

    public Map<String, BigDecimal> getTransactionsForYearAndMonth(String transactionType, int year, int month) {
        List<Transaction> transactions = transactionRepository.findByTransactionTypeYearAndMonth(TransactionType.valueOf(transactionType), year, month);

        if(transactionType.equalsIgnoreCase("Expenses")) {
            Map<String, BigDecimal> expenseMap = updateMonthlyDataValue(ExpenseCategory.getExpenseMap(), transactions);
            System.out.println("Expense Map: " + expenseMap);
            return expenseMap;
        } else if(transactionType.equalsIgnoreCase("Income")) {
            Map<String, BigDecimal> incomeMap = updateMonthlyDataValue(IncomeCategory.getIncomeMap(), transactions);
            System.out.println("Income Map: " + incomeMap);
            return incomeMap;
        }

        return null;
    }

    private Map<String, BigDecimal> updateMonthlyDataValue(Map<String, BigDecimal> monthlyMap, List<Transaction> transactions) {
        System.out.println("Expense Map: " + monthlyMap);
        for (Transaction transaction: transactions) {
            System.out.println("Transaction: " + transaction);
            String category = transaction.getCategory();
            BigDecimal currentValue = monthlyMap.get(transaction.getCategory());
            System.out.println("Current value: " + currentValue);
            monthlyMap.put(category, currentValue.add(transaction.getAmount()));
        }

        return monthlyMap;
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

}

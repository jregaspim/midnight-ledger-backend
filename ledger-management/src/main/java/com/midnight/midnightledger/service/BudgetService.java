package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.repository.BudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    @Transactional
    public void saveBudget(Budget budget) {
        budgetRepository.save(budget);
    }

    public List<Budget> getAllBudgets(Long accountId) {
        return budgetRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Budgets not found for account ID: " + accountId));
    }

    public boolean deleteBudget(Long id) {
        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void resetBudgetValues() {
        List<Budget> budgets = budgetRepository.findAll();
        budgets.forEach(budget -> budget.setAmountUsed(ZERO));
        budgetRepository.saveAll(budgets);
    }
}

package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    BudgetRepository budgetRepository;

    public void saveBudget(Budget budget){
        budgetRepository.save(budget);
    }

    public List<Budget> getAllBudget(){

        return budgetRepository.findAll();
    }

    public void deleteTransaction(Long id) {
        budgetRepository.deleteById(id);
    }


    public void resetBudgetValue() {
        List<Budget> budgets = budgetRepository.findAll();

        budgets.forEach(budget -> {
            budget.setAmountUsed(new BigDecimal(0));
        });

        budgetRepository.saveAll(budgets);
    }

}

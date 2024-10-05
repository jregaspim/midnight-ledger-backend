package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Budget;
import com.midnight.midnightledger.model.FinancialGoal;
import com.midnight.midnightledger.repository.BudgetRepository;
import com.midnight.midnightledger.repository.FinancialGoalRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FinancialGoalService {

    @Autowired
    FinancialGoalRepository financialGoalRepository;

    public void saveFinancialGoal(FinancialGoal financialGoal){
        financialGoalRepository.save(financialGoal);
    }

    public List<FinancialGoal> getAllFinancialGoal(){
        return financialGoalRepository.findAll();
    }

    @Transactional
    public boolean updateCurrentAmount(Long goalId, BigDecimal currentAmount) {
        FinancialGoal goal = financialGoalRepository.findById(goalId).orElse(null);
        if (goal != null) {
            goal.setCurrentAmount(currentAmount);
            financialGoalRepository.save(goal); // save the updated goal
            return true;
        } else {
            return false;
        }
    }

    public void deleteFinancialGoal(Long id) {
        financialGoalRepository.deleteById(id);
    }

}

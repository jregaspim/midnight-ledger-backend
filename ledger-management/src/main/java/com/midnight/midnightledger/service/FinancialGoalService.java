package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.FinancialGoal;
import com.midnight.midnightledger.model.SavingProgress;
import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.repository.FinancialGoalRepository;
import com.midnight.midnightledger.repository.SavingProgressRepository;
import com.midnight.midnightledger.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialGoalService {

    private final FinancialGoalRepository financialGoalRepository;

    private final SavingProgressRepository savingProgressRepository;

    private final TransactionRepository transactionRepository;

    public void saveFinancialGoal(FinancialGoal financialGoal, Long accountId){
        financialGoal.setAccountId(accountId);
        financialGoalRepository.save(financialGoal);
    }

    public List<FinancialGoal> getAllFinancialGoal(Long accountId){
        return financialGoalRepository.findByAccountId(accountId).get();
    }

    public  Map<String, Map<String, BigDecimal>> getAllFinancialGoalTotal(Long accountId){
        List<FinancialGoal> financialGoals = financialGoalRepository.findByAccountId(accountId).get();

        return financialGoals.stream()
                .collect(Collectors.toMap(
                        FinancialGoal::getGoalName,
                        financialGoal -> financialGoal.getSavingProgresses().stream()
                                .collect(Collectors.toMap(
                                        progress -> progress.getDateAdded().format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                                        SavingProgress::getAmount,
                                        BigDecimal::add
                                ))
                ));
    }

    @Transactional
    public boolean updateCurrentAmount(Long goalId, BigDecimal currentAmount, Long accountId) {
        FinancialGoal goal = financialGoalRepository.findById(goalId).orElse(null);
        if (goal != null) {

            BigDecimal oldCurrentAmount = goal.getCurrentAmount();
            BigDecimal progressAmount = currentAmount.subtract(oldCurrentAmount);

            SavingProgress savingProgress = new SavingProgress();
            savingProgress.setAmount(progressAmount);
            savingProgress.setDateAdded(LocalDate.now());
            savingProgress.setFinancialGoal(goal);

            savingProgress = savingProgressRepository.save(savingProgress);

            Transaction transaction = new Transaction();

            transaction.setAmount(progressAmount);
            transaction.setAccountId(1001L);
            transaction.setCategory("Savings");
            transaction.setTransactionType(TransactionType.SAVINGS);
            transaction.setDescription(goal.getGoalName());
            transaction.setTransactionDate(LocalDate.now());

            transactionRepository.save(transaction);

            goal.setCurrentAmount(currentAmount);
            goal.getSavingProgresses().add(savingProgress);

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

package midnightledger.service;

import midnightledger.model.Budget;
import midnightledger.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Debt;
import com.midnight.midnightledger.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService {
    @Autowired
    private DebtRepository debtRepository;

    public List<Debt> getAllDebts(Long accountId) {
        return debtRepository.findByAccountId(accountId).get();
    }

    public void saveDebt(Debt debt) {
        debtRepository.save(debt);
    }

    public void deleteDebt(Long id) {
        debtRepository.deleteById(id);
    }
}

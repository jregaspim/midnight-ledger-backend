package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Debt;
import com.midnight.midnightledger.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;

    public List<Debt> getAllDebts(Long accountId) {
        return debtRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("No debts found for account ID: " + accountId));
    }

    @Transactional
    public void saveDebt(Debt debt) {
        debtRepository.save(debt);
    }

    @Transactional
    public void deleteDebt(Long id) {
        if (!debtRepository.existsById(id)) {
            throw new RuntimeException("Debt not found with ID: " + id);
        }
        debtRepository.deleteById(id);
    }
}

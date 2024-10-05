package com.midnight.midnightledger.service;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    public void saveTransaction(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactionByTransactionType(TransactionType transactionType){

        return transactionRepository.findByTransactionType(transactionType).get();
    }

    public List<Transaction> getAllTransaction(){
        return transactionRepository.findAll();
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

}

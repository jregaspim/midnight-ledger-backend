package com.midnight.midnightledger.controller;

import com.midnight.midnightledger.model.Transaction;
import com.midnight.midnightledger.model.enums.TransactionType;
import com.midnight.midnightledger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/{transactionType}")
    public ResponseEntity<List<Transaction>> getAllTransactionByTransactionType(@PathVariable String transactionType) {
        return ResponseEntity.ok(transactionService.getAllTransactionByTransactionType(TransactionType.valueOf(transactionType)));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransaction() {
        return ResponseEntity.ok(transactionService.getAllTransaction());
    }

    @PostMapping
    public void saveTransaction(@RequestBody Transaction transaction){
        transaction.setAccountId(1001L); //temp set accountID
        System.out.println(transaction);
        transactionService.saveTransaction(transaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}

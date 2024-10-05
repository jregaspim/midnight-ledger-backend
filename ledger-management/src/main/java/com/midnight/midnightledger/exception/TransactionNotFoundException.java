package com.midnight.midnightledger.exception;

public class TransactionNotFoundException extends RuntimeException{
    public TransactionNotFoundException(Long id) {
        super("Could not find transaction with ID " + id);
    }
}

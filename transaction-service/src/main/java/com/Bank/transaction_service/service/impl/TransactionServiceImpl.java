package com.Bank.transaction_service.service.impl;

import com.Bank.transaction_service.entity.Transaction;
import com.Bank.transaction_service.repository.TransactionRepository;
import com.Bank.transaction_service.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;

    public TransactionServiceImpl(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactions(String accountNumber) {
        return repository.findByFromAccountOrToAccount(accountNumber, accountNumber);
    }
}
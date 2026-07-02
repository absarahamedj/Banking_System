package com.Bank.transaction_service.service;

import com.Bank.transaction_service.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction saveTransaction(Transaction transaction);

    List<Transaction> getTransactions(String accountNumber);
}
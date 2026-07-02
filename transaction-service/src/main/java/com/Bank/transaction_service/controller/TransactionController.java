package com.Bank.transaction_service.controller;

import com.Bank.transaction_service.entity.Transaction;
import com.Bank.transaction_service.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public Transaction save(@RequestBody Transaction transaction) {
        return service.saveTransaction(transaction);
    }

    @GetMapping("/{accountNumber}")
    public List<Transaction> history(@PathVariable String accountNumber) {
        return service.getTransactions(accountNumber);
    }
}
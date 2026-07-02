package com.Bank.account_service.service.impl;

import com.Bank.account_service.dto.CreateAccountRequest;
import com.Bank.account_service.dto.TransactionRequest;
import com.Bank.account_service.entity.Account;
import com.Bank.account_service.feign.TransactionClient;
import com.Bank.account_service.repository.AccountRepository;
import com.Bank.account_service.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionClient transactionClient;

    public AccountServiceImpl(AccountRepository accountRepository,
                              TransactionClient transactionClient) {
        this.accountRepository = accountRepository;
        this.transactionClient = transactionClient;
    }

    @Override
    public Account createAccount(CreateAccountRequest request) {

        String accountNumber = generateAccountNumber();

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .customerId(request.getCustomerId())
                .accountType(request.getAccountType())
                .branchName(request.getBranchName())
                .ifscCode(request.getIfscCode())
                .balance(request.getInitialBalance())
                .build();

        return accountRepository.save(account);
    }

    @Override
    public Account searchAccount(String accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new NoSuchElementException("Account Not Found"));
    }

    @Override
    public Double getBalance(String accountNumber) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        return account.getBalance();
    }

    @Override
    public Account deposit(String accountNumber, Double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        if (amount <= 0) {
            throw new RuntimeException("Invalid Amount");
        }

        account.setBalance(account.getBalance() + amount);

        return accountRepository.save(account);
    }

    @Override
    public Account withdraw(String accountNumber, Double amount) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        if (amount <= 0) {
            throw new RuntimeException("Invalid Amount");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient Balance");
        }

        account.setBalance(account.getBalance() - amount);

        return accountRepository.save(account);
    }

    @Override
    public Account transfer(String fromAccount, String toAccount, Double amount) {

        Account sender = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() ->
                        new RuntimeException("Sender Account Not Found"));

        Account receiver = accountRepository.findByAccountNumber(toAccount)
                .orElseThrow(() ->
                        new RuntimeException("Receiver Account Not Found"));

        if (amount <= 0) {
            throw new RuntimeException("Invalid Amount");
        }

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient Balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        // Save transaction in Transaction Service
        TransactionRequest request = new TransactionRequest();
        request.setFromAccount(fromAccount);
        request.setToAccount(toAccount);
        request.setAmount(amount);
        request.setTransactionType("TRANSFER");

        transactionClient.saveTransaction(request);

        return sender;
    }

    private String generateAccountNumber() {

        Random random = new Random();

        return String.valueOf(1000000000L + random.nextInt(900000000));
    }
}
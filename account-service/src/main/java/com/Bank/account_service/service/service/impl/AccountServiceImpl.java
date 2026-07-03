package com.Bank.account_service.service.impl;

import com.Bank.account_service.dto.CreateAccountRequest;
import com.Bank.account_service.dto.TransactionRequest;
import com.Bank.account_service.entity.Account;
import com.Bank.account_service.feign.TransactionClient;
import com.Bank.account_service.repository.AccountRepository;
import com.Bank.account_service.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
                .transactionPin(request.getTransactionPin())
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
    public Double getBalance(String accountNumber, String transactionPin) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        if (!account.getTransactionPin().equals(transactionPin)) {
            throw new RuntimeException("Incorrect Transaction PIN. Please check your PIN and try again.");
        }

        return account.getBalance();
    }
    @Override
    public Account deposit(String accountNumber,
                           Double amount,
                           String transactionPin) {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        Callable<Account> depositTask = () -> {

            Account account = accountRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() ->
                            new RuntimeException("Account Not Found"));

            if (!account.getTransactionPin().equals(transactionPin)) {
                throw new RuntimeException("Please check your transaction PIN and try again.");
            }

            if (amount <= 0) {
                throw new RuntimeException("Invalid Amount");
            }


            account.setBalance(account.getBalance() + amount);

            account = accountRepository.save(account);

            TransactionRequest request = new TransactionRequest();
            request.setFromAccount(accountNumber);
            request.setToAccount(accountNumber);
            request.setAmount(amount);
            request.setTransactionType("DEPOSIT");

            transactionClient.saveTransaction(request);

            return account;
        };

        try {
            Future<Account> future = executor.submit(depositTask);
            return future.get();
        } catch (InterruptedException | ExecutionException e) {

            Throwable cause = e.getCause();

            if (cause != null) {
                throw new RuntimeException(cause.getMessage());
            }

            throw new RuntimeException("Deposit Failed");

        } finally {
            executor.shutdown();
        }
    }

    @Override
    public Account withdraw(String accountNumber,
                            Double amount,
                            String transactionPin) {

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException("Account Not Found"));

        if (!account.getTransactionPin().equals(transactionPin)) {
            throw new RuntimeException("Incorrect Transaction PIN. Please check your PIN and try again.");
        }

        if (amount <= 0) {
            throw new RuntimeException("Invalid Amount");
        }

        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient Balance");
        }

        account.setBalance(account.getBalance() - amount);

        account = accountRepository.save(account);

        TransactionRequest request = new TransactionRequest();
        request.setFromAccount(accountNumber);
        request.setToAccount(accountNumber);
        request.setAmount(amount);
        request.setTransactionType("WITHDRAW");

        transactionClient.saveTransaction(request);

        return account;
    }

    @Override
    public Account transfer(String fromAccount, String toAccount, Double amount, String transactionPin) {

        Account sender = accountRepository.findByAccountNumber(fromAccount)
                .orElseThrow(() ->
                        new RuntimeException("Sender Account Not Found"));
        if (!sender.getTransactionPin().equals(transactionPin)) {
            throw new RuntimeException("Incorrect Transaction PIN. Please check your PIN and try again.");
        }
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
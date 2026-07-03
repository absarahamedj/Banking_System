package com.Bank.account_service.service;

import com.Bank.account_service.dto.CreateAccountRequest;
import com.Bank.account_service.entity.Account;

public interface AccountService {

    Account createAccount(CreateAccountRequest request);
    Account searchAccount(String accountNumber);
    Double getBalance(String accountNumber, String transactionPin);
    Account deposit(String accountNumber,
                    Double amount,
                    String transactionPin);
    Account withdraw(String accountNumber,
                     Double amount,
                     String transactionPin);
    Account transfer(String fromAccount,
                     String toAccount,
                     Double amount,
                     String transactionPin);

}
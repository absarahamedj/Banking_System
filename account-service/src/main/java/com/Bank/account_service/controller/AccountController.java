package com.Bank.account_service.controller;

import com.Bank.account_service.dto.BalanceRequest;
import com.Bank.account_service.dto.CreateAccountRequest;
import com.Bank.account_service.dto.TransactionPinRequest;
import com.Bank.account_service.dto.TransferRequest;
import com.Bank.account_service.entity.Account;
import com.Bank.account_service.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public Account createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }


@GetMapping("/{accountNumber}")
public Account searchAccount(@PathVariable String accountNumber) {

        return accountService.searchAccount(accountNumber);
}
    @PostMapping("/{accountNumber}/balance")
    public Double getBalance(@PathVariable String accountNumber,
                             @Valid @RequestBody BalanceRequest request) {

        return accountService.getBalance(
                accountNumber,
                request.getTransactionPin());
    }

    @PutMapping("/{accountNumber}/deposit")
    public Account deposit(@PathVariable String accountNumber,
                           @Valid @RequestBody TransactionPinRequest request) {

        return accountService.deposit(accountNumber, request.getAmount(), request.getTransactionPin());
    }

    @PutMapping("/{accountNumber}/withdraw")
    public Account withdraw(@PathVariable String accountNumber,
                            @Valid @RequestBody TransactionPinRequest request) {

        return accountService.withdraw(accountNumber, request.getAmount(), request.getTransactionPin());
    }
    @PutMapping("/transfer/{fromAccount}")
    public Account transfer(@PathVariable String fromAccount,
                            @Valid @RequestBody TransferRequest request) {

        return accountService.transfer(
                fromAccount,
                request.getToAccount(),
                request.getAmount(),
                request.getTransactionPin());
    }
@GetMapping("/test")
    public String test() {
        return "Working";
    }
}
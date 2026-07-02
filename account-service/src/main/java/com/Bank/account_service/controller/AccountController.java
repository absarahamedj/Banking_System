package com.Bank.account_service.controller;

import com.Bank.account_service.dto.CreateAccountRequest;
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

    @GetMapping("/{accountNumber}/balance")
    public Double getBalance(@PathVariable String accountNumber) {
        return accountService.getBalance(accountNumber);
    }

    @PutMapping("/{accountNumber}/deposit/{amount}")
    public Account deposit(@PathVariable String accountNumber,
                           @PathVariable Double amount) {

        return accountService.deposit(accountNumber, amount);
    }

    @PutMapping("/{accountNumber}/withdraw/{amount}")
    public Account withdraw(@PathVariable String accountNumber,
                            @PathVariable Double amount) {
        return accountService.withdraw(accountNumber, amount);
    }

    @PutMapping("/transfer/{fromAccount}/{toAccount}/{amount}")
    public Account transfer(@PathVariable String fromAccount,
                            @PathVariable String toAccount,
                            @PathVariable Double amount) {

        return accountService.transfer(fromAccount, toAccount, amount);
    }
@GetMapping("/test")
    public String test() {
        return "Working";
    }
}
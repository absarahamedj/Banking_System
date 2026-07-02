package com.Bank.account_service.feign;

import com.Bank.account_service.dto.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "TRANSACTION-SERVICE")
public interface TransactionClient {

    @PostMapping("/transactions")
    void saveTransaction(@RequestBody TransactionRequest request);
}
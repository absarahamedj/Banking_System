package com.Bank.account_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TransferRequest {

    @NotBlank
    private String toAccount;

    @NotNull
    private Double amount;

    @NotBlank
    @Pattern(regexp = "\\d{4}", message = "Transaction PIN must be 4 digits")
    private String transactionPin;
}
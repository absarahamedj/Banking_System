package com.Bank.account_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BalanceRequest {

    @NotBlank(message = "Transaction PIN is required")
    @Pattern(regexp = "\\d{4}", message = "Transaction PIN must be 4 digits")
    private String transactionPin;
}
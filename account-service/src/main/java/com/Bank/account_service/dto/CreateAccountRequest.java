package com.Bank.account_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull(message = "Customer Id is required")
    private Long customerId;

    @NotBlank(message = "Account Type is required")
    private String accountType;

    @NotBlank(message = "Branch Name is required")
    private String branchName;

    @NotBlank(message = "IFSC Code is required")
    private String ifscCode;

    @NotNull(message = "Initial Balance is required")
    @Min(value = 0, message = "Balance cannot be negative")
    private Double initialBalance;
}
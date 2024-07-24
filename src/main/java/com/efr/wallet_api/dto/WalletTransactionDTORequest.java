package com.efr.wallet_api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletTransactionDTORequest {

    @NotNull
    private UUID id;

    @NotNull
    private OperationType operationType;

    @NotNull
    @Positive
    @DecimalMin("0.01")
    private BigDecimal amount;
}

enum OperationType{
    DEPOSIT, WITHDRAW
}
package com.dariusz.calculator.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRequest {

    @NotEmpty(message = "Currency from cannot be empty")
    private String currencyCodeFrom;
    @NotEmpty(message = "Currency to cannot be empty")
    private String currencyCodeTo;
    @Positive(message = "Money amount must be positive")
    private double amount;
}

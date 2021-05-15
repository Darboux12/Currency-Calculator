package com.dariusz.calculator.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyExchangeRequest {
    private String currencyCodeFrom;
    private String currencyCodeTo;
    private double amount;
}

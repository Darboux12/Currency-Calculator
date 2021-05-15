package com.dariusz.calculator.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRatesRequest {
    private Set<String> currencyCodes;
}

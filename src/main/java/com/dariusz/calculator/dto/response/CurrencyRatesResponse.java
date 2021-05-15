package com.dariusz.calculator.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyRatesResponse {
    private String name;
    private String code;
    private double rate;
}

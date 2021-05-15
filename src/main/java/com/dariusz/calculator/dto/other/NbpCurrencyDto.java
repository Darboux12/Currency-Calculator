package com.dariusz.calculator.dto.other;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NbpCurrencyDto {
    private String currency;
    private String code;
    private double rate;
}
